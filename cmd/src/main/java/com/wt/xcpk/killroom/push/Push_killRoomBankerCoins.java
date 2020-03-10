package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomBankerCoins extends Response
{
	public int userId;
	public long coins;
	public Push_killRoomBankerCoins()
	{
		this.msgType = MsgTypeEnum.KillRoom_同步庄家金币.getType();
	}
	
	public Push_killRoomBankerCoins(int userId,long coins)
	{
		this.msgType = MsgTypeEnum.KillRoom_同步庄家金币.getType();
		this.coins = coins;
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "Push_killRoomBankerCoins [userId=" + userId + ", coins=" + coins + ", msgType=" + msgType + "]";
	}
}
