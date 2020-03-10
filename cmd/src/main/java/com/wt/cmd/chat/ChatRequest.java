package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class ChatRequest  extends Request
{
	public String chatTxt;
	public ChatRequest()
	{
		msgType = MsgType.CHAT_文字聊天;
	}
	
	public ChatRequest(String chatTxt)
	{
		msgType = MsgType.CHAT_文字聊天;
		this.chatTxt = chatTxt;
	}
}