package common.aeron.cluster;

import io.aeron.archive.ArchivingMediaDriver;
import io.aeron.cluster.ConsensusModule;
import io.aeron.cluster.service.ClusteredServiceContainer;
import org.agrona.CloseHelper;
import org.slf4j.*;

public class ClusterNode implements AutoCloseable {

    public static ClusterNode launch(ClusterNodeConfiguration configuration) {
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
}
