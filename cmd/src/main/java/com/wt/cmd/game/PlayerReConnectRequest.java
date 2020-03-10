package com.wt.cmd.game;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.util.security.token.TokenVO;
 
public class PlayerReConnectRequest extends Request
{
	public TokenVO tokenVO;
	
	/**
	 * 切出还是切入,false 切出 true  切入
	 */
	public boolean isPing;
	public PlayerReConnectRequest()
	{
		msgType = MsgTypeEnum.GAME_客户端要求断线重连.getType();
	}
}