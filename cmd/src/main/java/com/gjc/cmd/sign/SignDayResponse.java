package com.gjc.cmd.sign;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class SignDayResponse extends Response
{
	public boolean sign;//签到成功
	public int day;//第几天
	
	public SignDayResponse(int code,boolean sign,int day) 
	{
		this.msgType= MsgTypeEnum.Sign_签到.getType();
		this.code=code;
		this.sign = sign;
		this.day=day;
	}
	public SignDayResponse(int code) 
	{
		this.msgType= MsgTypeEnum.Sign_签到.getType();
		this.code=code;
	}
	
	public SignDayResponse()
	{
		this.msgType= MsgTypeEnum.Sign_签到.getType();
	}
	
	@Override
	public String toString() {
		return "SignDayResponse [sign=" + sign + ", day=" + day + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}

}
