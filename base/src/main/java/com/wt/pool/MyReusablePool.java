package com.wt.pool;

import java.util.ArrayList;

public interface MyReusablePool
{
	/**
	 * 获取池中所有对象
	 * @return
	 */
	ArrayList<MyReusable> getAllReusables();
	
	/**
	 * 获取空闲对象
	 * @return
	 */
	MyReusable getReusable();
	
	/**
	 * 重置某个元素到初始状态
	 * @param reusable
	 */
	void releaseReusable(MyReusable reusable);
	
	/**
	 * 设置池的最大容量
	 * @param size
	 */
	void setMaxPoolSize(int size);
	
	void initPool(int size); 
}
