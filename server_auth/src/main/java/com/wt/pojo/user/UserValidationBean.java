package com.wt.pojo.user;

import com.wt.naval.dao.model.antiaddiction.UserAntiAddictionModel;
import com.wt.naval.dao.model.user.UserInfoModel;

public class UserValidationBean
{
	public long createTime;
	public String pwd;
	public UserInfoModel userInfoModel;
	public UserAntiAddictionModel userAntiAddictionModel;
	
	public UserValidationBean(long createTime, String pwd, UserInfoModel userInfoModel)
	{
		super();
		this.createTime = createTime;
		this.pwd = pwd;
		this.userInfoModel = userInfoModel;
	}
	
	public void setUserAntiAddictionMode(UserAntiAddictionModel userAntiAddictionModel)
	{
		this.userAntiAddictionModel = userAntiAddictionModel;
	}

	public void setAntiAddictionCode(int isAdult)
	{
		userInfoModel.setAntiAddiction(isAdult);
	}
	
	public int getAntiAddictionCode()
	{
		return userInfoModel.getAntiAddiction();
	}
}
