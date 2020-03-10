package com.wt.job.further;

import com.wt.job.MyJobTask;

/**
 * 扩展普通job，增加任务完成后回调
 * @author WangTuo
 *
 */
public class MyFurtherJobTask
{
	private static MyJobTask furtherJobThread = MyJobTask.getJobTaskThread("further_job_task");
	
	/**
	 * 添加一个有回调事件的任务
	 * @param job
	 */
	 public static void addTask(IFurtherJob job)
	 {
		 furtherJobThread.addJob(job);
	 }
	 
	public static void addTask(IFurtherJob iJob, IFurtherJobListener furtherJobListener)
	{
		iJob.addFurtherListener(furtherJobListener);
		addTask(iJob);
	}
}