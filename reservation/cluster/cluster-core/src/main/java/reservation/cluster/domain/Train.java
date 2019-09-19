package reservation.cluster.domain;


import common.Chars;

import java.util.*;

public class Train {

    public static Factory withCoaches(int number) {
        return new Factory(number);
    }

    private Train(int id, int coachesCount, int seatsPerCoaches) {
        this.id = id;
        for (int i = 1; i <= coachesCount; i++) {
            this.coaches.add(Coach.create(seatsPerCoaches, Chars.toChar(i)));
        }
    }

    public int getId() {
        return id;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public ReservationOption findReservation(int seatsCount) {
        return ReservationOption.create();
    }

    private int id;
    private List<Coach> coaches = new LinkedList<>();

    public static class Factory {
        public Factory(int coaches) {
            this.coaches = coaches;
        }

        public Factory withSeatsPerCoach(int number) {
            seatsPerCoaches = number;
            return this;
        }

        public Train build(int id) {
            return new Train(id, coaches, seatsPerCoaches);
        }

        private int coaches;
        private int seatsPerCoaches;
    }
}
