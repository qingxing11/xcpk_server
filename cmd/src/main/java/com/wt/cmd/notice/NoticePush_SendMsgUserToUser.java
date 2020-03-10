package com.wt.cmd.notice;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class NoticePush_SendMsgUserToUser extends Response
{
	public int msgType;
	
	/**
	 * 发送者
	 */
	public String fromNickName;
	public String msg;
	
	public NoticePush_SendMsgUserToUser()
	{
		msgType = MsgType.PUSHUSER_玩家私聊;
	}
	
	
	public NoticePush_SendMsgUserToUser(String fromNickName,String msg)
	{
		msgType = MsgType.PUSHUSER_玩家私聊;
		this.msg = msg;
		this.fromNickName = fromNickName;
	}
}
