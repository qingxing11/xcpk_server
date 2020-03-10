package com.wt.job.further;

import com.wt.util.log.LogUtil;

public abstract class MyFurtherJob implements IFurtherJob
{
	private String name;
	public MyFurtherJob()
	{
		
	}
	
	public MyFurtherJob(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	private IFurtherJobListener furtherListener;
	public void operationComplete(Object object)
	{
		if(furtherListener == null)
		{
			LogUtil.print_error("任务:"+name+"的furtherListener为空");
			return;
		}
		try
		{
			furtherListener.operationComplete(object);
		}
		catch (Exception e)
		{
			LogUtil.print_error("执行"+name+"的回调任务时错误",e);
			e.printStackTrace();
		}
		
	}
	
	public void addFurtherListener(IFurtherJobListener furtherListener)
	{
		this.furtherListener = furtherListener;
		MyFurtherJobTask.addTask(this);
	}
	
	public  abstract void execute();
}
