package com.wt.cmd.pay;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class BuyItemRequest  extends Request
{
	public int payIndex;
	public BuyItemRequest()
	{
		msgType = MsgTypeEnum.PAY_BUYITEM.getType();
	}
	
}