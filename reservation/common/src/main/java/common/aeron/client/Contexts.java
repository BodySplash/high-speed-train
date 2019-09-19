package common.aeron.client;

import io.aeron.cluster.client.AeronCluster;
import io.aeron.driver.*;
import org.agrona.concurrent.SleepingMillisIdleStrategy;

import java.io.File;

class Contexts {

    static Contexts build(ClusterClient.Configuration configuration) {
        var aeronDirectoryName = String.join(File.separator, configuration.rootDirectory(), "driver");
        var driverCtx = new MediaDriver.Context()
                .aeronDirectoryName(aeronDirectoryName)
                .threadingMode(ThreadingMode.SHARED)
                .conductorIdleStrategy(new SleepingMillisIdleStrategy(10))
                .receiverIdleStrategy(new SleepingMillisIdleStrategy(10))
                .senderIdleStrategy(new SleepingMillisIdleStrategy(10));
        var clientCtx = new AeronCluster.Context()
                .isIngressExclusive(false)
                .aeronDirectoryName(aeronDirectoryName);
        return new Contexts(driverCtx, clientCtx);
    }

    private Contexts(MediaDriver.Context driverCtx, AeronCluster.Context clientCtx) {
        this.driverCtx = driverCtx;
        this.clientCtx = clientCtx;
    }

    final MediaDriver.Context driverCtx;
    final AeronCluster.Context clientCtx;
}
