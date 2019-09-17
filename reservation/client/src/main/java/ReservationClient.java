import common.aeron.client.ClusterClient;
import org.agrona.concurrent.ShutdownSignalBarrier;

public class ReservationClient {

    public static void main(String[] args) {
        var barrier = new ShutdownSignalBarrier();
        try (var clusterClient = new ClusterClient()) {
            clusterClient.start();
            System.out.println("connected");
            barrier.await();
        }
    }
}
