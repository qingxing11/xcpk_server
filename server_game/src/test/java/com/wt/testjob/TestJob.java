package com.wt.testjob;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestJob implements Job
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		System.err.println("TestJob======");
	}
}
