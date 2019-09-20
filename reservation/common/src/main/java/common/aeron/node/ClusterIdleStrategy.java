package common.aeron.node;

import io.aeron.cluster.service.Cluster;
import org.agrona.concurrent.IdleStrategy;

public class ClusterIdleStrategy implements IdleStrategy {
    public ClusterIdleStrategy(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public void idle(int i) {
        cluster.idle(i);
    }

    @Override
    public void idle() {
        cluster.idle();
    }

    @Override
    public void reset() {
    }

    private final Cluster cluster;
}
