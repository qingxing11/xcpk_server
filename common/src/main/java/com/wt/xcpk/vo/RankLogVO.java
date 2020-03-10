package com.wt.xcpk.vo;

public class RankLogVO
{
	public int userId;
	public long reward;
	public String InsertTime;
	public int type;
	
	public RankLogVO(long coins, int userId, String insertTime,int type)
	{
		this.userId =userId;
		this.reward = coins;
		this.InsertTime = insertTime;
		this.type= type;
	}
	
	public RankLogVO() {}

	@Override
	public String toString()
	{
		return "RankVO [userId=" + userId + ", InsertTime=" + InsertTime + ", reward=" + reward + ", type=" + type + "]";
	}

	public int getUserId()
	{
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public long getReward()
	{
		return reward;
	}
	
	public void setReward(long reward) {
		this.reward = reward;
	}
	
	public String getInsertTime()
	{
		return InsertTime;
	}
	
	public void setInsertTime(String InsertTime) {
		this.InsertTime = InsertTime;
	}
	
	public int getType()
	{
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
}
