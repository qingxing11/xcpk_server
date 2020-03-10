package com.wt.cmd.xcpk.manypepol.push;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.GameRoundDataVO;

public class ManyPepolPush_playerWin extends Response
{
//	public int pos;
//	public int winNum;
	public ArrayList<GameRoundDataVO> list_roundData;
	
	public long jackpotNum;
	public ManyPepolPush_playerWin()
	{
		msgType = MsgTypeEnum.manypepol_玩家胜利.getType();
	}
	
	public ManyPepolPush_playerWin(ArrayList<GameRoundDataVO> list_roundData,long jackpotNum)
	{
		msgType = MsgTypeEnum.manypepol_玩家胜利.getType();
		this.list_roundData = list_roundData;
		this.jackpotNum = jackpotNum;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_playerWin [list_roundData=" + list_roundData + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
