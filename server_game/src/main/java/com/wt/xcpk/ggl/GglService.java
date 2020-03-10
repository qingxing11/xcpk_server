package com.wt.xcpk.ggl;

import java.util.ArrayList;

import com.wt.xcpk.vo.GGLLotteryChessVO;

public interface GglService
{
	ArrayList<Integer> getLuckyChess();

	ArrayList<GGLLotteryChessVO> getLotteryChess(int level);
	
	int getCost(int level);

	int calcReward(ArrayList<Integer> list_luckyChess, ArrayList<GGLLotteryChessVO> list_myChess);

	ArrayList<Integer> getCustomizeBuy(int level, int num);
}
