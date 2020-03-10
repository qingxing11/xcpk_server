package com.wt.cmd.chat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

 
public class ChargesEmojiRequest  extends Request
{
	/**
	 * 表情使用收费 分三个档次：前面2个表情设置5万，中间两个10万，后面两个20万。被表情的玩家可以获得80%金币
	 */
	public int emojiId;
	public int toUserId;
	
	public int roomId;
	public ChargesEmojiRequest()
	{
		msgType = MsgTypeEnum.CHAT_收费表情.getType();
	}
	
	public ChargesEmojiRequest(int emojiId)
	{
		msgType = MsgTypeEnum.CHAT_收费表情.getType();
		this.emojiId = emojiId;
	}
}