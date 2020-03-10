package com.dataStruct.triggerData.Condition;

import com.dataStruct.triggerData.TriggerDataParent;

public abstract class TriggerConditionDataParent extends TriggerDataParent
{

	/**
	 * 实现条件类，所有条件子类均需实现该抽象方法
	 * 
	 * @return
	 */
	public abstract boolean DataProcessing(int userId);
}
