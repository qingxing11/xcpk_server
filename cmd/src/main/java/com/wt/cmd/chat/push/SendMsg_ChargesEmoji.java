package com.wt.cmd.chat.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
 
public class SendMsg_ChargesEmoji extends Response
{
	/**发送方id*/
	public int userId;
	
	/**表情id*/
	public int emojiId;
	
	/**目标玩家id,目标玩家应添加相应金币*/
	public int toUserId;
	
	public int roomId;
	public SendMsg_ChargesEmoji(int userId,int emojiId,int toUserId,int roomId)
	{
		this.msgType = MsgTypeEnum.PUSH_收费表情.getType();
		this.userId = userId;
		this.emojiId = emojiId;
		this.toUserId = toUserId;
		this.roomId = roomId;
	}
	
	public SendMsg_ChargesEmoji()
	{
		this.msgType = MsgTypeEnum.PUSH_收费表情.getType();
	}
}
