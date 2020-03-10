package com.gjc.cmd.friendChat;

import java.util.Arrays;

import com.gjc.naval.vo.friendChat.FriendChatVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class FriendChatResponse extends Response {
	public FriendChatVO vo;

	public FriendChatResponse() {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天.getType();
	}

	public FriendChatResponse(int code,FriendChatVO vo) {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天.getType();
		this.vo = vo;
		this.code=code;
	}

	@Override
	public String toString() {
		return "FriendChatResponse [vo=" + vo + ", msgType=" + msgType + ", data=" + Arrays.toString(data)
				+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}

}
