package io.infra.pool;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HashObjectPool<K, V> implements ObjectPool<K, V> {

    protected Map<K, V> objectPool = new ConcurrentHashMap<K, V>();

    public V get(K key) {
        return objectPool.get(key);
    }

    public void put(K key, V value) {
        if (value != null) {
            objectPool.put(key, value);
        }
    }

    public boolean containsKey(K key) {
        return objectPool.containsKey(key);
    }

    public void clear() {
        objectPool.clear();
    }

    public Set<K> keySet() {
        return objectPool.keySet();
    }

    public void remove(K k) {
        objectPool.remove(k);
    }

    public Collection<V> values() {
        return objectPool.values();
    }

    public int size() {
        return objectPool.size();
    }

    @Override
    public boolean isEmpty() {
        return objectPool.isEmpty();
    }

}
