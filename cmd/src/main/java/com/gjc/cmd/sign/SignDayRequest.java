package com.gjc.cmd.sign;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class SignDayRequest extends Request
{
	public int day;//第几天

	public SignDayRequest()
	{
		this.msgType= MsgTypeEnum.Sign_签到.getType();
	}
	public SignDayRequest(int day) 
	{
		this.msgType= MsgTypeEnum.Sign_签到.getType();
		this.day = day;
	}

	@Override
	public String toString()
	{
		return "SignDayRequest [day=" + day + ", msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
	
	
}
