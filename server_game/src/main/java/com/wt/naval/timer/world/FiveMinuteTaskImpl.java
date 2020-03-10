package com.wt.naval.timer.world;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.naval.event.timer.TimerEvent;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.timetask.SimpleTaskUtil;

@Service
public class FiveMinuteTaskImpl implements Runnable,FiveMinuteTaskService
{
	private static final int TIMER = (int) (MyTimeUtil.TIME_M * 5);

	public static FiveMinuteTaskImpl inst;
	 
//	private int lastDay;
	private int lastWeek;
	private long lastCheckTime;
	
	
	@PostConstruct
	private void init()
	{
		inst = this;
		initTimeEvent();
		try
		{
			SimpleTaskUtil.startTask("SaveWorldTask", TIMER, TIMER, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void initTimeEvent()
	{
		lastCheckTime = MyTimeUtil.getCurrTimeMM();
		lastWeek = MyTimeUtil.getWeekInYear(MyTimeUtil.getCurrTimeMM());
	}

	@Override
	public void run()
	{
		try
		{
			TimerEvent.fiveMinuteEvent();
		}
		catch (Exception e)
		{
			Tool.print_error("fiveMinuteEvent错误",e);
		}
		
		try
		{
			runCheckNewDayEvent();
		}
		catch (Exception e)
		{
			Tool.print_error("runCheckNewDayEvent错误",e);
		}
	
		try
		{
			runCheckNewWeekEvent();
		}
		catch (Exception e)
		{
			Tool.print_error("runCheckNewWeekEvent错误",e);
		}
	}

	private void runCheckNewWeekEvent()
	{
		int week = MyTimeUtil.getWeekInYear(MyTimeUtil.getCurrTimeMM());
		if(lastWeek != week)
		{
			lastWeek = week;
			TimerEvent.newWeekEvent();
		}
	}

	private void runCheckNewDayEvent()
	{
		if(!MyTimeUtil.isSameDay(lastCheckTime, MyTimeUtil.getCurrTimeMM()))
		{
			Tool.print_debug_level0("runCheckNewDayEvent---->");
			lastCheckTime = MyTimeUtil.getCurrTimeMM();
			TimerEvent.newDayEvent();
		}
	}
}