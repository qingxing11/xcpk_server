package com.wt.cmd.ggl;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.GGLLotteryChessVO;

public class GglBuyOnceRespone extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_购买场次错误 = 1;
	
	public ArrayList<Integer> list_luckyChess;
	public ArrayList<GGLLotteryChessVO> list_myChess;
	
	public int money;
	
	public int costMoney;
	public GglBuyOnceRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GglBuyOnceRespone(int code) {
		this.msgType = MsgTypeEnum.GGL_单次购买.getType();
		this.code = code;
	}
	
	public GglBuyOnceRespone(int code,ArrayList<Integer> list_luckyChess,ArrayList<GGLLotteryChessVO> list_myChess,int money,int costMoney) {
		this.msgType = MsgTypeEnum.GGL_单次购买.getType();
		this.code = code;
		
		this.list_luckyChess = list_luckyChess;
		this.list_myChess = list_myChess;
		this.money = money;
		this.costMoney = costMoney;
	}

	@Override
	public String toString()
	{
		return "GglBuyOnceRespone [list_luckyChess=" + list_luckyChess + ", list_myChess=" + list_myChess + ", money=" + money + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
