package com.gjc.naval.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import com.gjc.naval.vo.lottery.LotteryDataVO;
import com.gjc.naval.vo.lottery.TxtVO;
import com.gjc.naval.vo.lottery.WinVO;
import com.wt.xcpk.vo.poker.PokerVO;

public interface LotteryService 
{
	 	void askAutoLottery(int userId,int num);
	 	
		void askLottery(int userId,int type,int typeNum);
		
		void askLotteryTime(int userId);
		
		KeySetView<Integer, LotteryDataVO> getXZPlayer();
		
		HashMap<Integer,WinVO> getWinnerAndMoney(int type,ArrayList<PokerVO> list);
		
		void resWinTime();
		
		void setBuy();
		
		boolean getWinTime();
		
		boolean getBuy();
		
		int getBuyTime();
		
		int getBuyCost();
		
		void resTitleMoney();
		
		void setCurBuyTime();
		
		void pushTitleMoney();
		
		void removeUserList(Integer userId);
		
		HashSet<Integer> getUserList();
		
		HashSet<Integer> getResidueUserList();
		
		LotteryDataVO getBuyList(int userId);
		
		 void pushTxtVo(ArrayList<TxtVO> list);
}
