package com.brc.naval.ranking;

import com.wt.naval.vo.user.Player;

public interface RankingService 
{
	/**
	 * 判断能否领取奖励
	 */
	public boolean isFirst(int userId,int type);
	
	
	/**
	 * @param userId
	 * @param type 0:赢金 1：充值
	 * @return
	 */
	public boolean isGeting(int userId,int type);
	
	/**
	 * 领取奖励
	 * */
	public long getReward(Player player,int type);
	
	/**
	 *判断能否领取低保
	 */
	public boolean canLow(int userId);
	
	/**
	 * 领取低保
	 */
	public int getLow(Player player);
}
