package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;
import com.wt.util.Tool;

import model.UserSign;

public class SignDaoImpl
{
	private static final String UPDATE_Qiandao= "UPDATE `user_sign` SET `startTime`=?,`one`=?,`two`=?,`three`=?,`four`=?,`five`=?,`six`=?,`seven`=? WHERE `userId` = ?";
	/**更新签到*/
	public static boolean updataUserSign(int userId, boolean one, boolean two, boolean three, boolean four, boolean five, boolean six, boolean seven,long startTime) 
	{
		Tool.print_debug_level0("userId="+userId+",one="+one+",two="+two+",three="+three);
		return SqlSimpleUtil.update(UPDATE_Qiandao,startTime,one,two,three,four,five,six,seven,userId)>0;
	}
	
	private static final String Inset_插入签到= "INSERT INTO `user_sign`(`userId`,`startTime`,`one`,`two`,`three`,`four`,`five`,`six`,`seven`) VALUES (?,?,?,?,?,?,?,?,?)";
	/**插入签到*/
	public static void insetUserSign(int userId,boolean one, boolean two, boolean three, boolean four, boolean five, boolean six, boolean seven,long startTime) 
	{
		int num=SqlSimpleUtil.update(Inset_插入签到,userId,startTime,one,two, three, four, five, six, seven);
	}
	
	private static final String UPDATE_签到天数= "UPDATE `user_sign` SET `one`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数 one*/
	public static boolean updataSign(int userId,boolean one,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数,one,startTime,userId)>0;
	}
	
	private static final String UPDATE_签到天数2= "UPDATE `user_sign` SET `two`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数 two*/
	public static boolean updataSign2(int userId,boolean two,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数2,two,startTime,userId)>0;
	}
	
	private static final String UPDATE_签到天数3= "UPDATE `user_sign` SET `three`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数 three*/
	public static boolean updataSign3(int userId,boolean three,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数3,three,startTime,userId)>0;
	}
	
	private static final String UPDATE_签到天数4= "UPDATE `user_sign` SET `four`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数 four*/
	public static boolean updataSign4(int userId,boolean four,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数4,four,startTime,userId)>0;
	}
	
	private static final String UPDATE_签到天数5= "UPDATE `user_sign` SET `five`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数 five*/
	public static boolean updataSign5(int userId,boolean five,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数5,five,startTime,userId)>0;
	}
	
	private static final String UPDATE_签到天数6= "UPDATE `user_sign` SET `six`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数six*/
	public static boolean updataSign6(int userId,boolean six,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数6,six,startTime,userId)>0;
	}
	
	private static final String UPDATE_签到天数7= "UPDATE `user_sign` SET `seven`=?,`startTime`=? WHERE `userId` = ?";
	/**更新签到天数 seven*/
	public static boolean updataSign7(int userId,boolean seven,long startTime) 
	{
		return SqlSimpleUtil.update(UPDATE_签到天数7,seven,startTime,userId)>0;
	}
	
	private static final String Select_签到= "SELECT * FROM  `user_sign`  WHERE `userId` = ?";
	/**更新签到天数*/
	public static UserSign selectSign(int userId) 
	{
		return SqlSimpleUtil.selectBean(Select_签到,UserSign.class,userId);
	}
}
