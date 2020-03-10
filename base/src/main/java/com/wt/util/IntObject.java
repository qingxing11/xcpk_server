package com.wt.util;

public class IntObject
{
	private int value;
	public IntObject()
	{}
	
	public IntObject(int value)
	{
		this.value = value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	/**
	 * value++
	 */
	public void add()
	{
		value++;
	}

	public int value()
	{
		return value;
	}
}
