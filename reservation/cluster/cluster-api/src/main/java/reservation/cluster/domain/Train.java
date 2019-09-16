package reservation.cluster.domain;

public class Train {

    public static Factory withCoaches(int number) {
        return new Factory(number);
    }

    private Train(int id, int coaches, int seatsPerCoaches) {
        this.id = id;
        this.coaches = coaches;
        this.seatsPerCoaches = seatsPerCoaches;
    }

    public int getId() {
        return id;
    }

    public int getCoaches() {
        return coaches;
    }

    public int getSeatsPerCoaches() {
        return seatsPerCoaches;
    }

    private int id;
    private int coaches;
    private int seatsPerCoaches;

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
