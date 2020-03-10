package com.wt.naval.biz;

import com.wt.db.RedisUtil;

public class PlayerBiz
{

	private static String PLAYER_AUTOID = "playerAutoId";

	/**
	 * 获取玩家的唯一自增id
	 * 
	 * @param userId
	 *                玩家id
	 * @return
	 */
	public static Integer getPlayerAutoAddId(int userId)
	{
		int id = (int) RedisUtil.increment(PLAYER_AUTOID + userId, 1);
		return id;
	}

	/**
	 * 获取玩家的唯一自增id 建议一次性获取几个id时使用它
	 * 
	 * @param userId
	 * @param delta
	 *                自增的数量
	 * @return
	 */
	public static Integer getPlayerAutoAddId(int userId, int delta)
	{
		int id = (int) RedisUtil.increment(PLAYER_AUTOID + userId, delta);
		return id;
	}

}
