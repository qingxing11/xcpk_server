package com.gjc.cmd.lottery;

import java.util.ArrayList;
import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskLotteryTimeResponse extends Response
{
	public int  residueBuyTime;//剩余押注时间
	public int residueWinTime;//剩余开奖时间
	public ArrayList<Integer> recordWinType;//开奖记录
	public int curType = -1;
	public int curNum = 0;
	public AskLotteryTimeResponse() 
	{
		this.msgType= MsgTypeEnum.Lottery_同步时间.getType();	
	}
	public AskLotteryTimeResponse(int code,int residueBuyTime, int residueWinTime,ArrayList<Integer> recordWinType) 
	{
		this.msgType= MsgTypeEnum.Lottery_同步时间.getType();
		this.code=code;
		this.residueBuyTime = residueBuyTime;
		this.residueWinTime = residueWinTime;
		this.recordWinType=recordWinType;
	}

	@Override
	public String toString() {
		return "AskLotteryTimeResponse [residueBuyTime=" + residueBuyTime + ", residueWinTime=" + residueWinTime
				+ ", recordWinType=" + recordWinType + ", msgType=" + msgType + ", data=" + Arrays.toString(data)
				+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	

}
