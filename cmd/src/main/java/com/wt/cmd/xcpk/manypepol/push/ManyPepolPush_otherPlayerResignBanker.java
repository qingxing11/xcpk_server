package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_otherPlayerResignBanker extends Response
{
	public int userId;
	public ManyPepolPush_otherPlayerResignBanker()
	{
		this.msgType = MsgTypeEnum.manypepol_其他玩家下庄.getType();
	}
	
	public ManyPepolPush_otherPlayerResignBanker(int userId)
	{
		this.msgType = MsgTypeEnum.manypepol_其他玩家下庄.getType();
		this.userId = userId;
	}
}