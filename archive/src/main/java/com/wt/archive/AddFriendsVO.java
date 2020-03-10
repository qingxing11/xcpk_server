package com.wt.archive;

import java.io.Serializable;

import model.UserFriendseventModel;

public class AddFriendsVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;//玩家id
	public String nickName;//玩家昵称
	public String icon;//玩家头像
	public int lv;//玩家等级
	public int state;//请求状态:确定/拒绝/未处理
	public boolean readState;//请求添加消息状态
	public int roleId;//玩家角色
	 public int vipLv;//玩家vipLv

	
	public AddFriendsVO(int id, String nickName, String icon, int lv, int state, boolean readState, int roleId,int vipLv) 
	{
		this.id = id;
		this.nickName = nickName;
		this.icon = icon;
		this.lv = lv;
		this.state = state;
		this.readState = readState;
		this.roleId = roleId;
		this.vipLv = vipLv;
	}

	@Override
	public String toString() {
		return "AddFriendsVO [id=" + id + ", nickName=" + nickName + ", icon=" + icon + ", lv=" + lv + ", state="
				+ state + ", readState=" + readState + ", roleId=" + roleId + ", vipLv=" + vipLv + "]";
	}

	public AddFriendsVO() 
	{
		
	}
	
	public AddFriendsVO(UserFriendseventModel model)
	{
		this.id=model.getOtherID();
		this.nickName=model.getNikeName();
		this.icon=model.getIcon();
		this.lv=model.getLv();
		this.state=model.getState();
		this.readState=model.getResultReadState();
		this.roleId=model.getRoleId();
		this.vipLv=model.getVipLv();
	}
}
