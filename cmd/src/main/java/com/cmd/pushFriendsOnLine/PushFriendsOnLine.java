package com.cmd.pushFriendsOnLine;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class PushFriendsOnLine extends Response
{
	public int uId;
	public boolean onLine;//是否在线
	
	public PushFriendsOnLine()
	{
		this.code = Response.SUCCESS;
		this.msgType =MsgType.PushFriendsOnLine_好友是否在线 ;
	}
	
	public PushFriendsOnLine( int uId, boolean onLine)
	{
		this.code = Response.SUCCESS;
		this.msgType =MsgType.PushFriendsOnLine_好友是否在线;
		this.uId = uId;
		this.onLine=onLine;
	}
	@Override
	public String toString()
	{
		return "PushFriendsOnLine [uId=" + uId+",onLine="+onLine+ "]";
	}

}
