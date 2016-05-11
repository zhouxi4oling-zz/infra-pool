/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.Collection;
import java.util.Set;

/**
 * 失效对象池
 * @author lei.panglei
 * @version $Id: ExpiringObjectPool.java, v 0.1 2013-1-15 上午10:17:19 lei.panglei Exp $
 */
public class ExpiringObjectPool<K, V extends ExpiringObject<K>> implements ExpiringRecycler<K, V>,
                                                                ObjectPool<K, V> {

    /** 回收线程名字 */
    private static final String               EXPIRING_OBJECT_POOL_CLEANNER = "ExpiringObjectPool";

    /** 回收线程启动默认间隔(0.25秒) */
    public static final long                  DEFAULT_CLEANER_INTERVAL      = 250L;

    /** 回收线程启动间隔 */
    private long                              cleanerInterval               = DEFAULT_CLEANER_INTERVAL;

    /** 失效回收器 */
    private final ExpiringObjectCleaner<K, V> expiringObjectCleaner;

    /** 对象池 */
    private final ObjectPool<K, V>            objectPool;

    /**
     * 构造函数并启动回收线程
     */
    public ExpiringObjectPool() {
        objectPool = new HashObjectPool<K, V>();
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
            EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    /**
     * 指定回收线程启动间隔构造函数
     * @param cleanerInterval   回收线程启动间隔时间
     */
    public ExpiringObjectPool(long cleanerInterval) {
        objectPool = new HashObjectPool<K, V>();
        this.cleanerInterval = cleanerInterval;
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
            EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    /**
     * 指定回收线程启动间隔和失效回收回调构造函数
     * @param cleanerInterval               回收线程启动时间
     * @param expiryObjectCleanerListener   回收线程回调
     */
    public ExpiringObjectPool(long cleanerInterval,
                              ExpiryObjectCleanerListener<V> expiryObjectCleanerListener) {
        objectPool = new HashObjectPool<K, V>();
        this.cleanerInterval = cleanerInterval;
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
            EXPIRING_OBJECT_POOL_CLEANNER, this, expiryObjectCleanerListener);
    }

    /**
     * 指定失效对象池实现的构造函数
     * @param useLinkedObjectPool   当该值为true时，失效对象池使用LinkedObjectPool作为实现
     */
    public ExpiringObjectPool(boolean useLinkedObjectPool) {
        if (useLinkedObjectPool) {
            objectPool = new LinkedObjectPool<K, V>();
        } else {
            objectPool = new HashObjectPool<K, V>();
        }
        expiringObjectCleaner = new ExpiringObjectCleaner<K, V>(cleanerInterval,
            EXPIRING_OBJECT_POOL_CLEANNER, this);
    }

    /**
     * 指定失效对象池实现的构造函数
     * @param useLinkedObjectPool   指定失效对象池实现的构造函数
     * @param cleanerInterval       回收线程启动间隔时间
     */
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

    /**
     * 指定失效对象池实现的构造函数
     * @param useLinkedObjectPool           指定失效对象池实现的构造函数
     * @param cleanerInterval               回收线程启动间隔时间
     * @param expiryObjectCleanerListener   回收线程回调
     */
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

    /**
     * @see com.pingan.mobilecmng.biz.push.common.objectpool.ExpiringRecycler#recycleIndex()
     */
    public Collection<V> recycleIndex() {
        synchronized (this.objectPool) {
            return this.objectPool.values();
        }
    }

    /**
     * @see com.pingan.mobilecmng.biz.push.common.objectpool.ExpiringRecycler#remove(com.pingan.mobilecmng.biz.push.common.objectpool.ExpiringObject)
     */
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
