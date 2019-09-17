package common.aeron.client;

import io.aeron.cluster.client.AeronCluster;
import io.aeron.driver.*;
import org.agrona.CloseHelper;
import org.agrona.concurrent.SleepingMillisIdleStrategy;

public class ClusterClient implements AutoCloseable {

    public AeronCluster start() {
        driver = MediaDriver.launch(new MediaDriver.Context()
                .aeronDirectoryName("./data/driver")
                .threadingMode(ThreadingMode.SHARED)
                .conductorIdleStrategy(new SleepingMillisIdleStrategy(10))
                .receiverIdleStrategy(new SleepingMillisIdleStrategy(10))
                .senderIdleStrategy(new SleepingMillisIdleStrategy(10)));
        cluster = AeronCluster.connect(new AeronCluster.Context()
                .aeronDirectoryName("./data/driver"));
        return cluster;
    }

    @Override
    public void close() {
        CloseHelper.close(cluster);
        CloseHelper.close(driver);
    }

    private MediaDriver driver;
    private AeronCluster cluster;
}
