package com.wt.cmd.xcpk.classic.push;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.vo.poker.PokerVO;

/**
 * @author WangTuo
 *
 */
public class ClassicGamePush_reconnect extends Response
{
	public int state;
	public ArrayList<PlayerSimpleData> list_tablePlayer;
	public int bankerPos;
	public int pos;
	public int roundNum;
	public ArrayList<Integer> list_allBet;
	
	/**
	 * 当前行动玩家
	 */
	public int actionPos;
	
	/**
	 * 当前状态剩余时间
	 */
	public int actionTime;
	
	/**
	 * 玩家当前手牌，如果有
	 */
	public ArrayList<PokerVO> list_pokers;
	public int pokerType;
	public ClassicGamePush_reconnect()
	{
		msgType = MsgTypeEnum.classic_断线重连.getType();
	}
	
	
	/**
	 * @param state
	 * @param list_tablePlayer
	 * @param bankerPos
	 * @param pos
	 * @param roundNum
	 * @param list_allBet
	 * @param actionPos
	 * @param actionTime
	 */
	public void setData(int state,ArrayList<PlayerSimpleData> list_tablePlayer,int bankerPos,int pos,int roundNum,ArrayList<Integer> list_allBet,int actionPos,int actionTime)
	{
		this.state = state;
		this.list_tablePlayer = list_tablePlayer;
		this.bankerPos = bankerPos;
		this.pos = pos;
		this.roundNum = roundNum;
		this.list_allBet = list_allBet;
		this.actionPos = actionPos;
		this.actionTime = actionTime;
	}
	
	public void setMineData(ArrayList<PokerVO> list_pokers,int pokerType)
	{
		this.list_pokers = list_pokers;
		this.pokerType = pokerType;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_reconnect [state=" + state + ", list_tablePlayer=" + list_tablePlayer + ", bankerPos=" + bankerPos + ", pos=" + pos + ", roundNum=" + roundNum + ", list_allBet=" + list_allBet + ", actionPos=" + actionPos + ", actionTime=" + actionTime + ", list_pokers=" + list_pokers + ", pokerType=" + pokerType + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
