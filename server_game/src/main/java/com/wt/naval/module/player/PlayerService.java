package com.wt.naval.module.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import com.wt.archive.GameData;
import com.wt.cmd.Response;
import com.wt.cmd.user.PlayerNicknameVO;
import com.wt.naval.vo.user.Player;
import com.wt.util.security.MySession;
import com.wt.xcpk.PlayerSimpleData;

public interface PlayerService
{
	Player getPlayer(int userId);

	Player getPlayer(MySession session);

	/**
	 * 根据通道中的session获取player，并检查是否存在，如果不存在主动发送错误消息到请求方
	 * 
	 * @param session
	 * @return
	 */
	Player getPlayerAndCheck(MySession session);

	GameData getGameData(int userId);

	GameData getGameData(MySession session);

	/**
	 * 获取指定玩家简单数据，如果缓冲中没有则读取数据库
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	PlayerSimpleData getPlayerSimpleData(int userId);
	
	Collection<PlayerSimpleData> getPlayerSimpleData(Collection<Integer> userId);

	/**
	 * 玩家是否在线
	 * 
	 * @param userId
	 * @return
	 */
	boolean isOnline(int userId);

	ArrayList<PlayerNicknameVO> getPlayersNickname(ArrayList<Integer> list_userIds);

	void updatePlayerNickname(int userId, String nickName);

	/**
	 * 如果玩家在线，发送消息给指定玩家
	 * 
	 * @param response
	 * @param userId
	 */
	void sendMsg(Response response, int userId);

	/** 在线玩家列表 */
	ConcurrentHashMap<Integer, Player> getMapPlayers();

	/**
	 * 获取所有存档在线玩家，此时玩家有可能已经不在线
	 * 
	 * @return
	 */
	Collection<Player> getAllPlayer();

	Player getRobotBankerPlayer();

	void playerInGame(Integer userId);

	void addPlayerExp(int userId, int exp);

	/**
	 * 获取当前等级升级所需经验值
	 * 
	 * @param level
	 * @return
	 */
	int getCostExp(int level);

	int calcExp(Player player, int exp);

	/**
	 * 发送消息给所有玩家
	 * @param response
	 */
	void sendToAll(Response response);

	void bannid(int userId);
}
