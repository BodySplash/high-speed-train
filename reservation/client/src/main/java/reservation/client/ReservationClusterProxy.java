package reservation.client;

import common.aeron.client.ClusterClient;

public class ReservationClusterProxy {
    public ReservationClusterProxy(ClusterClient client) {
        this.client = client;
        marshaller = new MessageMarshaller(client::nextCorrelationId);
    }

    public void createTrain(int id, int coaches, int seats) {
        marshaller.createTrain(id, coaches, seats);
        client.offer(marshaller.buffer(), marshaller.offset(), marshaller.encodedLength());
    }

    private final ClusterClient client;
    private final MessageMarshaller marshaller;
}
