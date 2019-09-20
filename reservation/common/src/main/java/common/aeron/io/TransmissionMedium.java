package common.aeron.io;

import org.agrona.DirectBuffer;
import org.agrona.concurrent.IdleStrategy;

import static common.aeron.io.TransmissionStatus.*;

public interface TransmissionMedium {

    TransmissionStatus offer(final DirectBuffer buffer, final int offset, final int length);

    default TransmissionStatus offerRepeatedly(final DirectBuffer buffer,
                                               final int offset,
                                               final int length,
                                               final int maxAttempts,
                                               final IdleStrategy idleStrategy) {
        for (int attemptCount = 0; attemptCount < maxAttempts; attemptCount++) {
            var status = offer(buffer, offset, length);
            switch (status) {
                case OK:
                    return OK;
                case CLOSED:
                    return CLOSED;
                default:
                    idleStrategy.idle();
            }
        }

        return BACK_PRESSURE;
    }
}
