package com.wt.cmd.xcpk.classic;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.poker.PokerVO;

public class CheckPokerResponse extends Response
{
	public static final int ERROR_不在比赛中 = 1;
	
	public ArrayList<PokerVO> list_poker;
	public int pokerType;
	public CheckPokerResponse()
	{
		msgType = MsgTypeEnum.classic_玩家看牌.getType();
	}
	
	public CheckPokerResponse(int code)
	{
		msgType = MsgTypeEnum.classic_玩家看牌.getType();
		this.code = code;
	}
	
	public CheckPokerResponse(int code,ArrayList<PokerVO> list_poker,int pokerType)
	{
		msgType = MsgTypeEnum.classic_玩家看牌.getType();
		this.code = code;
		this.list_poker = list_poker;
		this.pokerType = pokerType;
	}

	@Override
	public String toString()
	{
		return "CheckPokerResponse [list_poker=" + list_poker + ", pokerType=" + pokerType + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
