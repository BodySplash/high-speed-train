package reservation.cluster.domain;

import common.Pool;

public class Seat {

    static Seat create(char coachId, int seatNumber) {
        var result = POOL.acquire();
        result.coach = coachId;
        result.number = seatNumber;
        return result;
    }

    private Seat() {

    }

    public char getCoach() {
        return coach;
    }

    public int getNumber() {
        return number;
    }

    static Pool<Seat> POOL = new Pool<>(100000, Seat::new, false);
    private char coach;
    private int number;
}
