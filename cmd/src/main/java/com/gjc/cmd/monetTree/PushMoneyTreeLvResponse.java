package com.gjc.cmd.monetTree;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushMoneyTreeLvResponse extends Response
{
	public int treeLv;

	public PushMoneyTreeLvResponse() 
	{
		this.msgType= MsgTypeEnum.MoneyTree_升级.getType();
	}
	
	public PushMoneyTreeLvResponse(int lv) 
	{
		this.msgType= MsgTypeEnum.MoneyTree_升级.getType();
		this.code=PushMoneyTreeResponse.SUCCESS;
		this.treeLv = lv;
	}

}
