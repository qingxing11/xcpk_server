package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GMUtilLoginRespone extends Response
{
	public int msgType;
	public int code;
	
	public String publicKey="";
	public GMUtilLoginRespone() {
	}

	public GMUtilLoginRespone(int code) {
		this.msgType = MsgType.GM_GM工具登录;
		this.code = code;
	}
	
	public GMUtilLoginRespone(int code,String publicKey) {
		this.msgType = MsgType.GM_GM工具登录;
		this.code = code;
		
		this.publicKey = publicKey;
	}
}
