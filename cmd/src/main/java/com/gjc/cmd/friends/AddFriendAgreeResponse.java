package com.gjc.cmd.friends;

import com.gjc.naval.vo.chat.FriendsDataVO;
import com.wt.archive.FriendInfoVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AddFriendAgreeResponse extends Response {
	
	public FriendsDataVO vo;
	public FriendInfoVO info;

	public AddFriendAgreeResponse() {
		this.msgType = MsgTypeEnum.Agree_同意添加好友.getType();
	}

	public AddFriendAgreeResponse(int code) {
		this.msgType = MsgTypeEnum.Agree_同意添加好友.getType();
		this.code = code;
	}

	public AddFriendAgreeResponse(int code, FriendsDataVO vo,FriendInfoVO info) {
		this.msgType = MsgTypeEnum.Agree_同意添加好友.getType();
		this.code = code;
		this.vo = vo;
		this.info=info;
	}

	@Override
	public String toString() {
		return "AddFriendAgreeResponse [vo=" + vo + ", info=" + info + "]";
	}
}
