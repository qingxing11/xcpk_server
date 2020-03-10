package com.wt.xcpk.vo;

import java.util.ArrayList;

import com.wt.xcpk.vo.poker.PokerVO;

public class GameRoundDataVO
{
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
	/**特殊，也是单张*/
	public static final int CARD_TYPE_特殊 = 7;
	
	public int pos;
	public long roundBet;
	public ArrayList<PokerVO> list_handPoker;
	public int pokerType;
	public int userId;
	
	public long jackpotWin;
	
	public long newCoins;

	public GameRoundDataVO() {
	}

	@Override
	public String toString()
	{
		return "GameRoundDataVO [pos=" + pos + ", roundBet=" + roundBet + ", list_handPoker=" + list_handPoker + ", pokerType=" + pokerType + ", userId=" + userId + ", jackpotWin=" + jackpotWin + ", newCoins=" + newCoins + "]";
	}
	
	// [state=4, list_tablePlayer=[PlayerSimpleData [userId=31763, nickName=31763, coins=42381591, pos=0, roleId=0, vipLv=3, lv=5, crystals=0, sign=null, victoryNum=0, failureNum=0, betNum=20000, isCheckPoker=false, gameState=0, headImgIcon=null], PlayerSimpleData [userId=32008, nickName=32008, coins=49937600, pos=1, roleId=0, vipLv=0, lv=1, crystals=50, sign=null, victoryNum=0, failureNum=0, betNum=20000, isCheckPoker=false, gameState=0, headImgIcon=null]], bankerPos=0, pos=1, roundNum=1, list_allBet=[20000], actionPos=1, actionTime=1, list_pokers=[红桃9[9],value:29,color:1,type:9, 红桃10[10],value:33,color:1,type:10, 红桃J[11],value:37,color:1,type:11], pokerType=2, msgType=293, code=0]

}
