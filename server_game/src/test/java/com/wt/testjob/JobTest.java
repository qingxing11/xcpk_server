package com.wt.testjob;

import com.wt.util.quartz.SimpleTriggerQuartz;

import junit.framework.TestCase;

public class JobTest extends TestCase
{
	public void testCase()
	{
		SimpleTriggerQuartz.startSimpleTask("test1111", 1000, 1000, 0, TestJob.class);
	}
}
