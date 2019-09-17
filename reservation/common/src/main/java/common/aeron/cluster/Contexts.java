package common.aeron.cluster;

import io.aeron.archive.*;
import io.aeron.cluster.ConsensusModule;
import io.aeron.cluster.service.ClusteredServiceContainer;
import io.aeron.driver.*;
import org.agrona.concurrent.*;

import java.io.File;
import java.util.function.Supplier;

public class Contexts {

    public static Contexts build(ClusterNode.Configuration configuration) {
        String aeronDirectoryName = String.join(File.separator, configuration.rootDirectory(), "driver");
        String archiveDirectoryName = String.join(File.separator, configuration.rootDirectory(), "archive");
        String clusterDirectoryName = String.join(File.separator, configuration.rootDirectory(), "cluster");
        var idleStrategySupplier = supplierFor(configuration);
        var driverCtx = new MediaDriver.Context()
                .aeronDirectoryName(aeronDirectoryName)
                .threadingMode(driverThreadingMode(configuration))
                .conductorIdleStrategy(idleStrategySupplier.get())
                .receiverIdleStrategy(idleStrategySupplier.get())
                .senderIdleStrategy(idleStrategySupplier.get());
        var archiveCtx = new Archive.Context()
                .aeronDirectoryName(aeronDirectoryName)
                .archiveDirectoryName(archiveDirectoryName)
                .idleStrategySupplier(idleStrategySupplier)
                .threadingMode(archiveThreadingMode(configuration));
        var consensusModuleCtx = new ConsensusModule.Context().aeronDirectoryName(aeronDirectoryName)
                .appointedLeaderId(0)
                .aeronDirectoryName(aeronDirectoryName)
                .clusterDirectoryName(clusterDirectoryName)
                .idleStrategySupplier(idleStrategySupplier)
                .clusterMemberId(0);
        var serviceCtx = new ClusteredServiceContainer.Context()
                .aeronDirectoryName(aeronDirectoryName)
                .clusterDirectoryName(clusterDirectoryName)
                .clusteredService(configuration.service());
        return new Contexts(driverCtx, archiveCtx, consensusModuleCtx, serviceCtx);

    }

    private static ArchiveThreadingMode archiveThreadingMode(ClusterNode.Configuration configuration) {
        switch (configuration.profile()) {

            case PERF:
                return ArchiveThreadingMode.DEDICATED;
            case SLOW:
            default:
                return ArchiveThreadingMode.SHARED;
        }
    }

    private static ThreadingMode driverThreadingMode(ClusterNode.Configuration configuration) {
        switch (configuration.profile()) {

            case PERF:
                return ThreadingMode.DEDICATED;
            case SLOW:
            default:
                return ThreadingMode.SHARED;
        }
    }

    private static Supplier<IdleStrategy> supplierFor(ClusterNode.Configuration configuration) {
        switch (configuration.profile()) {
            case PERF:
                return () -> new BusySpinIdleStrategy();
            case SLOW:
            default:
                return () -> new SleepingMillisIdleStrategy(10);
        }
    }

    public Contexts(MediaDriver.Context driverCtx, Archive.Context archiveCtx, ConsensusModule.Context consensusModuleCtx, ClusteredServiceContainer.Context serviceCtx) {
        this.driverCtx = driverCtx;
        this.archiveCtx = archiveCtx;
        this.consensusModuleCtx = consensusModuleCtx;
        this.serviceCtx = serviceCtx;
    }

    public final MediaDriver.Context driverCtx;
    public final Archive.Context archiveCtx;
    public final ConsensusModule.Context consensusModuleCtx;
    public final ClusteredServiceContainer.Context serviceCtx;
}
