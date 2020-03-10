package model;

import java.io.Serializable;


public class UserTask implements Serializable {
	private static final long serialVersionUID = 1L;

	private byte[] dayTask;

	private int freeChouJiang;

	private byte[] onLineTask;

	private byte[] personSelfTask;

	private byte[] systemTask;

	private int userId;

	public UserTask() {
	}

	public byte[] getDayTask() {
		return this.dayTask;
	}

	public void setDayTask(byte[] dayTask) {
		this.dayTask = dayTask;
	}

	public int getFreeChouJiang() {
		return this.freeChouJiang;
	}

	public void setFreeChouJiang(int freeChouJiang) {
		this.freeChouJiang = freeChouJiang;
	}

	public byte[] getOnLineTask() {
		return this.onLineTask;
	}

	public void setOnLineTask(byte[] onLineTask) {
		this.onLineTask = onLineTask;
	}

	public byte[] getPersonSelfTask() {
		return this.personSelfTask;
	}

	public void setPersonSelfTask(byte[] personSelfTask) {
		this.personSelfTask = personSelfTask;
	}

	public byte[] getSystemTask() {
		return this.systemTask;
	}

	public void setSystemTask(byte[] systemTask) {
		this.systemTask = systemTask;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}