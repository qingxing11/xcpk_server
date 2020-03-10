package com.yt.xcpk.fruitMachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.wt.naval.vo.user.Player;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;

public interface FruitMachineService {
		
	/**
	 * 水果机请求
	 * @param fruitTypes
	 * @param fruitTypeValue
	 */
	ArrayList<Integer> fruitMachineRequest();
	
	
	/**
	 * 获取结算结果   
	 * @param fruitType   当中奖类型是单点  此参数代表 配表 NormalReward 开火车时     代表区域连续的水果 所在区段  OnTrain1  OnTrain2  OnTrain3 
	 * @param fruitRewardType 中奖类型           NormalReward和 SpecialReward 类型
	 * @param specialRewardType  是否是特殊奖励  1为特殊 0为普通
	 * @param typeAndValue 玩家投注的类型的类型所投的数量
	 * @return
	 */
	ArrayList<Integer> fruitPlayerWinOrFail(int fruitType, int fruitRewardType, int specialRewardType,HashMap<Integer, Integer> typeAndValue,int fruitNum);

	/**
	 * 进入房间
	 * @param player
	 */
	void enterRoom(Player player);
	
	/**
	 * 离开房间
	 * @param player
	 */
	void leaveRoom(Player player);
	
	/**
	 * 请求上庄
	 * @param player
	 */
	boolean requestBanker(Player player);

	/**
	 * 下注
	 * @param player
	 * @param fruitMachineType
	 * @param fruitMachineValue
	 * @return
	 */
	boolean payXiaZhuValue(Player player, int fruitMachineType, int fruitMachineValue);


	Room getRoom();
	
	
	int getRoomState();
	
	long getStateTime();
	
	/***
	 * 庄家列表发生改变时   重新设置的庄家 推送给所有玩家 同时从庄家列表中移除
	 * @param player
	 * @param banker
	 */
	void push_changeBankerListInfo(Player player, ArrayList<PlayerSimpleData>  bankers,PlayerSimpleData nextBanker);


	void setPlayerContinueXiaZhu(Player player, boolean isContinueXiaZhu);

	/**
	 * 玩家断线重连水果机数据
	 * @param player
	 * @return
	 */
	boolean reconnect(Player player);


	Collection<Integer> getCurrentAllXiaZhuKey();


	Collection<Integer> getCurrentAllXiaZhuValue();
	
	
	ArrayList<String> getCurrentHistory();
	
	void refreshConfig();


	int getRoundIndex();
}
