package com.wt.util.timetask;

import java.text.SimpleDateFormat;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import com.wt.util.MyTimeUtil;

/**
 * 基于{@link ScheduledExecutorService}的简单计时任务分配 <br>
 * 通过任务名称{@link taskName}在线程池中分配线程，相同的任务名分配到同一个线程中
 * 
 * @author WangTuo
 * @see ScheduledExecutorService
 */

public class SimpleTaskUtil
{
	private static ConcurrentHashMap<String, ScheduledExecutorService> map_service = new ConcurrentHashMap<>();

	/**
	 * 开始一个简单任务
	 * 
	 * @param taskName  任务名
	 * @param startDelay  延迟指定毫秒时间后开始运行
	 * @param intervalTime   任务间隔毫秒周期
	 * @param clazz
	 * @throws Exception
	 */
	public static <T> void startTask(String taskName, long startDelay, long intervalTime, Class<? extends Runnable> clazz) throws Exception
	{
		if (taskName == null || taskName.isEmpty())
		{
			throw new Exception("taskName不能为空");
		}
		ScheduledExecutorService service = getScheduledExecutorService(taskName);
		if (service == null)
		{
			throw new Exception("任务调速器为空");
		}
		service.scheduleAtFixedRate(clazz.newInstance(), startDelay, intervalTime, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 开始一个简单任务
	 * @param taskName  任务名
	 * @param startDelay  延迟指定毫秒时间后开始运行
	 * @param intervalTime   任务间隔毫秒周期
	 * @param clazz
	 * @throws Exception
	 */
	public static void startTask(String taskName, long startDelay, long intervalTime, Runnable runnable) throws Exception
	{
		if (taskName == null || taskName.isEmpty())
		{
			throw new Exception("taskName不能为空");
		}
		ScheduledExecutorService service = getScheduledExecutorService(taskName);
		if (service == null)
		{
			throw new Exception("任务调速器为空");
		}
		service.scheduleAtFixedRate(runnable, startDelay, intervalTime, TimeUnit.MILLISECONDS);
	}

	/**
	 * 开始一个简单任务
	 * 
	 * @param taskName     任务名
	 * @param startTime      开始时间，格式:(yyyy-MM-dd HH:mm:ss)(2018-03-07 16:40:00)
	 * @param intervalTime  执行间隔，毫秒
	 * @param clazz             任务
	 * @throws Exception
	 */
	public static <T> void startTask(String taskName, String startTime, long intervalTime, Class<? extends Runnable> clazz) throws Exception
	{
		long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime).getTime();
		long startDelay = time - MyTimeUtil.getCurrTimeMM();
		startTask(taskName, startDelay, intervalTime, clazz);
	}

	/**
	 * 结束指定任务
	 * 
	 * @param taskName
	 * @throws Exception
	 */
	public static void shutDown(String taskName) throws Exception
	{
		ScheduledExecutorService service = getScheduledExecutorService(taskName);
		if (service == null)
		{
			throw new Exception("任务调速器为空");
		}
		service.shutdown();
	}

	/**
	 * 结束所有任务
	 */
	public static void shutDownAll()
	{
		for (ScheduledExecutorService iterable_element : map_service.values())
		{
			iterable_element.shutdown();
		}
	}

	private synchronized static ScheduledExecutorService getScheduledExecutorService(String taskName)
	{
		ScheduledExecutorService service = map_service.get(taskName);
		if (service == null)
		{
			service = Executors.newSingleThreadScheduledExecutor(new BasicThreadFactory.Builder().namingPattern("SimpleTaskUtil-" +taskName).build());
			map_service.put(taskName, service);
		}
		return service;
	}
}
