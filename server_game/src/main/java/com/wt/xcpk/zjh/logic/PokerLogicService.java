package com.wt.xcpk.zjh.logic;

import java.util.ArrayList;

import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.vo.poker.PokerVO;

public interface PokerLogicService
{
	/**
	 * 根据给定的牌得到对应牌型
	 * @param list_poker
	 * @return
	 */
	PokerGroup getPokerType(ArrayList<PokerVO> list_poker);
	
	 /**
	  * 扑克牌比较，比较两组牌的大小关系
	  * @param lastCard 被比较的已出牌组
	  * @param playPoker 需要比较的牌组
	  * @return -1：小于，0：等于,1：大于
	  */
	int pokerComparer(PokerGroup lastCard, PokerGroup playPoker);
	
	 /**
	  * 扑克牌比较，比较两组牌的大小关系
	  * @param lastCard 被比较的已出牌组
	  * @param playPoker 需要比较的牌组
	  * @return -1：小于，0：等于,1：大于 ,-2:错误
	  */
	int pokerComparer(ArrayList<PokerVO> lastCard, ArrayList<PokerVO> playPoker);
	
//	/**
//	 * 获得一张随机牌
//	 * @return PokerVO
//	 */
//	PokerVO getRandomPoker();
	
	/**
	 * 获得一副处大小王外的初始牌,对返回的列表进行操作将破坏初始牌
	 * @return
	 */
	ArrayList<PokerVO> getInitPokersWithOutJoker();

	/**根据牌型随机牌 0：豹子 1：顺金 2：金花 3：顺子 4：对子 5：单牌*/
	ArrayList<PokerVO> getPokerType(int type);
	
	int getBaseScore(int groupType);
}
