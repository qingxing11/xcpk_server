package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ChouJiangRequest extends Request {

	public int isFreeChouJiang;
	public ChouJiangRequest() {
		this.msgType=MsgTypeEnum.ChouJiangResult.getType();
	}
	public ChouJiangRequest(int isFreeChouJiang) {
		this.msgType=MsgTypeEnum.ChouJiangResult.getType();
		this.isFreeChouJiang=isFreeChouJiang;
	}
	
	@Override
	public String toString() {
		return "ChouJiangRequest [isFreeChouJiang=" + isFreeChouJiang + ", msgType=" + msgType + "]";
	}
	
}
