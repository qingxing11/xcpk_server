package com.wt.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadpool
{
	public static void sleep(int time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public final static ExecutorService workerThreadPool = MyThreadpool.newWorkerThreadPool(Runtime.getRuntime().availableProcessors());

	/**
	 * 阻塞的ExecutorService
	 * 
	 * @param size
	 * @return
	 */
	private static ExecutorService newWorkerThreadPool(int size)
	{
		return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), new RejectedExecutionHandler()
		{
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
			{
				try
				{
					executor.getQueue().put(r);
				}
				catch (InterruptedException e)
				{
					throw new RuntimeException(e);
				}
			}
		});
	}
}
