package com.wt.xcpk.vo.poker;


public enum CardTypeEnum {
	CardTypeEnum_Null(0, "空"),
	CardTypeEnum_豹子(1, "豹子"),//三张点相同的牌。例：AAA、222。
	CardTypeEnum_顺金(2, "顺金"),//花色相同的顺子。例：黑桃456、红桃789。最大的顺金为花色相同的QKA，最小的顺金为花色相同的123。
	CardTypeEnum_金花(3, "金花"),//花色相同，非顺子。例：黑桃368，方块145。
	CardTypeEnum_顺子(4, "顺子"),//花色不同的顺子。例：黑桃5红桃6方块7。最大的顺子为花色不同的QKA，最小的顺子为花色不同的123。
	
	CardTypeEnum_对子带单(5, "对子带单"),//两张点数相同的牌。例：223，334。
	CardTypeEnum_单张(6, "单张"),//三张牌不组成任何类型的牌。
	CardTypeEnum_特殊(7, "特殊"),//花色不同的235。
	
	;
	
	public static final int CARD_TYPE_NULL_VALUE = 0;
	/**豹子*/
	public static final int CARD_TYPE_豹子 = 1;
	/**顺金*/
	public static final int CARD_TYPE_顺金 = 2;
	/**金花 */
	public static final int CARD_TYPE_金花 = 3;
	/**顺子*/
	public static final int CARD_TYPE_顺子 = 4;
	/**对子带单*/
	public static final int CARD_TYPE_对子带单 = 5;
	/**单张*/
	public static final int CARD_TYPE_单张 = 6;
	/**特殊*/
	public static final int CARD_TYPE_特殊 = 7;

	private int  value;
	private String  desc;
	
	private CardTypeEnum(int nValueParam, String strDescParam){
		this.value = nValueParam;
		this.desc = strDescParam;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public String getDesc(){
		return this.desc;
	}
}

