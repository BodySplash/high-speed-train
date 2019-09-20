package reservation.client;

public interface ResponseConsumer {

    void onTrainCreated(long correlationId, int trainId);
}
