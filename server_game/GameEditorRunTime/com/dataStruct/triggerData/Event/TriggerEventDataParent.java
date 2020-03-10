package com.dataStruct.triggerData.Event;

import java.util.ArrayList;

import com.dataStruct.triggerData.TriggerDataParent;

public abstract class TriggerEventDataParent extends TriggerDataParent
{
	/// <summary>
	/// 数据在所在的触发器结构中的唯一次序
	/// 该数值在程序启动后自动生成
	/// 不需要额外维护
	/// </summary>
	public int triggerIndex;

	/// <summary>
	/// 该数据所在触发器结构自己的顺序
	/// 该数据在游戏启动时自动赋值
	/// 无需额外维护
	/// </summary>
	public float triggerStructIndex;

	@Override
	public void listVoluation(ArrayList<Object> list)
	{

	}

	/**
	 * 每个事件子类均需要实现该方法
	 * 
	 * @param origId
	 *                原始数据对应的唯一id
	 * @param obj
	 *                需要的参数，需要转化为自己需要的类型
	 * @return
	 */
	public abstract boolean DataProcessing(float origId, Object... obj);
}
