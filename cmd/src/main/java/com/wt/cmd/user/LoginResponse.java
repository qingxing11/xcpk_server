package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.util.security.token.TokenVO;
 
public class LoginResponse extends Response {
	public static final int ERROR_NO_USER = 100;
	public static final int ERROR_BANNED = 101;
	public static final int ERROR_WRONG_PWD = 102;
	public static final int ERROR_ALREADY_ONLINE = 103;// 重复登录
	public static final int ERROR_证书错误 = 104;
	public static final int ERROR_版本过低需要更新 = 105;
	public static final int ERROR_服务器重启维护 = 106;
	
	public TokenVO tokenVO;
	
	public int antiAddictionCode;
	public LoginResponse() {
	}

	public LoginResponse(int code) {
		this.msgType = MsgType.USER_LOGIN;
		this.code = code;
	}
	
	public LoginResponse(int code,TokenVO tokenVO,int antiAddictionCode) {
		this.msgType = MsgType.USER_LOGIN;
		this.code = code;
		this.tokenVO = tokenVO;
		this.antiAddictionCode = antiAddictionCode;
	}

	@Override
	public String toString()
	{
		return "LoginResponse [tokenVO=" + tokenVO + ", msgType=" + msgType + ", code=" + code + "]";
	}
}