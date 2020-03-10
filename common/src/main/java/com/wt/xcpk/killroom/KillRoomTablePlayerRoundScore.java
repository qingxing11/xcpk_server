package com.wt.xcpk.killroom;

public class KillRoomTablePlayerRoundScore
{
	public int pos;
	public long score;
	public int userId;
	public int direction;
	public KillRoomTablePlayerRoundScore(int pos, int score,int userId)
	{
		this.pos = pos;
		this.score = score;
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "KillRoomTablePlayerRoundScore [pos=" + pos + ", score=" + score + ", userId=" + userId + "]";
	}
}
