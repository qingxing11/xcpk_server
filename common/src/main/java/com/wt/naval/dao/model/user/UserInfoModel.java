package com.wt.naval.dao.model.user;

import java.sql.Timestamp;


/**
 * The persistent class for the user database table.
 * 
 */
public class UserInfoModel {

	private int userId;

	private String account;

	private String nickName;
	 
	private String email;

	private String imei;

	private Timestamp lastLoginTime;

	private String password;

	private String regPhoneNum;

	private Timestamp regTime;

	private String unionId;

	private int antiAddiction;
	
	private Timestamp lastLogoutTime;
	
	public UserInfoModel() {
	}
	
	public Timestamp getLastLogoutTime()
	{
		return lastLogoutTime;
	}

	public void setLastLogoutTime(Timestamp lastLoginOutTime)
	{
		this.lastLogoutTime = lastLoginOutTime;
	}

	public int getAntiAddiction()
	{
		return antiAddiction;
	}

	public void setAntiAddiction(int antiAddiction)
	{
		this.antiAddiction = antiAddiction;
	}
	
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRegPhoneNum() {
		return this.regPhoneNum;
	}

	public void setRegPhoneNum(String regPhoneNum) {
		this.regPhoneNum = regPhoneNum;
	}

	public Timestamp getRegTime() {
		return this.regTime;
	}

	public void setRegTime(Timestamp regTime) {
		this.regTime = regTime;
	}

	public String getUnionId() {
		return this.unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	@Override
	public String toString()
	{
		return "UserInfoModel [userId=" + userId + ", account=" + account + ", nickName=" + nickName + ", email=" + email + ", imei=" + imei + ", lastLoginTime=" + lastLoginTime + ", password=" + password + ", regPhoneNum=" + regPhoneNum + ", regTime=" + regTime + ", unionId=" + unionId + ", antiAddiction=" + antiAddiction + ", lastLogoutTime=" + lastLogoutTime + "]";
	}
}