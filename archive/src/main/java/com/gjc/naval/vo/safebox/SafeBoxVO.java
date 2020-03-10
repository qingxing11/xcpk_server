package com.gjc.naval.vo.safebox;

public class SafeBoxVO 
{
	public int userId;
	public int otherId;
	public long money;
	
	public SafeBoxVO() 
	{
		
	}
	
	public SafeBoxVO(int userId, int otherId, long money) 
	{
		this.userId = userId;
		this.otherId = otherId;
		this.money = money;
	}


	@Override
	public String toString() 
	{
		return "SafeBoxVO [userId=" + userId + ", otherId=" + otherId + ", money=" + money + "]";
	}
	
}
