package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class UserPublicChatResponse extends Response
{
	public static final int ERROR_钻石不足 = 0;//冷却时间外，优先使用免费次数，否则消耗钻石
	
	public int num;
	public UserPublicChatResponse()
	{}

	/**
	 * @param code
	 *                返回码
	 */
	public UserPublicChatResponse(int code,int callbackId)
	{
		this.msgType = MsgType.CHAT_玩家喊话;
		this.code = code;
	}
	
	public UserPublicChatResponse(int code,int num,int callbackId)
	{
		this.msgType = MsgType.CHAT_玩家喊话;
		this.code = code;
		
		this.num = num;
	}
}