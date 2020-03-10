package com.wt.job.further;

/**
 * 任务主题完成后的回调
 * @author WangTuo
 *
 */
public interface IFurtherJobListener
{
	/**
	 * 任务完成
	 */
	void operationComplete(Object object);
}
