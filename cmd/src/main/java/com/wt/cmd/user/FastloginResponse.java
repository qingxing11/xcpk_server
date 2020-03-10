package com.wt.cmd.user;

import com.wt.archive.UserData;
import com.wt.cmd.Response;

public class FastloginResponse extends Response {
	public static final int ERROR_BANNED = 0;
	public static final int ERROR_证书空 = 1;
	public static final int ERROR_版本过低需要更新 = 2;
	public static final int ERROR_服务器重启维护 = 3;
	public static final int ERROR_证书解密错误 = 4;
	public static final int ERROR_该openid不存在 = 5;
	public static final int ERROR_令牌过期 = 6;
	public static final int ERROR_需要更新版本 = 7;
	
	public UserData bean;
	public String publicKey="";
	
	
	public FastloginResponse() {
		msgType = USER_FASTLOGIN;
	}
	
	
	public FastloginResponse(int code) {
		msgType = USER_FASTLOGIN;
		this.code = code;
	}


	public FastloginResponse(int code, UserData gameData, String publicKey)
	{
		msgType = USER_FASTLOGIN;
		this.code = code;
		this.bean = gameData;
		this.publicKey = publicKey;
	}
}