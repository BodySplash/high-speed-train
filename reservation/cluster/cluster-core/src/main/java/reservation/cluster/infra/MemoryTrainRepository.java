package reservation.cluster.infra;

import org.agrona.collections.*;
import reservation.cluster.domain.*;

public class MemoryTrainRepository implements TrainRepository {
    @Override
    public int nextId() {
        return trains.size() + 1;
    }

    @Override
    public void add(Train train) {
        trains.put(train.getId(), train);
    }

    @Override
    public Train get(int trainId) {
        return trains.get(trainId);
    }

    private Int2ObjectHashMap<Train> trains = new Int2ObjectHashMap<>();
}
