package com.wt.naval.biz;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

import com.wt.archive.GameData;

public class LoadTestBiz
{
	public static ConcurrentHashMap<Integer, GameData> onlines = new ConcurrentHashMap<Integer, GameData>();//用户在线数据
	public static ConcurrentHashMap<Integer, Channel> sessions = new ConcurrentHashMap<Integer, Channel>();//用户在线session
	public static ConcurrentHashMap<Channel, String> keys = new ConcurrentHashMap<Channel, String>();//用户当前密钥
	public static boolean addOnline( Channel channel)
	{
		return true;
	}
}
