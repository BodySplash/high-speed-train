package reservation.cluster;

import io.aeron.cluster.service.ClientSession;
import org.agrona.concurrent.AtomicBuffer;
import org.slf4j.LoggerFactory;
import reservation.cluster.api.*;
import reservation.cluster.domain.*;

class MessageAdapter {

    public MessageAdapter(TicketOffice ticketOffice) {

        this.ticketOffice = ticketOffice;
    }

    public void adapt(ClientSession session, AtomicBuffer buffer) {
        headerDecoder.wrap(buffer, 0);
        switch (headerDecoder.templateId()) {
            case CreateTrainDecoder.TEMPLATE_ID:
                createTrainDecoder.wrap(buffer, headerDecoder.encodedLength(), headerDecoder.blockLength(), headerDecoder.version());
                var correlationId = createTrainDecoder.correlationId();
                var coachesNumber = createTrainDecoder.coachesNumber();
                var seatsNumber = createTrainDecoder.seatsNumber();
                LoggerFactory.getLogger(MessageAdapter.class).info("Create train {} {} {}", correlationId, coachesNumber, seatsNumber);
                var train = ticketOffice.createTrain(coachesNumber, seatsNumber);
                break;
        }
    }

    private MessageHeaderDecoder headerDecoder = new MessageHeaderDecoder();
    private CreateTrainDecoder createTrainDecoder = new CreateTrainDecoder();
    private TicketOffice ticketOffice;
}
