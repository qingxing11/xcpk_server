package com.wt.cmd.user;

import com.wt.cmd.Request;

/**
 * 玩家登陆，登录时如果离线时的比赛还没结束，会收到一个包含比赛数据的推送
 */
public class FastloginRequest extends Request {
	
	/**
	 * 授权微信登陆后获取到的令牌
	 */
	public String wx_openid;
	public FastloginRequest() {
		this.msgType = USER_FASTLOGIN;
	}

	public FastloginRequest(String wx_openid,int callBackId) {
		this.msgType = USER_FASTLOGIN;
		this.wx_openid = wx_openid;
		this.callBackId = callBackId;
	}
}
