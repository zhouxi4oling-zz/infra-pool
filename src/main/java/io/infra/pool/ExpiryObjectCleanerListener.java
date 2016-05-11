/**
 * Pingan.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package io.infra.pool;

/**
 * <code>ExpiryObjectCleanerListener</code>
 * <p>ExpiryObjecyCleanerListener监听器</p>
 * @author panglei
 * @version $Id: ExpiryObjectCleanerListener.java, v 0.1 2013-5-15 下午10:59:59 panglei Exp $
 */
public interface ExpiryObjectCleanerListener<V> {

    /**
     * 当对象被ExpiryObjectCleaner回收时调用该方法
     * 
     * @param v
     */
    public void onClean(V v);

}
