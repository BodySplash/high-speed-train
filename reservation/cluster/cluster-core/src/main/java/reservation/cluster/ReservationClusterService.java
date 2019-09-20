package reservation.cluster;

import common.aeron.node.*;
import io.aeron.*;
import io.aeron.cluster.codecs.CloseReason;
import io.aeron.cluster.service.*;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.agrona.collections.Long2ObjectHashMap;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.*;
import reservation.cluster.domain.TicketOffice;
import reservation.cluster.infra.MemoryTrainRepository;

public class ReservationClusterService implements ClusteredService {

    @Override
    public void onStart(Cluster cluster, Image snapshotImage) {
        LOGGER.info("STARTING");
        this.cluster = cluster;
        this.clusterIdleStrategy = new ClusterIdleStrategy(cluster);
    }

    @Override
    public void onSessionOpen(ClientSession session, long timestamp) {
        LOGGER.info("new session {}", session.id());
        var rpcSession = new RpcSession(session, clusterIdleStrategy);
        sessions.put(rpcSession.id(), rpcSession);
    }

    @Override
    public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason) {
        LOGGER.info("session closed {}", session.id());
        sessions.remove(session.id());
    }

    @Override
    public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer, int offset, int length, Header header) {
        LOGGER.info("Session message {}", session.id());
        this.buffer.wrap(buffer, offset, length);
        messageAdapter.adapt(sessions.get(session.id()), this.buffer);
    }

    @Override
    public void onTimerEvent(long correlationId, long timestamp) {

    }

    @Override
    public void onTakeSnapshot(Publication snapshotPublication) {

    }

    @Override
    public void onRoleChange(Cluster.Role newRole) {
        LOGGER.info("Role changed {}", newRole);
    }

    @Override
    public void onTerminate(Cluster cluster) {
        LOGGER.info("Shutting down");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationClusterService.class);
    private MessageAdapter messageAdapter = new MessageAdapter(new TicketOffice(new MemoryTrainRepository()));
    private UnsafeBuffer buffer = new UnsafeBuffer();
    private Cluster cluster;
    private ClusterIdleStrategy clusterIdleStrategy;
    private Long2ObjectHashMap<RpcSession> sessions = new Long2ObjectHashMap<>();
}
