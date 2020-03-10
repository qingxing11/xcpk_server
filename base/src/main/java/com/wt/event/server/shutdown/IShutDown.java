package com.wt.event.server.shutdown;

/**
 * 需要在服务器获得退出指令后继续执行的任务
 * @author WangTuo
 *
 */
public interface IShutDown
{
	/**
	 * 发送服务器退出指令后继续执行
	 */
	void shutDown();
}
