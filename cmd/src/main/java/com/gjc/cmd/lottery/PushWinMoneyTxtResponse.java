package com.gjc.cmd.lottery;

import java.util.ArrayList;
import java.util.Arrays;

import com.gjc.naval.vo.lottery.TxtVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushWinMoneyTxtResponse extends Response
{
	public ArrayList<TxtVO> vo;
	
	public PushWinMoneyTxtResponse( ArrayList<TxtVO> vo) 
	{
		this.msgType= MsgTypeEnum.Lottery_中奖提示.getType();
		this.code=PushWinMoneyTxtResponse.SUCCESS;
		this.vo = vo;
	}

	public PushWinMoneyTxtResponse() 
	{
		this.msgType= MsgTypeEnum.Lottery_中奖提示.getType();
	}

	@Override
	public String toString() 
	{
		return "PushWinMoneyTxtResponse [vo=" + vo + ", msgType=" + msgType + ", data=" + Arrays.toString(data)+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}
}
