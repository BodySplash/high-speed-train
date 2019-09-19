package reservation.cluster.domain;

public interface TrainRepository {

    int nextId();

    void add(Train train);

    Train get(int trainId);
}
