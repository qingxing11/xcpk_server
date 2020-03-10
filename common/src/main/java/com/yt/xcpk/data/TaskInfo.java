package com.yt.xcpk.data;

import java.io.Serializable;
import java.util.ArrayList;;

public class TaskInfo implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8671899392098623231L;

	public int userId;
	public ArrayList<TaskDetailedInfo> list_dayTaskInfo;

	public ArrayList<TaskDetailedInfo> list_personSelfTaskInfo;

	public ArrayList<TaskDetailedInfo> list_systemTaskInfo;
	
	public ArrayList<Integer> list_onLineReward;
	
	public int freeChouJiang;
	public ArrayList<TaskDetailedInfo> getDayTaskInfo() {

		return this.list_dayTaskInfo;
	}

	public ArrayList<TaskDetailedInfo> getPersonSelfTaskInfo() {
		return this.list_personSelfTaskInfo;
	}

	public ArrayList<TaskDetailedInfo> getSystemTaskInfo() {
		return this.list_systemTaskInfo;
	}
	
	public ArrayList<Integer> getOnLineChouJiangInfo()
	{
		return this.list_onLineReward;
	}
	public int getFreeChouJiang()
	{
		return this.freeChouJiang;
	}
	public int getCurrentTaskValue(int taskType, int smallTaskType) {
		int currentValue = 0;
		if (taskType == 1) {
			for (TaskDetailedInfo taskInfo : list_dayTaskInfo) {
				if (taskInfo.taskSmallType == smallTaskType) {
					currentValue = taskInfo.currentValue;
				}
			}
		} else {
			for (TaskDetailedInfo taskInfo : list_personSelfTaskInfo) {
				if (taskInfo.taskSmallType == smallTaskType) {
					currentValue = taskInfo.currentValue;
				}
			}
		}
		return currentValue;
	}

	@Override
	public String toString() {
		return "TaskInfo [userId=" + userId + ", list_dayTaskInfo=" + list_dayTaskInfo + ", list_personSelfTaskInfo="
				+ list_personSelfTaskInfo + ", list_systemTaskInfo=" + list_systemTaskInfo + "]";
	}
}
