package com.wt.xcpk.zjh.killroom;

import java.util.ArrayList;

import com.wt.util.sort.ISort;
import com.wt.xcpk.vo.poker.PokerVO;

public class JackpotSort implements ISort
{
	/***
	 * 庄家pos 5
	 */
	public int pos;
	public int type;
	
	public ArrayList<PokerVO> list_pokers = new ArrayList<PokerVO>();
	
	public int value;
	public int jackpotType;
	public JackpotSort(int pos, int type,ArrayList<PokerVO> list_pokers,int value,int jackpotType)
	{
		this.pos = pos;
		this.type = type;
		this.list_pokers.clear();
		this.list_pokers.addAll(list_pokers);
		this.value = value;
		this.jackpotType = jackpotType;
	}
	
	public JackpotSort(int pos, int bankerJockpotType)
	{
		this.pos = pos;
		this.type = bankerJockpotType;
	}

	@Override
	public int getSortIndex()
	{
		return value;
	}

	@Override
	public String toString()
	{
		return "JackpotSort [pos=" + pos + ", type=" + type + ", list_pokers=" + list_pokers + ", getSortIndex()=" + getSortIndex() + "]";
	}
}
