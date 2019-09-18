import common.aeron.client.ClusterClient;
import io.aeron.cluster.client.EgressListener;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.logbuffer.Header;
import org.agrona.*;
import org.agrona.concurrent.*;
import org.slf4j.*;

public class ReservationClient {
    public static void main(String[] args) {
        var barrier = new ShutdownSignalBarrier();
        try (var client = ClusterClient.launch(ClusterClient.configuration()
                .rootDirectory("./data")
                .egressListener(new MyListener()))) {
            var buffer = new ExpandableDirectByteBuffer(128);
            buffer.putLong(0, 8);
            client.offer(buffer, 0, BitUtil.SIZE_OF_LONG);
            barrier.await();
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
