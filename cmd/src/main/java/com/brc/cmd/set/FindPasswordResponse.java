package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 绑定手机号
 */
public class FindPasswordResponse extends Response 
{
	public static final int ERROR_账号密码错误 = 0;
	public static final int ERROR_验证码错误 = 1;
	
	public String password;
	public int userId;
	
	 public  FindPasswordResponse()
	 {
		 msgType = MsgTypeEnum.SET_找回密码.getType();
	}
	
	 public  FindPasswordResponse(int code,int userId)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.SET_找回密码.getType();
		 this.userId = userId;
	}
	 
	 public  FindPasswordResponse(int code)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.SET_找回密码.getType();
	}

	public FindPasswordResponse(int code, String password, int userId)
	{
		 this.code=code;
		 msgType = MsgTypeEnum.SET_找回密码.getType();
		 this.password = password;
		 this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "FindPasswordResponse [password=" + password + ", userId=" + userId + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
