package reservation.cluster.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainTest {

    @Test
    void is_created_with_wagons_and_seats() {
        var train = Train.withCoaches(3)
                .withSeatsPerCoach(10)
                .build(1);

        assertThat(train).isNotNull();
        assertThat(train.getId()).isEqualTo(1);
        assertThat(train.getCoaches()).isEqualTo(3);
        assertThat(train.getSeatsPerCoaches()).isEqualTo(10);
    }
}
