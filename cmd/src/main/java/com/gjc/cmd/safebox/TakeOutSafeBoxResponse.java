package com.gjc.cmd.safebox;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class TakeOutSafeBoxResponse extends Response
{
	public long money;

	public TakeOutSafeBoxResponse() 
	{
		this.msgType= MsgTypeEnum.SafeBox_取出银行.getType();
	}
	
	public TakeOutSafeBoxResponse(int code) 
	{
		this.msgType= MsgTypeEnum.SafeBox_取出银行.getType();
		this.code=code;
	}
	
	public TakeOutSafeBoxResponse(int code,long money) 
	{
		this.msgType= MsgTypeEnum.SafeBox_取出银行.getType();
		this.code=code;
		this.money = money;
	}



	@Override
	public String toString() 
	{
		return "TakeOutSafeBoxResponse [money=" + money + ", msgType=" + msgType + ", data=" + Arrays.toString(data)+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}
}
