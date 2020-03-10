package com.wt.job.further;

import com.wt.job.IJob;

/**
 * 返回further的job扩展
 * @author WangTuo
 *
 */
public interface IFurtherJob extends IJob
{
	/**
	 * 任务完成
	 * @param object
	 */
	void operationComplete(Object object);
	
	/***
	 * 添加任务完成需要执行的事件
	 * @param furtherListener
	 */
	void addFurtherListener(IFurtherJobListener furtherListener);
}
