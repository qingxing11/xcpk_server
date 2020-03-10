package com.gjc.cmd.safebox;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class TakeOutSafeBoxRequest extends Request
{
	public int userId;
	public long money;
	
	public TakeOutSafeBoxRequest() 
	{
		this.msgType= MsgTypeEnum.SafeBox_取出银行.getType();
	}
	
	public TakeOutSafeBoxRequest(int userId, long money) 
	{
		this.msgType= MsgTypeEnum.SafeBox_取出银行.getType();
		this.userId = userId;
		this.money = money;
	}



	@Override
	public String toString() 
	{
		return "TakeOutSafeBoxRequest [userId=" + userId + ", money=" + money + ", msgType=" + msgType+ ", callBackId=" + callBackId + "]";
	}
}
