package com.wt.xcpk.vo;

import com.wt.util.sort.ISortLong;

public class KillRoomBigWinVO implements ISortLong
{
	private  int userId;
	private String nickName;
	private int roleId;
	private String headIconUrl;
	private long score;
	
	public KillRoomBigWinVO(int userId, String nickName, int roleId,String headIconUrl, long score)
	{
		super();
		this.userId = userId;
		this.roleId = roleId;
		this.nickName = nickName;
		this.headIconUrl = headIconUrl;
		this.score = score;
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
	 
	public String getHeadIconUrl()
	{
		return headIconUrl;
	}
	public void setHeadIconUrl(String headIconUrl)
	{
		this.headIconUrl = headIconUrl;
	}
	public long getScore()
	{
		return score;
	}
	public void setScore(long score)
	{
		this.score = score;
	}
	
	public int getRoleId()
	{
		return roleId;
	}
	
	@Override
	public long getSortIndex()
	{
		return score;
	}
	@Override
	public String toString()
	{
		return "KillRoomBigWinVO [userId=" + userId + ", nickName=" + nickName + ", roleId=" + roleId + ", score=" + score + "]";
	}
}
