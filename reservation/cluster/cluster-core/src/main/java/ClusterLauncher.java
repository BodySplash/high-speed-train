import io.aeron.archive.*;
import io.aeron.cluster.ConsensusModule;
import io.aeron.cluster.service.*;
import io.aeron.driver.*;
import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.slf4j.*;
import reservation.cluster.ReservationService;

public class ClusterLauncher {

    public static void main(String[] args) {
        var ctx = new MediaDriver.Context()
                .aeronDirectoryName(AERON_DIRECTORY_NAME)
                .threadingMode(ThreadingMode.SHARED)
                .conductorIdleStrategy(idleStrategy())
                .receiverIdleStrategy(idleStrategy())
                .senderIdleStrategy(idleStrategy());
        var archiveCtx = new Archive.Context()
                .aeronDirectoryName(AERON_DIRECTORY_NAME)
                .archiveDirectoryName(ARCHIVE_DIRECTORY_NAME)
                .idleStrategySupplier(ClusterLauncher::idleStrategy);
        var consensusModuleCtx = new ConsensusModule.Context().aeronDirectoryName(AERON_DIRECTORY_NAME)
                .appointedLeaderId(0)
                .aeronDirectoryName(AERON_DIRECTORY_NAME)
                .clusterDirectoryName(CLUSTER_DIRECTORY_NAME)
                .idleStrategySupplier(ClusterLauncher::idleStrategy)
                .clusterMemberId(0);

        try (var mediaDriver = ArchivingMediaDriver.launch(ctx, archiveCtx);
             var consensusModule = ConsensusModule.launch(consensusModuleCtx)) {
            var node = ClusteredServiceContainer.launch(new ClusteredServiceContainer.Context()
                    .aeronDirectoryName(AERON_DIRECTORY_NAME)
                    .clusterDirectoryName(CLUSTER_DIRECTORY_NAME)
                    .clusteredService(new ReservationService()));
            LOGGER.info("Running");
            node.context().shutdownSignalBarrier().await();
        }


        //ClusteredServiceContainer.
    }

    private static SleepingMillisIdleStrategy idleStrategy() {
        return new SleepingMillisIdleStrategy(10);
    }

    public static final String AERON_DIRECTORY_NAME = "./data/driver";
    public static final String ARCHIVE_DIRECTORY_NAME = "./data/archive";
    public static final String CLUSTER_DIRECTORY_NAME = "./data/cluster";
    public static final Logger LOGGER = LoggerFactory.getLogger(ClusterLauncher.class);

}
