package com.wt.util.quartz;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import com.wt.config.BaseConfig;
import com.wt.util.Tool;
import com.wt.util.log.LogUtil;

public class SimpleTriggerQuartz 
{
//	public static void main(String[] args)
//	{
//		startSimpleTask("test1111", 1000, 1000, 0, TestJob.class);
//	}
	
//	@Override
//	public void execute(JobExecutionContext context) throws JobExecutionException
//	{
//		
//		System.out.println(context.getTrigger().getKey().getName());
//	}
	
	private static final String GROUP_NAME = "simpleTaskGroup";
	private static ConcurrentHashMap<String, TriggerKey> map_jobKey = new ConcurrentHashMap<>();

	/**
	 * 开始一个简单Quartz任务，至少执行一次
	 * 
	 * @param jobName
	 *                任务名
	 * @param delayStartTime
	 *                任务延迟时间
	 * @param intervalTime
	 *                每次执行间隔
	 * @param repeatCount
	 *                额外重复的次数
	 * @param clazz
	 */
	public static <T> void startSimpleTask(String jobName, long delayStartTime, long intervalTime, int repeatCount, Class<? extends Job> clazz)
	{
		long time = System.currentTimeMillis() + delayStartTime; // 延迟指定时间后执行
		startSimpleTask("", jobName, GROUP_NAME, time, intervalTime, repeatCount, clazz);
	}

	// 执行单次任务
	public static <T> void startSingleTask(String jobName, long delayStartTime, Class<? extends Job> clazz)
	{
		removeJob(jobName);

		long time = System.currentTimeMillis() + delayStartTime; // 延迟指定时间后执行
		startSimpleTask("", jobName, GROUP_NAME, time, 0, 0, clazz);
	}

	public static <T> void startSingleTask(int jobName, long delayStartTime, Class<? extends Job> clazz)
	{
		startSingleTask(jobName + "", delayStartTime, clazz);
	}
	
	public static <T> void startSimpleTask(String description, String jobName, String groupName, long startTime, long intervalTime, int repeatCount, Class<? extends Job> clazz)
	{
		try
		{
			// 1.创建Scheduler的工厂
			// 2.从工厂中获取调度器实例
			Scheduler scheduler = getSchedulerFactory().getScheduler();

			// 3.创建Job描述
			JobBuilder jb = JobBuilder.newJob(clazz).withDescription(description);
			// job的描述
			// if (!jobNameString.isEmpty() && !groupName.isEmpty())
			// {
			// jb.withIdentity(jobNameString, groupName); //
			// job的name和group
			// }

			// 任务运行的时间，SimpleSchedle类型触发器有效
			Date statTime = new Date(startTime);

			// 4.创建Trigger
			SimpleTrigger t = simpleTrigger(intervalTime, repeatCount, statTime, jobName, groupName);

			// 5.注册任务和定时器
			scheduler.scheduleJob(jb.build(), t);

			// 6.启动 调度器
			scheduler.start();

			// System.out.println("启动时间 ： " + new
			// Date()+",间隔:"+intervalTime+",执行次数:"+repeatCount);
		}
		catch (Exception e)
		{
			LogUtil.print_error("开始任务:" + jobName + "，时异常", e);
			e.printStackTrace();
		}
	}

	/**
	 * 暂停指定任务
	 * 
	 * @param name
	 * @throws Exception
	 */
	public static void pauseJob(String name) throws Exception
	{
		if (map_jobKey.get(name) == null)
		{
			throw new Exception("任务:" + name + "，不存在!");
		}
		sf.getScheduler().pauseTrigger(map_jobKey.get(name));
	}

	public static void removeJob(int name)
	{
		removeJob(String.valueOf(name));
	}

	public static void removeJob(String name)
	{
		if (map_jobKey.get(name) == null)
		{
			// throw new Exception("任务:"+name+"，不存在!");
			Tool.print_error("SimpleTriggerQuartz -> removeJob:任务:" + name + "，不存在!");
			return;
		}
		try
		{
			TriggerKey triggerKey = map_jobKey.remove(name);
			sf.getScheduler().unscheduleJob(triggerKey);
			sf.getScheduler().deleteJob(JobKey.jobKey(name, GROUP_NAME));// 删除任务
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();

			Tool.print_error("移出任务:" + name + "错误!");
		}
	}

	public static void clear()
	{
		try
		{
			sf.getScheduler().clear();
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
		}
	}

	public static void resume(String name) throws Exception
	{
		if (map_jobKey.get(name) == null)
		{
			throw new Exception("任务:" + name + "，不存在!");
		}
		sf.getScheduler().resumeTrigger(map_jobKey.get(name));
	}

	private static SimpleTrigger simpleTrigger(long intervalTime, int repeatCount, Date statTime, String jobName, String groupName)
	{
		SimpleTrigger t = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity(jobName, groupName)
				.startAt(statTime).withSchedule(SimpleScheduleBuilder// SimpleScheduleBuilder是简单调用触发器，它只能指定触发的间隔时间和执行次数；
				.simpleSchedule()// 创建一个SimpleScheduleBuilder
				.withIntervalInMilliseconds(intervalTime).withRepeatCount(repeatCount))// 指定反复的次数
				.build();
		
		if (t.getKey() != null)
		{
			map_jobKey.put(jobName, t.getKey());
		}
		return t;
	}

	private static SchedulerFactory sf = null;

	private static SchedulerFactory getSchedulerFactory()
	{
		if(sf == null)
		{
			synchronized (map_jobKey)
			{
				if (sf == null)
				{
					try
					{
						sf = new StdSchedulerFactory(BaseConfig.getProperties(BaseConfig.getConfigPath() + "quartz.properties"));
					}
					catch (SchedulerException e)
					{
						e.printStackTrace();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					} //
				}
			}
		}
		return sf;
	}
	
	public static boolean isJobExists(String jobName)
	{
		TriggerKey triggerKey = map_jobKey.get(jobName);
		if (triggerKey == null)
		{
			return false;
		}
		boolean isExists = false;
		try
		{
			isExists = getSchedulerFactory().getScheduler().checkExists(triggerKey);
		}
		catch (SchedulerException e)
		{
			e.printStackTrace();
			isExists = false;
		}
		return isExists;
	}
}
