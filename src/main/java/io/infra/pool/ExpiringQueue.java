/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * <code>ExpiringQueue</code>
 * <p></p>
 * @author panglei
 * @version $Id: ExpiringQueue.java, v 0.1 2013-5-15 下午10:53:12 panglei Exp $
 */
public class ExpiringQueue<I, V extends ExpiringObject<I>> implements ExpiringRecycler<I, V> {

    /** 回收线程名字 */
    private static final String               EXPIRING_OBJECT_POOL_CLEANNER = "ExpiringQueue";

    /** 回收线程启动默认间隔(0.25秒) */
    public static final long                  DEFAULT_CLEANER_INTERVAL      = 250L;

    /** 回收线程启动间隔 */
    private long                              cleanerInterval               = DEFAULT_CLEANER_INTERVAL;

    /** 失效回收器 */
    private final ExpiringObjectCleaner<I, V> expiringObjectCleaner;

    /** 队列容器 */
    private List<V>                           queue                         = new LinkedList<V>();

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
