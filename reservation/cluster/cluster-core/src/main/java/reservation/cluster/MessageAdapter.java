package reservation.cluster;

import common.aeron.node.RpcSession;
import org.agrona.concurrent.AtomicBuffer;
import org.slf4j.LoggerFactory;
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
            case CreateTrainDecoder.TEMPLATE_ID:
                createTrainDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
                var correlationId = createTrainDecoder.correlationId();
                var coachesNumber = createTrainDecoder.coachesNumber();
                var seatsNumber = createTrainDecoder.seatsNumber();
                LoggerFactory.getLogger(MessageAdapter.class).info("Create train {} {} {}", correlationId, coachesNumber, seatsNumber);
                var train = ticketOffice.createTrain(coachesNumber, seatsNumber);
                sendCreateTrainAck(session, train, correlationId);
                break;
        }
    }

    private void sendCreateTrainAck(RpcSession session, Train train, long correlationId) {
        responseMarshaller.createTrainAck(correlationId, train.getId());
        session.offer(responseMarshaller.buffer(), responseMarshaller.offset(), responseMarshaller.encodedLength());
    }

    private MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private CreateTrainDecoder createTrainDecoder = new CreateTrainDecoder();
    private ResponseMarshaller responseMarshaller = new ResponseMarshaller();
    private TicketOffice ticketOffice;
}
