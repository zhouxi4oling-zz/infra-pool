package io.infra.pool;

public abstract class Cachable<K> implements Identifiable<K> {

    private K id;

    public Cachable(K id) {
        this.id = id;
    }

    public K getId() {
        return this.id;
    }

}
