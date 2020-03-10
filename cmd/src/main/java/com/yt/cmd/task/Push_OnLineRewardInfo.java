package com.yt.cmd.task;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_OnLineRewardInfo extends Response {

	public ArrayList<Integer> list_onLineRewardInfo;
	public Push_OnLineRewardInfo(int code) {
		this.msgType=MsgTypeEnum.Push_onLineRewardInfo.getType();
		this.code=code;
	}
	
	public Push_OnLineRewardInfo(int code,ArrayList<Integer> list_onLineRewardInfo)
	{
		this.msgType=MsgTypeEnum.Push_onLineRewardInfo.getType();
		this.code=code;
		this.list_onLineRewardInfo=list_onLineRewardInfo;
	}

	@Override
	public String toString() {
		return "Push_OnLineRewardInfo [list_onLineRewardInfo=" + list_onLineRewardInfo + ", msgType=" + msgType
				+ ", code=" + code + "]";
	}
	
	
	
}
