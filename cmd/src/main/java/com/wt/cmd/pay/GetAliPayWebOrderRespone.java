package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetAliPayWebOrderRespone extends Response
{
	public static final int ERROR_不存在的订单号 = 0;
	public static final int ERROR_插入数据库错误 = 1;
	public static final int ERROR_初始化订单信息出错 = 2;
	
	public String payOrder;
	
	public GetAliPayWebOrderRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GetAliPayWebOrderRespone(int code) {
		this.msgType = MsgType.PAY_支付宝网页支付下单;
		this.code = code;
	}
	
	public GetAliPayWebOrderRespone(int code,String payOrder) {
		this.msgType = MsgType.PAY_支付宝网页支付下单;
		this.code = code;
		this.payOrder = payOrder;
	}
}
