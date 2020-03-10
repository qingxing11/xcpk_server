package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;
import com.wt.xcpk.classicgame.ConfigClassicGame;

public class ClassicGameDaoImpl
{
	private static final String GET_CLASSIC_GAME = "SELECT * FROM `config_classicgame`";
	public static ConfigClassicGame getConfigClassicGame()
	{
		ConfigClassicGame bean = SqlSimpleUtil.selectBean(GET_CLASSIC_GAME, ConfigClassicGame.class);
		return bean;
	}

}
