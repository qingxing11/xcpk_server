package com.wt.cmd.user;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

 
public class UpdateHeadIconRequest extends Request {

	public byte[] headIcon;
	public UpdateHeadIconRequest() {
		this.msgType = MsgTypeEnum.USER_上传自定义头像.getType();
	}
}
