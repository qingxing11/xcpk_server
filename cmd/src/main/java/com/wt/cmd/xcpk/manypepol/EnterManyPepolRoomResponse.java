package com.wt.cmd.xcpk.manypepol;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

/**
 * 进入经典场：
 * 房间当前状态
 * 当前座位上玩家
 * 当前庄家
 * 
 * 
 * @author akwang
 *
 */
public class EnterManyPepolRoomResponse extends Response
{
	/**0:待机  1:等待开始 2:发牌  3:等待玩家思考*/
	public int state;
	public ArrayList<PlayerSimpleData> list_tablePlayer;
	public int bankerPos;
	
	public int waitStartTime;//9秒
	
	public int waitPlayerThink;
	
	public int waitPayBetTime;
	
	public int allBet;
	
	public long jackpotNum;
	
	public int roundNum;
	
	public ArrayList<Integer> list_allBet = new ArrayList<Integer>();
	public EnterManyPepolRoomResponse()
	{
		msgType = MsgTypeEnum.manypepol_玩家进入.getType();
	}
	
	public EnterManyPepolRoomResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_玩家进入.getType();
		this.code = code;
	}
	
	public void setData(int state,ArrayList<PlayerSimpleData> list_tablePlayer,int bankerPos,int allBet,long jackpotNum,int roundNum,ArrayList<Integer> list_allBet)
	{
		this.state = state;
		this.list_tablePlayer = list_tablePlayer;
		this.bankerPos = bankerPos;
		this.allBet = allBet;
		this.jackpotNum = jackpotNum;
		this.roundNum = roundNum;
		if(list_allBet != null)
		{
			this.list_allBet.addAll(list_allBet);
		}
	}
	
	/**
	 * @param waitStartTime
	 * @param waitPlayerThink
	 * @param waitPayBetTime
	 */
	public void initConfig(int waitStartTime,int waitPlayerThink,int waitPayBetTime)
	{
		this.waitStartTime = waitStartTime;
		this.waitPlayerThink = waitPlayerThink;
		this.waitPayBetTime = waitPayBetTime;
	}

	@Override
	public String toString()
	{
		return "EnterManyPepolRoomResponse [state=" + state + ", list_tablePlayer=" + list_tablePlayer + ", bankerPos=" + bankerPos + ", waitStartTime=" + waitStartTime + ", waitPlayerThink=" + waitPlayerThink + ", waitPayBetTime=" + waitPayBetTime + ", allBet=" + allBet + ", jackpotNum=" + jackpotNum + ", roundNum=" + roundNum + ", list_allBet=" + list_allBet + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
