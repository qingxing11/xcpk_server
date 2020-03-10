package com.brc.cmd.ranking;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * 排行榜
 */
public class RankingRequest extends Request
{
	public RankingRequest()
	{
		msgType = MsgTypeEnum.RANKING.getType();
	}
}
