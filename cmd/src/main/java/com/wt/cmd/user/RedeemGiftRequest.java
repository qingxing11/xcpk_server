package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class RedeemGiftRequest extends Request
{
	/**
	 * 兑换码
	 */
	public String redeem;
	public RedeemGiftRequest()
	{}

	 /**
	  * 
	  * @param userId 
	  * @param type
	  * @param num
	  */
	public RedeemGiftRequest(String redeem)
	{
		msgType = MsgType.USER_使用激活码;
		 this.redeem = redeem;
	}
}
