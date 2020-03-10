package com.wt.xcpk.vo.poker;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 一组已排序的等待分析牌组
 * @author Wangtuo
 *
 */
public class PokerTeam
{
	/**
	 * 其他牌数
	 */
	public ArrayList<PokerVO> list_poker;
	
	/**
	 * key:排序过的扑克类型
	 * value:该类型张数
	 */
	public HashMap<Integer, Integer> hashMap_samePoker;
	public PokerTeam(ArrayList<PokerVO> list_poker)
	{
		this.list_poker = list_poker;
	}
	@Override
	public String toString()
	{
		return "PokerTeam [list_poker=" + list_poker + ", hashMap_samePoker=" + hashMap_samePoker.toString() + "]";
	}
	
}
