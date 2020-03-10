package com.gjc.cmd.friends;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class DeleteFriendResponse extends Response
{
	public static final int Error_不存在好友=0;
	
	public int userId;
	
	public int askUserId;//主动删除方
	
	public DeleteFriendResponse()
	{
		this.msgType=MsgTypeEnum.Delete_删除好友.getType(); 
	}

	public DeleteFriendResponse(int code,int userId,int askUserId)
	{
		this.msgType=MsgTypeEnum.Delete_删除好友.getType(); 
		this.code=code;
		this.userId=userId;
		this.askUserId=askUserId;
	}
	
	
	@Override
	public String toString() {
		return "DeleteFriendResponse [userId=" + userId + ", askUserId=" + askUserId + ", msgType=" + msgType
				+ ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
	
}
