package common.aeron.client;

import org.agrona.DirectBuffer;

public interface ClusterMessageSubscription {

    void accept(DirectBuffer buffer, int offset, int length);
}
