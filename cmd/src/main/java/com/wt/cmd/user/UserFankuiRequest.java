package com.wt.cmd.user;

import com.wt.cmd.Request;

/**
 * 玩家登陆，登录时如果离线时的比赛还没结束，会收到一个包含比赛数据的推送
 */
public class UserFankuiRequest extends Request {
	
	/**
	 * 授权微信登陆后获取到的code
	 */
	public String fankuiStr;
	public UserFankuiRequest() {
		this.msgType = USER_反馈;
	}

	public UserFankuiRequest(String fankuiStr) {
		this.msgType = USER_反馈;
		this.fankuiStr = fankuiStr;
	}
}
