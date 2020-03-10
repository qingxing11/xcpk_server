package com.wt.xcpk.zjh.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.xcpk.vo.poker.CardTypeEnum;
import com.wt.xcpk.vo.poker.FaceTypeEnum;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.vo.poker.PokerTeam;
import com.wt.xcpk.vo.poker.PokerVO;
import com.wt.xcpk.vo.poker.PokerValueEnum;

@Service
public class PokerLogicImpl implements PokerLogicService
{
	public static final boolean isDebug = false;
	
	private ArrayList<PokerVO> initPokerList;
	
	@PostConstruct
	private void init()
	{
		initPokerList = getInitPokerArray();
	}
	
	public ArrayList<PokerVO> getInitPokersWithOutJoker()
	{
		if(initPokerList.size() != 52)
		{
			initPokerList.clear();
			initPokerList.addAll(getInitPokerArray());
		}
		return initPokerList;
	}
	
	/**
	 * 得到初始一副牌,排除大小王
	 * @return
	 */
	public static ArrayList<PokerVO> getInitPokerArray()
	{
		ArrayList<PokerVO> initPokerList = new ArrayList<PokerVO>();
		// 两副牌
		for (int nPokerValue = PokerValueEnum.POKER_MIN_TYPE_VALUE ; nPokerValue <= PokerValueEnum.POKER_A_梅花 ; ++nPokerValue)
		{
			PokerVO poker = new PokerVO(nPokerValue);
			initPokerList.add(poker);
		}
		return initPokerList;
	}
	 
//	@Override
//	public PokerVO getRandomPoker()
//	{
//		int pokerValue = MyUtil.getRandom(PokerValueEnum.POKER_MIN_TYPE_VALUE, PokerValueEnum.POKER_A_梅花);
//		PokerVO poker = new PokerVO(pokerValue);
//		return poker;
//	}
	
	@Override
	public int pokerComparer(ArrayList<PokerVO> lastCard, ArrayList<PokerVO> playPoker)
	{
		PokerGroup pokerGroup0 = getPokerType(lastCard);
		PokerGroup pokerGroup1 = getPokerType(playPoker);
		if(pokerGroup0 == null || pokerGroup1 == null)
		{
			return -2;
		}
		return pokerComparer(pokerGroup0, pokerGroup1);
	}
	
	@Override
	public int pokerComparer(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (lastPoker.groupType)
		{
			case CardTypeEnum.CARD_TYPE_单张:
				return comparer_onePiece(initiator,lastPoker);
				
			case CardTypeEnum.CARD_TYPE_对子带单:
				return comparer_coupe(initiator,lastPoker);
				
			case CardTypeEnum.CARD_TYPE_特殊:
				return comparer_special(initiator,lastPoker);
				
			case CardTypeEnum.CARD_TYPE_豹子:
				return comparer_leopard(initiator,lastPoker);
				
			case CardTypeEnum.CARD_TYPE_金花:
				return comparer_sameColor(initiator,lastPoker);
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return comparer_normalFlush(initiator,lastPoker);
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return comparer_sameColorFlush(initiator,lastPoker);

			default:
				break;
		}
		return -1;
	}
	
