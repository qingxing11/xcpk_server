package com.wt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MyTimeUtil
{
	/** 秒 */
	public static final long TIME_S = 1000L;

	/** 分 */
	public static final long TIME_M = TIME_S * 60L;

	/** 小时 */
	public static final long TIME_H = TIME_M * 60L;

	/** 天 */
	public static final long TIME_DAY = TIME_H * 24L;

	public static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
	public static SimpleDateFormat sdf4 = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static SimpleDateFormat sdf6 = new SimpleDateFormat("HH");
	public static SimpleDateFormat sdf7 = new SimpleDateFormat("mm");
	public static SimpleDateFormat sdf8 = new SimpleDateFormat("ss");
	
	public static String getString(String str)
	{
		if (str == null)
			return "";
		return str.trim();
	}

	public static int getInt(String str)
	{
		if (str == null || "".equals(str.trim()))
		{
			return 0;
		}

		try
		{
			return Integer.decode(str.trim());
		}
		catch (Exception e)
		{
			return Integer.valueOf(str.trim());
		}
	}

	public static float getFloat(String str)
	{
		if (str == null || "".equals(str.trim()))
		{
			return 0;
		}
		return Float.valueOf(str.trim());
	}

	public static boolean isToday(String otherDay)
	{
		if (getToday().equals(otherDay))
		{
			return true;
		}
		return false;
	}

	public static boolean isSameDay(long time1, long time2)
	{
		return getDate(time1).equals(getDate(time2));
	}

	public static boolean isSameDay(String dateTime1, String dateTime2)
	{
		if (dateTime1 != dateTime2)
		{
			if ((("".equals(dateTime1) && "".equals(dateTime2))) || ("".equals(dateTime1) && !"".equals(dateTime2)) || (!"".equals(dateTime1) && "".equals(dateTime2)))
			{
				return true;
			}
			else
			{
				String d1 = dateTime1.substring(0, 10);
				String d2 = dateTime2.substring(0, 10);
				return d1.equals(d2);
			}
		}
		else
		{
			return true;
		}
	}

	/**
	 * 2个时间戳是否相同月份
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isSameMonth(long time1, long time2)
	{
		String date1 = getDate(time1);
		String date2 = getDate(time2);
		return date1.substring(0, 7).equals(date2.substring(0, 7));
	}

	public static String getDate(long time)
	{
		return sdf2.format(time);
	}

	public static String getTime(long time)
	{
		return sdf3.format(time);
	}

	public static String getTime2(long time)
	{
		return sdf4.format(time);
	}

	public static String getDateTime(long time)
	{
		return sdf1.format(time);
	}

	public static String getNow()
	{
		return sdf1.format(System.currentTimeMillis());
	}

	public static String getToday()
	{
		return sdf1.format(System.currentTimeMillis()).substring(0, 10);
	}

	public static String getHours(long time)
	{
		return sdf6.format(time);
	}
	
	public static long getHour(long time)
	{
		return time/1000/60/60;
	}

	/**
	 * 根据日期时间字串获取时间戳
	 * 
	 * @param dateTime
	 *                yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long getTimeStamp(String dateTime)
	{
		if (dateTime == null || "".equals(dateTime.trim()))
		{
			return 0;
		}
		try
		{
			return sdf1.parse(dateTime).getTime();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据毫秒数获取当前年中的星期数
	 * @param time
	 * @return
	 */
	public static int getWeekInYear(long time)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		return week;
	}

	/** 根据当前日期获取周几* */
	public static int getWeekDay(long time)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		int num = calendar.get(Calendar.DAY_OF_WEEK);
		if (num == Calendar.SUNDAY)
		{
			return 7;
		}
		else if (num == Calendar.MONDAY)
		{
			return 1;
		}
		else if (num == Calendar.TUESDAY)
		{
			return 2;
		}
		else if (num == Calendar.WEDNESDAY)
		{
			return 3;
		}
		else if (num == Calendar.THURSDAY)
		{
			return 4;
		}
		else if (num == Calendar.FRIDAY)
		{
			return 5;
		}
		else
		{
			return 6;
		}
	}

	/** 现在到下个整点的分钟差* */
	public static int getMin(long time)
	{
		String minStr = getNow().substring(14, 16);
		return 60 - getInt(minStr);
	}

	/** 俩个时间差多少秒* */
	public static int getSubSecond(long time1, long time2)
	{
		long time = time2 - time1;
		return getInt(sdf7.format(time)) * 60 + getInt(sdf8.format(time));
	}

	/** 俩个时间差多少小时* */
	public static int getSubHour(int time1, int time2)
	{
		int time = Math.abs(time2) - Math.abs(time1);
		int hour = (int) (time / 60 / 60);
//		Tool.print_debug_level0("#上一次时间：" + time1 + ", 本次时间：" + time2 + ", 相差几个小时：" + hour);
		return hour;
	}

	/**
	 * 刨去整点小时剩余秒
	 * 
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int getRemainSecond(int time1, int time2)
	{
		int time = Math.abs(time2) - Math.abs(time1);
		int second = time - (int) (time / 60 / 60) * 60 * 60;
//		Tool.print_debug_level0("#上一次时间：" + time1 + ", 本次时间：" + time2 + ", 刨除整点还剩多少秒：" + second);
		return second;
	}

	/** 俩个时间差多少分钟* */
	public static int getSubMin(long time1, long time2)
	{
		long time = time2 - time1;
		return getMin(time);
	}

	/**
	 * 现在到当天23:59:59的时间差毫秒数
	 * 
	 * @return 时间差毫秒数
	 */
	public static long getDayEndTime()
	{
		Calendar c1 = new GregorianCalendar();
		c1.set(Calendar.HOUR_OF_DAY, 23);
		c1.set(Calendar.MINUTE, 59);
		c1.set(Calendar.SECOND, 59);
		long tragetTime = c1.getTimeInMillis();
		long millis = tragetTime - getCurrTimeMM();
		return millis;
	}

	//今天过了多少秒
	public static long getDayPassTime()
	{
		Calendar c1 = new GregorianCalendar();
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		long tragetTime = c1.getTimeInMillis();
		long millis = getCurrTimeMM()-tragetTime;
		return millis/TIME_S;
	}
	
	public static long getTimeText(int day, int hour, int minute, int second)
	{
		Calendar c1 = new GregorianCalendar();
		c1.set(Calendar.DAY_OF_WEEK, day);
		c1.set(Calendar.HOUR_OF_DAY, hour);
		c1.set(Calendar.MINUTE, minute);
		c1.set(Calendar.SECOND, second);
		long tragetTime = c1.getTimeInMillis();
		long millis = tragetTime - getCurrTimeMM();
		return millis;
	}

	/** 结束时间与今天之间的时间差* */
	public static int getDays(String time)
	{
		if (getToday().equals(time))// 如果同一天则以小时计
		{
			return (int) (getDayEndTime() / 1000 / 60);
		}
		else
		{
			int day = getInt(time.substring(8, 10)) - getInt(getNow().substring(8, 10));
			return 24 * day * 60;
		}
	}

	// 对比两天时间，之前，之后
	public static boolean compareTime(String time1, String time2)
	{
		Date date1 = null;
		Date date2 = null;
		Date now = null;
		try
		{
			if (time1 != null && time1.length() > 0)
			{
				date1 = sdf2.parse(time1);
			}
			if (time2 != null && time2.length() > 0)
			{
				date2 = sdf2.parse(time2);
			}

			now = sdf2.parse(getDate(System.currentTimeMillis()));
		}
		catch (Exception e)
		{
			System.out.println("日期转换错误" + e.getMessage());
		}
		if (date1 != null && date2 != null && now != null)
		{
			if (date1.before(now) && date2.after(now))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}

	/**
	 * 获取本月的天数
	 * 
	 * @return
	 */
	public static int getDayNumsOfThisMonth()
	{
		String nowDate = getDate(System.currentTimeMillis());
		String[] ss = nowDate.split("-");
		int year = getInt(ss[0]);
		int month = getInt(ss[1]);

		int dayNums = 0;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)
		{
			dayNums = 31;
		}
		else if (month == 2)
		{
			// 闰月
			if ((year % 100 == 0 && year % 400 == 0) || (year % 100 != 0 && year % 4 == 0))
			{
				dayNums = 29;
			}
			else
			{
				dayNums = 28;
			}
		}
		else
		{
			dayNums = 30;
		}
		return dayNums;
	}

	/**
	 * 计算x天后的日期
	 * 
	 * @param date
	 * @param x
	 * @return
	 */
	public static String ofterDayDate(String date, int x)
	{
		String[] dateStrings = date.split("-");
		int year = Integer.parseInt(dateStrings[0]);
		int month = Integer.parseInt(dateStrings[1]);
		int day = Integer.parseInt(dateStrings[2]) + x;

		if (day > 30 && (month == 4 || month == 6 || month == 9 || month == 11))
		{
			day = day - 30;
			if (month == 12)
			{
				month = 1;
				year = year + 1;
			}
			else
			{
				month = month + 1;
			}
		}
		else if (day > 31 && (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12))
		{
			day = day - 31;
			if (month == 12)
			{
				month = 1;
				year = year + 1;
			}
			else
			{
				month = month + 1;
			}
		}
		else if (month == 2)
		{
			if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
			{
				if (day > 29)
				{
					day = day - 29;
					month = 3;
				}
			}
			else
			{
				if (day > 28)
				{
					day = day - 28;
					month = 3;
				}
			}
		}

		String y = year + "-";
		String m = "";
		String d = "";
		if (month < 10)
		{
			m = "0" + month + "-";
		}
		else
		{
			m = month + "-";
		}
		if (day < 10)
		{
			d = "0" + day;
		}
		else
		{
			d = day + "";
		}
		return y + m + d;
	}

	/***
	 * 2个日期相差几天
	 * 
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static int compareTo(String day1, String day2)
	{
		int day = 0;
		try
		{
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(sdf2.parse(day1));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(sdf2.parse(day2));
			long l = cal2.getTimeInMillis() - cal1.getTimeInMillis();
			day = new Long(l / (1000 * 60 * 60 * 24)).intValue();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return day;
	}

	public static long getCurrTimeMM()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 根据毫秒数获取到天数
	 * @param mm
	 * @return
	 */
	public static int getDay(long mm)
	{
		return (int) (mm / 86400000L);
	}
	
	/**
	 * 获取当前时间戳对应的天数
	 * @return
	 */
	public static int getDayNow()
	{
		return (int) (getCurrTimeMM() / 86400000L);
	}

	/**
	 * 小时：分钟：秒
	 * 
	 * @param mm
	 * @return
	 */
	public static String mmToTimeHHMMSS(long mm)
	{
		if (mm < 0)
		{
			return "0:00:00";
		}
		long h = mm / 3600000;
		h %= 24;
		long m = (60 + mm / 60000) % 60;
		long s = (60 + mm / 1000) % 60;
		StringBuffer buffer = new StringBuffer();
		buffer.append(h < 10 ? "0" + h : h);
		buffer.append(":");
		buffer.append(m < 10 ? ("0" + m) : ("" + m));
		buffer.append(":");
		buffer.append(s < 10 ? ("0" + s) : ("" + s));
		return buffer.toString();
	}

	public static String mmToTimeHHMM(long mm)
	{
		if (mm < 0)
		{
			return "00:00";
		}
		long h = mm / 3600000;
		h %= 24;
		long m = (60 + mm / 60000) % 60;
		StringBuffer buffer = new StringBuffer();
		buffer.append(h);
		buffer.append(":");
		buffer.append(m < 10 ? ("0" + m) : ("" + m));
		return buffer.toString();
	}

	public static String mmToTimeMMSS(int mm)
	{
		if (mm < 0)
		{
			return "00:00";
		}
		int m = (60 + mm / 60000) % 60;
		int s = (60 + mm / 1000) % 60;
		StringBuffer buffer = new StringBuffer();
		buffer.append(m < 10 ? ("0" + m) : ("" + m));
		buffer.append(":");
		buffer.append(s < 10 ? ("0" + s) : ("" + s));
		return buffer.toString();
	}

	// 另一种展示方式
	public static String mmToTimeHHMMSS_HMS(long mm)
	{
		if (mm < 0)
		{
			return "0h00m00s";
		}
		long h = mm / 3600000;
		h %= 24;
		long m = (60 + mm / 60000) % 60;
		long s = (60 + mm / 1000) % 60;
		StringBuffer buffer = new StringBuffer();
		if (h > 0)
		{
			buffer.append(h);
			buffer.append("h");
		}
		if (m > 0)
		{
			buffer.append(m < 10 ? ("0" + m) : ("" + m));
			buffer.append("m");
		}
		else if (h > 0)
		{
			buffer.append("00m");
		}
		buffer.append(s < 10 ? ("0" + s) : ("" + s));
		buffer.append("s");
		return buffer.toString();
	}

	public static String mmToTimeHHMM_HM(long mm)
	{
		if (mm < 0)
		{
			return "0h00m";
		}
		long h = mm / 3600000;
		h %= 24;
		long m = (60 + mm / 60000) % 60;
		StringBuffer buffer = new StringBuffer();
		if (h > 0)
		{
			buffer.append(h);
			buffer.append("h");
		}
		buffer.append(m < 10 ? ("0" + m) : ("" + m));
		buffer.append("m");
		return buffer.toString();
	}

	public static String mmToTimeMMSS_MS(int mm)
	{
		if (mm < 0)
		{
			return "0m00s";
		}
		int m = (60 + mm / 60000) % 60;
		int s = (60 + mm / 1000) % 60;
		StringBuffer buffer = new StringBuffer();
		if (m > 0)
		{
			buffer.append(m < 10 ? ("0" + m) : ("" + m));
			buffer.append("m");
		}
		buffer.append(s < 10 ? ("0" + s) : ("" + s));
		buffer.append("s");
		return buffer.toString();
	}

	/**
	 * 获取当前时间里当天最后一秒剩余的秒数
	 * 
	 * @return
	 */
	public static int getDayLastSLeft()
	{
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dstr = "2020-" + month + "- " + day + " 23:59:59 ";
		Date date = null;
		try
		{
			date = sdf.parse(dstr);
		}
		catch (ParseException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		long s1 = date.getTime();// 将时间转为毫秒
		long s2 = System.currentTimeMillis();// 得到当前的毫秒
		int s = (int) (s1 - s2) / 1000;
		return s;
	}

	public static int subDay(long oldTime, long curTime) 
	{
		String date1 = getDate(oldTime);
		String date2 = getDate(curTime);
		int day=Integer.parseInt(date1.substring(9, 10))-Integer.parseInt(date2.substring(9, 10));
		return Math.abs(day);
	}
}
