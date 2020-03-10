package com.wt.xcpk.vo;

import java.util.ArrayList;

import com.wt.util.sort.ISort;
import com.wt.xcpk.vo.poker.PokerVO;

public class JackpotRoundSort implements ISort
{
	public int score;
	public int userId;
	public int type;
	public ArrayList<PokerVO> list_handPokers = new ArrayList<PokerVO>();
	public JackpotRoundSort()
	{}
	
	public JackpotRoundSort(int score,int userId,int type,ArrayList<PokerVO> list_handPokers)
	{
		this.score = score;
		this.userId = userId;
		this.type = type;
		this.list_handPokers.clear();
		this.list_handPokers.addAll(list_handPokers);
	}

	@Override
	public int getSortIndex()
	{
		return score;
	}

	@Override
	public String toString()
	{
		return "JackpotRoundSort [score=" + score + ", userId=" + userId + ", type=" + type + ", list_handPokers=" + list_handPokers + "]";
	}
}
