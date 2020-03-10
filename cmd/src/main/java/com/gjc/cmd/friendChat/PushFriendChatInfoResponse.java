package com.gjc.cmd.friendChat;

import java.util.ArrayList;
import java.util.Arrays;

import com.gjc.naval.vo.friendChat.FriendChatVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushFriendChatInfoResponse extends Response
{
		public ArrayList<FriendChatVO> list;
		
		public PushFriendChatInfoResponse() {
			this.msgType = MsgTypeEnum.ChatInfo_好友聊天消息.getType();
		}

		public PushFriendChatInfoResponse(ArrayList<FriendChatVO> list) {
			this.msgType = MsgTypeEnum.ChatInfo_好友聊天消息.getType();
			this.code=PushFriendChatInfoResponse.SUCCESS;
			this.list = list;
		}

		@Override
		public String toString() {
			return "PushFriendChatInfoResponse [list=" + list + ", msgType=" + msgType + ", data="
					+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
		}
		
		
}
