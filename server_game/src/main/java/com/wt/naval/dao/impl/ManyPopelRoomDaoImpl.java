package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;

import model.ConfigManypepolroompoker;
import model.GameWanren;

public class ManyPopelRoomDaoImpl
{
	private static final String GET_CLASSIC_GAME = "SELECT * FROM `config_manypepolroompoker`";
	public static ConfigManypepolroompoker getConfigManypepolGame()
	{
		ConfigManypepolroompoker bean = SqlSimpleUtil.selectBean(GET_CLASSIC_GAME, ConfigManypepolroompoker.class);
		return bean;
	}
	
	private static final String getGameWanren = "SELECT * FROM `game_wanren`";
	/**
	 * 上庄单牌率	上庄对子率	上庄大牌率	豹子AAA率	豹子率	顺金率
	 * @return
	 */
	public static GameWanren getGameWanren()
	{
		GameWanren bean = SqlSimpleUtil.selectBean(getGameWanren, GameWanren.class);
		return bean;
	}

}
