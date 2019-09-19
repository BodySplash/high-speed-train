package reservation.cluster.domain;

public class Coach {

    public static Coach create(int seatsPerCoaches, char id) {
        return new Coach(id, seatsPerCoaches);
    }

    private Coach(char id, int seatsPerCoaches) {
        this.id = id;
        seatsNumber = seatsPerCoaches;
    }

    public char getId() {
        return id;
    }

    public int getSeatsNumber() {
        return seatsNumber;
    }

    public void tryFill(ReservationOption result) {
        if (result.getExpectedSeats() > (seatsNumber - reservationCount) * 0.7) {
            return;
        }
        while (!result.isFullFilled()) {
            result.bookSeat(id, ++reservationCount);
        }
    }


    private final char id;
    private final int seatsNumber;
    private int reservationCount;
}
