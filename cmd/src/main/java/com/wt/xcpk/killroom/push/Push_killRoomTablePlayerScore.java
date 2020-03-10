package com.wt.xcpk.killroom.push;

import java.util.ArrayList;
import java.util.Collection;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.KillRoomDirectionPlayer;
import com.wt.xcpk.killroom.KillRoomLog;
import com.wt.xcpk.killroom.KillRoomTablePlayerRoundScore;
import com.wt.xcpk.vo.poker.PokerVO;

/**
 * 开奖消息：
 * 
 * 庄家手牌
 * 4个方位手牌
 * 输赢分数：
 * 		庄家输赢
 * 		闲家赢分前几名，如果都输分则不显示
 * 		自己输赢
 * 
 * @author WangTuo
 */
public class Push_killRoomTablePlayerScore extends Response
{
	/**上桌玩家输赢*/
	public Collection<KillRoomTablePlayerRoundScore> list_tablePlayerScore = new ArrayList<KillRoomTablePlayerRoundScore>();
	public Push_killRoomTablePlayerScore()
	{
		this.msgType = MsgTypeEnum.KillRoom_通杀场开奖状态.getType();
	}

	@Override
	public String toString()
	{
		return "Push_killRoomTablePlayerScore [list_tablePlayerScore=" + list_tablePlayerScore + ", msgType=" + msgType + ", code=" + code + "]";
	}
}