package common.aeron.node;

import io.aeron.archive.ArchivingMediaDriver;
import io.aeron.cluster.ConsensusModule;
import io.aeron.cluster.service.*;
import org.agrona.CloseHelper;
import org.slf4j.*;

public class ClusterNode implements AutoCloseable {

    public static Configuration configuration() {
        return new Configuration();
    }

    public static ClusterNode launch(Configuration configuration) {
        var clusterNode = new ClusterNode(Contexts.build(configuration));
        clusterNode.start();
        return clusterNode;
    }

    private void start() {
        mediaDriver = ArchivingMediaDriver.launch(contexts.driverCtx, contexts.archiveCtx);
        consensusModule = ConsensusModule.launch(contexts.consensusModuleCtx);
        node = ClusteredServiceContainer.launch(contexts.serviceCtx);
        LOGGER.info("Running");
    }

    public ClusterNode(Contexts contexts) {
        this.contexts = contexts;
    }

    public void awaitShutDown() {
        node.context().shutdownSignalBarrier().await();
    }

    @Override
    public void close() {
        CloseHelper.close(node);
        CloseHelper.close(consensusModule);
        CloseHelper.close(mediaDriver);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterNode.class);
    private Contexts contexts;
    private ArchivingMediaDriver mediaDriver;
    private ConsensusModule consensusModule;
    private ClusteredServiceContainer node;

    public static class Configuration {

        private Profile profile = Profile.SLOW;
        private String rootDirectory = "./data";
        private ClusteredService service;
        private int memberId = 0;

        public Configuration profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public Profile profile() {
            return profile;
        }

        public Configuration rootDirectory(String rootDirectory) {
            this.rootDirectory = rootDirectory;
            return this;
        }

        public String rootDirectory() {
            return rootDirectory;
        }

        public Configuration service(ClusteredService service) {
            this.service = service;
            return this;
        }

        public ClusteredService service() {
            return service;
        }

        public Configuration memberId(int memberId) {
            this.memberId = memberId;
            return this;
        }

        public int memberId() {
            return memberId;
        }
    }
}
