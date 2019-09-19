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


    private final char id;
    private final int seatsNumber;
}
