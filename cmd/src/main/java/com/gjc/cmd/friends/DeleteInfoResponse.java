package com.gjc.cmd.friends;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class DeleteInfoResponse extends Response
{
	public int userId;

	public DeleteInfoResponse() 
	{
		this.msgType= MsgTypeEnum.Delete_删除消息  .getType();
	}

	public DeleteInfoResponse(int userId) 
	{
		this.msgType= MsgTypeEnum.Delete_删除消息  .getType();
		this.code=DeleteInfoResponse.SUCCESS;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "DeleteInfoResponse [userId=" + userId + ", msgType=" + msgType + ", data=" + Arrays.toString(data)
				+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}

	
}
