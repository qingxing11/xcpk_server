package com.yt.cmd.task;


import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.yt.xcpk.data.TaskInfo;

public class PushPlayerTaskInfoResponse extends Response {
	
	
	public TaskInfo taskInfo;
	public PushPlayerTaskInfoResponse(int code,TaskInfo taskInfo) {
		this.msgType = MsgTypeEnum.Push_TaskInfo.getType();
		this.taskInfo=taskInfo;
		this.code=code;
	}

	@Override
	public String toString() {
		return "PushPlayerTaskInfoResponse [taskInfo=" + taskInfo + ", msgType=" + msgType + ", code=" + code + "]";
	}	
}
