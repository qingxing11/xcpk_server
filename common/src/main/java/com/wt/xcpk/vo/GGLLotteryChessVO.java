package com.wt.xcpk.vo;

public class GGLLotteryChessVO
{
	public static final int CHESS_兵 = 0;
	public static final int CHESS_红炮 = 1;
	public static final int CHESS_红车 = 2;
	public static final int CHESS_红马 = 3;
	public static final int CHESS_红相 = 4;
	public static final int CHESS_红士 = 5;
	public static final int CHESS_帅 = 6;
	public static final int CHESS_卒 = 7;
	public static final int CHESS_黑炮 = 8;
	public static final int CHESS_黑车 = 9;
	public static final int CHESS_黑马 = 10;
	public static final int CHESS_黑相 = 11;
	public static final int CHESS_黑士 = 12;
	public static final int CHESS_将 = 13;
	
	
	/**
	 * 0-13,红色在前
	 * 0:兵 1:红炮  2:红车 3:红马 4:红相 5:红士 6:帅
	 * 7:卒 8:黑炮 9:黑车  10:黑马 11:黑相 12:黑士 13:将
	 */
	public int chessId;
	public int money;
	public boolean isHitReward;
	
	@Override
	public String toString()
	{
		return "GGLLotteryChessVO [chessId=" + chessId + ", money=" + money + "]";
	}

	public void hitReward()
	{
		isHitReward = true;
	}
}
