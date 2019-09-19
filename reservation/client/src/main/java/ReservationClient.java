import common.aeron.client.ClusterClient;
import io.aeron.cluster.client.EgressListener;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.slf4j.*;
import reservation.client.ReservationClusterProxy;

import java.util.Scanner;

public class ReservationClient {

    public static void main(String[] args) {
        try (var client = ClusterClient.launch(ClusterClient.configuration()
                .rootDirectory("./data")
                .egressListener(new MyListener()))) {
            var proxy = new ReservationClusterProxy(client);
            var scanner = new Scanner(System.in);
            while (true) {
                System.out.print('>');
                var command = scanner.nextLine();
                if ("exit".equals(command)) {
                    break;
                }
                if (command.startsWith("ct")) {
                    var params = command.split(" ");
                    proxy.createTrain(Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                }
            }

        }
    }

    private static class MyListener implements EgressListener {
        @Override
        public void onMessage(long clusterSessionId, long timestamp, DirectBuffer buffer, int offset, int length, Header header) {

        }

        @Override
        public void sessionEvent(long correlationId, long clusterSessionId, long leadershipTermId, int leaderMemberId, EventCode code, String detail) {
            LOGGER.info("Event {} {}", code, detail);
        }

        @Override
        public void newLeader(long clusterSessionId, long leadershipTermId, int leaderMemberId, String memberEndpoints) {
            LOGGER.info("new leader {}", leaderMemberId);
        }

        static final Logger LOGGER = LoggerFactory.getLogger(MyListener.class);
    }
}
