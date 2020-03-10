package com.wt.event.server.shutdown;

import java.util.ArrayList;

public class ShutDownEvent
{
	private static ArrayList<IShutDown> list_shutDown = new ArrayList<>();
	
	private static boolean isShutDown;
	
	/**
	 * 添加一个任务到优雅退出
	 * @param shutdown
	 */
	public static void addShutDownTask(IShutDown shutdown)
	{
		synchronized (list_shutDown)
		{
			if(!isShutDown)
				list_shutDown.add(shutdown);
		}
	}
	
	/**
	 * 服务器退出时需要额外执行的任务
	 * @param shutdown
	 */
	public static void shutdown()
	{
		synchronized (list_shutDown)
		{
			isShutDown = true;
			for (IShutDown iShutDown : list_shutDown)
			{
				iShutDown.shutDown();
			}
		}
	}
}
