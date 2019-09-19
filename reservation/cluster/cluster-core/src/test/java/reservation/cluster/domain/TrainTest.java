package reservation.cluster.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainTest {

    @Test
    void is_created_with_wagons_and_seats() {
        var train = Train.withCoaches(2)
                .withSeatsPerCoach(10)
                .build(1);

        assertThat(train).isNotNull();
        assertThat(train.getId()).isEqualTo(1);
        assertThat(train.getCoaches()).hasSize(2);
        var firstCoach = train.getCoaches().get(0);
        assertThat(firstCoach.getId()).isEqualTo('A');
        assertThat(firstCoach.getSeatsNumber()).isEqualTo(10);
        assertThat(train.getCoaches().get(1).getId()).isEqualTo('B');
    }

    @Test
    void can_book_one_seat() {
        var train = aTrainWith1CoachAnd10Seats();

        var reservation = train.findReservation(1);

        assertThat(reservation).isNotNull();
        assertThat(reservation.getSeats()).hasSize(1);
        var seat = reservation.getSeats().get(0);
        assertThat(seat.getCoach()).isEqualTo('A');
        assertThat(seat.getNumber()).isEqualTo(1);
    }

    @Test
    void can_book_several_seats() {
        var train = aTrainWith1CoachAnd10Seats();

        var reservation = train.findReservation(2);

        assertThat(reservation.getSeats()).hasSize(2);
        assertThat(reservation.getSeats().get(1).getNumber()).isEqualTo(2);
    }

    @Test
    void returns_empty_option_if_not_enough_seats() {
        var train = aTinyTrain();
        train.findReservation(1);

        var reservation = train.findReservation(1);

        assertThat(reservation.isFullFilled()).isFalse();
        assertThat(reservation.getSeats()).isEmpty();
    }

    @Test
    void returns_empty_option_if_seats_number_exceeds_70_percent() {
        var train = aTrainWith1CoachAnd10Seats();

        var reservation = train.findReservation(8);

        assertThat(reservation.isFullFilled()).isFalse();
    }

    private Train aTrainWith1CoachAnd10Seats() {
        return Train.withCoaches(1)
                .withSeatsPerCoach(10)
                .build(1);
    }

    private Train aTinyTrain() {
        return Train.withCoaches(1)
                .withSeatsPerCoach(1)
                .build(1);
    }
}
