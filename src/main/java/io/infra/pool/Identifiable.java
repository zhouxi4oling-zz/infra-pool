/**
 * Pingan.com Inc.
 * Copyright (c) 2012-2020 All Rights Reserved.
 */
package io.infra.pool;

/**
 * Identifiable 可以唯一标识基类
 * 
 * @author lei.panglei
 * @version $Id: Identifiable.java, v 0.1 2013-1-7 上午11:20:17 lei.panglei Exp $
 */
public interface Identifiable<T> {

	/**
	 * 唯一标识符
	 * 
	 * @return 唯一标识符
	 */
	public T getId();

}
