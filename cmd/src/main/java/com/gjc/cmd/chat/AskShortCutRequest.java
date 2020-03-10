package com.gjc.cmd.chat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskShortCutRequest extends Request
{
	public int infoIndex;
	public AskShortCutRequest() 
	{
		this.msgType= MsgTypeEnum.Chat_快捷回复.getType();
	}
	public AskShortCutRequest(int infoIndex) 
	{
		this.msgType= MsgTypeEnum.Chat_快捷回复.getType();
		this.infoIndex=infoIndex;
	}
	
	@Override
	public String toString() 
	{
		return "AskShortCutRequest  快捷回复[消息来源 " +",infoIndex"+infoIndex+ "]";
	}
}
