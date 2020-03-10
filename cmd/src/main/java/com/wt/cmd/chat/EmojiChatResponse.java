package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class EmojiChatResponse extends Response
{
	public static final int ERROR_不在游戏中 = 0;
	
	public EmojiChatResponse()
	{
		msgType = MsgType.CHAT_表情聊天;	
	}

	public EmojiChatResponse(int code)
	{
		msgType = MsgType.CHAT_表情聊天;
		this.code = code;
	}
}