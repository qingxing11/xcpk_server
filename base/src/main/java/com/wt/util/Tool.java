package com.wt.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.util.log.CoinsLogger;
import com.wt.util.log.DbErrorLogger;
import com.wt.util.log.ErrorLogger;
import com.wt.util.log.LogUtil;
import com.wt.util.security.MySession;
import io.netty.channel.ChannelHandlerContext;

public class Tool implements MsgType
{
	/**秒*/
	public static final long TIME_S = 1000L;
	
	/**分*/
	public static final long TIME_M = TIME_S * 60L;
	
	/**小时*/
	public static final long TIME_H = TIME_M * 60L;
	
	/**天*/
	public static final long TIME_D = TIME_H * 24L;
	
	private static Random random = new Random();

	private static int print_debug_leven;
	
	public static void setPrintLevel(int level)
	{
		print_debug_leven = level;
	}
	
	public static void setPrintLine(boolean isPrintLine)
	{
		LogUtil.setPrintLine(isPrintLine);
	}
	
	/**
	 * 取得一个0~MAX的随机数字
	 */
	public static int getRandom()
	{
		return random.nextInt() >>> 1;
	}

	public static float getRandomFloat(float a,float b)
	{
		return RandomUtils.nextFloat(a, b);
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
		return (random.nextInt() >>> 1) % limit;
	}

	public static long getCurrTimeMM(int timeZone)
	{
		return System.currentTimeMillis() + timeZone * 3600 * 1000L;
	}

