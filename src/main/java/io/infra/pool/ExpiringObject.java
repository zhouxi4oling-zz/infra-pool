/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 失效对象基类
 * @author lei.panglei
 * @version $Id: ExpiringObject.java, v 0.1 2013-1-15 上午10:03:57 lei.panglei Exp $
 */
public abstract class ExpiringObject<K> extends Cachable<K> implements
                                                           Comparable<ExpiringObject<K>> {

    /** 失效时间 */
    protected long          expiry       = 0L;

    /** 失效时间同步锁 */
    protected ReadWriteLock expiringLock = new ReentrantReadWriteLock();

    /**
     * Expiry对象失效时间
     * @param id
     */
    public ExpiringObject(K id) {
        super(id);
    }

    /**
     * 获取失效时间
     * 
     * @return      返回失效时间
     */
    public long getExpiring() {
        expiringLock.readLock().lock();
        try {
            return expiry;
        } finally {
            expiringLock.readLock().unlock();
        }
    }

    /**
     * 设置失效时间
     * 失效时间小于等于0表示该对象永不失效
     * 失效时间为毫秒计时的UNIX时间戳
     * @param expiring  失效时间
     */
    public void setExpiring(long expiring) {
        expiringLock.writeLock().lock();
        try {
            this.expiry = expiring;
        } finally {
            expiringLock.writeLock().unlock();
        }
    }

    /**
     * 判断对象是否失效
     * 
     * @return
     */
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
