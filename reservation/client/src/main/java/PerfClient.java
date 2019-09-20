import common.aeron.client.ClusterClient;
import org.agrona.concurrent.ShutdownSignalBarrier;
import reservation.client.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PerfClient {

    public static void main(String[] args) {
        try (var client = ClusterClient.launch(ClusterClient.configuration()
                .rootDirectory("./data"))) {
            var proxy = new ReservationClusterProxy(client, new PerfMetterConsumer(RESERVATIONS));
            client.subscribe(proxy);
            proxy.createTrain(1000, 1000);
            for (int i = 0; i < RESERVATIONS; i++) {
                proxy.makeReservation(1, 1);
            }
            BARRIER.await();
        }
    }

    public static final int RESERVATIONS = 100_000;
    public static final ShutdownSignalBarrier BARRIER = new ShutdownSignalBarrier();

    static class PerfMetterConsumer implements ResponseConsumer {

        public PerfMetterConsumer(int expected) {
            this.expected = expected;
        }

        @Override
        public void onTrainCreated(long correlationId, int trainId) {
            start = System.nanoTime();
        }

        @Override
        public void onReservationMade(long correlationId, int trainId, ArrayList<Seat> seats) {
            count++;
            if (count % 10000 == 0) System.out.println(count);
            var end = System.nanoTime();
            if (count == expected) {
                System.out.println(String.format("%d created in %d ms", RESERVATIONS, TimeUnit.NANOSECONDS.toMillis(end - start)));
                BARRIER.signal();
            }
        }

        private long start;
        private int count;
        private int expected;
    }

}


