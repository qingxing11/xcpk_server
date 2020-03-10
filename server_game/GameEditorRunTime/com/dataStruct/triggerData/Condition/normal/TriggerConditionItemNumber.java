package com.dataStruct.triggerData.Condition.normal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.dataStruct.triggerData.Condition.TriggerConditionDataParent;
import com.wt.util.Tool;

/**
 * 当指定单位达到指定数量后返回结果为true
 * 
 * @author W000
 *
 */

@Scope("prototype")
@Service
public class TriggerConditionItemNumber extends TriggerConditionDataParent
{
	public float item_id;
	public float item_num;

	@Override
	public boolean DataProcessing(int userId)
	{
		Tool.print_debug_level0("--选择的单位id为：" + item_id + ",选择的单位数量为：" + item_num);
		return false;
	}

}
