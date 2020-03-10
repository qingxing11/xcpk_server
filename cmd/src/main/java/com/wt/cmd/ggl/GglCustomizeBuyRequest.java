package com.wt.cmd.ggl;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class GglCustomizeBuyRequest  extends Request
{
	public int level;
	public int num;
	public GglCustomizeBuyRequest()
	{
		msgType = MsgTypeEnum.GGL_自定义购买.getType();
	}
	@Override
	public String toString()
	{
		return "GglCustomizeBuyRequest [level=" + level + ", num=" + num + ", msgType=" + msgType + "]";
	}
}