/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 失效对象回收线程
 * @author lei.panglei
 * @version $Id: ExpiringObjectCleaner.java, v 0.1 2013-1-15 上午10:52:24 lei.panglei Exp $
 */
public class ExpiringObjectCleaner<K, T extends ExpiringObject<K>> implements Runnable {

    /** 日志 */
    private static final Logger            LOGGER      = LoggerFactory
                                                           .getLogger(ExpiringObjectCleaner.class);

    /** 对象回收容器 */
    private ExpiringRecycler<K, T>         expiringRecycler;
    /** 是否运行 */
    private boolean                        running     = false;
    /** 是否运行同步锁 */
    private ReadWriteLock                  runningLock = new ReentrantReadWriteLock();
    /** 回收线程运行间隔 */
    private long                           interval;
    /** 回收线程 */
    private final Thread                   expiringObjectCleanerThread;
    /** 回收回调 */
    private ExpiryObjectCleanerListener<T> expiryObjectCleanerListener;

    /** 线程名 */
    private String                         name;

    /**
     * 构造函数
     * @param interval
     * @param name
     * @param expiringRecycler
     */
    public ExpiringObjectCleaner(long interval, String name, ExpiringRecycler<K, T> expiringRecycler) {
        this.interval = interval;
        this.name = name;
        this.expiringRecycler = expiringRecycler;
        this.expiringObjectCleanerThread = new Thread(this);
    }

    /**
     * 构造函数
     * @param interval
     * @param name
     * @param expiringRecycler
     * @param expiryObjectCleanerListener
     */
    public ExpiringObjectCleaner(long interval, String name,
                                 ExpiringRecycler<K, T> expiringRecycler,
                                 ExpiryObjectCleanerListener<T> expiryObjectCleanerListener) {
        this.interval = interval;
        this.name = name;
        this.expiringRecycler = expiringRecycler;
        this.expiryObjectCleanerListener = expiryObjectCleanerListener;
        this.expiringObjectCleanerThread = new Thread(this);
    }

    /** 
     * @see java.lang.Runnable#run()
     */
    public void run() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("expiringRecyclerCleanerThread[" + name + "] start");
        }

        while (running) {
            try {
                // clean
                cleanExpiryObject();
                // sleep
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (Exception e) {
                // resume on error
                LOGGER.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 清理过期对象
     */
    public void cleanExpiryObject() {
        // 如果recycler为空，则退出回收方法
        if (expiringRecycler.isEmpty()) {
            return;
        }
        // TODO menglg no need to use the copyonwritearraylist here
        List<T> recycleIndex = new CopyOnWriteArrayList<T>(expiringRecycler.recycleIndex());
        // 开始回收对象
        for (T t : recycleIndex) {
            // 增加当对象失效值为负数时，该对象用不失效
            if (t.getExpiring() < 0) {
                continue;
            }

            if (t.getExpiring() <= System.currentTimeMillis()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("remove object id:" + t.getId());
                }
                // 对象失效自动清除
                expiringRecycler.remove(t.getId());

                if (expiryObjectCleanerListener != null) {
                    //对象失效被回收后调用该回调函数
                    expiryObjectCleanerListener.onClean(t);
                }

            }
        }
    }

    /**
     * 如果清理线程没有启动则启动清理线程
     */
    public void startCleanIfNotStarted() {
        // 判断是否清理线程是否启动
        runningLock.readLock().lock();
        try {
            if (running) {
                return;
            }
        } finally {
            runningLock.readLock().unlock();
        }

        // 启动清理线程
        runningLock.writeLock().lock();
        try {
            if (!running) {
                running = true;
                expiringObjectCleanerThread.start();
            }
        } finally {
            runningLock.writeLock().unlock();
        }
    }

    /**
     * 停止回收
     */
    public void stopClean() {
        runningLock.writeLock().lock();
        try {
            if (running) {
                running = false;
                expiringObjectCleanerThread.interrupt();
            }
        } finally {
            runningLock.writeLock().unlock();
        }
    }

    public ExpiringRecycler<K, T> getExpiringRecycler() {
        return expiringRecycler;
    }

    public void setExpiringRecycler(ExpiringRecycler<K, T> expiringRecycler) {
        this.expiringRecycler = expiringRecycler;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
