package com.wt.cmd.xcpk.classic;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

/**
 * 进入经典场：
 * 房间当前状态
 * 当前座位上玩家<玩家状态和位置>
 * 当前庄家
 * 自己位置
 * 回合数
 * 当前筹码
 * 当前行动玩家
 * 
 * @author akwang
 *
 */
public class EnterBeginnerResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_金币过多 = 1;
	public static final int ERROR_进入房间错误 = 2;
	
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
	
	public int type;
	public EnterBeginnerResponse()
	{
		msgType = MsgTypeEnum.classic_进入新手场.getType();
	}
	
	public EnterBeginnerResponse(int code)
	{
		msgType = MsgTypeEnum.classic_进入新手场.getType();
		this.code = code;
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
	public void setData(int state,ArrayList<PlayerSimpleData> list_tablePlayer,int bankerPos,int pos,int roundNum,ArrayList<Integer> list_allBet,int actionPos,int actionTime,int type)
	{
		this.state = state;
		this.list_tablePlayer = list_tablePlayer;
		this.bankerPos = bankerPos;
		this.pos = pos;
		this.roundNum = roundNum;
		this.list_allBet = list_allBet;
		this.actionPos = actionPos;
		this.actionTime = actionTime;
		this.type = type;
	}

	@Override
	public String toString()
	{
		return "EnterBeginnerResponse [state=" + state + ", list_tablePlayer=" + list_tablePlayer + ", bankerPos=" + bankerPos + ", pos=" + pos + ", roundNum=" + roundNum + ", list_allBet=" + list_allBet + ", actionPos=" + actionPos + ", actionTime=" + actionTime + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
