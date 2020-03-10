package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;

@Service @Scope("prototype") public class TriggerEventTriggerOperate extends TriggerEventDataParent
{
	/// <summary>
	/// 触发器
	/// </summary>
	public float triggerId;

	@Override
	public boolean DataProcessing(float origId, Object... obj)
	{
		if ((int) origId != (int) this.origId)
		{
			return false;
		}

		if (obj != null && obj.length > 0)
		{
			if ((int) this.triggerIndex == (int) obj[0])
			{
				return true;
			}
		}

		return false;
	}

}
