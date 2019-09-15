package reservation.cluster.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainTest {

    @Test
    void is_created_with_wagons_and_seats() {
        var train = Train.withWagons(3)
                .withSeatsPerWagon(10)
                .build(1);

        assertThat(train).isNotNull();
    }
}
