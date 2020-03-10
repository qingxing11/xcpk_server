package com.gjc.cmd.friends;

import java.util.ArrayList;

import com.wt.archive.AddFriendsVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushAddFriendsEventResponse extends Response {
	
	public ArrayList<AddFriendsVO> list;

	public PushAddFriendsEventResponse() {
		this.msgType=MsgTypeEnum.ASK_推送添加好友.getType();
	}

	public PushAddFriendsEventResponse(int code,ArrayList<AddFriendsVO> list) {
		this.code=code;
		this.msgType=MsgTypeEnum.ASK_推送添加好友.getType();
		this.list = list;
	}

	@Override
	public String toString() {
		return "PushAddFriendsEventResponse [list=" + list + "]";
	}
}
