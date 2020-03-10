package com.wt.cmd.user;

import com.wt.cmd.Response;

public class UserFankuiResponse extends Response {
	public UserFankuiResponse() {
		msgType = USER_反馈;
	}
	
	
	public UserFankuiResponse(int code) {
		msgType = USER_反馈;
		this.code = code;
	}

}