package reservation.client;

public class Seat {
    public Seat(char coach, int number) {
        this.coach = coach;
        this.number = number;
    }

    public char getCoach() {
        return coach;
    }

    public int getNumber() {
        return number;
    }

    private final char coach;
    private final int number;
}
