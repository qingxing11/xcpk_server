package com.dataStruct.triggerData;

import java.util.ArrayList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wt.util.Tool;

@DataStructAnnotat 
@Scope("prototype")
@Service
public class ExportData implements IListVoluation
{

	public ArrayList<ExportTriggerDataStruct> listChuFaQi;
	public String fullName;

	@Override
	public void listVoluation(ArrayList<Object> list)
	{
		if (list == null || list.size() <= 0)
		{
			return;
		}

		listChuFaQi = new ArrayList<ExportTriggerDataStruct>();

		for (Object data : list)
		{
			if (data instanceof ExportTriggerDataStruct)
			{
				ExportTriggerDataStruct export = (ExportTriggerDataStruct) data;
				listChuFaQi.add(export);
			}
		}

		Tool.print_debug_level0("数据加载完成！！！");
	}

}
