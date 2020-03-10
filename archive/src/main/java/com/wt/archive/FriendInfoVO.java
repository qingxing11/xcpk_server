package com.wt.archive;

public class FriendInfoVO 
{
	public int userId;
	public String info;
	public int read;//删除/已读/未读
	
	public FriendInfoVO(int userId, String info, int read) {
		this.userId = userId;
		this.info = info;
		this.read = read;
	}

	@Override
	public String toString() {
		return "FriendInfoVO [userId=" + userId + ", info=" + info + ", read=" + read + "]";
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getRead() {
		return read;
	}

	public FriendInfoVO() {
		// TODO Auto-generated constructor stub
	}
	
}
