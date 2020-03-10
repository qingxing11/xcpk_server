package com.wt.naval.worldmap.event;

import com.wt.naval.vo.user.Player;

import io.netty.channel.Channel;

public interface PlayerListener extends MapUnitEventListener
{
	/**
	 * 玩家上线
	 * @param player
	 * @param channel
	 */
	void online(Player player,Channel channel);
	
	/**
	 * 玩家在游戏中离线，存档暂时缓存在服务器中
	 * @param player
	 */
	void offlineOnGame(Player player);
	
	/**
	 * 玩家在服务器中离线，移除服务器内存中玩家存档
	 * @param player
	 */
	void offlineOnServer(Player player);
}
