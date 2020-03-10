package com.gjc.cmd.chat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskShortCutResponse extends Response
{
	public static final int Error_暂无聊天功能 = 0;

	public int userId;// 消息来源Id
	public String nikeName;
	public int vipLv;
	public String info;

	public AskShortCutResponse()
	{
		this.msgType = MsgTypeEnum.Chat_快捷回复.getType();
	}

	public AskShortCutResponse(int code)
	{
		this.msgType = MsgTypeEnum.Chat_快捷回复.getType();
		this.code = code;
	}

	public AskShortCutResponse(int code, int userId, String nikeName, int vipLv, String info)
	{
		this.msgType = MsgTypeEnum.Chat_快捷回复.getType();
		this.code = code;
		this.nikeName = nikeName;
		this.vipLv = vipLv;
		this.info = info;
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "PushShortCutResponse  快捷回复[消息来源 id=" + userId + ",info=" + info + ", nikeName=" + nikeName + ",vipLv=" + vipLv + "]";
	}
}
