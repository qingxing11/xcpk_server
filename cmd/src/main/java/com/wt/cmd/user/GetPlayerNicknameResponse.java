package com.wt.cmd.user;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class GetPlayerNicknameResponse extends Response
{
	public static final int EROOR_请求信息为空 = 1;
	public static final int EROOR_请求玩家不存在 = 2;
	public static final int EROOR_请求出错 = 3;

	public ArrayList<PlayerNicknameVO> list_playersNickname;

	public GetPlayerNicknameResponse()
	{
		msgType = MsgTypeEnum.GAME_GETPLAYERNICKNAMES.getType();
		code = SUCCESS;
	}

	public GetPlayerNicknameResponse(int code)
	{
		msgType = MsgTypeEnum.GAME_GETPLAYERNICKNAMES.getType();
		code = code;
	}

	public GetPlayerNicknameResponse(ArrayList<PlayerNicknameVO> list_playersNickname)
	{
		msgType = MsgTypeEnum.GAME_GETPLAYERNICKNAMES.getType();
		code = SUCCESS;
		this.list_playersNickname = list_playersNickname;
	}

}