	public static long getCurrTimeMM()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 取得start到end大小的随机数字
	 * 取得start到end大小的随机数字
	 * 取得start到end大小的随机数字
	 * 
	 * @param start
	 * @param end
	 */
	public static int getRandom(int start, int end)
	{
		return getRandom() % (end - start + 1) + start;
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

	public static void print_debug_level0(String nickName, String str)
	{
		print_debug_level0("【" + nickName + "】" + str);
	}
	
	public static void print_debug_level1(String nickName, String str)
	{
		print_debug_level1("【" + nickName + "】" + str);
	}
	
	public static void print_debug_level2(String nickName, String str)
	{
		print_debug_level2("【" + nickName + "】" + str);
	}

	public static void print_debug_level0(String nickName, int tileIndex, String txt)
	{
		if(print_debug_leven <= 0)
			LogUtil.print_debug("【" + nickName + "】" + "【" + MsgType.getTile(tileIndex) + "】" + txt);
	}
	
	public static void print_debug_level0(String nickName, MsgTypeEnum tileIndex, String txt)
	{
		print_debug_level0("【" + nickName + "】" + "【" + tileIndex.getText() + "】" + txt);
	}
	
	public static void print_debug_level1(String nickName, int tileIndex, String txt)
	{
		print_debug_level1("【" + nickName + "】" + "【" + MsgType.getTile(tileIndex) + "】" + txt);
	}
	
	public static void print_debug_level1(String nickName, MsgTypeEnum msgType, String txt)
	{
		print_debug_level1("【" + nickName + "】" + "【" +msgType.getText() + "】" + txt);
	}
	
	public static void print_debug_level2(String nickName, int tileIndex, String txt)
	{
		print_debug_level2("【" + nickName + "】" + "【" + MsgType.getTile(tileIndex) + "】" + txt);
	}

	public static void print_debug_level0(String string)
	{
		if(print_debug_leven <= 0)
		LogUtil.print_debug(string);
	}
	
	/**
	 * 金币变化
	 * @param nickName 昵称
	 * @param num 数量
	 * @param res 来源
	 */
	public static void print_coins(String nickName,long num,String res,long now)
	{
		if(num != 0)
		{
			LogUtil.pring_coins("玩家获得金币["+nickName+"],金币变化["+num+"],来源:"+res+",now:"+now);
		}
	}
	
	public static void print_crystals(String nickName,long num,String res)
	{
		if(num != 0)
		{
			LogUtil.pring_coins("玩家获得钻石["+nickName+"],钻石变化["+num+"],来源:"+res);
		}
	}
	
	public static void print_subCoins(String nickName,long num,String res,long now)
	{
		if(num != 0)
		{
			LogUtil.pring_coins("玩家减少金币["+nickName+"],金币变化["+num+"],来源:"+res+",now:"+now);
		}
	}
	
	public static void print_debug_level1(String string)
	{
		if(print_debug_leven <= 1)
			LogUtil.print_debug(string);
	}
	
	public static void print_debug_level2(String string)
	{
		if(print_debug_leven <= 2)
			LogUtil.print_debug(string);
	}

	public static void print_debug_level0(int tile, String str)
	{
		if(print_debug_leven <= 0)
			LogUtil.print_debug("【" + MsgType.getTile(tile) + "】" + str);
	}
	
	public static void print_debug_level0(MsgTypeEnum tile, String str)
	{
		if(print_debug_leven <= 0)
			LogUtil.print_debug("【" + tile.getText() + "】" + str);
	}
	

	
	public static void print_debug_level1(int tile, String str)
	{
		print_debug_level1("【" + MsgType.getTile(tile) + "】" + str);
	}
	
	public static void print_debug_level2(int tile, String str)
	{
		print_debug_level2("【" + MsgType.getTile(tile) + "】" + str);
	}
	

	public static void print_error(String string)
	{
//		MyLogger.getLogger(MyConfig.instance().log4j_path).info("【error】"+string);
		LogUtil.print_error(string);
	}
	
	public static void print_error(Class<?> clazz,String string)
	{
//		MyLogger.getLogger(MyConfig.instance().log4j_path).info("【error】"+string);
		LogUtil.print_error(StringUtils.join(clazz.getName(),string));
	}
	
	public static void print_error(String string,Throwable e)
	{
//		MyLogger.getLogger(MyConfig.instance().log4j_path).info("【error】"+string);
		LogUtil.print_error(string,e);
	}
	
	public static void print_error(Throwable e)
	{
//		MyLogger.getLogger(MyConfig.instance().log4j_path).info("【error】"+string);
		e.printStackTrace();
		ErrorLogger.logger.error(e);
	}
	
	public static void print_error(String nickName, int tileIndex, String string)
	{
		print_error("【" + nickName + "】" + "【" + MsgType.getTile(tileIndex) + "】" + string);
	}
	
	public static void print_error(String nickName, MsgTypeEnum tileIndex, String string)
	{
		print_error("【" + nickName + "】" + "【" + tileIndex.getText() + "】" + string);
	}
	
	public static void print_error(MsgTypeEnum tileIndex, String string)
	{
		print_error("【" + tileIndex.getText() + "】" + string);
	}
	
	public static void print_error(int tileIndex, String string)
	{
		print_error("【" + MsgType.getTile(tileIndex) + "】" + string);
	}
	
	public static void print_dbError(String string,Throwable e)
	{
		DbErrorLogger.logger.error(string, e);
	}
	
	public static void print_dbError(Throwable e)
	{
		e.printStackTrace();
		DbErrorLogger.logger.error(e);
	}
	
	public static void print_dbError(String string)
	{
		DbErrorLogger.logger.error(string);
	}
	
	public static void print_debug_admin(String string)
	{
//		MyLogger.getLogger(MyConfig.instance().log4j_info).info("【管理员指令】【" + string + "】");
		LogUtil.print_debug_admin("【管理员指令】【" + string + "】");
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
	
	public static String getAddresByChannel(String channel)
	{
		if(channel.isEmpty())
		{
			return "";
		}
		if(channel.contains("/"))
		{
			String addres = channel.substring(1);
			String[] ip_port = addres.split(":");
			if(ip_port.length > 1)
			{
				return  ip_port[0];
			}
		}
		return channel;
	}
	
	public static MySession getSession(ChannelHandlerContext ctx)
	{
		return ctx.channel().attr(MySession.attr_session).get();
	}
}
