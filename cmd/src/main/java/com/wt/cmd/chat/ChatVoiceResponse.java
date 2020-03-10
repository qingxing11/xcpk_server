package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class ChatVoiceResponse extends Response
{
	public static final int ERROR_不在游戏中 = 0;
	
	public int pos;
	
	public String voice_id;
	
	public ChatVoiceResponse(int code)
	{
		msgType = MsgType.CHAT_语音聊天;	
		
		this.code = code ;//
	}
	public ChatVoiceResponse(int pos,String voice_id)
	{
		msgType = MsgType.CHAT_语音聊天;
		
		this.pos = pos;
		
		this.voice_id = voice_id;
		
		this.code = ChatVoiceResponse.SUCCESS;
	}
}
