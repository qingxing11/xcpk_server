package com.wt.pay;

public class AliPayOrderInfo
{
	public String out_trade_no;
	public String total_fee;
	
	public String orderInfo;

	public AliPayOrderInfo() {}
	
	public void initOrderInfo(String out_trade_no,String total_fee,String orderInfo)
	{
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.orderInfo = orderInfo;
	}

	@Override
	public String toString()
	{
		return "AliPayOrderInfo [out_trade_no=" + out_trade_no + ", total_fee=" + total_fee + ", orderInfo=" + orderInfo + "]";
	}
}
