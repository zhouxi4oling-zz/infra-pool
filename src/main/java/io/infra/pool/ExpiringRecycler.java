/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

import java.util.Collection;

/**
 * 可回收对象池
 * @author lei.panglei
 * @version $Id: ExpiringRecycler.java, v 0.1 2013-1-15 上午10:41:22 lei.panglei Exp $
 */
public interface ExpiringRecycler<K, T extends ExpiringObject<K>> {

    /**
     * 移除一个对象池的对象
     * 
     * @param t 待移除对象
     */
    public void remove(K t);

    /**
     * 获取可回收对象列表
     * 
     * @return
     */
    public Collection<T> recycleIndex();

    /**
     *  回收对象池是否为空
     * 
     * @return
     */
    public boolean isEmpty();

}
