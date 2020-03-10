package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;

@Scope("prototype") @Service public class TriggerEventItemBehavior extends TriggerEventDataParent
{
	public float item_Num;

	/**
	 * @param obj[0]item_Num
	 */
	@Override
	public boolean DataProcessing(float origId, Object... obj)
	{

		if ((int) origId != (int) this.origId)
		{
			return false;
		}

		if (obj != null && obj.length > 0)
		{
			if ((int) item_Num == (int) obj[0])
			{
				return true;
			}
		}

		return false;
	}

}
