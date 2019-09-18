package reservation.cluster;

import org.agrona.concurrent.AtomicBuffer;
import org.slf4j.LoggerFactory;
import reservation.cluster.api.*;

public class MessageAdapter {

    public void adapt(AtomicBuffer buffer) {
        headerDecoder.wrap(buffer, 0);
        switch (headerDecoder.templateId()) {
            case CreateTrainDecoder.TEMPLATE_ID:
                createTrainDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
                LoggerFactory.getLogger(MessageAdapter.class).info("Create train {} {} {}", createTrainDecoder.trainId(), createTrainDecoder.coachesNumber(), createTrainDecoder.seatsNumber());
        }
    }

    private MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private CreateTrainDecoder createTrainDecoder = new CreateTrainDecoder();
}
