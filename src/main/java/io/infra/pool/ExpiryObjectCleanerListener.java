package io.infra.pool;

public interface ExpiryObjectCleanerListener<V> {

    public void onClean(V v);

}
