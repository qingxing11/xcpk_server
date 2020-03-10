package com.wt.cache;

import java.util.concurrent.ConcurrentHashMap;

import com.wt.pojo.user.UserValidationBean;

public class UserCache
{
	/**
	 * 玩家在线密钥，每次登陆验证后随机生成，用于玩家断线重连等快速验证
	 * 十分钟未进行验证的密码自动清除
	 */
	private static ConcurrentHashMap<Integer,UserValidationBean> map_userValidationBean = new ConcurrentHashMap<>();

	
	/**
	 * 登陆成功后添加在线密钥
	 * @param userId
	 * @param pwd
	 */
	public static void addValidationBean(int userId,UserValidationBean userValidationBean)
	{
		map_userValidationBean.put(userId, userValidationBean);
	}

	public static UserValidationBean getUserValidation(int uid)
	{
		return map_userValidationBean.get(uid);
	}
	
	public void removeUserValidation(int uid)
	{
		map_userValidationBean.remove(uid);
	}
}
