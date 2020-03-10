package com.gjc.cmd.friends;

import java.util.Arrays;

import com.wt.archive.AddFriendsVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AddFriendsResponse extends Response
{
	public static final int Error_重复请求添加=0;
	public static final int Error_已经添加好友=1;
	public static final int Error_超过好友上限=2;
			
	public AddFriendsVO vo;
	
	public AddFriendsResponse()
	{
		this.msgType=MsgTypeEnum.ADDFRIENDS.getType();
	}
	public AddFriendsResponse(int code)
	{
		this.msgType=MsgTypeEnum.ADDFRIENDS.getType();
		this.code=code;
	}
	
	public AddFriendsResponse(int code,AddFriendsVO vo)
	{
		this.msgType=MsgTypeEnum.ADDFRIENDS.getType();
		this.code=code;
		this.vo=vo;
	}
	
	@Override
	public String toString() {
		return "AddFriendsResponse [vo=" + vo + ", msgType=" + msgType + ", data=" + Arrays.toString(data)
				+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}
}
