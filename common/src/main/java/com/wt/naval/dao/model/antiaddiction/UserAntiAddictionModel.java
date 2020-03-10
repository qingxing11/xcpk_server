package com.wt.naval.dao.model.antiaddiction;

import java.sql.Timestamp;


public class UserAntiAddictionModel{

	private int userId;

	private Timestamp antiAddictionTime;

	public UserAntiAddictionModel() {
	}
	
	public UserAntiAddictionModel(int userId,Timestamp antiAddictionTime) {
		this.userId = userId;
		this.antiAddictionTime = antiAddictionTime;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Timestamp getAntiAddictionTime() {
		return this.antiAddictionTime;
	}

	public void setAntiAddictionTime(Timestamp antiAddictionTime) {
		this.antiAddictionTime = antiAddictionTime;
	}

	@Override
	public String toString()
	{
		return "UserAntiAddictionModel [userId=" + userId + ", antiAddictionTime=" + antiAddictionTime + "]";
	}

	
	
}