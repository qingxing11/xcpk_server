package com.wt.cmd.pay;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class BuyGoldRequest  extends Request
{
	public int payIndex;
	public BuyGoldRequest()
	{
		msgType = MsgTypeEnum.PAY_BUYGOLD.getType();
	}
	
}