package com.yt.cmd.antiaddiction;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushUser_AntiaddictionResponse extends Response
{
	public static final int TYPE_1小时 = 1;
	public static final int TYPE_2小时 = 2;
	public static final int TYPE_3小时 = 3;
	public static final int TYPE_4小时 =4;
	public static final int TYPE_4个半小时 =5;
	public static final int TYPE_5小时 = 6;
	
	public int type;
	
	public PushUser_AntiaddictionResponse()
	{
		this.code = SUCCESS;;
		msgType = MsgTypeEnum.AntiAddictionTips.getType();
	}
	
	public PushUser_AntiaddictionResponse(int type)
	{
		msgType = MsgTypeEnum.AntiAddictionTips.getType();
		this.code = SUCCESS;;
		this.type = type;
	}
}
