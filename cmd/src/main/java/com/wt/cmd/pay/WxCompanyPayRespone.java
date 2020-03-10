package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class WxCompanyPayRespone extends Response
{
	public static final int ERROR_提取金额必须大于零 = 0;
	public static final int ERROR_余额不足 = 1;
	public static final int ERROR_更新到数据库失败 = 2;
	public static final int ERROR_提现API返回错误 = 3;
	
	public String err_text;
	
	public float amount;
	public WxCompanyPayRespone() {
		this.msgType = MsgType.PAY_提现;
	}

	/**
	 * @param code 返回码
	 */
	public WxCompanyPayRespone(int code) {
		this.msgType = MsgType.PAY_提现;
		this.code = code;
	}
	
	public WxCompanyPayRespone(int code,String err_text) {
		this.msgType = MsgType.PAY_提现;
		this.code = code;
		this.err_text = err_text;
	}

	public WxCompanyPayRespone(int code, float amount)
	{
		this.msgType = MsgType.PAY_提现;
		this.code = code;
		this.amount = amount;
	}
}