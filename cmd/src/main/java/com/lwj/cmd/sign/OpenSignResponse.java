package com.lwj.cmd.sign;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class OpenSignResponse extends Response
{
	public int SignDays;
	public boolean IsClick;
	public String AwardsList; 
	
	public OpenSignResponse() {
		msgType = MsgType.GAME_打开签到;
	}
	
	public OpenSignResponse(int code) {
		msgType = MsgType.GAME_打开签到;
		this.code = code;
	}
	public OpenSignResponse(int code,int signDays,boolean isClick,String awardsList)
	{
		msgType = MsgType.GAME_打开签到;
		this.code = code;
		this.SignDays = signDays;
		this.IsClick = isClick;
		this.AwardsList = awardsList;
	}
}
