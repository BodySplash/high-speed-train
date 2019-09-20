package reservation.cluster;

import common.aeron.node.RpcSession;
import org.agrona.concurrent.AtomicBuffer;
import org.slf4j.*;
import reservation.cluster.api.*;
import reservation.cluster.domain.*;
import reservation.cluster.infra.ResponseMarshaller;

class MessageAdapter {

    public MessageAdapter(TicketOffice ticketOffice) {

        this.ticketOffice = ticketOffice;
    }

    public void adapt(RpcSession session, AtomicBuffer buffer) {
        headerDecoder.wrap(buffer, 0);
        switch (headerDecoder.templateId()) {
            case CreateTrainDecoder.TEMPLATE_ID: {
                createTrainDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
                var correlationId = createTrainDecoder.correlationId();
                var coachesNumber = createTrainDecoder.coachesNumber();
                var seatsNumber = createTrainDecoder.seatsNumber();
                LOGGER.info("Create train {} {} {}", correlationId, coachesNumber, seatsNumber);
                var train = ticketOffice.createTrain(coachesNumber, seatsNumber);
                LOGGER.info("Train created {} ", train.getId());
                sendCreateTrainAck(session, train, correlationId);
                break;
            }
            case ReservationRequestDecoder.TEMPLATE_ID: {
                reservationRequestDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
                var correlationId = reservationRequestDecoder.correlationId();
                var trainId = reservationRequestDecoder.trainId();
                var seats = reservationRequestDecoder.seatsNumber();
                LOGGER.info("Make reservation {} {} {}", correlationId, trainId, seats);
                var reservationOption = ticketOffice.requestReservation(trainId, seats);
                LOGGER.info("Reservation made {} {}", correlationId, reservationOption.getSeats().size());
                sendReservationRequestResponse(session,trainId, reservationOption, correlationId);
                break;
            }
        }
    }

    private void sendReservationRequestResponse(RpcSession session, int trainId, ReservationOption reservationOption, long correlationId) {
        responseMarshaller.reservationRequestResponse(correlationId, trainId, reservationOption);
        send(session);
    }

    private void sendCreateTrainAck(RpcSession session, Train train, long correlationId) {
        responseMarshaller.createTrainAck(correlationId, train.getId());
        send(session);
    }

    private void send(RpcSession session) {
        session.offer(responseMarshaller.buffer(), responseMarshaller.offset(), responseMarshaller.encodedLength());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageAdapter.class);
    private MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private CreateTrainDecoder createTrainDecoder = new CreateTrainDecoder();
    private ResponseMarshaller responseMarshaller = new ResponseMarshaller();
    private TicketOffice ticketOffice;
    private ReservationRequestDecoder reservationRequestDecoder = new ReservationRequestDecoder();
}
