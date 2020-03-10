package com.gjc.cmd.friends;

import com.wt.archive.AddFriendsVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushAddFriendsResponse extends Response
{
	public AddFriendsVO vo;
	
	public PushAddFriendsResponse()
	{
		this.msgType=MsgTypeEnum.ASK_推送添加好友.getType();
	}
	public PushAddFriendsResponse(int code)
	{
		this.msgType=MsgTypeEnum.ASK_推送添加好友.getType();
		this.code=code;
	}
	
	public PushAddFriendsResponse(int code,AddFriendsVO vo)
	{
		this.msgType=MsgTypeEnum.ASK_推送添加好友.getType();
		this.code=code;
		this.vo=vo;
	}
	
	@Override
	public String toString()
	{
		return "PushAddFriendsResponse [AddFriendsVO=" + vo+"]";
	}
}
