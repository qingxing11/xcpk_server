package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威
 * 获取验证码
 */
public class BindPhoneGetCodeRequest extends Request
{
	public String phoneNumber;
	public BindPhoneGetCodeRequest()
	{
		msgType = MsgTypeEnum.BINDPHONE_GETCODE.getType();
	}
}
