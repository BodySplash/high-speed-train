package common;

import java.util.ArrayDeque;
import java.util.function.Supplier;

public final class Pool<T> {
    private final int maxSize;
    private final ArrayDeque<T> pooled;
    private final Supplier<T> factory;

    public Pool(final int maxSize, final Supplier<T> factory, final boolean preAllocate) {
        this.maxSize = maxSize;
        pooled = new ArrayDeque<>(maxSize);
        this.factory = factory;
        if (preAllocate) {
            for (int i = 0; i < maxSize; i++) {
                release(factory.get());
            }
        }
    }

    public T acquire() {
        if (pooled.isEmpty()) {
            return factory.get();
        }

        return pooled.pollLast();
    }

    public void release(final T instance) {
        if (pooled.size() < maxSize) {
            pooled.addLast(instance);
        }
    }
}
