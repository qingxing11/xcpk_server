package com.brc.cmd.lucky;

import java.util.ArrayList;

import com.brc.cmd.mail.Attach;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威 宝箱
 */
public class LuckyBoxResponse extends Response
{
	public static final int ERROR_未胜利 = 0;
	public static final int ERROR_不是这个宝箱 = 1;
	public int userId;
	public ArrayList<Attach> attachs;

	
	public LuckyBoxResponse(int code)
	{
		msgType = MsgTypeEnum.LUCKY_BOX.getType();
		this.code = code;
	}

	public LuckyBoxResponse(int code,int userId, ArrayList<Attach> attachs)
	{
		msgType = MsgTypeEnum.LUCKY_BOX.getType();
		this.userId = userId;
		this.code = code;
		this.attachs = attachs;
	}

	@Override
	public String toString()
	{
		return "LuckyBoxResponse [userId=" + userId + ", attachs=" + attachs + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
