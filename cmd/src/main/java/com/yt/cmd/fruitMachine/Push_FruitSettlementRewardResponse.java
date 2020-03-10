package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_FruitSettlementRewardResponse extends Response {
	
	public static final int CurrentPlayerNotOnline = 0;// 当前玩家不在线
	
	public int fruitType; // 当中奖类型不是单点和开火车时  与 fruitNoramlOrSpecialType 相同
	public int fruitRewardType;// 水果中奖类型  包含普通类型和特殊类型
	public int isSpecialRewardType;//1  特殊奖励  0 普通奖励
	public int fruitNum;//用于开火车 爆出个数
	public int playerwining;
	public int zhuangJiaWinging;
	public int roundIndex;
	//public ArrayList<Integer> list_playerAndZhuangJiaWiningValue;//当前玩家的庄家的结算结果 第一个元素 代表 玩家的结算结果   第二个元素代表庄家的结算结果
	
	public Push_FruitSettlementRewardResponse() {
		this.msgType = MsgTypeEnum.Push_FruitMechine_结算.getType();
	}

	
	public Push_FruitSettlementRewardResponse(int code,int fruitType, int fruitNoramlOrSpecialType, int specialRewardType,int fruitNum,int playerWining,int zhuangJiaWining) {
		this.msgType = MsgTypeEnum.Push_FruitMechine_结算.getType();
		this.code=code;
		this.fruitType = fruitType;
		this.fruitRewardType = fruitNoramlOrSpecialType;
		this.isSpecialRewardType = specialRewardType;
		this.fruitNum=fruitNum;
		this.playerwining=playerWining;
		this.zhuangJiaWinging=zhuangJiaWining;
	}

	@Override
	public String toString()
	{
		return "Push_FruitSettlementRewardResponse [fruitType=" + fruitType + ", fruitRewardType=" + fruitRewardType + ", isSpecialRewardType=" + isSpecialRewardType + ", fruitNum=" + fruitNum + ", playerwining=" + playerwining + ", zhuangJiaWinging=" + zhuangJiaWinging + "]";
	}

}
