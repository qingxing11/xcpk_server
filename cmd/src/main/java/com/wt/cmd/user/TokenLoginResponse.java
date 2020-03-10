package com.wt.cmd.user;

import com.wt.archive.GameData;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class TokenLoginResponse extends Response {
	public static final int ERROR_TOKEN错误 = 0;
	public static final int ERROR_BANNED = 1;
	public static final int ERROR_令牌过期 = 2;
	public static final int ERROR_TOKEN空 = 3;
	
	public GameData gameData;
	
	/**
	 * 返回加密后的openid，客户端存储openid以备下次作为登录令牌 
	 */
	public String ip;
	public String port;
	public TokenLoginResponse() {
		this.msgType = MsgTypeEnum.USER_TOKEN_LOGIN.getType();
	}
	
	public TokenLoginResponse(int code) {
		this.msgType = MsgTypeEnum.USER_TOKEN_LOGIN.getType();
		this.code = code;
	}

	public TokenLoginResponse(int code,GameData gameData) {
		this.msgType = MsgTypeEnum.USER_TOKEN_LOGIN.getType();
		this.code = code;
		this.gameData = gameData;
	}
}