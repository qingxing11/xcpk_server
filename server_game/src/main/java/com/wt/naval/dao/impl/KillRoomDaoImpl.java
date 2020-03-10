package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;
import com.wt.naval.dao.model.Config_killroomPoker;

import model.ConfigKillroom;
import model.GameTongsha;

public class KillRoomDaoImpl
{
	private static final String GET_ROOMCONFIG = "SELECT * FROM `config_killRoom`";
	public static ConfigKillroom getRoomConfig()
	{
		return SqlSimpleUtil.selectBean(GET_ROOMCONFIG, ConfigKillroom.class);
	}
	
	private static final String GET_KILLROOMPOKERCONFIG = "SELECT * FROM `config_killroompoker`";
	public static Config_killroomPoker getKillRoomPokerConfig()
	{
		return SqlSimpleUtil.selectBean(GET_KILLROOMPOKERCONFIG, Config_killroomPoker.class);
	}
	
	private static final String GetGameTongsha = "SELECT * FROM game_tongsha";
	public static GameTongsha getGameTongsha()
	{
		return SqlSimpleUtil.selectBean(GetGameTongsha, GameTongsha.class);
	}
}
