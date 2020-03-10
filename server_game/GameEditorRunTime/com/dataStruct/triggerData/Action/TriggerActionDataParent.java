package com.dataStruct.triggerData.Action;

import com.dataStruct.triggerData.TriggerDataParent;

public abstract class TriggerActionDataParent extends TriggerDataParent
{
	/**
	 * 实现动作类，所有动作子类均需实现该抽象方法
	 * 
	 * @return
	 */
	public abstract boolean DataProcessing(int userId);
}
