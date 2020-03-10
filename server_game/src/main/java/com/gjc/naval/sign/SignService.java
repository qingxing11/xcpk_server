package com.gjc.naval.sign;

public interface SignService 
{
	 void registerPlayerInitSign(int userId,long startTime,boolean one, boolean two, boolean three, boolean four, boolean five, boolean six, boolean seven);
	
	/**登陆时初始化*/
	 void initPlaySign(int userId);
	 
	 /**签到*/
	 boolean sign(int userId,int daysIndex) ;
}
