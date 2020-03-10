package com.brc.cmd.update;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class UpdateNickNameResponse extends Response 
{
	public static final int ERROR_昵称重复 = 0;
	public static final int ERROR_改名卡不足 = 1;
	
	 public String nickName;
	
	public UpdateNickNameResponse(int code)
	{
		msgType=MsgTypeEnum.UPDATENICKNAME.getType();
		this.code=code;
	}
	
	public UpdateNickNameResponse(int code,String nickName)
	{
		msgType=MsgTypeEnum.UPDATENICKNAME.getType();
		this.code=code;
		this.nickName=nickName;
	}

	@Override
	public String toString()
	{
		return "UpdateNickNameResponse [nickName=" + nickName + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
