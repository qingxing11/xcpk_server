package com.brc.cmd.ranking;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 排行榜奖励
 */
public class RankRewardResponse extends Response
{
	public static final int ERROR_未上榜 = 0;
	public static final int ERROR_已领取 =1;
	public static final int ERROR_统计中 =2;
	/**0:赢金榜 1：充值榜*/
	public int type;
	public long coins;
	public RankRewardResponse(int code,int type)
	{
		this.type=type;
		msgType=MsgTypeEnum.RANKINGREWARD.getType();
		this.code=code;
	}
	public RankRewardResponse(int code,long coins,int type)
	{
		this.type=type;
		msgType=MsgTypeEnum.RANKINGREWARD.getType();
		this.code=code;
		this.coins=coins;
	}
}
