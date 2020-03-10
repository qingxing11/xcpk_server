package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class EmojiChatRequest  extends Request
{
	public int emojiId;
	public EmojiChatRequest()
	{
		msgType = MsgType.CHAT_表情聊天;
	}
	
	public EmojiChatRequest(int emojiId)
	{
		msgType = MsgType.CHAT_表情聊天;
		this.emojiId = emojiId;
	}
}