package reservation.cluster.domain;

public class TicketOffice {

    public TicketOffice(TrainRepository repository) {
        this.repository = repository;
    }

    public Train createTrain(int coachesCount, int seatsCounts) {
        var train = Train.withCoaches(coachesCount)
                .withSeatsPerCoach(seatsCounts)
                .build(repository.nextId());
        repository.add(train);
        return train;
    }

    public ReservationOption requestReservation(int trainId, int seatsCount) {
        var train = repository.get(trainId);
        if(train == null) {
            return ReservationOption.create(seatsCount);
        }
        return train.findReservation(seatsCount);
    }

    private TrainRepository repository;
}
