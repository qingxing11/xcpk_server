package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class ManyPepolPush_otherPlayerApplicationBanker extends Response
{
	public PlayerSimpleData bankerRequest;
	public ManyPepolPush_otherPlayerApplicationBanker()
	{
		this.msgType = MsgTypeEnum.manypepol_其他玩家上庄.getType();
	}
	
	public ManyPepolPush_otherPlayerApplicationBanker(PlayerSimpleData bankerRequest)
	{
		this.msgType = MsgTypeEnum.manypepol_其他玩家上庄.getType();
		this.bankerRequest = bankerRequest;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_otherPlayerApplicationBanker [bankerRequest=" + bankerRequest + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
