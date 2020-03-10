package com.wt.dao.impl;

import java.sql.Timestamp;

import com.wt.db.SqlSimpleUtil;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;

public class UserDaoImpl
{
	// 获取玩家信息
	private static final String GET_USER_BY_NAME = "SELECT * FROM `user` WHERE `account`=?";
	public static UserInfoModel getUser(String account)
	{
		return SqlSimpleUtil.selectBean(GET_USER_BY_NAME, UserInfoModel.class, account);
	}
	
	private static final String GET_USER_BY_USERID = "SELECT * FROM `user` WHERE `userId`=?";
	public static UserInfoModel getUser(int userId)
	{
		return SqlSimpleUtil.selectBean(GET_USER_BY_USERID, UserInfoModel.class, userId);
	}
	
	private static final String GET_USER_BY_IMEI = "SELECT * FROM `user` WHERE `imei`=?";
	public static UserInfoModel getGuestUser(String imei)
	{
		return SqlSimpleUtil.selectBean(GET_USER_BY_IMEI, UserInfoModel.class, imei);
	}

	// 更新登录时间
	private static final String UPDATE_LOGIN_TIME = "UPDATE `user` SET `lastLoginTime` = ? WHERE `userId` = ?";
	public static void updateLoginTime(int userId)
	{
		SqlSimpleUtil.update(UPDATE_LOGIN_TIME, new Timestamp(Tool.getCurrTimeMM()), userId);
	}
	
	// 保存用户信息
	private static final String INSERT_USER_INFO_FAST = "INSERT INTO `user`(`account`,`regTime`) VALUES (?,?)";
	public static Integer insertUser(String account) {
		return SqlSimpleUtil.insert(INSERT_USER_INFO_FAST, account,new Timestamp(MyTimeUtil.getCurrTimeMM()));
	}
	
	private static final String INSERT_USER_GUEST = "INSERT INTO `user`(`imei`,`regTime`,`registerip`) VALUES (?,?,?)";
	public static Integer insertGuestUser(String imei,String ip) {
		
		return SqlSimpleUtil.insert(INSERT_USER_GUEST, imei,new Timestamp(MyTimeUtil.getCurrTimeMM()),ip);
	}
	
	private static final String INSERT_USER = "INSERT INTO `user`(`account`,`password`,`nickName`) VALUES (?,?,?)";
	public static Integer insertUser(String account,String password,String nickName) {
		return SqlSimpleUtil.insert(INSERT_USER, account,password,nickName);
	}
	
	private static final String GET_ACCOUNT_COUNT = "SELECT COUNT(`account`) FROM `user` WHERE `account`=?";
	/***账号是否存在*/
	public static boolean isAccountExists(String account)
	{
		return SqlSimpleUtil.selectCount(GET_ACCOUNT_COUNT, account) > 0;
	}
	
	private static final String GET_NICKNAME_COUNT = "SELECT COUNT(`nickName`) FROM `user` WHERE `nickName`=?";
	/***账号是否存在*/
	public static boolean isNickNameExists(String nickName)
	{
		return SqlSimpleUtil.selectCount(GET_NICKNAME_COUNT, nickName) > 0;
	}
	
	private static final String IS_ACCOUNT_OR_NICKNAME_EXIST = "SELECT COUNT(`userId`) FROM `user` WHERE `account`= ? OR `nickName` = ?";
	public static boolean isAccountORNickNameExist(String account,String nickName)
	{
		return SqlSimpleUtil.selectCount(IS_ACCOUNT_OR_NICKNAME_EXIST, account,nickName) > 0;
	}
	
	private static final String UPDATE_ANTIADDICTION = "UPDATE `user` SET `antiAddiction` = ? WHERE `userId` = ?";
	public static boolean updateAntiAddiction(int isAdult, int userId)
	{
		return SqlSimpleUtil.update(UPDATE_ANTIADDICTION, isAdult,userId) > 0;
	}
	
	private static final String UPDATE_LOGOUTTIME = "UPDATE `user` SET `lastLogoutTime` = ? WHERE `userId` = ?";
	public static void updateLogoutTime(int userId)
	{
		Timestamp timestamp = new Timestamp(MyTimeUtil.getCurrTimeMM());
		SqlSimpleUtil.update(UPDATE_LOGOUTTIME,timestamp,userId);
	}
	
	private static final String UPDATE_USER_PHONENUMBER = "UPDATE `user` SET `regPhoneNum` = ? WHERE `userId` = ?";
	public static void updatePhoneNumber(int userId, String phoneNumber)
	{
		SqlSimpleUtil.update(UPDATE_USER_PHONENUMBER,phoneNumber,userId);
	}
	
	private static final String UPDATE_USER = "UPDATE `user` SET `account` = ?,`nickName` = ?,`password` = ? WHERE `userId` = ?";
	public static boolean updateUserSupplementaryAccount(String account, String nickName, String password, int userId)
	{
		return SqlSimpleUtil.update(UPDATE_USER, account,nickName,password,userId) > 0;
	}
	
	private static final String CHECK_ACCOUT = "SELECT COUNT(`userId`) FROM `user` WHERE `account`=? AND `password` = ?";
	public static boolean checkAccount(String accout,String password)
	{
		return SqlSimpleUtil.selectCount(CHECK_ACCOUT, accout,password) > 0;
	}
	
	private static final String UPDATE_USER_PASSWORD = "UPDATE `user` SET `password` = ? WHERE `userId` = ?";
	public static boolean updateUserPassword(String newPwd, int userId)
	{
		return SqlSimpleUtil.update(UPDATE_USER_PASSWORD, newPwd,userId) > 0;
	}
	
	private static final String UPDATE_USER_NAME = "UPDATE `user` SET `nickName` = ? WHERE `userId` = ?";
	public static boolean updateNickName(Integer userId)
	{
		return SqlSimpleUtil.update(UPDATE_USER_NAME, userId,userId) > 0;
	}
	
	private static final String CHECK_SAME_IP = "SELECT COUNT(`userId`) FROM `user` WHERE `registerip`=?";
	public static  long checkSameIP(String registerip)
	{
		return SqlSimpleUtil.selectCount(CHECK_SAME_IP, registerip);
	}
}