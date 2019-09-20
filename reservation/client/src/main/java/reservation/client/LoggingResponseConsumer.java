package reservation.client;

import java.util.ArrayList;

public class LoggingResponseConsumer implements ResponseConsumer {

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
}
