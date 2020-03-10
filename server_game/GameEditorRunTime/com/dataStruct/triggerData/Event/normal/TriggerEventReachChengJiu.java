package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.orignalEnumType.EnumOrignalEventData;
import com.dataStruct.triggerData.Event.TriggerEventDataParent;
import com.wt.util.Tool;

@Service
@Scope("prototype")
public class TriggerEventReachChengJiu extends TriggerEventDataParent
{
	public float item_id;
	public float item_num;

	/***
	 * @param obj={item_id,item_num}
	 */
	@Override
	public boolean DataProcessing(float origId, Object... obj)
	{
		Tool.print_debug_level0("TriggerEventReachChengJiu：origId=" + origId + ",this.origId=" + this.origId + ",obj.length=" + obj.length);

		if ((int) this.origId != (int) origId)
		{
			Tool.print_debug_level0("origId不对应返回fasle");
			return false;
		}

		if (obj == null || obj.length < 2)
		{
			Tool.print_debug_level0("TriggerEventReachChengJiu：obj == null || obj.length < 2");
			return false;
		}

		if (Integer.parseInt(obj[0].toString()) != (int) item_id)
		{
			Tool.print_debug_level0("TriggerEventReachChengJiu：Integer.parseInt(obj[0].toString())=" + Integer.parseInt(obj[0].toString()) + ",item_id=" + item_id);
			return false;
		}

		// ---
		switch ((int) this.item_id)
		{
			case EnumOrignalEventData.CreatSthNum:
				if (Integer.parseInt(obj[1].toString()) != (int) item_num)
				{
					Tool.print_debug_level0("TriggerEventReachChengJiu：Integer.parseInt(obj[1].toString())=" + Integer.parseInt(obj[1].toString()) + ",item_num=" + (int) item_num);

					return false;
				}

				break;
			case EnumOrignalEventData.CreatSthFrequency:
				if ((int) item_num == -1)// -1表示每次发生均会通过
				{
					return true;
				}

				if (Integer.parseInt(obj[1].toString()) != (int) item_num)
				{
					Tool.print_debug_level0("TriggerEventReachChengJiu：Integer.parseInt(obj[1].toString())=" + Integer.parseInt(obj[1].toString()) + ",item_num=" + (int) item_num);

					return false;
				}
				break;
			default:
				break;
		}

		return true;
	}

}
