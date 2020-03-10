package com.wt.cmd.util;

import com.wt.cmd.Response;

public class GetZxingResponse extends Response {

	public byte[] zxingImage;
	public String downloadZxingURL;
	public GetZxingResponse() {
		this.msgType = UTIL_获取推广二维码;
	}
	
	public GetZxingResponse(int code) {
		this.msgType = UTIL_获取推广二维码;
		this.code = code;
	}
	
	public GetZxingResponse(int code,byte[] zxingImage) {
		this.msgType = UTIL_获取推广二维码;
		this.code = code;
		this.zxingImage = zxingImage;
	}
	
	public GetZxingResponse(int code,String downloadZxingURL) {
		this.msgType = UTIL_获取推广二维码;
		this.code = code;
		this.downloadZxingURL = downloadZxingURL;
	}
}
