package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetShareUrlResponse extends Response {
	public String shareUrl;
	
	public GetShareUrlResponse() {
		this.msgType = MsgType.SHARE_获取推广链接;
	}

	public GetShareUrlResponse(int code) {
		this.msgType = MsgType.SHARE_获取推广链接;
		this.code = code;
	}
	
	public GetShareUrlResponse(int code,String shareUrl) {
		this.msgType = MsgType.SHARE_获取推广链接;
		this.code = code;
		this.shareUrl = shareUrl; 
	}
}