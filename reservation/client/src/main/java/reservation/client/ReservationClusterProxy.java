package reservation.client;

import common.aeron.client.ClusterClient;

public class ReservationClusterProxy {
    public ReservationClusterProxy(ClusterClient client) {
        this.client = client;
    }

    public void createTrain(int id, int coaches, int seats) {
        marshaller.createTrain(id, coaches, seats);
        client.offer(marshaller.buffer(), marshaller.offset(), marshaller.encodedLength());
    }

    private ClusterClient client;
    private MessageMarshaller marshaller = new MessageMarshaller();
}
