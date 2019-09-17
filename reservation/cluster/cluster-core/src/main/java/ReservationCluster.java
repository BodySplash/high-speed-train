import common.aeron.cluster.*;
import reservation.cluster.ReservationService;

public class ReservationCluster {

    public static void main(String[] args) {
        try (var launch = ClusterNode.launch(new ClusterNodeConfiguration()
                .mode(ClusterNodeConfiguration.Mode.LOCAL)
                .memberId(0)
                .rootDirectory("./data")
                .service(new ReservationService()))) {
            launch.awaitShutDown();
        }

    }
}
