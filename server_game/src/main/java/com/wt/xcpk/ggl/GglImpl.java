package com.wt.xcpk.ggl;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.xcpk.vo.GGLLotteryChessVO;

@Service
public class GglImpl implements GglService
{
	public static void main(String[] args)
	{
	}
	
	private ArrayList<Integer> list_allchess = new ArrayList<Integer>();
	
	@Override
	public ArrayList<Integer> getLuckyChess()
	{
		ArrayList<Integer> list_luckyChess = new ArrayList<Integer>();
		int first = MyUtil.getRandom(14);
		list_luckyChess.add(first);
		int secord = MyUtil.getRandom(14);
		while (secord == first)
		{
			secord = MyUtil.getRandom(14);
		}
		list_luckyChess.add(secord);
		return list_luckyChess;
	}

	@Override
	public ArrayList<GGLLotteryChessVO> getLotteryChess(int level)
	{
		ArrayList<Integer> list_chess = new ArrayList<Integer>(list_allchess);
		ArrayList<GGLLotteryChessVO> list_myChess = new ArrayList<GGLLotteryChessVO>();
		for (int i = 0 ; i < 6 ; i++)
		{
			GGLLotteryChessVO chessVO = new GGLLotteryChessVO();
			int selectChess = MyUtil.getRandom(list_chess.size());
			int chessId = list_chess.remove(selectChess);
			chessVO.chessId = chessId;
			chessVO.money = getGift(level);
			list_myChess.add(chessVO);
		}
		return list_myChess;
	}

	
	@PostConstruct
	private void init()
	{
		for (int i = 0 ; i < 14 ; i++)
		{
			list_allchess.add(i);
		}
	}
	
	private int getGift(int level)
	{
		int rate = getGiftRate();
		switch (rate)
		{
			case 0:
				return getLevel0Gift(level);
				
			case 1:
				return getLevel1Gift(level);
				
			case 2:
				return getLevel2Gift(level);
				
			default:
				break;
		}
		return 0;
	}

	/**
	 * 最少的奖励，对应3个场
	 * @param level
	 * @return
	 */
	private int getLevel2Gift(int level)
	{
		switch (level)
		{
			case 0:
				int num = MyUtil.getRandom(50,1000);
				if(num > 100)
				{
					num = num / 100 * 100;
				}
				return num;//50-1000

			case 1:
				num = MyUtil.getRandom(50,10000);
				if(num > 100)
				{
					num = num / 100 * 100;
				}
				return num;// 
				
			case 2:
				num = MyUtil.getRandom(50,100000);
				if(num > 100)
				{
					num = num / 100 * 100;
				}
				return num;// 
				
			default:
				break;
		}
		return 0;
	}
	
	 

	private int getLevel1Gift(int level)
	{
		switch (level)
		{
			case 0:
				return MyUtil.getRandom(1000,200000)/100*100;//1000-200000
				
			case 1:
				return MyUtil.getRandom(10000, 2000000)/100*100;

			case 2:
				return MyUtil.getRandom(100000, 20000000)/100*100;
				
			default:
				break;
		}
		return 0;
	}

	/**
	 * 最高奖
	 * @param level
	 * @return
	 */
	private int getLevel0Gift(int level)
	{
		switch (level)
		{
			case 0:
				return MyUtil.getRandom(20000,1000001)/100*100;
				
			case 1:
				return MyUtil.getRandom(200000, 10000001)/100*100;

			case 2:
				return MyUtil.getRandom(2000000, 100000001)/100*100;
				
			default:
				break;
		}
		return 0;
	}

	private int getGiftRate()
	{
		int rate = MyUtil.getRandom(1000);
		
		 if(rate < 1)
		 {
			 return 0;
		 }
		 else if(rate >= 1 && rate < 3)
		 {
			 return 1;
		 }
		return 2;
	}

	@Override
	public int getCost(int level)
	{
		switch (level)
		{
			case 0:
				return 1000;

			case 1:
				return 10000;
				
			case 2:
				return 100000;
				
			default:
				return 100000;
		}
	}

	@Override
	public int calcReward(ArrayList<Integer> list_luckyChess, ArrayList<GGLLotteryChessVO> list_myChess)
	{
		int reward = 0;
		boolean isGetAll = false;
		if(list_luckyChess.get(0) == 1 && list_luckyChess.get(1) == 8 ||  list_luckyChess.get(0) == 8 && list_luckyChess.get(1) == 1)
		{
			isGetAll = true;
		}
		
		
		for (Integer cardId : list_luckyChess)
		{
			for (GGLLotteryChessVO item : list_myChess)
			{
				if(cardId == item.chessId || isGetAll)
				{
					reward += item.money;
					item.hitReward();
				}
			}
		}
		return reward;
	}

	@Override
	public ArrayList<Integer> getCustomizeBuy(int level, int num)
	{
		ArrayList<Integer> list_customize = new ArrayList<Integer>();
		for (int i = 0 ; i < num ; i++)
		{
			int gift = 0;
			int rate = MyUtil.getRandom(100);
			if(rate < 30)
			{
				gift = getGift(level);
			}
			list_customize.add(gift);
		}
		return list_customize;
	}
}
