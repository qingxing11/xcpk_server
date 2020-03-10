package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class ChatResponse extends Response
{
	public static final int ERROR_不在游戏中 = 0;
	
	public ChatResponse()
	{
		msgType = MsgType.CHAT_文字聊天;	
	}

	public ChatResponse(int code)
	{
		msgType = MsgType.CHAT_文字聊天;
		this.code = code;
	}
}