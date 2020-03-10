package com.gjc.cmd.lottery;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskAutoLotteryResponse extends Response
{
	public static final int Error_钱不够=0;
	public static final int Error_不能押注=1;
	
	public long titleMoney;
	public int type;	
	public long costMoney;
	public int num;
	public AskAutoLotteryResponse() 
	{
		this.msgType= MsgTypeEnum.Lottery_自动下注.getType();
	}
	public AskAutoLotteryResponse(int code) 
	{
		this.code=code;
		this.msgType= MsgTypeEnum.Lottery_自动下注.getType();
	}
	public AskAutoLotteryResponse(int code,long titleMoney,int type, long costMoney,int num) 
	{
		this.msgType= MsgTypeEnum.Lottery_自动下注.getType();
		this.code=code;
		this.titleMoney = titleMoney;
		this.type=type;
		this.costMoney=costMoney;
		this.num=num;
	}
	public AskAutoLotteryResponse(int code,int type,int num) 
	{
		this.msgType= MsgTypeEnum.Lottery_自动下注.getType();
		this.code=code;
		this.type=type;
		this.num=num;
	}
	@Override
	public String toString()
	{
		return "AskAutoLotteryResponse [titleMoney=" + titleMoney + ", type=" + type + ", costMoney=" + costMoney + ", num=" + num + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
