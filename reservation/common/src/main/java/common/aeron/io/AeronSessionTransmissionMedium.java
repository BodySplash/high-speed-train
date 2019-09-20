package common.aeron.io;

import io.aeron.cluster.service.ClientSession;
import org.agrona.DirectBuffer;

public class AeronSessionTransmissionMedium implements TransmissionMedium {

    public AeronSessionTransmissionMedium(ClientSession session) {
        this.session = session;
    }

    @Override
    public TransmissionStatus offer(DirectBuffer buffer, int offset, int length) {
        return TransmissionStatus.fromAeronCode(session.offer(buffer, offset, length));
    }

    private ClientSession session;
}
