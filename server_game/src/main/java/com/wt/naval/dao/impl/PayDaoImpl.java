package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;

public class PayDaoImpl
{
	private static final String INSERT_PAY_LOG = "INSERT INTO `user_pay_log`(outTradeNo,payPlatform,totalFee,userId) VALUES(?,?,?,?)";
	public static Integer insertPayLog(String outTradeNo,String payPlatform,String totalFee,int userId)
	{
		return SqlSimpleUtil.insert(INSERT_PAY_LOG, outTradeNo, payPlatform,totalFee,userId);
	}
}
