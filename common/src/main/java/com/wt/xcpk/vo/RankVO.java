package com.wt.xcpk.vo;

import com.wt.util.sort.ISortLong;

public class RankVO implements ISortLong
{
	public int userId;
	public String nickName;
	public long score;
	public int rank;
	public int level;
	
	public RankVO(long coins, int userId, String nickName,int rank,int level)
	{
		this.userId =userId;
		this.nickName  = nickName;
		this.score = coins;
		this.rank = rank;
		this.level= level;
	}
	
	public RankVO() {}

	@Override
	public String toString()
	{
		return "RankVO [userId=" + userId + ", nickName=" + nickName + ", score=" + score + ", level=" + level + ", rank=" + rank + "]";
	}

	@Override
	public long getSortIndex()
	{
		return score;
	}

	public void setRank(int i)
	{
		this.rank = i;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public long getScore()
	{
		return score;
	}

	public void setScore(long score)
	{
		this.score = score;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getRank()
	{
		return rank;
	}

	public void addScore(long winNum)
	{
		this.score += winNum;
	}
	
	
}
