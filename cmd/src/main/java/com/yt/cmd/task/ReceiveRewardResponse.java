package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.yt.xcpk.data.TaskDetailedInfo;

public class ReceiveRewardResponse extends Response {
	
	public static final int 当前任务不存在=0;
	public static final int 当前任务已领取奖励=1;
	public static final int 当前任务未到领取条件=2;
	public TaskDetailedInfo taskDetailedInfo;
	public ReceiveRewardResponse(int code) {
		this.msgType = MsgTypeEnum.ReceiveReward.getType();
		this.code=code;
	}
	
	public ReceiveRewardResponse(int code,TaskDetailedInfo taskDetailedInfo) {
		this.msgType = MsgTypeEnum.ReceiveReward.getType();
		this.code=code;
		this.taskDetailedInfo=taskDetailedInfo;
	}

	@Override
	public String toString() {
		return "ReceiveRewardResponse [taskDetailedInfo=" + taskDetailedInfo + ", msgType=" + msgType + ", code=" + code
				+ "]";
	}
	
}
