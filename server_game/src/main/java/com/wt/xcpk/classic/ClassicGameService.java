package com.wt.xcpk.classic;

import com.wt.naval.vo.user.Player;

public interface ClassicGameService
{
	/**
	 * 加入新手场队列
	 * <br> 底注100金币，入场3000，>=10w无法入场
	 * @param player
	 */
	void addBeginnerList(Player player,int type);

	void checkPoker(Player player);

	void playerFollowBet(Player player);

	void raiseBet(Player player, int betNum);

	void leaveRoom(Player player);

	void comparerPoker(Player player, int pos);

	/**
	 * 弃牌
	 * @param player
	 */
	void fold(Player player);

	void allIn(Player player);

	void ready(Player player);

	/**
	 * 经典场换桌
	 * @param player
	 */
	void changeTable(Player player);

	/**
	 * 经典场断线重连
	 * @param player
	 */
	boolean reconnect(Player player);
	
	boolean setGetBoxUserId(Player player);
	
	boolean isGetBoxIdExist(Player player);

	int getCost(int type);
}