package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class UpdatePlayerPhoneNumberRequest extends Request
{
	public int userId;
	public String phoneNumber;

	public UpdatePlayerPhoneNumberRequest()
	{
		msgType = MsgTypeEnum.SET_更新玩家手机号.getType();
	}
	
	public UpdatePlayerPhoneNumberRequest(int userId,String phoneNumber)
	{
		msgType = MsgTypeEnum.SET_更新玩家手机号.getType();
		this.phoneNumber = phoneNumber;
		this.userId = userId;
	}
}
