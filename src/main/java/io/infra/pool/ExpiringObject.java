package io.infra.pool;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ExpiringObject<K> extends Cachable<K> implements
        Comparable<ExpiringObject<K>> {

    protected long expiry = 0L;

    protected ReadWriteLock expiringLock = new ReentrantReadWriteLock();

    public ExpiringObject(K id) {
        super(id);
    }

    public long getExpiring() {
        expiringLock.readLock().lock();
        try {
            return expiry;
        } finally {
            expiringLock.readLock().unlock();
        }
    }

    public void setExpiring(long expiring) {
        expiringLock.writeLock().lock();
        try {
            this.expiry = expiring;
        } finally {
            expiringLock.writeLock().unlock();
        }
    }

    public boolean isExpiry() {
        // 失效时间加锁
        expiringLock.readLock().lock();
        try {
            // 默认当失效时间小于等于0时，该对象永不失效
            if (expiry <= 0) {
                return false;
            }
            // 如果失效时间大于当前时间则对象不失效，否则失效
            if (expiry > System.currentTimeMillis()) {
                return false;
            }
            return true;
        } finally {
            // 释放失效时间锁
            expiringLock.readLock().unlock();
        }
    }

    public int compareTo(ExpiringObject<K> o) {
        if (o.getExpiring() == this.getExpiring()) {
            return 0;
        } else if (o.getExpiring() > this.getExpiring()) {
            return -1;
        }
        return 1;
    }

}
