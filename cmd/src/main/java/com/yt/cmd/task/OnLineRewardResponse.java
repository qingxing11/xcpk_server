package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class OnLineRewardResponse extends Response {

	public static final int 该时间段的奖励已领取=0;
	public static final int 没有找到该时间段的奖励=1;
	
	public static final int 未到领取该时间段的时间=2;
	public int timeId;
	public OnLineRewardResponse(int code) {
		this.msgType = MsgTypeEnum.RequestLingQuOnLineReward.getType();
		this.code = code;
	}
	public OnLineRewardResponse(int code,int timeId) {
		this.msgType = MsgTypeEnum.RequestLingQuOnLineReward.getType();
		this.code = code;
		this.timeId=timeId;
	}
	@Override
	public String toString() {
		return "OnLineRewardResponse [timeId=" + timeId + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
