package com.gjc.cmd.friendChat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FriendChatInfoRequest extends Request
{
	public int friendId;
	public FriendChatInfoRequest() {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天状态.getType();
	}
	
	public FriendChatInfoRequest(int friendId) {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天状态.getType();
		this.friendId = friendId;
	}
	
	@Override
	public String toString() {
		return "FriendChatInfoRequest [friendId=" + friendId + ", msgType=" + msgType + ", callBackId=" + callBackId
				+ "]";
	}
	
	
}
