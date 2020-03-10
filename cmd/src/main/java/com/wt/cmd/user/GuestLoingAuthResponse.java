package com.wt.cmd.user;


import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.util.security.token.TokenVO;

public class GuestLoingAuthResponse extends Response {
	public static final int ERROR_CODE空 = 0;
	
	public TokenVO tokenVO;
	public String device_code;
	
	public int antiAddictionCode;
	
	public long lastLogoutTime;
	public GuestLoingAuthResponse() {
	}
	
	public GuestLoingAuthResponse(int code) {
		this.code = code;
		this.msgType = MsgType.USER_游客快速登录;
	}

	public GuestLoingAuthResponse(int code, TokenVO tokenVO, String device_code,int antiAddictionCode) {
		this.msgType = MsgType.USER_游客快速登录;
		this.code = code;
		this.tokenVO = tokenVO;
		this.device_code=device_code;
		this.antiAddictionCode = antiAddictionCode;
	}

	@Override
	public String toString()
	{
		return "GuestLoingAuthResponse [tokenVO=" + tokenVO + ", device_code=" + device_code + "]";
	}
}
