package com.wt.xcpk.vo.poker;

import java.util.ArrayList;

/**
 * 出牌记录
 * @author Wangtuo
 */
public class PokerGroup
{
	/**
	 * 打出的手牌*/
	public ArrayList<PokerVO> list_poker;
	
	/**
	 * 打出的手牌类型*/
	public int groupType;
	
	/**打出的手牌牌值*/
	public int groupValue;
	
//	public int pos;
	public PokerGroup()
	{} 
	
	public PokerGroup(ArrayList<PokerVO> list_poker,int groupType,int groupValue)
	{
		this.list_poker = list_poker;
		this.groupType = groupType;
		this.groupValue = groupValue;
	}
	
	@Override
	public String toString()
	{
		if(list_poker != null)
		{
			String type = "";
			switch (groupType)
			{
				case CardTypeEnum.CARD_TYPE_单张:
					type = "单张";
					break;
					
				case CardTypeEnum.CARD_TYPE_对子带单:
					type = "对子带单";
					break;
					
				case CardTypeEnum.CARD_TYPE_特殊:
					type = "特殊";
					break;
					
				case CardTypeEnum.CARD_TYPE_豹子:
					type = "豹子";
					break;
					
				case CardTypeEnum.CARD_TYPE_金花:
					type = "金花";
					break;
					
				case CardTypeEnum.CARD_TYPE_顺子:
					type = "顺子";
					break;
					
				case CardTypeEnum.CARD_TYPE_顺金:
					type = "顺金";
					break;

				default:
					break;
			}
			return "牌型【"+list_poker+"】"+",牌型:"+groupType+"="+type+",牌值:"+groupValue;
		}
		return null;
	}
}
