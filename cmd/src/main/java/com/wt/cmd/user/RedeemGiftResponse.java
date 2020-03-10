package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class RedeemGiftResponse extends Response
{
	public static final int ERROR_没有这个兑换码= 0;
	public static final int ERROR_兑换码已被使用= 1;
	public static final int ERROR_数据库更新状态失败= 2;
	
	public int msgType;
	public int code;
	
	public String item_type;
	public String item_num;
	
	public int land_id;
	public RedeemGiftResponse() {
	}

	/**
	 * @param code 返回码
	 */
	public RedeemGiftResponse(int code) {
		this.msgType = MsgType.USER_使用激活码;
		this.code = code;
	}
	
	public RedeemGiftResponse(int code,String item_type,String item_num,int land_id) {
		this.msgType = MsgType.USER_使用激活码;
		this.code = code;
		this.item_type = item_type;
		this.item_num = item_num;
		this.land_id = land_id;
	}
}