	private int comparer_sameColorFlush(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)
		{
			case CardTypeEnum.CARD_TYPE_豹子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return integetValueComparer(initiator, lastPoker);

			case CardTypeEnum.CARD_TYPE_对子带单:
			case CardTypeEnum.CARD_TYPE_金花:
			case CardTypeEnum.CARD_TYPE_顺子:
			case CardTypeEnum.CARD_TYPE_单张:
			case CardTypeEnum.CARD_TYPE_特殊:
				return -1;
				
			default:
				break;
		}
		return -1;
	}

	private int comparer_normalFlush(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)
		{
			case CardTypeEnum.CARD_TYPE_豹子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_金花:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return integetValueComparer(initiator, lastPoker);
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return 1;

			case CardTypeEnum.CARD_TYPE_单张:
			case CardTypeEnum.CARD_TYPE_特殊:
			case CardTypeEnum.CARD_TYPE_对子带单:
				return -1;
				
			default:
				break;
		}
		return -1;
	}

	private int integetValueComparer(PokerGroup initiator, PokerGroup lastPoker)
	{
		if(initiator.groupValue > lastPoker.groupValue)
		{
			return 1;
		}
		else if(initiator.groupValue < lastPoker.groupValue)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}

	private int comparer_sameColor(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)
		{
			case CardTypeEnum.CARD_TYPE_豹子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_金花:
				return integetValueComparer(initiator, lastPoker);
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return 1;

			case CardTypeEnum.CARD_TYPE_对子带单:
			case CardTypeEnum.CARD_TYPE_顺子:
			case CardTypeEnum.CARD_TYPE_单张:
			case CardTypeEnum.CARD_TYPE_特殊:
				return -1;
				
			default:
				break;
		}
		return -1;
	}

	/**
	 *豹子对比
	 * @param initiator
	 * @param lastPoker
	 * @return
	 */
	private int comparer_leopard(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)//对方是豹子，我的牌型
		{
			case CardTypeEnum.CARD_TYPE_对子带单:
				return -1;
				
			case CardTypeEnum.CARD_TYPE_豹子:
				return integetValueComparer(initiator, lastPoker);
				
			case CardTypeEnum.CARD_TYPE_金花:
				return -1;
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return -1;
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return -1;

			case CardTypeEnum.CARD_TYPE_单张:
				return -1;
				
			case CardTypeEnum.CARD_TYPE_特殊:
				return 1;
				
			default:
				break;
		}
		return -1;
	}

	/**
	 * 特殊牌对比
	 * @param initiator
	 * @param lastPoker
	 * @return
	 */
	private int comparer_special(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)
		{
			case CardTypeEnum.CARD_TYPE_对子带单:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_豹子:
				return -1;
				
			case CardTypeEnum.CARD_TYPE_金花:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return 1;

			case CardTypeEnum.CARD_TYPE_单张:
			case CardTypeEnum.CARD_TYPE_特殊:
				return integetValueComparer(initiator, lastPoker);
				
			default:
				break;
		}
		return -1;
	}

	/**
	 * 对子对比
	 * @param initiator
	 * @param lastPoker
	 * @return
	 */
	private int comparer_coupe(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)//对方是对子，我的牌型
		{
			case CardTypeEnum.CARD_TYPE_对子带单:
				return integetValueComparer(initiator, lastPoker);
				
			case CardTypeEnum.CARD_TYPE_豹子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_金花:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return 1;

			case CardTypeEnum.CARD_TYPE_单张:
			case CardTypeEnum.CARD_TYPE_特殊:
				return -1;
				
			default:
				break;
		}
		return -1;
	
	}

	/**
	 * 单牌对比
	 * @param initiator
	 * @param lastPoker
	 * @return
	 */
	private int comparer_onePiece(PokerGroup initiator, PokerGroup lastPoker)
	{
		switch (initiator.groupType)
		{
			case CardTypeEnum.CARD_TYPE_对子带单:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_豹子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_金花:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return 1;
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return 1;

			case CardTypeEnum.CARD_TYPE_单张:
			case CardTypeEnum.CARD_TYPE_特殊:
				return integetValueComparer(initiator, lastPoker);
				
			default:
				break;
		}
		return -1;
	}

	@Override
	public PokerGroup getPokerType(ArrayList<PokerVO> list_poker)
	{
		if(list_poker==null)
			return null;
		
		PokerTeam pokerTeam = pokerTeam_sortAndSearch(list_poker);
		if(isDebug)Tool.print_debug_level0("获取牌类型,将牌分组:"+pokerTeam);
		switch (pokerTeam.hashMap_samePoker.size())
		{
			case 1://只有一组，只能是豹子
				if(isDebug)Tool.print_debug_level0("获取牌类型,只有一组，只能是豹子");
				return checkBaozi(pokerTeam);
				
			case 2://有两组，只能是对子加单排
				if(isDebug)Tool.print_debug_level0("获取牌类型,有两组，只能是对子带单");
				return check2SizePoker(pokerTeam);
				
			case 3://三组，同花顺，乱同花，顺子，单牌，特殊
				if(isDebug)Tool.print_debug_level0("获取牌类型,有3组，可能是同花顺，乱同花，顺子，单牌，特殊");
				return check3SizePoker(pokerTeam);

			default:
				break;
		}
		
		return null;
	}

	private PokerGroup check3SizePoker(PokerTeam pokerTeam)
	{
		PokerGroup group = check3Flush(pokerTeam);//同花顺，乱同花，顺子
		if(group != null)
		{
			if(isDebug)Tool.print_debug_level0("获取牌类型,有3组，同花顺，乱同花，顺子,group:"+group);
			return group;
		}
		 //对子
//		group = checkCouple(pokerTeam);
//		if(group != null)
//		{
//			Tool.print_debug_level0("获取牌类型,有3组，对子带单,group:"+group);
//			return group;
//		}
		
		group = checkSpecial(pokerTeam);//特殊
		if(group != null)
		{
			if(isDebug)Tool.print_debug_level0("获取牌类型,有3组，特殊,group:"+group);
			return group;
		}
		
		int value = 0;
		value += pokerTeam.list_poker.get(0).getType();
		value += pokerTeam.list_poker.get(1).getType() * 10;
		value += pokerTeam.list_poker.get(2).getType() * 100;
		
		group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_单张, value);
		return group;
	}

	private static final int specialNum = 5*100 + 3*10 + 2;
	private PokerGroup checkSpecial(PokerTeam pokerTeam)
	{
		PokerGroup group = null;
		HashMap<Integer,Integer> hashMap_samePoker = pokerTeam.hashMap_samePoker;
		if (hashMap_samePoker.containsKey(FaceTypeEnum.FACE_TYPE_2_VALUE) && hashMap_samePoker.containsKey(FaceTypeEnum.FACE_TYPE_3_VALUE) && hashMap_samePoker.containsKey(FaceTypeEnum.FACE_TYPE_5_VALUE))
		{
			HashSet<Integer> set = new HashSet<Integer>();
			
			for (PokerVO poker : pokerTeam.list_poker)
			{
				if(set.contains(poker.getColor()))
				{
					return null;
				}
				set.add(poker.getColor());
			}
			group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_特殊, specialNum);
			return group;
		}
		return null;
	}

	private PokerGroup check3Flush(PokerTeam pokerTeam)
	{
		PokerGroup group;
		if(isMin3Flush(pokerTeam.hashMap_samePoker))//最小顺
		{
			 if(isSameColor(pokerTeam.list_poker))//同花
			 {
				 group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_顺金, FaceTypeEnum.FACE_TYPE_MIN_VALUE);
			 }
			 else//普通顺子
			 {
				 group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_顺子, FaceTypeEnum.FACE_TYPE_MIN_VALUE);
			 }
			return group;
		}
		
		if(isFlush(pokerTeam.list_poker))//普通顺
		{
			int value = pokerTeam.list_poker.get(pokerTeam.list_poker.size() - 1).getType();
			 if(isSameColor(pokerTeam.list_poker))//同花
			 {
				 group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_顺金, value);
			 }
			 else//普通顺子
			 {
				 group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_顺子, value);
			 }
			 return group;
		}
		
		 if(isSameColor(pokerTeam.list_poker))//金顺
		 {
			int value = 0;
			value += pokerTeam.list_poker.get(0).getType();
			value += pokerTeam.list_poker.get(1).getType() * 10;
			value += pokerTeam.list_poker.get(2).getType() * 100;
			 
			 group = new PokerGroup(pokerTeam.list_poker, CardTypeEnum.CARD_TYPE_金花, value);
			 return group;
		 }
		 return null;
	}
	 
	
	/**
	 * 所比较牌组是否为同花色
	 * 
	 * @param list_temp
	 * @return
	 */
	private static boolean isSameColor(ArrayList<PokerVO> list_temp)
	{
		int color = list_temp.get(0).getColor();
		for (PokerVO poker : list_temp)
		{
			if (color != poker.getColor())
				return false;
		}
		return true;
	}
	
	private static boolean isMin3Flush(HashMap<Integer, Integer> hashMap_samePoker)
	{
		if (hashMap_samePoker.containsKey(FaceTypeEnum.FACE_TYPE_2_VALUE) && hashMap_samePoker.containsKey(FaceTypeEnum.FACE_TYPE_A_VALUE) && hashMap_samePoker.containsKey(FaceTypeEnum.FACE_TYPE_3_VALUE))
		{
			return true;
		}
		return false;
	}
	
	private static boolean isFlush(ArrayList<PokerVO> list_poker)
	{
		int index = 0;
		int value = list_poker.get(0).getType();
		for (int i = 1 ; i < list_poker.size() ; i++)
		{
			int num = list_poker.get(i).getType() - (value + 1);
			if(num < 0)
			{
				return false;
			}
			index += num;
			value = list_poker.get(i).getType();
		}
		if (index == 0)// 连续的
		{
			return true;
		}
		return false;
	}

	private PokerGroup check2SizePoker(PokerTeam pokerTeam)
	{
		int score = 0;
		Iterator<Entry<Integer, Integer>> iter = pokerTeam.hashMap_samePoker.entrySet().iterator();
		while (iter.hasNext())
		{
			Entry<Integer, Integer> entry = iter.next();
			if(entry.getValue() == 2)
			{
				score += entry.getKey() * 100;
			}
			else
			{
				score += entry.getKey();
			}
		}
		
		PokerGroup group = new PokerGroup(pokerTeam.list_poker,CardTypeEnum.CARD_TYPE_对子带单,score);
		
		if(isDebug)Tool.print_debug_level0("获取牌类型,有两组，只能是对子带单,group:"+group);
		return group;
	}

	private PokerGroup checkBaozi(PokerTeam pokerTeam)
	{
		PokerGroup group = new PokerGroup(pokerTeam.list_poker,CardTypeEnum.CARD_TYPE_豹子,pokerTeam.list_poker.get(0).type);
		if(isDebug)Tool.print_debug_level0("获取牌类型,group:"+group);
		return group;
	}

	/**
	 * 获得牌组王牌的数量与玩牌，万能牌外的数量,按2到A排序
	 * 
	 * @param list_poker
	 * @return
	 */
	public static PokerTeam pokerTeam_sortAndSearch(ArrayList<PokerVO> list_poker)
	{
		Collections.sort(list_poker, getComparator_upPoker());
		PokerTeam pokerTeam = new PokerTeam(list_poker);
		pokerTeam.hashMap_samePoker = searcherPoker_kv(list_poker);
		return pokerTeam;
	}
	
	/**
	 * 检查牌，记录每种类型有多少张 key:类型，value:该类型张数
	 * 
	 * @param pokerTeam
	 * @return
	 */
	public static HashMap<Integer, Integer> searcherPoker_kv(ArrayList<PokerVO> list_poker)
	{
		HashMap<Integer, Integer> hashMap_samePoker = new HashMap<>();
		for (PokerVO poker : list_poker)
		{
			hashMap_samePoker.put(poker.getType(), hashMap_samePoker.get(poker.getType()) == null ? 1 : hashMap_samePoker.get(poker.getType()) + 1);
		}
		return hashMap_samePoker;
	}
	
	private static Comparator<PokerVO> getComparator_upPoker()
	{
		Comparator<PokerVO> comparator = new Comparator<PokerVO>()
		{
			public int compare(PokerVO p1, PokerVO p2)
			{
				if (p1.getValue() < p2.getValue())
				{
					return -1;
				}
				else if (p1.getValue() > p2.getValue())
				{
					return 1;
				}
				return 0;
			}
		};
		return comparator;
	}
	
	@Override
	public int getBaseScore(int groupType)
	{
		switch (groupType)
		{
			case CardTypeEnum.CARD_TYPE_豹子:
				return 5;
				
			case CardTypeEnum.CARD_TYPE_顺金:
				return 4;
				
			case CardTypeEnum.CARD_TYPE_金花:
				return 3;
				
			case CardTypeEnum.CARD_TYPE_顺子:
				return 2;
		}
		return 1;
	}

	private ArrayList<PokerVO> list_3Poker = new ArrayList<PokerVO>();
	private ArrayList<PokerVO> list_allPoker = new ArrayList<PokerVO>();
	@Override
	public ArrayList<PokerVO> getPokerType(int type) {
		while (true)
		{
			list_3Poker.clear();
			list_allPoker.clear();
			list_allPoker.addAll(getInitPokersWithOutJoker());
			for (int i = 0 ; i < 3 ; i++)
			{
				int index = MyUtil.getRandom(list_allPoker.size());
				list_3Poker.add(list_allPoker.remove(index));
			}
			PokerGroup pokerGroup = getPokerType(list_3Poker);
			if(pokerGroup.groupType == type + 1)
			{
				return list_3Poker;
			}
		}
		
//		switch (type)// 0：豹子 1：顺金 2：金花 3：顺子 4：对子 5：单牌
//		{
//			case 0:
//				return randomBaozi();
//
//			case 1:
//				return randomSameColorFlush();
//				
//			default:
//				break;
//		}
//		return null;
	}
	
	private ArrayList<PokerVO> randomSameColorFlush()
	{
		
		return null;
	}

	private ArrayList<PokerVO> randomBaozi()
	{
		ArrayList<PokerVO> list = new ArrayList<PokerVO>();
		int randomPoker = MyUtil.getRandom(2, 15);
		for (int i = 0 ; i < 3 ; i++)
		{
			PokerVO pokerVO = new PokerVO(randomPoker);
			list.add(pokerVO);
		}
		return list;
	}
}