package com.gjc.cmd.monetTree;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class GetMoneyTreeResponse extends Response
{
	public static final int No_Money=0;
	
	public long money;

	public GetMoneyTreeResponse() 
	{
		this.msgType= MsgTypeEnum.MoneyTree_领取.getType();
	}
	public GetMoneyTreeResponse(int code) 
	{
		this.msgType= MsgTypeEnum.MoneyTree_领取.getType();
		this.code=code;
	}
	public GetMoneyTreeResponse(int code,long money) 
	{
		this.msgType= MsgTypeEnum.MoneyTree_领取.getType();
		this.code=code;
		this.money = money;
	}

	@Override
	public String toString() 
	{
		return "GetMoneyTreeResponse [money=" + money + ", msgType=" + msgType + ", data=" + Arrays.toString(data)+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
}
