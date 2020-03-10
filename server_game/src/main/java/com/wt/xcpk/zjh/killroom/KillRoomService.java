package com.wt.xcpk.zjh.killroom;

import java.util.ArrayList;

import com.wt.naval.vo.user.Player;
import com.wt.xcpk.Room;
import com.wt.xcpk.killroom.KillRoomLog;
import com.wt.xcpk.vo.JackpotData;
import com.wt.xcpk.vo.KillRoomBigWinVO;
import com.wt.xcpk.vo.RankVO;

import model.ConfigKillroom;

public interface KillRoomService
{	
	/**
	 * 下筹码
	 * @param player
	 * @param chipNum
	 * @return
	 */
	boolean payChip(Player player,int pos,int chipNum);
	
	/**
	 * 上庄
	 * @param player
	 * @return
	 */
	int requestBanker(Player player);
	
	/**
	 * 离开房间
	 * @param player
	 * @return
	 */
	boolean leaveRoom(Player player);
	
	/**
	 * 进入房间
	 * @param player
	 * @return
	 */
	boolean enterRoom(Player player);
	
	/**
	 * 选座坐下
	 * @return
	 */
	int sitDown(Player player,int pos);
	
	/**站起
	 * */
	boolean standUp(Player player);
	
	/**
	 * 获取通杀场房间
	 * @return
	 */
	Room getRoom();

	int getState();
	
	/**获取通杀场配置*/
	ConfigKillroom getConfigKillroom();

	/**
	 * 玩家申请上庄
	 *	public static final int ERROR_上庄人数太多 = 1;
	public static final int ERROR_申请上庄时已是庄家 = 2;
	public static final int ERROR_申请上庄时人数太多 = 3;
	public static final int ERROR_申请上庄时已在列表 = 4;
	 * 1000:成功
	 * @param player
	 * @return
	 */
	int applicationKillRoomBanker(Player player);

	long getStateTime();

	ArrayList<KillRoomLog> getKillRoomLog();
	
	JackpotData getJackpotData();

	int getBankerRound();
	
	/**
	 * 更新通杀场应金榜，客户端显示昵称和头像
	 * @param list_bigWin
	 */
	void updateBigWinPlayer(ArrayList<RankVO> list_bigWin);

	ArrayList<KillRoomBigWinVO> getBigWin();
	
	void refreshKillroomConfig();

	Player getBanker();

	void bankerWithdrawalSafebox(Player play);
}
