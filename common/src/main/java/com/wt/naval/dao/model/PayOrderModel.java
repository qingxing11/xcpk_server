package com.wt.naval.dao.model;

public class PayOrderModel
{
	public static transient final String PAY_TRAGET_ALI = "Ali";
	public static transient final String PAY_TRAGET_WX = "WX";
	
	public int order_id;//计费点id,对应config_pay_order表中的id
	public String out_trade_no;//唯一订单号
	public int user_id;//发起者uid,
	public String total_fee;//订单金额
 	public int order_complete;//订单是否完成，0：未完成，1：完成
 	public String prepay_id;//预支付交易会话ID
	public String nick_name;//微信昵称
	public String pay_traget;//支付平台，WX&Ali
	public PayOrderModel(){}
	
	public PayOrderModel(int order_id, String out_trade_no, int user_id, String total_fee,String prepay_id, String nick_name,String pay_traget)
	{
		this.order_id = order_id;
		this.out_trade_no = out_trade_no;
		this.user_id = user_id;
		this.total_fee = total_fee;
		this.nick_name = nick_name;
		this.prepay_id = prepay_id;
		this.pay_traget = pay_traget;
//		System.out.println("order_id:"+order_id+",out_trade_no:"+out_trade_no+",user_id:"+user_id+",total_fee:"+total_fee+",nick_name:"+nick_name+",sign:"+sign+",prepay_id:"+prepay_id);
	}
	
	public PayOrderModel(int order_id, String out_trade_no, int user_id,String total_fee, String nick_name,String pay_traget)
	{
		this.order_id = order_id;
		this.out_trade_no = out_trade_no;
		this.user_id = user_id;
		this.total_fee = total_fee;
		this.nick_name = nick_name;
		this.pay_traget = pay_traget;
		this.prepay_id = "";
//		System.out.println("order_id:"+order_id+",out_trade_no:"+out_trade_no+",user_id:"+user_id+",total_fee:"+total_fee+",nick_name:"+nick_name+",sign:"+sign+",prepay_id:"+prepay_id);
	}

	public PayOrderModel(String out_trade_no, int order_complete)
	{
		this.out_trade_no = out_trade_no;
		this.order_complete = order_complete;
	}
	
	@Override
	public String toString()
	{
		return "order_id:"+order_id+",out_trade_no:"+out_trade_no+",user_id:"+user_id+",total_fee:"+total_fee+",nick_name:"+nick_name+",prepay_id:"+prepay_id;
	}
}
