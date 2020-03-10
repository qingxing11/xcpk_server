package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class GetShareGameUrlRequest extends Request {
	public GetShareGameUrlRequest() {
		this.msgType = MsgType.SHARE_获取邀请游戏链接;
	}
}