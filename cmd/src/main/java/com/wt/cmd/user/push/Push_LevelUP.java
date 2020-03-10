package com.wt.cmd.user.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_LevelUP extends Response
{
	/**
	 * 升级后剩余经验值
	 */
	public int exp;
	
	/**
	 * 升级到等级
	 */
	public int level;
	public Push_LevelUP()
	{
		msgType = MsgTypeEnum.PUSHUSER_等级变化.getType();
	}
	
	public Push_LevelUP(int exp,int level)
	{
		msgType = MsgTypeEnum.PUSHUSER_等级变化.getType();
		this.exp = exp;
		this.level = level;
	}
}
