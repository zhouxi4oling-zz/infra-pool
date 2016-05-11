/**
 * Pingan.com Inc.
 * Copyright (c) 2012-2020 All Rights Reserved.
 */
package io.infra.pool;

/**
 * Cachable接受缓存的对象父类
 * 
 * @author lei.panglei
 * @version $Id: Cachable.java, v 0.1 2013-1-7 上午11:07:38 lei.panglei Exp $
 */
public abstract class Cachable<K> implements Identifiable<K> {

	/** 字符串类型 唯一表示 */
	private K id;
	
	/**
	 * 使用唯一表示构建对象
	 * @param id
	 */
	public Cachable(K id) {
		this.id = id;
	}

	
	/**
	 * 获取唯一标识
	 * @see com.pingan.mobilecmng.biz.push.common.objectpool.Identifiable#getId()
	 */
	public K getId() {
		return this.id;
	}

}
