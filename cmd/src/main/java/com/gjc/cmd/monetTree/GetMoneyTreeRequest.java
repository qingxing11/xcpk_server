package com.gjc.cmd.monetTree;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class GetMoneyTreeRequest extends Request
{
	
		public GetMoneyTreeRequest() 
		{
			this.msgType= MsgTypeEnum.MoneyTree_领取.getType();
		}

		@Override
		public String toString() 
		{
			return "GetMoneyTreeRequest [msgType=" + msgType + ", callBackId=" + callBackId + "]";
		}
		
	
}
