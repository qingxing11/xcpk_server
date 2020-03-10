package com.wt.cmd.user;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class GetPlayerNicknameRequest extends Request
{
	public ArrayList<Integer> list_playerIds;

	public GetPlayerNicknameRequest()
	{
		msgType = MsgTypeEnum.GAME_GETPLAYERNICKNAMES.getType();
	}

	public GetPlayerNicknameRequest(ArrayList<Integer> list_playerIds)
	{
		msgType = MsgTypeEnum.GAME_GETPLAYERNICKNAMES.getType();
		this.list_playerIds = list_playerIds;
	}

}
