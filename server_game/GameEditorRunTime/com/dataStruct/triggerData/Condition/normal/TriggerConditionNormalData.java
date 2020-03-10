package com.dataStruct.triggerData.Condition.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Condition.TriggerConditionDataParent;
@Scope("prototype")
@Service
public class TriggerConditionNormalData extends TriggerConditionDataParent
{

	@Override
	public boolean DataProcessing(int userId)
	{

		return false;
	}

}
