package com.wt.cmd.user.push;

import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_GameMoneyChange extends Response
{

	/** 金币 */
	public static final int MONEYTYPE_COIN = 0; // 金币

	/** 钻石 */
	public static final int MONEYTYPE_CRYTSTAL = 1;// 钻石
	
	
	public static final int STATE_ADD = 0;
	public static final int STATE_SUB = 1;

	/**
	 * 改变的货币:金币或钻石
	 */
	public int moneyType; // 改变的货币:豆子或金币
	public int subType; // 改变方式:
	public int changeNum; // 改变数量
	public int curNum; // 现有数量

	public Push_GameMoneyChange()
	{
		this.msgType = MsgType.GAME_货币变化;
	}

	public Push_GameMoneyChange(int moneyType, int subType, int changeNum, int curNum)
	{
		this.msgType = MsgType.GAME_货币变化;
		this.moneyType = moneyType;
		this.subType = subType;
		this.changeNum = changeNum;
		this.curNum = curNum;
	}
}
