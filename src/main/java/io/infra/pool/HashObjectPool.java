/**
 * Pingan.com Inc.
 * Copyright (c) 2012-2020 All Rights Reserved.
 */
package io.infra.pool;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象池
 * 
 * @author lei.panglei
 * @version $Id: ObjectPool.java, v 0.1 2013-1-7 上午11:21:06 lei.panglei Exp $
 */
public class HashObjectPool<K, V> implements ObjectPool<K, V> {

    /** 对象池  */
    protected Map<K, V> objectPool = new ConcurrentHashMap<K, V>();

    /**
     * 获取一个对象池中的对象
     * 
     * @param key
     * @return
     */
    public V get(K key) {
        return objectPool.get(key);
    }

    /**
     * 保存一个对象到对象池
     * 
     * @param key
     * @param value
     */
    public void put(K key, V value) {
        if (value != null) {
            objectPool.put(key, value);
        }
    }

    /**
     * 判断对象池否存在
     * 
     * @param key
     * @return
     */
    public boolean containsKey(K key) {
        return objectPool.containsKey(key);
    }

    /**
     * 清空对象池
     */
    public void clear() {
        objectPool.clear();
    }

    /**
     * 获取Key集合
     * 
     * @return
     */
    public Set<K> keySet() {
        return objectPool.keySet();
    }

    /**
     * 通过Key删除一个对象
     * 
     * @param k
     */
    public void remove(K k) {
        objectPool.remove(k);
    }

    /**
     * 获取对象池中全部对象
     * 
     * @return
     */
    public Collection<V> values() {
        return objectPool.values();
    }

    /**
     * 获取对象池大小
     * 
     * @return
     */
    public int size() {
        return objectPool.size();
    }

    @Override
    public boolean isEmpty() {
        return objectPool.isEmpty();
    }

}
