package io.infra.pool;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpiringObjectCleaner<K, T extends ExpiringObject<K>> implements Runnable {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ExpiringObjectCleaner.class);

    private ExpiringRecycler<K, T> expiringRecycler;

    private boolean running = false;

    private ReadWriteLock runningLock = new ReentrantReadWriteLock();

    private long interval;

    private final Thread expiringObjectCleanerThread;

    private ExpiryObjectCleanerListener<T> expiryObjectCleanerListener;

    private String name;

    public ExpiringObjectCleaner(long interval, String name, ExpiringRecycler<K, T> expiringRecycler) {
        this.interval = interval;
        this.name = name;
        this.expiringRecycler = expiringRecycler;
        this.expiringObjectCleanerThread = new Thread(this);
    }

    public ExpiringObjectCleaner(long interval, String name,
                                 ExpiringRecycler<K, T> expiringRecycler,
                                 ExpiryObjectCleanerListener<T> expiryObjectCleanerListener) {
        this.interval = interval;
        this.name = name;
        this.expiringRecycler = expiringRecycler;
        this.expiryObjectCleanerListener = expiryObjectCleanerListener;
        this.expiringObjectCleanerThread = new Thread(this);
    }

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

    public void cleanExpiryObject() {
        // 如果recycler为空，则退出回收方法
        if (expiringRecycler.isEmpty()) {
            return;
        }

        // TODO no need to use the copyonwritearraylist here
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
