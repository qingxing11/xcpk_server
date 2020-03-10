package com.yt.cmd.task;


import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.yt.xcpk.data.TaskDetailedInfo;

public class PushPlayerTaskEveryTimeCompleteResponse extends Response {

	//推送當前完成的任務信息
	public TaskDetailedInfo taskDetailInfo;

	public PushPlayerTaskEveryTimeCompleteResponse(int code,TaskDetailedInfo taskDetailInfo) {
		this.msgType = MsgTypeEnum.Push_TaskEveryTimeComplete.getType();
		this.code=code;
		this.taskDetailInfo=taskDetailInfo;
	}

	@Override
	public String toString() {
		return "PushPlayerTaskEveryTimeCompleteResponse [taskDetailInfo=" + taskDetailInfo + ", msgType=" + msgType
				+ ", code=" + code + "]";
	}	
}
