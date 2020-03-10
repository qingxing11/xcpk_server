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
public class Push_killRoomChangeToLotteryResponse extends Response
{
	/**庄家手牌*/
	public ArrayList<PokerVO> list_bankerPoker = new ArrayList<PokerVO>();
	
	/**各方位数据：手牌，*/
	public ArrayList<KillRoomDirectionPlayer> list_directionPlayers = new ArrayList<KillRoomDirectionPlayer>();

	/**庄家当前金币*/
	public long bankerScore;
	
	/**赢的金币*/
	public long calcScore;
	
	/**
	 * 庄家手牌类型
	 */
	public int bankerType;
  
	/**上桌玩家输赢*/
	public Collection<KillRoomTablePlayerRoundScore> list_tablePlayerScore = new ArrayList<KillRoomTablePlayerRoundScore>();
	
	public long jackpot;
	
	public long jackpotWin;
	
	public int exp;
	
	public KillRoomLog killRoomLog;
	
	/**下注的金币*/
	public int subCoins;
	
	/**
	 * 额外扣除金币
	 */
	public int otherSubCoins;
	
	/**当前金币*/
	public long nowCoins;
	public Push_killRoomChangeToLotteryResponse()
	{
		this.msgType = MsgTypeEnum.KillRoom_通杀场开奖状态.getType();
	}

	public void init()
	{
		bankerScore = 0;
		calcScore = 0;
		bankerType = 0;
		list_tablePlayerScore.clear();
		jackpot = 0;
		jackpotWin = 0;
		exp = 0;
		killRoomLog = null;
		subCoins = 0;
		otherSubCoins = 0;
		nowCoins = 0;
	}
	
	@Override
	public String toString()
	{
		return "Push_killRoomChangeToLotteryResponse [list_bankerPoker=" + list_bankerPoker + ", list_directionPlayers=" + list_directionPlayers + ", bankerScore=" + bankerScore + ", calcScore=" + calcScore + ", bankerType=" + bankerType + ", list_tablePlayerScore=" + list_tablePlayerScore + ", jackpot=" + jackpot + ", jackpotWin=" + jackpotWin + ", exp=" + exp + ", killRoomLog=" + killRoomLog + ", subCoins=" + subCoins + ", otherSubCoins=" + otherSubCoins + ", nowCoins=" + nowCoins + ", msgType=" + msgType + ", code=" + code + "]";
	}
}