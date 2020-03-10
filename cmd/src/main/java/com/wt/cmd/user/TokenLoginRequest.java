package com.wt.cmd.user;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.util.security.token.TokenVO;

/**
 * 玩家登陆，登录时如果离线时的比赛还没结束，会收到一个包含比赛数据的推送
 */
public class TokenLoginRequest extends Request {
	
	/**
	 * 授权微信登陆后获取到令牌
	 */
	public TokenVO token;
	public TokenLoginRequest() {
		this.msgType = MsgTypeEnum.USER_TOKEN_LOGIN.getType();
	}

}
