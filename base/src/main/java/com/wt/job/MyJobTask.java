package com.wt.job;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.wt.event.server.shutdown.IShutDown;
import com.wt.event.server.shutdown.ShutDownEvent;
import com.wt.util.log.LogUtil;

/**
 *提供一个独立的线程运行指定任务，并且在服务器安全退出时等待剩余任务完成。适合需要线程安全，io读写等的场景
 * @author WangTuo
 *
 */
public class MyJobTask implements Runnable,IShutDown
{
	private static ConcurrentHashMap<String,MyJobTask> map_jobTaskThread = new ConcurrentHashMap<>();
	
	private static MyJobTask jobTask = getJobTaskThread("job_task_thread");
	public static void addTask(IJob job)
	{
		jobTask.addJob(job);
	}
	
	public static MyJobTask getJobTaskThread(String name)
	{
		MyJobTask jobTask = map_jobTaskThread.get(name);
		if(jobTask == null)
		{
			jobTask = new MyJobTask(name);
		}
		return jobTask;
	}
	
	
	private boolean isRun;
	private LinkedBlockingQueue<IJob> queue_job = new LinkedBlockingQueue<>();
	
 	private Thread taskThread;
	private String name;
	private boolean isShutDown;
	private MyJobTask(String name)
	{
		this.name = name;
		isRun = true;
		taskThread = new Thread(this,name);
		taskThread.start();//further线程单独启动
		ShutDownEvent.addShutDownTask(this);
	}
	
	public void addJob(IJob futherTask)
	{
		try
		{
			queue_job.put(futherTask);
		}
		catch (InterruptedException e)
		{
			LogUtil.print_error("加入Further任务时错误", e);
			e.printStackTrace();
		}
		taskResume();
	}

	public boolean allComplete()
	{
		return queue_job.size() == 0;
	}
	
	public void shutDown()
	{
		taskResume();
		synchronized (taskThread)
		{
			isShutDown = true;
			if(!allComplete())
			{
				try
				{
					taskThread.join();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					LogUtil.print_error(e);
				}
			}
		}
	}
	
	@Override
	public void run()
	{
		while (isRun)
		{
			try
			{
				IJob further = queue_job.take();
				further.execute();
			}
			catch (Exception e)
			{
				LogUtil.print_error("执行【"+name+"】任务出错", e);
				e.printStackTrace();
			}
 			
			if (queue_job.size() == 0)
			{
				synchronized (taskThread)
				{
					if(isShutDown)
					{
						isRun = false;
						return;
					}
					else
					{
						taskWait();
					}
				}
			}
		}
	}

	private synchronized void taskWait()
	{
		try
		{
			this.wait();
		}
		catch (InterruptedException e)
		{
			LogUtil.print_error("任务【"+name+"】等待时出错", e);
			e.printStackTrace();
		}
	}

	private synchronized void taskResume()
	{
		this.notify();
	}
}