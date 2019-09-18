package reservation.cluster;

import io.aeron.*;
import io.aeron.cluster.codecs.CloseReason;
import io.aeron.cluster.service.*;
import io.aeron.logbuffer.Header;
import org.agrona.DirectBuffer;
import org.slf4j.*;

public class ReservationService implements ClusteredService {

    @Override
    public void onStart(Cluster cluster, Image snapshotImage) {
        LOGGER.info("STARTING");
    }

    @Override
    public void onSessionOpen(ClientSession session, long timestamp) {
        LOGGER.info("new session {}", session.id());
    }

    @Override
    public void onSessionClose(ClientSession session, long timestamp, CloseReason closeReason) {
        LOGGER.info("session closed {}", session.id());
    }

    @Override
    public void onSessionMessage(ClientSession session, long timestamp, DirectBuffer buffer, int offset, int length, Header header) {
        LOGGER.info("Session message {}", session.id());
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
}
