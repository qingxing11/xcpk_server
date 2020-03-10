package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;
import com.wt.util.security.token.TokenVO;

public class GServerValidationLoginRequest extends Request
{
	public TokenVO tokenVO;
	public GServerValidationLoginRequest()
	{
		msgType = MsgType.USER_VALIDATION_TOKEN;
	}
	
	public GServerValidationLoginRequest(TokenVO tokenVO)
	{
		msgType = MsgType.USER_VALIDATION_TOKEN;
		this.tokenVO = tokenVO;
	}
}
