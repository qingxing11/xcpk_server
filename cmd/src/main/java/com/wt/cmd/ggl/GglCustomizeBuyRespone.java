package com.wt.cmd.ggl;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class GglCustomizeBuyRespone extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_购买场次错误 = 1;
	
	public ArrayList<Integer> list_money;
	public int costMoney;
	public GglCustomizeBuyRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GglCustomizeBuyRespone(int code) {
		this.msgType = MsgTypeEnum.GGL_自定义购买.getType();
		this.code = code;
	}
	

	public GglCustomizeBuyRespone(int code, ArrayList<Integer> list_money,int costMoney)
	{
		this.msgType = MsgTypeEnum.GGL_自定义购买.getType();
		this.code = code;
		this.list_money = list_money;
		this.costMoney = costMoney;
	}

	@Override
	public String toString()
	{
		return "GglCustomizeBuyRespone [list_money=" + list_money + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
