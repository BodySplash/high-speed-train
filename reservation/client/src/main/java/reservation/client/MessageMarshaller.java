package reservation.client;

import org.agrona.*;
import org.agrona.sbe.EncoderFlyweight;
import reservation.cluster.api.*;

public class MessageMarshaller implements EncoderFlyweight {

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
        return headerEncoder.encodedLength() + messageLength;
    }

    @Override
    public int sbeSchemaId() {
        return CreateTrainEncoder.SCHEMA_ID;
    }

    @Override
    public int sbeSchemaVersion() {
        return CreateTrainEncoder.SCHEMA_VERSION;
    }

    @Override
    public EncoderFlyweight wrap(MutableDirectBuffer buffer, int offset) {
        return this;
    }

    public void createTrain(int id, int coaches, int seats) {
        createTrainEncoder.wrapAndApplyHeader(buffer, 0, headerEncoder)
                .trainId(id)
                .coachesNumber(coaches)
                .seatsNumber(seats);
        messageLength = createTrainEncoder.encodedLength();
    }



    private ExpandableDirectByteBuffer buffer = new ExpandableDirectByteBuffer(128);
    private MessageHeaderEncoder headerEncoder = new MessageHeaderEncoder();
    private CreateTrainEncoder createTrainEncoder = new CreateTrainEncoder();
    private int messageLength;
}
