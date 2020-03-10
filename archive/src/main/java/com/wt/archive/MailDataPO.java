package com.wt.archive;

import java.io.Serializable;

public class MailDataPO implements Serializable
{
	public static final int STATE_未读=0;
	public static final int STATE_已读=1;
	
	public static final int ATTACHSTATE_无附件=0;
	public static final int ATTACHSTATE_未领取=1;	
	public static final int ATTACHSTATE_已领取=2;
	
	/**
	 * 邮件id
	 */
	private int mailID;
	
	private int userId;
	
	/**
	 * 发件时间
	 */
	private String sendtime;

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
	 * */
	private String attachContent="";
	
	/**
	 * 邮件状态
	 */
	private int state;
	
	/**
	 * 附件状态 0无附件，1未领取，2已领取
	 */
	private int attachState;
	
	
	public MailDataPO(int mailID,int userId,  String sendtime, String title, String content, String attachContent, int state,int attachState) {
		this.mailID = mailID;
		this.userId=userId;
		this.sendtime = sendtime;
		this.title = title;
		this.content = content;
		this.attachContent = attachContent;
		this.state = state;
		this.attachState = attachState;
	}

	public int getMailID() {
		return mailID;
	}

	public void seteMailID(int mailID) {
		this.mailID = mailID;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getSendtime() {
		return sendtime;
	}

	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
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
