package com.gjc.naval.vo.lottery;

public class LotteryDataVO
{
	public int userId;//玩家id
	public int type;//彩票类型
	public int typeNum;  //购买数量

	public LotteryDataVO()
	{
		
	}
	
	public LotteryDataVO(int userId, int type, int typeNum) 
	{
		this.userId = userId;
		this.type = type;
		this.typeNum = typeNum;
	}



	@Override
	public String toString() {
		return "LotteryDataVO [userId=" + userId + ", type=" + type + ", typeNum=" + typeNum + "]";
	}
}
