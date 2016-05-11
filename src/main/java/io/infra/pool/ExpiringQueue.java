package io.infra.pool;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ExpiringQueue<I, V extends ExpiringObject<I>> implements ExpiringRecycler<I, V> {

    private static final String EXPIRING_OBJECT_POOL_CLEANNER = "ExpiringQueue";

    public static final long DEFAULT_CLEANER_INTERVAL = 250L;

    private long cleanerInterval = DEFAULT_CLEANER_INTERVAL;

    private final ExpiringObjectCleaner<I, V> expiringObjectCleaner;

    private List<V> queue = new LinkedList<V>();

    public ExpiringQueue() {
        expiringObjectCleaner = new ExpiringObjectCleaner<I, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this);
        expiringObjectCleaner.startCleanIfNotStarted();
    }

    public ExpiringQueue(ExpiryObjectCleanerListener<V> listener) {
        expiringObjectCleaner = new ExpiringObjectCleaner<I, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this, listener);
        expiringObjectCleaner.startCleanIfNotStarted();
    }

    public ExpiringQueue(long interval, ExpiryObjectCleanerListener<V> listener) {
        expiringObjectCleaner = new ExpiringObjectCleaner<I, V>(interval,
                EXPIRING_OBJECT_POOL_CLEANNER, this, listener);
        expiringObjectCleaner.startCleanIfNotStarted();
    }

    public void add(V v) {
        synchronized (queue) {
            queue.add(v);
        }
    }

    @Override
    public void remove(I i) {
        synchronized (queue) {
            // 按照索引下表删除的queue中的数据元素
            queue.remove(i);
        }
    }

    @Override
    public Collection<V> recycleIndex() {
        return queue;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

}
