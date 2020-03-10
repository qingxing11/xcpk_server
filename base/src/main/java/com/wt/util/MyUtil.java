package com.wt.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.wt.unity.unityengine.Vector2;

public class MyUtil
{
	private static Random random = new Random();

	/**
	 * 两点间距离
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double getDistance(Vector2 v1,Vector2 v2)
	{
		return  Math.sqrt(Math.pow(v1.x-v2.x, 2)+Math.pow(v1.y-v2.y, 2));
		
	}
	
	/**
	 * 取得一个0~MAX的随机数字
	 */
	public static int getRandom()
	{
		return random.nextInt() >>> 1;
	}

	/**
	 * 取得一个0~MAX的随机数字
	 */
	public static float getRandomFloat()
	{
		return random.nextFloat();
	}

	/**
	 * 取得limit范围内的随机数字
	 * 
	 * @param limit
	 */
	public static int getRandom(int limit)
	{
		return random.nextInt(limit);
	}

	public static long getCurrTimeMM(int timeZone)
	{
		return System.currentTimeMillis() + timeZone * 3600 * 1000L;
	}

	/**
	 * 取得start到end大小的随机数字,不包含end
	 * 
	 * @param start
	 * @param end
	 */
	public static int getRandom(int start, int end)
	{
		return getRandom() % (end - start ) + start;
	}

	/**
	 * 取得随机boolean值
	 */
	public static boolean getRandomBoolean()
	{
		return (random.nextInt() % 2 == 0) ? true : false;
	}

	/**
	 * 返回1表示成功获得概率内值，返回0表示不成功
	 */
	public static int getProb(int prob, int base)
	{
		if (random.nextInt(base) < prob)
		{
			return 1;
		}
		return 0;
	}

	// 毫秒转化为天
	public static int mmToDay(long mm)
	{
		return (int) (mm / 86400000L);
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
	 * @return
	 */
	public static int getDayLastSLeft()
	{
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dstr="2016-"+month+"- "+day+ " 23:59:59 ";
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
		long s1=date.getTime();//将时间转为毫秒
		long s2=System.currentTimeMillis();//得到当前的毫秒
		int s=(int) (s1 - s2)/1000;
		return s;
	}
	
	public static boolean isEmpty(CharSequence str)
	{
		if (str == null || str.length() == 0)
			return true;
		else
			return false;
	}
	
	public static String getMD5(byte[] data)
	{
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(data);
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 获得0-25对应的字母，
	 * @param index 0:a;1:b;2:c....25:z;
	 * @return
	 */
	public static String getAbc(int index)
	{
		char a = 'a';
		a += index;
		return (a + "");
	}
	
	 public static final int randomBetween(int min, int max)
	{
		return (int) (min + Math.round(Math.random() * (max - min)));
	}
	 
	public static byte[] intToBytes(int num)
	{
		byte[] b = new byte[4];
		for (int i = 0 ; i < 4 ; i++)
		{
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}
	
	public static byte[] getStringBytes(String str)
	{
		try
		{
			return str.getBytes("utf-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String filterEmoji(String source)
	{
		if (StringUtils.isNotBlank(source))
		{
			return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		}
		else
		{
			return source;
		}
	}
}
