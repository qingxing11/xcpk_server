package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 绑定手机号
 */
public class BindPhoneResponse extends Response 
{
	public static final int ERROR_验证码错误 = 0;
	 public  BindPhoneResponse(int code)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.BINDPHONE.getType();
	}
}
