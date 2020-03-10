package com.wt.dao.impl;

import com.wt.db.SqlSimpleUtil;
import com.wt.model.HotfixModel;

public class ResImpl
{
	private static final String getHotfix = "SELECT * FROM `hotfix` WHERE `platform` = ?";
	public static HotfixModel getHotfixModel_andorid()
	{
		HotfixModel hotfixModel = SqlSimpleUtil.selectBean(getHotfix, HotfixModel.class,"android");
		return hotfixModel;
	}
	
	public static HotfixModel getHotfixModel_ios()
	{
		HotfixModel hotfixModel = SqlSimpleUtil.selectBean(getHotfix, HotfixModel.class,"ios");
		return hotfixModel;
	}
	
	public static HotfixModel getHotfixModel_windows()
	{
		HotfixModel hotfixModel = SqlSimpleUtil.selectBean(getHotfix, HotfixModel.class,"windows");
		return hotfixModel;
	}
}
