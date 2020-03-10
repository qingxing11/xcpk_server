package com.wt.xcpk.account;

public interface AccountService
{
	/**
	 * 获得并发送验证码
	 * @param userId
	 * @param phoneNum
	 * @return
	 */
	int getBindPhoneCode(int userId,String phoneNum);
	
	/**
	 * 验证绑定手机验证码
	 * @param userId
	 * @param code
	 * @return
	 */
	boolean validateBindPhoneCode(int userId,int code);
	
	/**
	 * 获取找回密码手机验证码
	 * @param userId
	 * @return
	 */
	int getFindPasswordCode(int userId,String phoneNum);
	
	/**
	 * 验证找回密码code
	 * @param userId
	 * @param code
	 * @return
	 */
	boolean validateFindPasswordCode(int userId,int code);
}
