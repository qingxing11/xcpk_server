package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetWxPayOrderRespone extends Response
{
	public static final int ERROR_不存在的订单号 = 0;
	public static final int ERROR_下单错误 = 1;
	public static final int ERROR_插入数据库错误 = 2;
	public static final int ERROR_初始化订单信息出错 = 3;
	
	public String wxPayOrder;
	
	public GetWxPayOrderRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GetWxPayOrderRespone(int code) {
		this.msgType = MsgType.PAY_微信支付下单;
		this.code = code;
	}
	
	public GetWxPayOrderRespone(int code,String wxPayOrder) {
		this.msgType = MsgType.PAY_微信支付下单;
		this.code = code;
		this.wxPayOrder = wxPayOrder;
	}
}
