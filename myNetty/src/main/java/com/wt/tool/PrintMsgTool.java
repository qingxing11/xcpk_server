package com.wt.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class PrintMsgTool
{
	private static ArrayList<Integer> list_filter = new ArrayList<>();
	
	private PrintMsgTool(){}
	
	public static void init()
	{
		Properties props = new Properties();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader("./config/tool/printFilterMsg.properties"));//
			props.load(br);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Iterator<Entry<Object, Object>> iter = props.entrySet().iterator();//遍历api的所有需要注册方法
		while (iter.hasNext())
		{
			Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) iter.next();
			String msgType_str = (String) entry.getKey();
			int msgType = Integer.parseInt(msgType_str);
			System.out.println("过滤消息:"+msgType);
			addFilter(msgType);
		}
	}
	
	private static void addFilter(int msgType)
	{
		list_filter.add(msgType);
	}
	
	public static boolean isFilterMsgType(int msgType)
	{
		for (Integer integer : list_filter)
		{
			if(msgType == integer)
				return true;
		}
		return false;
	}
}
