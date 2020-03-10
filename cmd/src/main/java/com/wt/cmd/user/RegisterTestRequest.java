package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class RegisterTestRequest extends Request
{
	public String userName = "";
	public RegisterTestRequest()
	{}

	public RegisterTestRequest(String userName,int callBackId)
	{
		this.msgType = MsgType.USER_REGISTER_TESTACCOUNT;
		this.userName = userName;
 		
 		this.callBackId = callBackId;
	}
}
