package io.infra.pool;

import java.util.Collection;
import java.util.Set;

public class ExpiringObjectPool<K, V extends ExpiringObject<K>> implements ExpiringRecycler<K, V>,
        ObjectPool<K, V> {

    private static final String EXPIRING_OBJECT_POOL_CLEANNER = "ExpiringObjectPool";

    public static final long DEFAULT_CLEANER_INTERVAL = 250L;

    private long cleanerInterval = DEFAULT_CLEANER_INTERVAL;

    private final ExpiringObjectCleaner<K, V> expiringObjectCleaner;

    private final ObjectPool<K, V> objectPool;

    public ExpiringObjectPool() {
        objectPool = new HashObjectPool<K, V>();
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    public ExpiringObjectPool(long cleanerInterval) {
        objectPool = new HashObjectPool<K, V>();
        this.cleanerInterval = cleanerInterval;
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    public ExpiringObjectPool(long cleanerInterval,
                              ExpiryObjectCleanerListener<V> expiryObjectCleanerListener) {
        objectPool = new HashObjectPool<K, V>();
        this.cleanerInterval = cleanerInterval;
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this, expiryObjectCleanerListener);
    }

    public ExpiringObjectPool(boolean useLinkedObjectPool) {
        if (useLinkedObjectPool) {
            objectPool = new LinkedObjectPool<K, V>();
        } else {
            objectPool = new HashObjectPool<K, V>();
        }
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    public ExpiringObjectPool(boolean useLinkedObjectPool, long cleanerInterval) {
        if (useLinkedObjectPool) {
            objectPool = new LinkedObjectPool<K, V>();
        } else {
            objectPool = new HashObjectPool<K, V>();
        }
        this.cleanerInterval = cleanerInterval;
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    public ExpiringObjectPool(boolean useLinkedObjectPool, long cleanerInterval,
                              ExpiryObjectCleanerListener<V> expiryObjectCleanerListener) {
        if (useLinkedObjectPool) {
            objectPool = new LinkedObjectPool<K, V>();
        } else {
            objectPool = new HashObjectPool<K, V>();
        }
        this.cleanerInterval = cleanerInterval;
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
                EXPIRING_OBJECT_POOL_CLEANNER, this, expiryObjectCleanerListener);
    }

    public Collection<V> recycleIndex() {
        synchronized (this.objectPool) {
            return this.objectPool.values();
        }
    }

    public void remove(V t) {
        synchronized (this.objectPool) {
            this.remove(t.getId());
        }
    }

    @Override
    public V get(K key) {
        return this.objectPool.get(key);
    }

    @Override
    public void put(K key, V value) {
        this.objectPool.put(key, value);
        expiringObjectCleaner.startCleanIfNotStarted();
    }

    @Override
    public boolean containsKey(K key) {
        return objectPool.containsKey(key);
    }

    @Override
    public void clear() {
        objectPool.clear();
    }

    @Override
    public Set<K> keySet() {
        return objectPool.keySet();
    }

    @Override
    public Collection<V> values() {
        return objectPool.values();
    }

    @Override
    public int size() {
        return objectPool.size();
    }

    @Override
    public void remove(K t) {
        objectPool.remove(t);
    }

    @Override
    public boolean isEmpty() {
        if (objectPool == null) {
            return true;
        }
        return objectPool.isEmpty();
    }

}
