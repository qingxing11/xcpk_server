package com.wt.xcpk.killroom;

import com.wt.util.sort.ISortLong;

public class KillRoomRoundRankVO implements ISortLong
{
	public int userId;
	public long score;
	public KillRoomRoundRankVO(int userId, long score)
	{
		this.userId = userId;
		this.score = score;
	}
	
	public KillRoomRoundRankVO() {}

	@Override
	public long getSortIndex()
	{
		return score;
	}
	public KillRoomRoundRankVO(int userId, int score)
	{
		this.userId = userId;
		this.score = score;
	}
}
