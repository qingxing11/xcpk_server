package com.wt.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

import io.netty.util.internal.StringUtil;

/**
 * 服务器端配置类，字段名必须和<.properties>相同
 * 
 * @author WangTuo
 *
 */
public class BaseConfig
{

	public static String getConfigPath()
	{
		return "./config/";

		// System.getProperty("user.dir") + "/gameMain/config/";
	}

	public static String getApiPath()
	{
		return "./api/";
		// System.getProperty("user.dir") + "/gameMain/api/";
	}

	// 配置数据块路径
	public static String getDefDataPath()
	{
		return "./def/";
	}
	
	public static Properties getProperties(String path) throws IOException
	{
		//props.load(RedisUtil.class.getClassLoader().getResourceAsStream(path));
		Properties props = new Properties();
		BufferedReader br = new BufferedReader(new FileReader(path));
		props.load(br);
		return props;
	}

	/**
	 * 通过反射将配置文件中的值赋给指定类,字段名必须相同
	 * 
	 * @param t
	 * @param configName
	 */
	public static <T> void init(T t, String configName)
	{
		T instance = t;
		Properties props = new Properties();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(configName));
			props.load(br);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Field[] fields = t.getClass().getFields();

		for (int i = 0 ; i < fields.length ; i++)
		{
			String objName = fields[i].getName();
			try
			{
				System.out.println("参数:【" + objName + " = " + props.getProperty(objName) + "】");
				if (fields[i].getType() == Integer.TYPE)
				{
					if(!StringUtil.isNullOrEmpty(props.getProperty(objName)) )
					{
						instance.getClass().getField(objName).setInt(instance, Integer.parseInt(props.getProperty(objName)));
					}
					else
					{
						instance.getClass().getField(objName).setInt(instance,0);
					}
				}
				else if (fields[i].getType() == Boolean.TYPE)
				{
					instance.getClass().getField(objName).set(instance, props.getProperty(objName).equals("true"));
				}
				else
				{
					instance.getClass().getField(objName).set(instance, props.getProperty(objName));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	private BaseConfig()
	{}
}
