package com.wt.naval.event.timer;

/***
 * 5分钟任务定时事件
 * @author akwang
 *
 */
public interface FiveMinuteEventListener extends TimeEventListener
{
	/**
	 * 5分钟任务定时事件
	 */
	void fiveMinuteTimeUp();
}
