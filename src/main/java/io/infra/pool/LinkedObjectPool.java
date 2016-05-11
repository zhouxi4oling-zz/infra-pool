/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * <code>LinkedObjectPool</code>
 * <p>顺序对象池</p>
 * @author panglei
 * @version $Id: LinkedObjectPool.java, v 0.1 2013-5-28 下午03:58:22 panglei Exp $
 */
public class LinkedObjectPool<K, V> implements ObjectPool<K, V> {

    /** 使用linkedhashmap作为LinkedObjectPool实现 */
    private Map<K, V> delegate = new LinkedHashMap<K, V>();

    @Override
    public V get(K key) {
        synchronized (delegate) {
            return delegate.get(key);
        }
    }

    @Override
    public void put(K key, V value) {
        synchronized (delegate) {
            delegate.put(key, value);
        }
    }

    @Override
    public boolean containsKey(K key) {
        synchronized (delegate) {
            return delegate.containsKey(key);
        }
    }

    @Override
    public void clear() {
        synchronized (delegate) {
            delegate.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        synchronized (delegate) {
            return delegate.keySet();
        }
    }

    @Override
    public void remove(K k) {
        synchronized (delegate) {
            delegate.remove(k);
        }
    }

    @Override
    public Collection<V> values() {
        synchronized (delegate) {
            return delegate.values();
        }
    }

    @Override
    public int size() {
        synchronized (delegate) {
            return delegate.size();
        }
    }

    @Override
    public boolean isEmpty() {
        synchronized (delegate) {
            return delegate.isEmpty();
        }
    }

}
