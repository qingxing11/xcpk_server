package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 获取验证码
 */
public class BindPhoneGetCodeResponse extends Response 
{
	public static final int ERROR_已绑定手机号 = 0;
	public static final int ERROR_获取验证码失败 = 1;
	public static final int ERROR_未完善账号 = 2;
	
	public BindPhoneGetCodeResponse(int code)
	{
		this.code=code;
		msgType = MsgTypeEnum.BINDPHONE_GETCODE.getType();
	}
}