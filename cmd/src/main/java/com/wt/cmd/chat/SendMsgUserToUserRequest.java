package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class SendMsgUserToUserRequest  extends Request
{
	public String txt;
	
	public int toUserId;
	public SendMsgUserToUserRequest()
	{
		msgType = MsgType.CHAT_玩家私聊;
	}
	
	public SendMsgUserToUserRequest(String txt,int toUserId)
	{
		msgType = MsgType.CHAT_玩家私聊;
		this.txt = txt;
		this.toUserId = toUserId;
	}
}