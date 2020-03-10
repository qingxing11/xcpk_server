package com.gjc.cmd.friends;

import com.gjc.naval.vo.chat.FriendsDataVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class LookFriendsResponse extends Response
{
	public static final int ERROR_查无此人= 0; 
	public FriendsDataVO vo;
	
	public LookFriendsResponse()
	{
		this.msgType=MsgTypeEnum.Look_查找好友.getType();
	}
	public LookFriendsResponse(int code)
	{
		this.msgType=MsgTypeEnum.Look_查找好友.getType();
		this.code=code;
	}
	public LookFriendsResponse(int code, FriendsDataVO vo)
	{
		this.msgType=MsgTypeEnum.Look_查找好友.getType();
		this.code=code;
		this.vo=vo;
	}
	
	@Override
	public String toString()
	{
		return "LookFriendsResponse [vo=" + vo+"]";
	}
}
