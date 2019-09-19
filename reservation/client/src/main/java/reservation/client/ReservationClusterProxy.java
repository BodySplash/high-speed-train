package reservation.client;

import common.aeron.client.*;
import org.agrona.DirectBuffer;

public class ReservationClusterProxy implements ClusterMessageSubscription {

    public ReservationClusterProxy(ClusterClient client) {
        this.client = client;
        marshaller = new MessageMarshaller(client::nextCorrelationId);
    }

    public void createTrain(int coaches, int seats) {
        marshaller.createTrain(coaches, seats);
        client.offer(marshaller.buffer(), marshaller.offset(), marshaller.encodedLength());
    }

    @Override
    public void accept(DirectBuffer buffer, int offset, int length) {

    }

    private final ClusterClient client;
    private final MessageMarshaller marshaller;
}
