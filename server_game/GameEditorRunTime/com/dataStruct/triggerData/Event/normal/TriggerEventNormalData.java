package com.dataStruct.triggerData.Event.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Event.TriggerEventDataParent;
import com.wt.util.Tool;

@Service @Scope("prototype") public class TriggerEventNormalData extends TriggerEventDataParent
{

	@Override
	public boolean DataProcessing(float origId, Object... obj)
	{
		if (origId == this.origId)
		{
			Tool.print_debug_level0("接受到事件处理this.origId=" + this.origId + ",origId=" + origId);
			return true;
		}
		return false;
	}

}
