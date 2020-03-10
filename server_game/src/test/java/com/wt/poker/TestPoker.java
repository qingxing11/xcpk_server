package com.wt.poker;

import java.util.ArrayList;

import com.wt.xcpk.vo.poker.CardTypeEnum;
import com.wt.xcpk.vo.poker.FaceTypeEnum;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.vo.poker.PokerVO;
import com.wt.xcpk.vo.poker.PokerValueEnum;
import com.wt.xcpk.zjh.logic.PokerLogicImpl;

import junit.framework.TestCase;

public class TestPoker extends TestCase
{
	PokerLogicImpl impl;
	
//	CardTypeEnum_豹子(1, "豹子"),//三张点相同的牌。例：AAA、222。
//	CardTypeEnum_顺金(2, "顺金"),//花色相同的顺子。例：黑桃456、红桃789。最大的顺金为花色相同的QKA，最小的顺金为花色相同的123。
//	CardTypeEnum_金花(3, "金花"),//花色相同，非顺子。例：黑桃368，方块145。
//	CardTypeEnum_顺子(4, "顺子"),//花色不同的顺子。例：黑桃5红桃6方块7。最大的顺子为花色不同的QKA，最小的顺子为花色不同的123。
//	CardTypeEnum_对子带单(5, "对子带单"),//两张点数相同的牌。例：223，334。
//	CardTypeEnum_单张(6, "单张"),//三张牌不组成任何类型的牌。
//	CardTypeEnum_特殊(7, "特殊"),//花色不同的235。
	public void testPoker()
	{
		impl = new PokerLogicImpl();
		
		addPoker(PokerValueEnum.POKER_A_红桃);
		addPoker(PokerValueEnum.POKER_A_红桃);
		addPoker(PokerValueEnum.POKER_A_红桃);
		
		PokerGroup group = impl.getPokerType(list_testHand);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_豹子);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_A_VALUE);
		list_testHand.clear();
		
		
		addPoker(PokerValueEnum.POKER_A_红桃);
		addPoker(PokerValueEnum.POKER_2_红桃);
		addPoker(PokerValueEnum.POKER_3_红桃);
		group = impl.getPokerType(list_testHand);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_顺金);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_MIN_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_A_红桃);
		addPoker(PokerValueEnum.POKER_3_红桃);
		addPoker(PokerValueEnum.POKER_4_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_金花);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_A_VALUE * 100 + FaceTypeEnum.FACE_TYPE_4_VALUE * 10 + FaceTypeEnum.FACE_TYPE_3_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_A_红桃);
		addPoker(PokerValueEnum.POKER_2_梅花);
		addPoker(PokerValueEnum.POKER_3_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_顺子);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_MIN_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_Q_红桃);
		addPoker(PokerValueEnum.POKER_A_梅花);
		addPoker(PokerValueEnum.POKER_K_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_顺子);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_A_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_Q_红桃);
		addPoker(PokerValueEnum.POKER_Q_梅花);
		addPoker(PokerValueEnum.POKER_K_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_对子带单);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_Q_VALUE  * 100 + FaceTypeEnum.FACE_TYPE_K_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_Q_红桃);
		addPoker(PokerValueEnum.POKER_Q_梅花);
		addPoker(PokerValueEnum.POKER_K_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_对子带单);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_Q_VALUE  * 100 + FaceTypeEnum.FACE_TYPE_K_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_2_红桃);
		addPoker(PokerValueEnum.POKER_2_梅花);
		addPoker(PokerValueEnum.POKER_A_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_对子带单);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_2_VALUE  * 100 + FaceTypeEnum.FACE_TYPE_A_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_4_红桃);
		addPoker(PokerValueEnum.POKER_2_梅花);
		addPoker(PokerValueEnum.POKER_A_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_单张);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_A_VALUE  * 100 + FaceTypeEnum.FACE_TYPE_4_VALUE * 10 + FaceTypeEnum.FACE_TYPE_2_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_2_红桃);
		addPoker(PokerValueEnum.POKER_4_梅花);
		addPoker(PokerValueEnum.POKER_5_红桃);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_单张);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_5_VALUE  * 100 + FaceTypeEnum.FACE_TYPE_4_VALUE * 10 + FaceTypeEnum.FACE_TYPE_2_VALUE);
		list_testHand.clear();
		
		
		addPoker(PokerValueEnum.POKER_2_红桃);
		addPoker(PokerValueEnum.POKER_3_梅花);
		addPoker(PokerValueEnum.POKER_5_方块);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_特殊);
		assertEquals(group.groupValue,FaceTypeEnum.FACE_TYPE_5_VALUE  * 100 + FaceTypeEnum.FACE_TYPE_3_VALUE * 10 + FaceTypeEnum.FACE_TYPE_2_VALUE);
		list_testHand.clear();
		
		addPoker(PokerValueEnum.POKER_3_红桃);
		addPoker(PokerValueEnum.POKER_4_梅花);
		addPoker(PokerValueEnum.POKER_5_方块);
		group = impl.getPokerType(list_testHand);
		System.out.println("group:"+group);
		assertEquals(group.groupType,CardTypeEnum.CARD_TYPE_顺子);
		list_testHand.clear();
	}
	
	ArrayList<PokerVO> list_testHand = new ArrayList<>();
	private void addPoker(int nPokerValue)
	{
		PokerVO poker = new PokerVO(nPokerValue);
		list_testHand.add(poker);
	}
}
