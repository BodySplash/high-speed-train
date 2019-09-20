package reservation.cluster.infra;

import org.agrona.*;
import org.agrona.sbe.EncoderFlyweight;
import reservation.cluster.api.*;

public class ResponseMarshaller implements EncoderFlyweight {

    public void createTrainAck(long correlationId, int id) {
        createTrainResponseEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder)
                .correlationId(correlationId)
                .trainId(id);
        encodedLength = createTrainResponseEncoder.encodedLength();
    }

    @Override
    public MutableDirectBuffer buffer() {
        return buffer;
    }

    @Override
    public int offset() {
        return 0;
    }

    @Override
    public int encodedLength() {
        return headerEncoder.encodedLength() + encodedLength;
    }

    @Override
    public int sbeSchemaId() {
        return MessageHeaderEncoder.SCHEMA_ID;
    }

    @Override
    public int sbeSchemaVersion() {
        return MessageHeaderEncoder.SCHEMA_VERSION;
    }

    @Override
    public EncoderFlyweight wrap(MutableDirectBuffer buffer, int offset) {
        return this;
    }

    private ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer();
    private MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
    private CreateTrainResponseEncoder createTrainResponseEncoder = new CreateTrainResponseEncoder();
    private int encodedLength;
}
