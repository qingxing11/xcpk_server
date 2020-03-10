package com.dataStruct.triggerData;

import java.util.ArrayList;

import org.apache.http.util.Args;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Action.TriggerActionDataParent;
import com.dataStruct.triggerData.Condition.TriggerConditionDataParent;
import com.dataStruct.triggerData.Event.TriggerEventDataParent;
import com.wt.util.Tool;

@DataStructAnnotat
@Scope("prototype")
@Service
public class ExportTriggerDataStruct implements IListVoluation
{

	public ArrayList<TriggerEventDataParent> list_event = new ArrayList<TriggerEventDataParent>();
	public ArrayList<TriggerConditionDataParent> list_condition = new ArrayList<TriggerConditionDataParent>();
	public ArrayList<TriggerActionDataParent> list_action = new ArrayList<TriggerActionDataParent>();

	public String fullName;
	public float triggerStructIndex;

	@Override
	public void listVoluation(ArrayList<Object> list)
	{
		if (list == null || list.size() <= 0)
		{
			return;
		}

		for (Object obj : list)
		{
			addTriggerData((TriggerDataParent) obj);
		}
	}

	private void addTriggerData(TriggerDataParent data)
	{
		if (data instanceof TriggerEventDataParent)
		{
			TriggerEventDataParent de = (TriggerEventDataParent) data;

			de.triggerIndex = list_event.size();
			de.triggerStructIndex = triggerStructIndex;

			list_event.add(de);
		}
		else if (data instanceof TriggerConditionDataParent)
		{
			TriggerConditionDataParent dc = (TriggerConditionDataParent) data;
			list_condition.add(dc);
		}
		else if (data instanceof TriggerActionDataParent)
		{
			TriggerActionDataParent da = (TriggerActionDataParent) data;
			list_action.add(da);
		}
	}

	/**
	 * 接受处理事件的结果
	 * 
	 * @param index
	 * @param res
	 */
	public boolean receiveEventResult(int userId, int index, boolean res)
	{
		if (res)
		{
			return judgeConditionDataParocessing(userId);
		}

		return false;
	}

	public boolean judgeConditionDataParocessing(int userId)
	{
		if (list_condition != null && list_condition.size() > 0)
		{
			for (TriggerConditionDataParent con : list_condition)
			{
				boolean res = con.DataProcessing(userId);
				if (!res)
				{
					Tool.print_debug_level0("条件不满足不执行动作。对应的原始数据id=" + con.origId);
					return false;
				}
			}

		}

		return actionDataProcessing(userId);

	}

	public boolean actionDataProcessing(int userId)
	{
		if (list_action != null && list_action.size() > 0)
		{
			for (TriggerActionDataParent data : list_action)
			{
				boolean res = data.DataProcessing(userId);
				Tool.print_debug_level0("执行动作后的反馈。res=" + res + ",ActionData origId=" + data.origId);
			}
		}

		return true;
	}

}
