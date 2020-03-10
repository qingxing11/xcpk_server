package com.dataStruct.triggerData.Action.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Action.TriggerActionDataParent;
import com.wt.util.Tool;
@Scope("prototype")
@Service
public class TriggerActionReachChengJiu extends TriggerActionDataParent
{
	/// <summary>
	/// 成就对应表的id
	/// </summary>
	public float achievement_id;

	@Override
	public boolean DataProcessing(int userId)
	{
		Tool.print_debug_level0("达成成就，成就对应的表的id=" + achievement_id);
		return true;
	}

}
