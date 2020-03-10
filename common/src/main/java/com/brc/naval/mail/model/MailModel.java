package com.brc.naval.mail.model;

public class MailModel {
	/**
	 * 邮件id
	 */
	private int mailId;
	
	/**
	 * 玩家的userId
	 * */
	private int userId;
	
	/**
	 * 发件时间
	 */
	private String sendTime;	
	
	/**
	 * 标题
	 */
	private String title = "";
	
	/**
	 * 内容
	 */
	private String content = "";
	
	/**
	 * 附件内容
	 */
	private String attachContent="";
	
	/**
	 * 邮件状态  0未读取 1已读取
	 */
	private int state;
	
	/**
	 * 附件状态 0无附件，1由附件
	 */
	private int attachState;

	public int getMailId() {
		return mailId;
	}

	public void setMailId(int mailId) {
		this.mailId = mailId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachContent() {
		return attachContent;
	}

	public void setAttachContent(String attachContent) {
		this.attachContent = attachContent;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getAttachState() {
		return attachState;
	}

	public void setAttachState(int attachState) {
		this.attachState = attachState;
	}
}
