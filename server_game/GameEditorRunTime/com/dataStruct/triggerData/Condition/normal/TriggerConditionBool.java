package com.dataStruct.triggerData.Condition.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Condition.TriggerConditionDataParent;
import com.wt.util.Tool;

@Service
@Scope("prototype")
public class TriggerConditionBool extends TriggerConditionDataParent
{
	public boolean condition_bool;

	@Override
	public boolean DataProcessing(int userId)
	{
		Tool.print_debug_level0("TriggerConditionBool结果为=" + condition_bool);
		return false;
	}

}
