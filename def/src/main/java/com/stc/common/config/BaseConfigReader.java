package com.stc.common.config;

import java.io.InputStream;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wt.util.io.FileTool;

public class BaseConfigReader
{
	public <T> Map<String, T> loadConfig(Class<T> cla, String name)
	{
		try
		{
			return JSON.parseObject(loadJson(name), new TypeReference<Map<String, T>>()
			{});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	protected String loadJson(String fileName)
	{
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("json/" + fileName + ".json");
		String content = null;
		try
		{
			content = new String(FileTool.readStream(stream));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return content;
	}

	public void init()
	{

	}
}
