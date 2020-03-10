package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ReceiveRewardRequest extends Request {

	public int taskId;
	public ReceiveRewardRequest() {
		this.msgType = MsgTypeEnum.ReceiveReward.getType();
	}
	
	public ReceiveRewardRequest(int taskId) {
		this.msgType = MsgTypeEnum.ReceiveReward.getType();
		this.taskId=taskId;
	}

	@Override
	public String toString() {
		return "ReceiveRewardRequest [taskId=" + taskId + ", msgType=" + msgType + "]";
	}	
}
