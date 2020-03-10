package com.wt.util.security;

import com.wt.util.Tool;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * 通道附加属性，当玩家通过安全验证途径创建连接后为该通道附加本属性
 * 
 * @author WangTuo
 */
public class MySession
{
	public static final long OFFLINE_TIME = Tool.TIME_M * 10;
	
	
	public static final AttributeKey<MySession> attr_session = AttributeKey.valueOf("session");//

	/** 玩家编号 */
	private Integer userId;

	/** 引用的通道 */
	private Channel channel;
	/** 远程地址 */
	private String hostAddress;

	/** 包的个数 */
	private int packageNum;

	/** 开始统计包的时间 */
	private long startTime;
	/** 空闲时间 */
	private long offlineStartTime;
	/*** 记录解包失败的个数 */
	private int errorPackageNum;

	/**
	 * 心跳超时次数
	 */
	private int idleNum;

	private State state;

	public enum State
	{
		State_normal, State_offline,
	}

	public MySession(Channel channel, int userId)
	{
		this.channel = channel;
		this.userId = userId;
	}

	public State getState()
	{
		return state;
	}

	public void setState(State state)
	{
		this.state = state;
		if (state == State.State_offline)
		{
			setOfflineStartTime(Tool.getCurrTimeMM());
		}
	}

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}

	public String getHostAddress()
	{
		return hostAddress;
	}

	public void setHostAddress(String hostAddress)
	{
		this.hostAddress = hostAddress;
	}

	public int getPackageNum()
	{
		return packageNum;
	}

	public void setPackageNum(int packageNum)
	{
		this.packageNum = packageNum;
	}

	public long getStartTime()
	{
		return startTime;
	}

	public void setStartTime(long startTime)
	{
		this.startTime = startTime;
	}

	public long getOfflineStartTime()
	{
		return offlineStartTime;
	}

	public void setOfflineStartTime(long offlineTime)
	{
		this.offlineStartTime = offlineTime;
	}

	public int getErrorPackageNum()
	{
		return errorPackageNum;
	}

	public void setErrorPackageNum(int errorPackageNum)
	{
		this.errorPackageNum = errorPackageNum;
	}

	public void destroy()
	{
		channel.close();
	}

	public boolean isClosed()
	{
		return !channel.isOpen();
	}

	public int getIdleNum()
	{
		return idleNum;
	}

	public void setIdleNum(int idleNum)
	{
		this.idleNum = idleNum;
	}

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public void addIdleNum()
	{
		this.idleNum++;
	}

	public int getOfflineTime()
	{
		return (int) (Tool.getCurrTimeMM() - offlineStartTime);
	}

	public void clearIdleNum()
	{
		idleNum = 0;
	}

	public boolean isOffline()
	{
		return state == State.State_offline;
	}
	
	public boolean offlineOverTime()
	{
		return Tool.getCurrTimeMM() - getOfflineStartTime() >= OFFLINE_TIME;
	}

	public void close()
	{
		getChannel().close();
	}
}