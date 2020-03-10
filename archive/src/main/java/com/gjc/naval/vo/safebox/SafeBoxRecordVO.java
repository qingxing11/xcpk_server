package com.gjc.naval.vo.safebox;

import java.io.Serializable;

public class SafeBoxRecordVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 547005444312490485L;
	public int userId;
	public int otherId;
	public long money;
	public long time;
	public int type;
	public float pre;//手续费率
	
	public SafeBoxRecordVO() 
	{
		
	}
	public SafeBoxRecordVO(int userId, int otherId, long money, long time, int type,float pre) 
	{
		this.userId = userId;
		this.otherId = otherId;
		this.money = money;
		this.time = time;
		this.type = type;
		this.pre=pre;
	}


	@Override
	public String toString() {
		return "SafeBoxRecordVO [userId=" + userId + ", otherId=" + otherId + ", money=" + money + ", time=" + time
				+ ", type=" + type + ", pre=" + pre + "]";
	}

}
