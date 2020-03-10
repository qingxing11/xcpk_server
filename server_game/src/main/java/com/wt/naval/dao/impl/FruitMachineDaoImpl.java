package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;

import model.GameFruit;

public class FruitMachineDaoImpl {
	/**
	 * 获取所有的系统邮件
	 */
	private static final String GETALL_SERVER_FruitMachine = "SELECT * FROM `game_fruit` ";
	public static GameFruit getRewardOdds()
	{ 
		return (GameFruit)SqlSimpleUtil.selectBean(GETALL_SERVER_FruitMachine,GameFruit.class);
	}
}
