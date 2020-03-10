package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.util.security.token.TokenVO;

public class RegisterResponse extends Response {
	public static final int ERROR_账号已存在 = 0;
	
	public String account;
	public String password;
	public TokenVO tokenVO;
	public  String nickName;
	public int antiAddictionCode;
	public RegisterResponse() {
	}

	public RegisterResponse(int code) {
		this.msgType = MsgType.USER_REGISTER;
		this.code = code;
		
	}

	public RegisterResponse(int code, String account,String password,TokenVO tokenVO,String nickName, int antiAddictionCode) {
		this.msgType = MsgType.USER_REGISTER;
		this.code = code;
		this.account = account;
		this.password = password;
		this.tokenVO = tokenVO;
		this.nickName=nickName;
		this.antiAddictionCode = antiAddictionCode;
	}
}