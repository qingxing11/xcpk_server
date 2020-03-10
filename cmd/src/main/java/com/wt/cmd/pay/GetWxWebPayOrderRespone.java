package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetWxWebPayOrderRespone extends Response
{
	public static final int ERROR_不存在的订单号 = 0;
	public static final int ERROR_下单错误 = 1;
	public static final int ERROR_插入数据库错误 = 2;
	public static final int ERROR_初始化订单信息出错 = 3;
	
	public String wxPayOrder;
	
	public GetWxWebPayOrderRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GetWxWebPayOrderRespone(int code) {
		this.msgType = MsgType.PAY_微信网页支付下单;
		this.code = code;
	}
	
	public GetWxWebPayOrderRespone(int code,String wxPayOrder) {
		this.msgType = MsgType.PAY_微信网页支付下单;
		this.code = code;
		this.wxPayOrder = wxPayOrder;
	}
}
