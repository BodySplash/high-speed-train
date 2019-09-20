package common.aeron.client;

import common.aeron.io.*;
import io.aeron.cluster.client.*;
import io.aeron.cluster.codecs.EventCode;
import io.aeron.driver.MediaDriver;
import io.aeron.logbuffer.Header;
import org.agrona.*;
import org.agrona.concurrent.*;
import org.slf4j.*;

public class ClusterClient implements AutoCloseable {

    public static ClusterClient launch(Configuration configuration) {
        var contexts = Contexts.build(configuration);
        var client = new ClusterClient(contexts);
        client.start();
        return client;
    }

    private ClusterClient(Contexts contexts) {
        this.contexts = contexts;
    }

    public static Configuration configuration() {
        return new Configuration();
    }

    private void start() {
        driver = MediaDriver.launch(contexts.driverCtx);
        cluster = AeronCluster.connect(contexts.clientCtx.egressListener(new InnerEgressListener()));
        transmission = new AeronClusterTransmissionMedium(cluster);
        var agent = new ClusterClientAgent();
        agentRunner = new AgentRunner(new SleepingMillisIdleStrategy(10), err -> LOGGER.error("Error in cluster client", err), null, agent);
        AgentRunner.startOnThread(agentRunner);
        LOGGER.info("Connected");
    }

    public void subscribe(ClusterMessageSubscription subscription) {
        this.subscription = subscription;
    }

    public TransmissionStatus offer(DirectBuffer buffer, int offset, int length) {
        return transmission.offerRepeatedly(buffer, offset, length, 10, sendIdleStrategy);
    }

    public long nextCorrelationId() {
        return cluster.context().aeron().nextCorrelationId();
    }

    @Override
    public void close() {
        CloseHelper.close(agentRunner);
        CloseHelper.close(cluster);
        CloseHelper.close(driver);
    }

    private final Contexts contexts;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterClient.class);

    private MediaDriver driver;
    private AeronCluster cluster;
    private AgentRunner agentRunner;
    private ClusterMessageSubscription subscription;
    private AeronClusterTransmissionMedium transmission;
    private SleepingMillisIdleStrategy sendIdleStrategy = new SleepingMillisIdleStrategy(10);

    public static class Configuration {

        private Configuration() {
        }

        public Configuration rootDirectory(String aeronDirectoryName) {
            this.aeronDirectoryName = aeronDirectoryName;
            return this;
        }

        String rootDirectory() {
            return aeronDirectoryName;
        }

        private String aeronDirectoryName;
    }

    private class ClusterClientAgent implements Agent {

        @Override
        public int doWork() {
            cluster.sendKeepAlive();
            return cluster.pollEgress();
        }

        @Override
        public String roleName() {
            return "cluster-client-agent";
        }
    }

    private class InnerEgressListener implements EgressListener {

        @Override
        public void onMessage(long clusterSessionId, long timestamp, DirectBuffer buffer, int offset, int length, Header header) {
            if (subscription == null) return;
            subscription.accept(buffer, offset, length);
        }

        @Override
        public void sessionEvent(long correlationId, long clusterSessionId, long leadershipTermId, int leaderMemberId, EventCode code, String detail) {
            LOGGER.info("Event {} {}", code, detail);
        }

        @Override
        public void newLeader(long clusterSessionId, long leadershipTermId, int leaderMemberId, String memberEndpoints) {
            LOGGER.info("new leader {}", leaderMemberId);
        }
    }
}
