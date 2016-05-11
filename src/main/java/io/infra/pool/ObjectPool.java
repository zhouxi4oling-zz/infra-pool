package io.infra.pool;

import java.util.Collection;
import java.util.Set;

public interface ObjectPool<K, V> {

    V get(K key);

    void put(K key, V value);

    boolean containsKey(K key);

    void clear();

    Set<K> keySet();

    void remove(K k);

    Collection<V> values();

    int size();

    boolean isEmpty();

}
