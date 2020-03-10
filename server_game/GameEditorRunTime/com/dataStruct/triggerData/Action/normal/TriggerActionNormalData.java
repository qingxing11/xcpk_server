package com.dataStruct.triggerData.Action.normal;

import com.dataStruct.triggerData.Action.TriggerActionDataParent;
import com.wt.util.Tool;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.orignalEnumType.EnumOrignalActionData;
import com.dataStruct.orignalEnumType.OrignalActionDataEnum;
@Scope("prototype")
@Service
public class TriggerActionNormalData extends TriggerActionDataParent
{

	@Override
	public boolean DataProcessing(int userId)
	{

		switch ((int) this.origId)
		{
			case EnumOrignalActionData.DoNothing:
				Tool.print_debug_level0("没有任何动作！");
				break;

			default:
				break;
		}
		return true;
	}

}
