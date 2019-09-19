package common.aeron.client;

import io.aeron.cluster.client.*;
import io.aeron.driver.MediaDriver;
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
        cluster = AeronCluster.connect(contexts.clientCtx);
        var agent = new ClusterClientAgent();
        agentRunner = new AgentRunner(new SleepingMillisIdleStrategy(10), err -> LOGGER.error("Error in cluster client", err), null, agent);
        AgentRunner.startOnThread(agentRunner);
        LOGGER.info("Connected");
    }

    public long offer(DirectBuffer buffer, int offset, int length) {
        return cluster.offer(buffer, offset, length);
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

    public static class Configuration {

        private Configuration() {
        }

        public Configuration egressListener(EgressListener listener) {
            this.listener = listener;
            return this;
        }

        EgressListener egressListener() {
            return listener;
        }

        public Configuration rootDirectory(String aeronDirectoryName) {
            this.aeronDirectoryName = aeronDirectoryName;
            return this;
        }

        String rootDirectory() {
            return aeronDirectoryName;
        }

        private EgressListener listener;
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
}
