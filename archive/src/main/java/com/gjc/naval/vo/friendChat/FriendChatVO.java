package com.gjc.naval.vo.friendChat;

import java.io.Serializable;

public class FriendChatVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6769649440135494494L;
	public int userId;// 发送方
	public String info;// 消息
	public int receiveUserId;// 接收方
	public boolean read;// 已读未读
	
	public FriendChatVO() {
		
	}

	public FriendChatVO(int userId, String info, int receiveUserId, boolean read) {
		this.userId = userId;
		this.info = info;
		this.receiveUserId = receiveUserId;
		this.read = read;
	}

	@Override
	public String toString() {
		return "FriendChatVO [userId=" + userId + ", info=" + info + ", receiveUserId=" + receiveUserId + ", read="
				+ read + "]";
	}
	
	
}
