package com.wt.factory;


import org.springframework.context.ApplicationContext;

public class MyBeanFactory
{
 	private static ApplicationContext applicationContext;
	
 	public static void setApplicationContext(ApplicationContext appContext)
 	{
 		applicationContext = appContext;
 	}
 	
	/**
	 * 根据class加载类
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz)
	{
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * 根据class和参数加载类
	 * @param clazz
	 * @param params
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz,Object... params)
	{
		return applicationContext.getBean(clazz,params);
	}
	
	/**
	 * 根据类名来加载类
	 * <h1>类名必须小写
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked") public static <T> T getBean(String name)
	{
		return (T) applicationContext.getBean(name);
	}
	
	/**
	 *  根据类名来加载类
	 * <h1>类名必须小写
	 * @param name
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked") public static <T> T getBean(String name,Object... params)
	{
		return (T) applicationContext.getBean(name,params);
	}
}
