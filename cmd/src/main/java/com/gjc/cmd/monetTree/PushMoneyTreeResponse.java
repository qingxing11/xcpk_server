package com.gjc.cmd.monetTree;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushMoneyTreeResponse extends Response
{
	public long time;//剩余时间（毫秒）
	public long moneyTree;//可以领取金币数

	public PushMoneyTreeResponse() 
	{
		this.msgType= MsgTypeEnum.MoneyTree_推送产出.getType();
	}
	
	public PushMoneyTreeResponse(long time, long moneyTree) 
	{
		this.msgType= MsgTypeEnum.MoneyTree_推送产出.getType();
		this.code=PushMoneyTreeResponse.SUCCESS;
		this.time = time;
		this.moneyTree = moneyTree;
	}
	
	@Override
	public String toString() {
		return "PushMoneyTreeResponse [time=" + time + ", moneyTree=" + moneyTree + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
}
