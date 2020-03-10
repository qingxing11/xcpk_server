package com.wt.xcpk;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import com.wt.xcpk.vo.poker.PokerVO;

public class KillRoomDirectionPlayer
{
	/**
	 * 该方向手牌
	 */
	private ArrayList<PokerVO> list_poker;
	
	/**东南西北*/
	private int pos;
	
	private int pokerType;
	
	private boolean isWin;
	/**
	 * 该方向筹码,每个玩家的筹码
	 * 筹码上限无限制
	 */
	private transient  ConcurrentHashMap<Integer,Integer> map_gameChips;
	private transient long directionAllChips;
	private transient int jackpotType;
	
	private transient long jackpotNum;
	private transient int baseRate;
	
	public KillRoomDirectionPlayer()
	{
		list_poker = new ArrayList<PokerVO>();
		
		map_gameChips = new ConcurrentHashMap<Integer, Integer>();
	}
	
	public boolean isExistsChip()
	{
		return map_gameChips != null && map_gameChips.size() > 0;
	}
	
	public int getPokerType()
	{
		return pokerType;
	}

	public void clearDirection()
	{
		directionAllChips = 0;
		list_poker.clear();
		map_gameChips.clear();
		jackpotNum = 0;
		jackpotType = 0;
		baseRate = 0;
	}
	
	public boolean isWin()
	{
		return isWin;
	}

	public void setWin(boolean isWin)
	{
		this.isWin = isWin;
	}
	
	/**
	 * 获取方位总筹码数
	 * @return
	 */
	public long getAllChips()
	{
		return directionAllChips;
	}
	
	/**东南西北*/
	public int getPos()
	{
		return pos;
	}

	public void setPos(int pos)
	{
		this.pos = pos;
	}
	
	/**
	 * 设置该方位手牌
	 * @param list_pokers
	 * @param groupType 
	 */
	public void setPokers(ArrayList<PokerVO> list_pokers, int groupType)
	{
		this.list_poker.addAll(list_pokers);
		this.pokerType = groupType;
	}
	
	/**
	 * 该方向增加筹码
	 * @param userId
	 * @param num
	 */
	public void addChip(int userId,int num)
	{
		int hasNum = getPlayerChipNum(userId) + num;
		map_gameChips.put(userId, hasNum);
		directionAllChips += num;
	}
	
	public ConcurrentHashMap<Integer, Integer> getAllPlayerAndChip()
	{
		return map_gameChips;
	}
	
	public int getPlayerChipNum(int userId)
	{
		Integer chipNum = map_gameChips.get(userId);
		return chipNum == null ? 0 : chipNum;
	}

	public ArrayList<PokerVO> getPokers()
	{
		return list_poker;
	}

	public boolean isPlayerEmpty()
	{
		return map_gameChips == null || map_gameChips.size() == 0;
	}

	public void setJockPotType(int jockpotType)
	{
		this.jackpotType = jockpotType;
	}
	
	public int getJockPotType()
	{
		return jackpotType;
	}

	/**
	 * 记录奖池金币
	 * @param num
	 */
	public void addJackpotScore(long num)
	{
		this.jackpotNum = num;
	}

	@Override
	public String toString()
	{
		return "KillRoomDirectionPlayer [list_poker=" + list_poker + ", pos=" + pos + ", pokerType=" + pokerType + ", isWin=" + isWin + "]";
	}

	/**
	 * 方位奖池总额
	 * @return
	 */
	public long getJockPotScore()
	{
		return jackpotNum;
	}

	public void setBaseRate(int baseRate)
	{
		this.baseRate = baseRate;
	}
	
	public int getBaseRate()
	{
		return baseRate;
	}
}
