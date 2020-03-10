package com.wt.cmd.backend;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class PaySuccessRequest extends Request
{
	public int userId;
	public int giftNum;
	public PaySuccessRequest()
	{
		msgType = MsgType.BACKEND_购买金币成功;
	}
	
	public PaySuccessRequest(int userId,int giftNum)
	{
		msgType = MsgType.BACKEND_购买金币成功;
		this.giftNum = giftNum;
		this.userId = userId;
	}
}
