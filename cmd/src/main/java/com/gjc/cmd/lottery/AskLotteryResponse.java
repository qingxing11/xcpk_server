package com.gjc.cmd.lottery;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskLotteryResponse extends Response
{
	public static final int Error_钱不够=0;
	public static final int Error_不能押注=1;
	
	public long titleMoney;
	public long costMoney;
	public int num;
	public int type;
	public AskLotteryResponse() 
	{
		this.msgType= MsgTypeEnum.Lottery_下注.getType();
	}
	public AskLotteryResponse(int code) 
	{
		this.code=code;
		this.msgType= MsgTypeEnum.Lottery_下注.getType();
	}
	public AskLotteryResponse(int code,long titleMoney, long costMoney,int num) 
	{
		this.msgType= MsgTypeEnum.Lottery_下注.getType();
		this.code=code;
		this.titleMoney = titleMoney;
		this.costMoney=costMoney;
		this.num=num;
	}
	public AskLotteryResponse(int code,int type,int num) 
	{
		this.code=code;
		this.msgType=type;
		this.num=num;
		this.msgType= MsgTypeEnum.Lottery_下注.getType();
	}
	
	@Override
	public String toString() {
		return "AskLotteryResponse [titleMoney=" + titleMoney + ", costMoney=" + costMoney + ", num=" + num + ", type="
				+ type + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId
				+ ", code=" + code + "]";
	}

}
