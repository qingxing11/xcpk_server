package com.wt.biz;

import com.wt.cache.UserCache;
import com.wt.dao.impl.UserDaoImpl;
import com.wt.db.SqlSimpleUtil;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.pojo.user.UserValidationBean;
import com.wt.util.Tool;
import com.wt.util.security.MyPBKDF2;
import com.wt.util.security.token.MyTokenUtil;
import com.wt.util.security.token.TokenVO;

public class UserBiz
{
	public static void main(String[] args)
	{
		UserInfoModel userInfoModel = getUser("t1");
		System.out.println("userInfoModel:"+userInfoModel);
	}
	
	/**
	 * 获取用户信息,io
	 * 
	 * @param account
	 * @return
	 */
	public static UserInfoModel getUser(String account)
	{
		if (account == null || "".equals(account))
		{
			return null;
		}

		UserInfoModel userInfoModel = UserDaoImpl.getUser(account);
		if (userInfoModel == null)
		{
			return null;
		}
		return userInfoModel;
	}
	
	public static UserInfoModel getUser(int userId)
	{
		UserInfoModel userInfoModel = UserDaoImpl.getUser(userId);
		if (userInfoModel == null)
		{
			return null;
		}
		return userInfoModel;
	}
	
	public static UserInfoModel getGuestUser(String imei)
	{
		if (imei == null || "".equals(imei))
		{
			return null;
		}

		UserInfoModel userInfoModel = UserDaoImpl.getGuestUser(imei);
		if (userInfoModel == null)
		{
			return null;
		}
		return userInfoModel;
	}

	/**
	 * 注册游客账号到帐号服，io
	 * 
	 * @param imei
	 * @return
	 */
	public static Integer userGuestRegister(String imei,String ip)
	{
		return UserDaoImpl.insertGuestUser(imei,ip);
	}

	// 保存用户信息
	private static final String INSERT_USER_INFO_FAST = "INSERT INTO `user`(`account`) VALUES (?)";

	public static boolean saveUserInfo(String account)
	{
		return SqlSimpleUtil.insert(INSERT_USER_INFO_FAST, account) != null;
	}

	// 更新登录时间
	public static void updateLoginTime(int userId)
	{
		UserDaoImpl.updateLoginTime(userId);
	}

	/**
	 * 为玩家创建一个简单token，并保存到缓存
	 * 
	 * @param userId
	 * @return
	 */
	public static TokenVO createToken(int userId, String pwd)
	{
		TokenVO tokenVO = MyTokenUtil.createSimpleToken(userId, pwd);
		return tokenVO;
	}

	/**
	 * 玩家离线重连时快速登陆，校验玩家令牌是否有效
	 * 
	 * @param tokenVO
	 * @return 0:success , 1:token空 ,2:不是在线玩家 ,3:检验失败,令牌错误
	 */
	public static int validationSimpleToken(TokenVO tokenVO)
	{
		if (tokenVO == null)
		{
			return 1;
		}

		UserValidationBean userValidationBean = UserCache.getUserValidation(tokenVO.uid);
		if (userValidationBean == null)
		{
			return 2;
		}
		boolean isSuccess = false;
		try
		{
			isSuccess = MyPBKDF2.validatePassword(userValidationBean.pwd, tokenVO.pwd);
		}
		catch (Exception e)
		{
			Tool.print_error("验证simple token时错误", e);
		}
		if (isSuccess)
		{
			return 0;
		}
		return 3;
	}
}
