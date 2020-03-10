package com.wt.cmd.notice;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class NoticePush_userHorn extends Response
{
	public int msgType;
	
	/**
	 * 喊话者姓名
	 */
	public String nickName;
	
	/**
	 * 喊话内容
	 */
	public String txt;

	/**
	 * 喊话者id
	 */
	public int userId;
	public NoticePush_userHorn()
	{
		msgType = MsgType.NOTICE_玩家喊话;
	}
	
	public NoticePush_userHorn(String name,String txt,int userId)
	{
		msgType = MsgType.NOTICE_玩家喊话;
		this.nickName = name;
		this.txt = txt;
		this.userId = userId;
	}
}
