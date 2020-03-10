package com.dataStruct.triggerData;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;


@DataStructAnnotat
public abstract class TriggerDataParent implements IListVoluation
{
	/// <summary>
	/// 对应原始数据的唯一id
	/// </summary>
	public float origId;

	public String fullName;

	public TriggerDataParent(int origId)
	{
		this.origId = origId;
		fullName = this.getClass().getSimpleName();
	}

	public TriggerDataParent()
	{
		fullName = this.getClass().getSimpleName();
	}

	public void SetOrigId(int origId)
	{
		this.origId = origId;
	}

	@Override
	public void listVoluation(ArrayList<Object> list)
	{

	}
}
