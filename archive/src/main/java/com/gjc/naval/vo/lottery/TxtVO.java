package com.gjc.naval.vo.lottery;


public class TxtVO
{
	public int userId;
	public String nikeName;
	public long money;
	public int type;//场次：彩票场0
	
	@Override
	public String toString() 
	{
		return "TxtVO [userId=" + userId + ", nikeName=" + nikeName + ", money=" + money + ", type=" + type + "]";
	}

	public TxtVO(WinVO vo,String nikeName) 
	{
		this.userId=vo.userId;
		this.nikeName =nikeName;
		this.money=vo.money;
		this.type=0;
	}
}
