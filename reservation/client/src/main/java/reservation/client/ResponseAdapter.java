package reservation.client;

import org.agrona.DirectBuffer;
import reservation.cluster.api.*;

import java.util.ArrayList;

public class ResponseAdapter {

    public void adapt(DirectBuffer buffer, int offset, ResponseConsumer consumer) {
        headerDecoder.wrap(buffer, offset);
        switch (headerDecoder.templateId()) {
            case CreateTrainResponseDecoder.TEMPLATE_ID:
                createTrainResponseDecoder.wrap(buffer, headerDecoder.encodedLength() + offset, headerDecoder.encodedLength(), headerDecoder.version());
                consumer.onTrainCreated(createTrainResponseDecoder.correlationId(), createTrainResponseDecoder.trainId());
                break;
            case ReservationRequestResponseDecoder.TEMPLATE_ID:
                reservationRequestResponseDecoder.wrap(buffer, offset + headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
                var correlationId = reservationRequestResponseDecoder.correlationId();
                var trainId = reservationRequestResponseDecoder.trainId();
                var seats = new ArrayList<Seat>();
                for (ReservationRequestResponseDecoder.SeatsDecoder seat : reservationRequestResponseDecoder.seats()) {
                    seats.add(new Seat((char) seat.coach(), seat.number()));
                }
                consumer.onReservationMade(correlationId, trainId, seats);
        }
    }

    private MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private CreateTrainResponseDecoder createTrainResponseDecoder = new CreateTrainResponseDecoder();
    private ReservationRequestResponseDecoder reservationRequestResponseDecoder = new ReservationRequestResponseDecoder();
}
