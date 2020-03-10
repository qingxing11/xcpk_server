package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class OnLineRewardRequest extends Request {

	public int timeId;
	public OnLineRewardRequest() {
		this.msgType=MsgTypeEnum.RequestLingQuOnLineReward.getType();
	}
	public OnLineRewardRequest(int timeId)
	{
		this.msgType=MsgTypeEnum.RequestLingQuOnLineReward.getType();
		this.timeId=timeId;
	}
	@Override
	public String toString() {
		return "OnLineRewardRequest [timeId=" + timeId + ", msgType=" + msgType + "]";
	}	
}
