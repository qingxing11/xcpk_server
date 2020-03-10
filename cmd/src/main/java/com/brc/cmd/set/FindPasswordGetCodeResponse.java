package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 绑定手机号
 */
public class FindPasswordGetCodeResponse extends Response 
{
	public static final int ERROR_还未完善账号 = 0;
	public static final int ERROR_还未绑定手机 = 1;
	public static final int ERROR_获取验证码失败 = 2;
	
	 public  FindPasswordGetCodeResponse()
	 {
		 msgType = MsgTypeEnum.SET_找回密码获取验证码.getType();
	}
	
	 public  FindPasswordGetCodeResponse(int code)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.SET_找回密码获取验证码.getType();
	}

	@Override
	public String toString()
	{
		return "FindPasswordGetCodeResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}
