package com.wt.cmd.chat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ChargesEmojiResponse extends Response
{
	public static final int ERROR_不在游戏中 = 0;
	public static final int ERROR_金币不足 = 1;
	
	public int emojiId;
	public int toUserId;
	
	public int roomId;
	
	public ChargesEmojiResponse()
	{
		msgType = MsgTypeEnum.CHAT_收费表情.getType();
	}

	public ChargesEmojiResponse(int code)
	{
		msgType = MsgTypeEnum.CHAT_收费表情.getType();
		this.code = code;
	}

	public ChargesEmojiResponse(int code, int emojiId, int toUserId)
	{
		msgType = MsgTypeEnum.CHAT_收费表情.getType();
		this.code = code;
		this.emojiId = emojiId;
		this.toUserId = toUserId;
	}

	@Override
	public String toString()
	{
		return "ChargesEmojiResponse [emojiId=" + emojiId + ", toUserId=" + toUserId + ", msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}