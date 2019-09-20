package reservation.client;

import java.util.ArrayList;

public interface ResponseConsumer {

    void onTrainCreated(long correlationId, int trainId);

    void onReservationMade(long correlationId, int trainId, ArrayList<Seat> seats);
}
