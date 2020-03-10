package com.gjc.cmd.safebox;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskSafeBoxRecordRequest extends Request
{
	public int userId;
	
	public AskSafeBoxRecordRequest() 
	{
		this.msgType= MsgTypeEnum.SafeBox_银行记录.getType();
	}

	public AskSafeBoxRecordRequest(int userId)
	{
		this.msgType= MsgTypeEnum.SafeBox_银行记录.getType();
		this.userId = userId;
	}



	@Override
	public String toString() {
		return "AskSafeBoxRecordRequest [userId=" + userId + ", msgType=" + msgType + ", callBackId=" + callBackId
				+ "]";
	}
	
}
