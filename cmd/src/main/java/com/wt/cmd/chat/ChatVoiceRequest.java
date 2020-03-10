package com.wt.cmd.chat;
import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class ChatVoiceRequest extends Request 
{
	public String voice_id;
	public ChatVoiceRequest()
	{
		msgType = MsgType.CHAT_语音聊天;
	}
	
	public ChatVoiceRequest(String id)
	{
		msgType = MsgType.CHAT_语音聊天;
		this.voice_id = id;
	}
}
