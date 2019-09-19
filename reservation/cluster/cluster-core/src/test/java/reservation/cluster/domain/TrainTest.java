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
        var train = Train.withCoaches(1)
                .withSeatsPerCoach(1)
                .build(1);

        var reservation = train.findReservation(1);

        assertThat(reservation).isNotNull();

    }
}
