package com.wt.naval.event.timer;

import java.util.ArrayList;

public class TimerEvent
{
	private static ArrayList<TimeEventListener> list_fiveMinuteEvent = new ArrayList<>();

	public static void addEventListener(TimeEventListener listener)
	{
		list_fiveMinuteEvent.add(listener);
	}

	/**
	 * 
	 */
	public static void fiveMinuteEvent()
	{
		for (TimeEventListener fiveMinuteEventListener : list_fiveMinuteEvent)
		{
			if (fiveMinuteEventListener instanceof FiveMinuteEventListener)
			{
				((FiveMinuteEventListener) fiveMinuteEventListener).fiveMinuteTimeUp();
			}
		}
	}
	
	public static void newDayEvent()
	{
		for (TimeEventListener newDayEventListener : list_fiveMinuteEvent)
		{
			if (newDayEventListener instanceof NewDayEventListener)
			{
				((NewDayEventListener) newDayEventListener).newDayEvent();
			}
		}
	}

	public static void newWeekEvent()
	{
		for (TimeEventListener timeEventListener : list_fiveMinuteEvent)
		{
			if (timeEventListener instanceof NewWeekEventListener)
			{
				((NewWeekEventListener) timeEventListener).newWeekEvent();
			}
		}
	}
}