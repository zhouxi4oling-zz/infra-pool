/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.Collection;
import java.util.Set;

/**
 * <code>ObjectPool</code>
 * <p>对象池接口</p>
 * @author panglei
 * @version $Id: ObjectPool.java, v 0.1 2013-5-28 下午03:51:25 panglei Exp $
 */
public interface ObjectPool<K, V> {

    /**
     * 获取对象池中的对象
     * 
     * @param key   对象池中索引
     * @return
     */
    public V get(K key);

    /**
     * 将对象放入对象池
     * 
     * @param key
     * @param value
     */
    public void put(K key, V value);

    /**
     * 判断对象是否粗在
     * 
     * @param key
     * @return
     */
    public boolean containsKey(K key);

    /**
     * 清除对象池
     */
    public void clear();

    /**
     * 返回对象池中的key列表
     * 
     * @return
     */
    public Set<K> keySet();

    /**
     * 删除指定key对应的对象
     * 
     * @param k
     */
    public void remove(K k);

    /**
     * 返回对象池中所有对象
     * 
     * @return
     */
    public Collection<V> values();

    /**
     * 返回对象池列表
     * 
     * @return
     */
    public int size();

    /**
     * 判断对象池是否为空
     * 
     * @return
     */
    public boolean isEmpty();

}
