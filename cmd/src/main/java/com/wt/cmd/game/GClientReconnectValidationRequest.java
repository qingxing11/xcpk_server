package com.wt.cmd.game;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.util.security.token.TokenVO;

public class GClientReconnectValidationRequest extends Request
{
	public TokenVO tokenVO;
	public GClientReconnectValidationRequest()
	{
		msgType = MsgTypeEnum.GAME_游戏服断线重连验证.getType();
	}
	
	public GClientReconnectValidationRequest(TokenVO tokenVO)
	{
		msgType = MsgTypeEnum.GAME_游戏服断线重连验证.getType();
		this.tokenVO = tokenVO;
	}
}
