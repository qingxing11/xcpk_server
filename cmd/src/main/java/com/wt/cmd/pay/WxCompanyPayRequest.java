package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class WxCompanyPayRequest  extends Request
{
	/**
	 * 玩家真实姓名
	 */
	public String re_user_name;
	
	public int amount;
	public WxCompanyPayRequest()
	{
		msgType = MsgType.PAY_提现;
	}
	
	public WxCompanyPayRequest(String re_user_name,int amount)
	{
		msgType = MsgType.PAY_提现;
		this.re_user_name = re_user_name;
		this.amount = amount;
	}
}