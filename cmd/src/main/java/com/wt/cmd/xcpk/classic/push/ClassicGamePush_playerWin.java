package com.wt.cmd.xcpk.classic.push;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.GameRoundDataVO;

public class ClassicGamePush_playerWin extends Response
{
//	public int pos;
//	public int winNum;
//	public ArrayList<HandPokerVO> handPokerVO;
	
	public ArrayList<GameRoundDataVO> list_roundData;
	public ClassicGamePush_playerWin()
	{
		msgType = MsgTypeEnum.classic_玩家胜利.getType();
	}
	
	public ClassicGamePush_playerWin(ArrayList<GameRoundDataVO> list_roundData)
	{
		msgType = MsgTypeEnum.classic_玩家胜利.getType();
//		this.pos = pos;
//		this.winNum = winNum;
		this.list_roundData = list_roundData;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerWin [list_roundData=" + list_roundData + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
