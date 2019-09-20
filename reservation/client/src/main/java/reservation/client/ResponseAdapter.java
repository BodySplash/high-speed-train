package reservation.client;

import org.agrona.DirectBuffer;
import reservation.cluster.api.*;

public class ResponseAdapter {

    public void adapt(DirectBuffer buffer, int offset, ResponseConsumer consumer) {
        headerDecoder.wrap(buffer, offset);
        switch (headerDecoder.templateId()) {
            case CreateTrainResponseDecoder.TEMPLATE_ID:
                createTrainResponseDecoder.wrap(buffer, headerDecoder.encodedLength() + offset, headerDecoder.encodedLength(), headerDecoder.version());
                consumer.onTrainCreated(createTrainResponseDecoder.correlationId(), createTrainResponseDecoder.trainId());
                break;

        }
    }

    private MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private CreateTrainResponseDecoder createTrainResponseDecoder = new CreateTrainResponseDecoder();
}
