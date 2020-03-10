package com.gjc.cmd.friendChat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FriendChatRequest extends Request
{
	public int userId;
	public int friendId;
	public String info;
	
	public FriendChatRequest() {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天.getType();
	}

	public FriendChatRequest(int userId, int friendId, String info) {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天.getType();
		this.userId = userId;
		this.friendId = friendId;
		this.info = info;
	}

	@Override
	public String toString() {
		return "FriendChatRequest [userId=" + userId + ", friendId=" + friendId + ", info=" + info + ", msgType="
				+ msgType + ", callBackId=" + callBackId + "]";
	}

	
}
