package com.brc.cmd.ranking;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.RankVO;

/**
 * 排行榜
 */
public class RankingResponse extends Response
{
	/**
	 * 充值榜显示前20名-每日充值榜前三次名，分别获取充值所得钻石100%50%30%金币奖励，次日直接到账（奖励按1元=1钻石=1万金币结算）
	 */
	public ArrayList<RankVO> payRank;
	/**
	 * 土豪榜：显示前20名账号金币最多的玩家
	 */
	public ArrayList<RankVO> coinsRank;
	/**
	 * 赢金榜：显示赢金榜最多的前20名。奖励规则参考友乐
	 */
	public ArrayList<RankVO> bigWinRank;

	/** 是否领取充值榜 */
	public boolean isGetPay;

	/** 是否领取赢金榜 */
	public boolean isGetWin;

	public RankingResponse()
	{
		msgType = MsgTypeEnum.RANKING.getType();
	}

	public RankingResponse(int code)
	{
		msgType = MsgTypeEnum.RANKING.getType();
		this.code = code;
	}

	public RankingResponse(int code, ArrayList<RankVO> payRank, ArrayList<RankVO> coinsRank, ArrayList<RankVO> bigWinRank,boolean isGetPay,boolean isGetWin)
	{
		msgType = MsgTypeEnum.RANKING.getType();
		this.code = code;
		this.payRank = payRank;
		this.coinsRank = coinsRank;
		this.bigWinRank = bigWinRank;
		this.isGetPay=isGetPay;
		this.isGetWin=isGetWin;
	}

	@Override
	public String toString()
	{
		return "RankingResponse [payRank=" + payRank + ", coinsRank=" + coinsRank + ", bigWinRank=" + bigWinRank + ", isGetPay=" + isGetPay + ", isGetWin=" + isGetWin + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
