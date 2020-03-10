package com.wt.xcpk.vo;

public class KillRoomNoticVO
{
	public int userId;
	public String nickName;
	public long score;
	
	public KillRoomNoticVO()
	{
	}
	
	public KillRoomNoticVO(int userId, String nickName, long score)
	{
		super();
		this.userId = userId;
		this.nickName = nickName;
		this.score = score;
	}

	@Override
	public String toString()
	{
		return "KillRoomNoticVO [userId=" + userId + ", nickName=" + nickName + ", score=" + score + "]";
	}
}
