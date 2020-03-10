package com.wt.cmd.xcpk.manypepol;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.poker.PokerVO;

/**
 * 进入经典场：
 * 房间当前状态
 * 当前座位上玩家
 * 当前庄家
 * 
 * 
 * @author akwang
 *
 */
public class ManyPepolRoomCheckPokerResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_没轮到行动 = 1;
	
	public ArrayList<PokerVO> list_poker;
	public int pokerType;
	public ManyPepolRoomCheckPokerResponse()
	{
		msgType = MsgTypeEnum.manypepol_玩家看牌.getType();
	}
	
	public ManyPepolRoomCheckPokerResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_玩家看牌.getType();
		this.code = code;
	}
	
	public ManyPepolRoomCheckPokerResponse(int code,ArrayList<PokerVO> list_poker,int pokerType)
	{
		msgType = MsgTypeEnum.manypepol_玩家看牌.getType();
		this.code = code;
		this.list_poker = list_poker;
		this.pokerType = pokerType;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomCheckPokerResponse [list_poker=" + list_poker + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
