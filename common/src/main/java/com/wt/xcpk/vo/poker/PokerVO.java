package com.wt.xcpk.vo.poker;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 一张扑克牌，包含自己的花色，牌型，面值
 */
public class PokerVO {
	/**
	 * 牌值
	 */
	public int  value = 0;
	
	/**
	 * 牌型
	 */
	public int type=0;
	
	/**
	 * 花色
	 */
	public int color = 0;
	
	@JSONField(serialize = false)
	private transient PokerValueEnum pokerValueEnum;;
	public PokerVO(int nPokerEnumValue)
	{
		this.value = nPokerEnumValue;
		this.type = PokerValueEnum.getFaceValueByPokerValueOf(nPokerEnumValue);
		this.color = PokerValueEnum.getCardColorByPokerValue(nPokerEnumValue);
		
 		pokerValueEnum = PokerValueEnum.values()[value - 1];
	}
	
	public PokerVO()
	{
	}
	
	/**
	 * 牌型
	 */
	@JSONField(serialize = false)
	public int getType(){
		return type;
	}
	
	/**
	 * 获取牌值，判断是否为主牌,主牌返回
	 * @return
	 */
	@JSONField(serialize = false)
	public int getTypeByMain(int type)
	{
		if(isMainByType(type))
		{
			return FaceTypeEnum.FACE_TYPE_MAIN_VALUE;
		}
		return getType();
	}
	
	/**
	 * 花色
	 * @return
	 */
	@JSONField(serialize = false)
	public int getColor(){
		return color;
	}
	
	/**
	 * 牌值
	 * @return
	 */
	@JSONField(serialize = false)
	public int getValue(){
		return value;
	}
	
	@JSONField(serialize = false)
	public boolean isBigKing(){
		if(PokerValueEnum.POKER_JOKER_BIG == getValue()){
			return true;
		}
		return false;
	}
	
	/**
	 * 小王
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isSmallKing(){
		if(PokerValueEnum.POKER_JOKER_SMALL == getValue()){
			return true;
		}
		return false;
	}
	
	/**
	 * 是王牌
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isKing()
	{
		return isBigKing() || isSmallKing();
	}
	
	/**
	 * 是否是癞子(红桃主牌)
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isMajorByValue(int value)
	{
		if(getValue() == value)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 是否主牌
	 * @param type
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isMainByType(int type)
	{
		if (getType() == type)
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		if(pokerValueEnum == null)
		{
			return "value:"+value+",type:"+type+",color:"+color;
		}
		return pokerValueEnum.getDesc()+"["+type+"]"+",value:"+value+",color:"+color+",type:"+type;
	}
	
	public boolean equals(Object obj)
	{
		return value == ((PokerVO)obj).value;
	}
}