package reservation.client;

import common.aeron.client.*;
import org.agrona.DirectBuffer;

import java.util.ArrayList;

public class ReservationClusterProxy implements ClusterMessageSubscription, ResponseConsumer {

    public ReservationClusterProxy(ClusterClient client) {
        this.client = client;
        marshaller = new MessageMarshaller(client::nextCorrelationId);
    }

    public void createTrain(int coaches, int seats) {
        marshaller.createTrain(coaches, seats);
        send();
    }

    public void makeReservation(int trainId, int seatsCount) {
        marshaller.makeReservation(trainId, seatsCount);
        send();
    }

    private void send() {
        client.offer(marshaller.buffer(), marshaller.offset(), marshaller.encodedLength());
    }

    @Override
    public void accept(DirectBuffer buffer, int offset, int length) {
        adapter.adapt(buffer, offset, this);
    }

    @Override
    public void onTrainCreated(long correlationId, int trainId) {
        System.out.println();
        System.out.println(String.format("Train created %d %d", correlationId, trainId));
        System.out.print(">");
    }

    @Override
    public void onReservationMade(long correlationId, int trainId, ArrayList<Seat> seats) {
        System.out.println();
        System.out.print(String.format("Reservation result in %d : ", trainId));
        for (Seat seat : seats) {
            System.out.print(String.format("[%s, %s]", seat.getCoach(), seat.getNumber()));
        }
        System.out.println();
        System.out.print(">");
    }

    private final ClusterClient client;
    private final ResponseAdapter adapter = new ResponseAdapter();
    private final MessageMarshaller marshaller;
}
