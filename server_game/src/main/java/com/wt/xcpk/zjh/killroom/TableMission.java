package com.wt.xcpk.zjh.killroom;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.wt.util.Tool;

public class TableMission implements Job
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		try
		{
			String jobName = context.getTrigger().getKey().getName();
			String[] jobNameToArr = StringUtils.split(jobName,":");
			
			int userId = Integer.parseInt(jobNameToArr[1]);
			KillRoomImpl.inst.tableTimeUP(userId);
		}
		catch (Exception e)
		{
			Tool.print_error("执行卡座站起任务时错误", e);
		}
	}
}
