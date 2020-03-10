package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威 绑定手机号
 */
public class FindPasswordRequest extends Request
{
	public String password;
	public int code;
	public int userId;

	public FindPasswordRequest()
	{
		msgType = MsgTypeEnum.SET_找回密码.getType();
	}

	@Override
	public String toString()
	{
		return "FindPasswordRequest [password=" + password + ", code=" + code + ", userId=" + userId + ", msgType=" + msgType + "]";
	}
	
	
}
