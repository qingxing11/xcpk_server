package com.yt.cmd.fruitMachine;

import java.util.ArrayList;
import java.util.Collection;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class EnterFruitMachineResponse extends Response
{
	public PlayerSimpleData playerBankerData;// 庄家信息
	public int roomState;
	public long stateTime;
	public long jiangPoolCoins;// 奖池金额
	public Collection<Integer> list_xiaZhuKey;
	public Collection<Integer> list_xiaZhuValue;
	public ArrayList<String> list_history;
	public int roundIndex;
	public EnterFruitMachineResponse()
	{
		msgType = MsgTypeEnum.EnterFruitMechine.getType();
	}

	public EnterFruitMachineResponse(int code)
	{
		msgType = MsgTypeEnum.EnterFruitMechine.getType();
		this.code = code;
	}

	public EnterFruitMachineResponse(int code, PlayerSimpleData playerBankerData, int roomState, long stateTime, long jiangPoolCoins, Collection<Integer> list_xiaZhuKey, Collection<Integer> list_xiaZhuValue,ArrayList<String> list_history,int roundIndex)
	{
		msgType = MsgTypeEnum.EnterFruitMechine.getType();
		this.code = code;
		this.playerBankerData = playerBankerData;
		this.roomState = roomState;
		this.stateTime = stateTime;
		this.jiangPoolCoins = jiangPoolCoins;
		this.list_xiaZhuKey = list_xiaZhuKey;
		this.list_xiaZhuValue = list_xiaZhuValue;
		this.list_history=list_history;
		this.roundIndex = roundIndex;
	}

	@Override
	public String toString()
	{
		return "EnterFruitMachineResponse [playerBankerData=" + playerBankerData + ", roomState=" + roomState + ", stateTime=" + stateTime + ", jiangPoolCoins=" + jiangPoolCoins + ", list_xiaZhuKey=" + list_xiaZhuKey + ", list_xiaZhuValue=" + list_xiaZhuValue + ", list_history=" + list_history + "]";
	}

}