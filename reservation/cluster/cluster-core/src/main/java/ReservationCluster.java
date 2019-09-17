import common.aeron.node.*;
import reservation.cluster.ReservationService;

public class ReservationCluster {

    public static void main(String[] args) {
        try (var launch = ClusterNode.launch(ClusterNode.configuration()
                .profile(Profile.SLOW)
                .memberId(0)
                .rootDirectory("./data")
                .service(new ReservationService()))) {
            launch.awaitShutDown();
        }

    }

}
