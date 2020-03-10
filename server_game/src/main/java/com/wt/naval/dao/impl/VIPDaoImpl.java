package com.wt.naval.dao.impl;

import java.util.List;

import com.wt.db.SqlSimpleUtil;

import model.UserVip;

public class VIPDaoImpl 
{
	private static final String Get_USERVIP = "SELECT * FROM `user_vip`";

	/**获取VIP配置表
	 * @return */
	public static  List<UserVip> getUserVIPModel()
	{
		return SqlSimpleUtil.selectBeanList(Get_USERVIP, UserVip.class);
	}
}
