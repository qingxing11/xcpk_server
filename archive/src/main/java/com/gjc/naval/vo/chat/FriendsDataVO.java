package com.gjc.naval.vo.chat;

import java.io.Serializable;

import com.wt.archive.AddFriendsVO;
import com.wt.archive.FriendsData;

public class FriendsDataVO implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public FriendsData friendsData;
	public boolean stateOnLine;

	public FriendsDataVO(FriendsData friendsData, boolean stateOnLine)
	{
		this.friendsData = friendsData;
		this.stateOnLine = stateOnLine;
	}

	public FriendsDataVO()
	{

	}

	public FriendsDataVO(AddFriendsVO addVO, boolean stateOnLine)
	{
		FriendsData data = new FriendsData();
		data.icon = addVO.icon;
		data.id = addVO.id;
		data.lv = addVO.lv;
		data.nickName = addVO.nickName;
		data.roleId = addVO.roleId;
		data.vipLv = addVO.vipLv;
		this.friendsData = data;
		this.stateOnLine = stateOnLine;
	}

	public FriendsDataVO(int id, int lv, String icon, String nikeName, boolean stateOnLine, int roleId, int vipLv, String headIconUrl)
	{
		FriendsData data = new FriendsData();
		data.icon = icon;
		data.id = id;
		data.lv = lv;
		data.nickName = nikeName;
		data.roleId = roleId;
		data.vipLv = vipLv;
		data.headIconUrl = headIconUrl;
		this.friendsData = data;
		this.stateOnLine = stateOnLine;
	}

	@Override
	public String toString()
	{
		return "FriendsDataVO [friendsData=" + friendsData + ", stateOnLine=" + stateOnLine + "]";
	}
}
