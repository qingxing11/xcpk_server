package com.yt.cmd.fruitMachine.push;

import java.util.ArrayList;
import java.util.Collection;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * 
 * @author WangTuo
 *
 */
public class FruitPush_reconnect extends Response
{
	/**
	 * 当前状态剩余时间
	 */
	public PlayerSimpleData playerBankerData;// 庄家信息
	public int roomState;
	public long stateTime;
	public long jiangPoolCoins;// 奖池金额	
	public Collection<Integer> list_xiaZhuKey;
	public Collection<Integer> list_xiaZhuValue;
	public ArrayList<String> list_history;
	 public int roundIndex;
	public FruitPush_reconnect()
	{
		msgType = MsgTypeEnum.Fruit_断线重连.getType();
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
	public void setData(int roomState, long stateTime,PlayerSimpleData playerSimpleData,long jiangPoolCoins,Collection<Integer> list_xiaZhuKey,Collection<Integer> list_xiaZhuValue, ArrayList<String> list_history,int roundIndex)
	{
		this.roomState = roomState;
		this.stateTime = stateTime;
		this.playerBankerData=playerSimpleData;
		this.jiangPoolCoins=jiangPoolCoins;
		this.list_xiaZhuKey=list_xiaZhuKey;
		this.list_xiaZhuValue=list_xiaZhuValue;
		this.list_history=list_history;
		this.roundIndex = roundIndex;
	}

	@Override
	public String toString()
	{
		return "FruitPush_reconnect [playerBankerData=" + playerBankerData + ", roomState=" + roomState + ", stateTime=" + stateTime + ", jiangPoolCoins=" + jiangPoolCoins + ", list_xiaZhuKey=" + list_xiaZhuKey + ", list_xiaZhuValue=" + list_xiaZhuValue + ", list_history=" + list_history + "]";
	}
}
