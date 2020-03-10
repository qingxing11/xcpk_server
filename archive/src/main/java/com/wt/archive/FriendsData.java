package com.wt.archive;

import java.io.Serializable;

public class FriendsData implements Serializable
{
	private static final long serialVersionUID = -1131191243818227890L;

	public int id;
	public String nickName;
	public String icon;
	public int lv;
	public int roleId;
	public int vipLv;
	// public byte[] headIconImg;
	public String headIconUrl;

	public FriendsData(int id, String nickName, String icon, int lv, int roleId, int vipLv, String headIconUrl)
	{
		this.id = id;
		this.nickName = nickName;
		this.icon = icon;
		this.lv = lv;
		this.roleId = roleId;
		this.vipLv = vipLv;
		this.headIconUrl = headIconUrl;
	}

	public FriendsData()
	{

	}

	@Override
	public String toString()
	{
		return "FriendsData [id=" + id + ", nickName=" + nickName + ", icon=" + icon + ", lv=" + lv + ", roleId=" + roleId + ", vipLv=" + vipLv + ", headIconUrl=" + headIconUrl + "]";
	}

}
