package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetShareGameUrlResponse extends Response {
	public static final int ERROR_不在游戏中 = 0;
	
	public String shareUrl;
	
	public GetShareGameUrlResponse() {
		this.msgType = MsgType.SHARE_获取邀请游戏链接;
	}

	public GetShareGameUrlResponse(int code) {
		this.msgType = MsgType.SHARE_获取邀请游戏链接;
		this.code = code;
	}
	
	public GetShareGameUrlResponse(int code,String shareUrl) {
		this.msgType = MsgType.SHARE_获取邀请游戏链接;
		this.code = code;
		this.shareUrl = shareUrl; 
	}
}