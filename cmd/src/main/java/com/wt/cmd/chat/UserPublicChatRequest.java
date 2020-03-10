package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class UserPublicChatRequest  extends Request
{
	public String txt;
	public int userId;
	public UserPublicChatRequest()
	{
		msgType = MsgType.CHAT_玩家喊话;
	}
	
	public UserPublicChatRequest(int userId,String txt,int callBackId)
	{
		msgType = MsgType.CHAT_玩家喊话;
		this.txt = txt;
		this.callBackId = callBackId;
		this.userId = userId;
	}
}