package com.dataStruct.triggerData;

import java.util.ArrayList;

public interface IListVoluation
{

	/**
	 * 传入一个List<object> 类型 将其转化为list具体类型
	 *  若子类中有ArrayList或者其他的集合请务必实现该方法
	 * 否则会导致数据丢失
	 * @param list
	 */
	void listVoluation(ArrayList<Object> list);
}
