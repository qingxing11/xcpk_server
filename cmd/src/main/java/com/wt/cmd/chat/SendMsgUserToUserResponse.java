package com.wt.cmd.chat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class SendMsgUserToUserResponse extends Response
{
	public static final int ERROR_玩家离线 = 0;//发送消息时玩家已离线
	
	public int msgType;
	public int code;

	/**
	 * 对方id
	 */
	public int toUserId;
	/**
	 * 对方名字
	 */
	public String toNickName;
	public SendMsgUserToUserResponse()
	{}

	/**
	 * @param code
	 *                返回码
	 */
	public SendMsgUserToUserResponse(int code)
	{
		this.msgType = MsgType.CHAT_玩家私聊;
		this.code = code;
	}
	
	public SendMsgUserToUserResponse(int code,int toUserId,String toNickName)
	{
		this.msgType = MsgType.CHAT_玩家私聊;
		this.code = code;
		
		this.toUserId = toUserId;
		this.toNickName = toNickName;
	}
}