package reservation.cluster.domain;

public class Train {
    public static Factory withWagons(int number) {
        return new Factory(number);
    }

    public static class Factory {
        public Factory(int wagonsNumber) {
            this.wagonsNumber = wagonsNumber;
        }

        public Factory withSeatsPerWagon(int number) {
            seatsNumber = number;
            return this;
        }

        public Train build(int id) {
            return null;
        }

        private int wagonsNumber;
        private int seatsNumber;
    }
}
