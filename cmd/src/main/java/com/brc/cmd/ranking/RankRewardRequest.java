package com.brc.cmd.ranking;

import com.wt.cmd.Request;

/**
 * 排行榜奖励领取
 */
public class RankRewardRequest extends Request
{
	/**
	 * 0 赢金榜 1 充值榜
	 */
	public int type;
}
