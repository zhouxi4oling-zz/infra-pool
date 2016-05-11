package io.infra.pool;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class LinkedObjectPool<K, V> implements ObjectPool<K, V> {

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
