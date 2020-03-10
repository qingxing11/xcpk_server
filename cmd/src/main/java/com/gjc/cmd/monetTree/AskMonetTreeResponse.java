package com.gjc.cmd.monetTree;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskMonetTreeResponse extends Response
{
	public long time;// 剩余时间（毫秒）
	public long moneyTree;// 可以领取金币数

	public AskMonetTreeResponse()
	{
		this.msgType = MsgTypeEnum.MoneyTree_同步.getType();
	}

	public AskMonetTreeResponse(long time, long moneyTree)
	{
		this.msgType = MsgTypeEnum.MoneyTree_同步.getType();
		this.code = PushMoneyTreeResponse.SUCCESS;
		this.time = time;
		this.moneyTree = moneyTree;
	}

	@Override
	public String toString()
	{
		return "AskMonetTreeResponse [time=" + time + ", moneyTree=" + moneyTree + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
}
