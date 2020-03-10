package com.yt.cmd.antiaddiction;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AntiaddictionNoticeRequest extends Request
{
	public int userId;
	public int antiAddiction;
	public AntiaddictionNoticeRequest()
	{
		msgType = MsgTypeEnum.COMMUNICATION_AUTH_实名验证.getType();
	}
	
	public AntiaddictionNoticeRequest(int userId,int antiAddiction)
	{
		this.userId = userId;
		this.antiAddiction = antiAddiction;
		msgType = MsgTypeEnum.COMMUNICATION_AUTH_实名验证.getType();
	}
}