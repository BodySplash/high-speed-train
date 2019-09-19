package reservation.cluster.domain;

import java.util.*;

public class ReservationOption {

    public static ReservationOption create(int seatsCount) {
        INSTANCE.seats.clear();
        INSTANCE.expectedSeats = seatsCount;
        return INSTANCE;
    }

    public boolean isFullFilled() {
        return seats.size() == expectedSeats;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public int getExpectedSeats() {
        return expectedSeats;
    }

    public void bookSeat(char coachId, int seatNumber) {
        seats.add(Seat.create(coachId, seatNumber));
    }

    private List<Seat> seats = new ArrayList<>();
    private static final ReservationOption INSTANCE = new ReservationOption();
    private int expectedSeats;
}
