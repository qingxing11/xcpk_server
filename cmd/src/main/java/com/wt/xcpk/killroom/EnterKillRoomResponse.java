package com.wt.xcpk.killroom;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.vo.KillRoomBigWinVO;

/**
 * 进入房间，返回房间信息
 * 房间状态
 * 当前庄家
 * 座位玩家
 * 上庄列表
 * @author WangTuo
 *
 */
public class EnterKillRoomResponse extends Response
{
	public int state;
	public PlayerSimpleData banker;
	public ArrayList<PlayerSimpleData> list_tablePlayer;
	public long jackpot;
	public long stateTime;
	public ArrayList<KillRoomLog> list_killRoomLog;
	
	public int bankerRound;
	
	public ArrayList<KillRoomBigWinVO> list_bigWin;
	
	/**
	 * 方位下注
	 */
	public ArrayList<Long> list_directionBetNum = new ArrayList<Long>();
	
	public int roundIndex;
 
	public EnterKillRoomResponse()
	{
		msgType = MsgTypeEnum.KillRoom_进入通杀场.getType();
	}
	
	public EnterKillRoomResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_进入通杀场.getType();
		this.code = code;
	}
	
	public EnterKillRoomResponse(int code,int state)
	{
		msgType = MsgTypeEnum.KillRoom_进入通杀场.getType();
		this.code = code;
		this.state = state;
	}
	
	public void setBanker(PlayerSimpleData banker)
	{
		this.banker = banker;
	}
	
	public void setTablePlayer(ArrayList<PlayerSimpleData> list_tablePlayer)
	{
		this.list_tablePlayer = list_tablePlayer;
	}

	@Override
	public String toString()
	{
		return "EnterKillRoomResponse [state=" + state + ", banker=" + banker + ", list_tablePlayer=" + list_tablePlayer + ", jackpot=" + jackpot + ", stateTime=" + stateTime + ", list_killRoomLog=" + list_killRoomLog + ", bankerRound=" + bankerRound + ", list_bigWin=" + list_bigWin + ", list_directionBetNum=" + list_directionBetNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}