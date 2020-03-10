package com.wt.xcpk.vo;

import java.util.ArrayList;

import com.wt.xcpk.vo.poker.PokerVO;

public class HandPokerVO
{
	public int pos;
	public ArrayList<PokerVO> list_handPoker;
	
	public HandPokerVO() {}
	
	public HandPokerVO(int pos,ArrayList<PokerVO> list_handPoker)
	{
		this.pos = pos;
		this.list_handPoker = list_handPoker;
	}
}
