package reservation.client;

import common.aeron.client.*;
import org.agrona.DirectBuffer;

public class ReservationClusterProxy implements ClusterMessageSubscription {

    public ReservationClusterProxy(ClusterClient client, ResponseConsumer responseConsumer) {
        this.client = client;
        marshaller = new MessageMarshaller(client::nextCorrelationId);
        this.responseConsumer = responseConsumer;
    }

    public long createTrain(int coaches, int seats) {
        var correlationId = marshaller.createTrain(coaches, seats);
        send();
        return correlationId;
    }

    public long makeReservation(int trainId, int seatsCount) {
        var correlationId = marshaller.makeReservation(trainId, seatsCount);
        send();
        return correlationId;
    }

    private void send() {
        client.offer(marshaller.buffer(), marshaller.offset(), marshaller.encodedLength());
    }

    @Override
    public void accept(DirectBuffer buffer, int offset, int length) {
        adapter.adapt(buffer, offset, responseConsumer);
    }

    private final ClusterClient client;
    private final ResponseAdapter adapter = new ResponseAdapter();
    private final MessageMarshaller marshaller;
    private final ResponseConsumer responseConsumer;

}
