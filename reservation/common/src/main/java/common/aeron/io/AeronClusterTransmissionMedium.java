package common.aeron.io;

import io.aeron.cluster.client.AeronCluster;
import org.agrona.DirectBuffer;

public class AeronClusterTransmissionMedium implements TransmissionMedium {

    public AeronClusterTransmissionMedium(AeronCluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public TransmissionStatus offer(DirectBuffer buffer, int offset, int length) {
        return TransmissionStatus.fromAeronCode(cluster.offer(buffer, offset, length));
    }

    private AeronCluster cluster;
}
