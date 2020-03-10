package com.yt.xcpk.data;

import java.io.Serializable;

public class TaskDetailedInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9133726107877004458L;

	public int taskId;//任务id
	public int taskBigType;//任务大类型 日常任务/终极任务
	public int taskSmallType;//任务小类型  日常任务之一/终极任务之一
	public int currentValue;//任务当前达到的值
	public int completeValue;//任务完成的值
	public boolean isGetReward;//是否可以领奖励
	public boolean  isLingQu;//是否已经领取过奖励 
	public TaskDetailedInfo( int taskId,int taskBigType,int taskSmallType,int currentValue,int complateValue,boolean isGetReward,boolean isLingQu) {
		this.taskId=taskId;
		this.taskBigType=taskBigType;
		this.taskSmallType=taskSmallType;
		this.currentValue=currentValue;
		this.completeValue=complateValue;
		this.isGetReward=isGetReward;
		this.isLingQu=isLingQu;
	}
	
	
	public void SetCurrentValue(int currentValue)
	{
		this.currentValue=currentValue;
	}
	
	public boolean isGetLingQuState()
	{
		return isLingQu;	
	}
	
	//任务进行的时候   判断任务是否已经完成  
	public boolean isJudgyTaskAlreadyComplete()
	{		
		return currentValue==completeValue;
	}
	
	public boolean isJudgyTaskComplete()
	{	
		if(currentValue==completeValue)
		{
			isGetReward=true;
			return true;
		}
		else {
			isGetReward=false;
			return false;
		}
	}
	@Override
	public String toString() {
		return "TaskDetailedInfo [taskId=" + taskId + ", taskBigType=" + taskBigType + ", taskSmallType="
				+ taskSmallType + ", currentValue=" + currentValue + ", completeValue=" + completeValue
				+ ", isGetReward=" + isGetReward + "]";
	}
}
