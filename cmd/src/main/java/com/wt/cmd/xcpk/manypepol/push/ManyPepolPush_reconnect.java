package com.wt.cmd.xcpk.manypepol.push;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.vo.poker.PokerVO;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ManyPepolPush_reconnect extends Response
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
	 * 玩家当前手牌，如果有并且看过牌
	 */
	public ArrayList<PokerVO> list_pokers = new ArrayList<PokerVO>();
	public int pokerType;
	
	public long jackpotNum;
	
	public int nowBet;
	/**0:不显示全压  1:显示全压  2:强制全压 3:第一回合*/
	public int allinState;
	public ManyPepolPush_reconnect()
	{
		msgType = MsgTypeEnum.manypepol_断线重连.getType();
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
	public void setData(int state,ArrayList<PlayerSimpleData> list_tablePlayer,int bankerPos,int pos,int roundNum,ArrayList<Integer> list_allBet,int actionPos,int actionTime,long jackpotNum)
	{
		this.state = state;
		this.list_tablePlayer = list_tablePlayer;
		this.bankerPos = bankerPos;
		this.pos = pos;
		this.roundNum = roundNum;
		this.list_allBet = list_allBet;
		this.actionPos = actionPos;
		this.actionTime = actionTime;
		this.jackpotNum = jackpotNum;
	}
	
	public void setMineData(ArrayList<PokerVO> list_pokers,int pokerType,int nowBet,int lastPlayAllInState)
	{
		this.list_pokers.clear();
		if(list_pokers != null)
		{
			this.list_pokers.addAll(list_pokers);
		}
		this.pokerType = pokerType;
		this.nowBet = nowBet;
		this.allinState = lastPlayAllInState;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_reconnect [state=" + state + ", list_tablePlayer=" + list_tablePlayer + ", bankerPos=" + bankerPos + ", pos=" + pos + ", roundNum=" + roundNum + ", list_allBet=" + list_allBet + ", actionPos=" + actionPos + ", actionTime=" + actionTime + ", list_pokers=" + list_pokers + ", pokerType=" + pokerType + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
