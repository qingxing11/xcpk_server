package com.gjc.cmd.lottery;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.poker.PokerVO;

public class PushLotteryResultResponse extends Response
{
	public static final int Error_没有赢家 = 0;
	public static final int Error_没有下注 = 1;

	public boolean win;
	public long winnerMoney;
	public int type;
	public PokerVO card1;
	public PokerVO card2;
	public PokerVO card3;

	public PushLotteryResultResponse(int code, boolean win, long winnerMoney, int type, PokerVO card1, PokerVO card2, PokerVO card3)
	{
		this.msgType = MsgTypeEnum.Lottery_彩票结果.getType();
		this.code = code;
		this.win = win;
		this.winnerMoney = winnerMoney;
		this.type = type;
		this.card1 = card1;
		this.card2 = card2;
		this.card3 = card3;
	}

	public PushLotteryResultResponse(int code, boolean win, int type, PokerVO card, PokerVO card2, PokerVO card3)
	{
		this.msgType = MsgTypeEnum.Lottery_彩票结果.getType();
		this.win = win;
		this.code = code;
		this.type = type;
		this.card1 = card;
		this.card2 = card2;
		this.card3 = card3;
	}

	public PushLotteryResultResponse()
	{
		this.msgType = MsgTypeEnum.Lottery_彩票结果.getType();
	}

	@Override
	public String toString()
	{
		return "PushLotteryResultResponse [win=" + win + ", winnerMoney=" + winnerMoney + ", type=" + type + ", card=" + card1 + ", card2=" + card2 + ", card3=" + card3 + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}

}
