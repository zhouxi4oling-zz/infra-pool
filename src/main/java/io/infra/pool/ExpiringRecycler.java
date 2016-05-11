package io.infra.pool;

import java.util.Collection;

public interface ExpiringRecycler<K, T extends ExpiringObject<K>> {

    void remove(K t);

    Collection<T> recycleIndex();

    boolean isEmpty();

}
