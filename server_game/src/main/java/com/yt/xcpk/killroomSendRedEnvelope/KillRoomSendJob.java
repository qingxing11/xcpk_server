package com.yt.xcpk.killroomSendRedEnvelope;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class KillRoomSendJob implements Job
{
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException
	{
		KillRoomSendRedEnvelopeImpl.inst.clearRedPackState();
	}
}
