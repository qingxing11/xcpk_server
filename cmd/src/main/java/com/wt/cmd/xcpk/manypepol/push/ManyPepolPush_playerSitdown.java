package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class ManyPepolPush_playerSitdown extends Response
{
	public PlayerSimpleData playerSimpleData;

	public ManyPepolPush_playerSitdown()
	{
		msgType = MsgTypeEnum.manypepol_有玩家上桌.getType();
	}

	public ManyPepolPush_playerSitdown(PlayerSimpleData playerSimpleData)
	{
		this.playerSimpleData = playerSimpleData;
		msgType = MsgTypeEnum.manypepol_有玩家上桌.getType();
	}

	@Override
	public String toString() {
		return "ManyPepolPush_playerSitdown [playerSimpleData=" + playerSimpleData + ", msgType=" + msgType + ", code="
				+ code + "]";
	}
}
