package com.gjc.cmd.monetTree;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskMonetTreeRequest extends Request
{
	public AskMonetTreeRequest() 
	{
		this.msgType= MsgTypeEnum.MoneyTree_同步.getType();
	}

	@Override
	public String toString() 
	{
		return "AskMonetTreeRequest [msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
}
