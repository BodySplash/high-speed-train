package common.aeron.node;

import common.aeron.io.*;
import io.aeron.cluster.service.ClientSession;
import org.agrona.DirectBuffer;
import org.agrona.concurrent.IdleStrategy;

public class RpcSession {

    public RpcSession(ClientSession session, ClusterIdleStrategy idleStrategy) {
        this.session = session;
        this.idleStrategy = idleStrategy;
        transmission = new AeronSessionTransmissionMedium(session);
    }

    public TransmissionStatus offer(DirectBuffer buffer, int offset, int length) {
        return transmission.offerRepeatedly(buffer, offset, length, 10, idleStrategy);
    }

    public long id() {
        return session.id();
    }

    private final ClientSession session;
    private final IdleStrategy idleStrategy;
    private final AeronSessionTransmissionMedium transmission;
}
