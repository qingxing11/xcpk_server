package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威 绑定手机号
 */
public class FindPasswordGetCodeRequest extends Request
{
	public FindPasswordGetCodeRequest()
	{
		msgType = MsgTypeEnum.SET_找回密码获取验证码.getType();
	}

	@Override
	public String toString()
	{
		return "FindPasswordGetCodeRequest [msgType=" + msgType + "]";
	}
}
