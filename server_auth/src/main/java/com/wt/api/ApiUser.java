package com.wt.api;

import java.net.InetSocketAddress;
import java.sql.Timestamp;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.biz.UserBiz;
import com.wt.cache.UserCache;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.Response;
import com.wt.cmd.user.GServerValidationLoginRequest;
import com.wt.cmd.user.GServerValidationLoginResponse;
import com.wt.cmd.user.GuestLoginAuthRequest;
import com.wt.cmd.user.GuestLoingAuthResponse;
import com.wt.cmd.user.LoginRequest;
import com.wt.cmd.user.LoginResponse;
import com.wt.cmd.user.RegisterRequest;
import com.wt.cmd.user.RegisterResponse;
import com.wt.cmd.user.UserAntiaddictionLogoutRequest;
import com.wt.dao.impl.UserDaoImpl;
import com.wt.iserver.ServerHelper;
import com.wt.main.AuthServerHelper;
import com.wt.naval.dao.model.antiaddiction.UserAntiAddictionModel;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.pojo.user.UserValidationBean;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.UuidUtil;
import com.wt.util.security.token.TokenVO;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.user") 
public class ApiUser extends ServerHelper
{
	private static boolean isDebug = true;
	
	@Protocol(msgType = MsgTypeEnum.USER_REGISTER)
	public void register(ChannelHandlerContext ctx, Request request)
	{
		RegisterRequest registerRequest = (RegisterRequest)request;
		RegisterResponse response = null;
		
		if(UserDaoImpl.isAccountExists(registerRequest.account))
		{
			response = new RegisterResponse(RegisterResponse.ERROR_账号已存在);
			sendResponse(ctx, response);
			return;
		}
		
		int userId = UserDaoImpl.insertUser(registerRequest.account,registerRequest.password,registerRequest.nickName);
		if(userId <= 0)
		{
			response = new RegisterResponse(RegisterResponse.ERROR_更新数据库失败);
			sendResponse(ctx, response);
			return;
		}
		Timestamp timestamp = new Timestamp(MyTimeUtil.getCurrTimeMM());
		
		UserInfoModel user = new UserInfoModel();
		user.setAccount(registerRequest.account);
		user.setNickName(registerRequest.nickName);
		user.setUserId(userId);
		String pwd = UuidUtil.generateShortUuid();
		TokenVO tokenVO = UserBiz.createToken(userId,pwd);
		UserValidationBean bean = new UserValidationBean(Tool.getCurrTimeMM(),pwd,user);
		bean.setUserAntiAddictionMode(new UserAntiAddictionModel(userId,timestamp));
		UserCache.addValidationBean(userId,bean);
		
		response = new RegisterResponse(RegisterResponse.SUCCESS,registerRequest.account,registerRequest.password,tokenVO,registerRequest.nickName,bean.getAntiAddictionCode());
		sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.USER_LOGIN)
	public void login(ChannelHandlerContext ctx, Request request)
	{
		LoginRequest loginRequest = (LoginRequest)request;
		LoginResponse response = null;
		
		UserInfoModel user = UserBiz.getUser(loginRequest.account);
		if(isDebug)
		{
			Tool.print_debug_level0(MsgTypeEnum.USER_LOGIN,"玩家登陆，user："+user);
		}
		if(user == null)
		{
			response = new LoginResponse(LoginResponse.ERROR_NO_USER);
			sendResponse(ctx, response);
			return;
		}

		Tool.print_debug_level0(MsgTypeEnum.USER_LOGIN,"数据库中密码:"+user.getPassword()+",客户端密码:"+loginRequest.password);
		if(!user.getPassword().equals(loginRequest.password))
		{
			response = new LoginResponse(LoginResponse.ERROR_WRONG_PWD);
			sendResponse(ctx, response);
			return;
		}
		
		UserBiz.updateLoginTime(user.getUserId());
		String pwd = UuidUtil.generateShortUuid();
		TokenVO tokenVO = UserBiz.createToken(user.getUserId(),pwd);
		UserValidationBean bean = new UserValidationBean(Tool.getCurrTimeMM(),pwd,user);
		UserCache.addValidationBean(user.getUserId(),bean);
		
		response = new LoginResponse(LoginResponse.SUCCESS,tokenVO,bean.getAntiAddictionCode());
//		checkAntiAddiction(user, bean);
		sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.USER_游客快速登录)
	public static void guestLoginAuth(ChannelHandlerContext ctx, GuestLoginAuthRequest request)
	{
		GuestLoingAuthResponse response = null;
		String device_code = request.device_code;
		boolean isRegister = false;
		if (device_code == null || device_code.equals(""))
		{
			isRegister = true;
		}

		// 开始登录或注册
		UserInfoModel user = null;
		if(!isRegister)
		{
			user = UserBiz.getGuestUser(device_code);
		}
		
		if (user == null)// 用户不存在,进行注册
		{
			 InetSocketAddress ipSocket = (InetSocketAddress)ctx.channel().remoteAddress();
			 String clientIp = ipSocket.getAddress().getHostAddress();
			 
			long ipCount = UserDaoImpl.checkSameIP(clientIp);
			
			if(ipCount>=3)
			{
				Tool.print_debug_level0("初始化userData失败...:" + device_code);
				response = new GuestLoingAuthResponse(GuestLoingAuthResponse.ERROR_IP注册账号达到上限);
				AuthServerHelper.sendResponse(ctx, response);
				return;
			}	
//			device_code = UuidUtil.generateShortUuid() + Tool.getCurrTimeMM() % 100000;
			Integer userId = UserBiz.userGuestRegister(device_code,clientIp);
			if (userId == null)
			{
				Tool.print_debug_level0("初始化userData失败...:" + device_code);
				response = new GuestLoingAuthResponse(GuestLoingAuthResponse.ERROR_更新数据库失败);
				AuthServerHelper.sendResponse(ctx, response);
				return;
			}
			
			Timestamp timestamp = new Timestamp(MyTimeUtil.getCurrTimeMM());
			user = new UserInfoModel();
			user.setAccount(device_code);
			user.setUserId(userId);
			user.setNickName(userId+"");
			
			Tool.print_debug_level0(MsgType.USER_游客快速登录, "用户不存在,新注册：" + device_code);

			UserDaoImpl.updateNickName(userId);
			

			String pwd = UuidUtil.generateShortUuid();
			TokenVO tokenVO = UserBiz.createToken(userId,pwd);
			UserValidationBean bean = new UserValidationBean(Tool.getCurrTimeMM(),pwd,user);
			bean.setUserAntiAddictionMode(new UserAntiAddictionModel(userId,timestamp));
			UserCache.addValidationBean(userId,bean);
			
			response = new GuestLoingAuthResponse(GuestLoingAuthResponse.SUCCESS, tokenVO,device_code,bean.getAntiAddictionCode());
			Tool.print_debug_level0(device_code, "注册成功");
			AuthServerHelper.sendResponse(ctx, response);
		}
		else// 已有用户
		{
			Tool.print_debug_level0(MsgTypeEnum.USER_游客快速登录, "玩家上线，不在比赛中,device_code:"+device_code);

			user.setNickName(user.getUserId()+"");
			
			// 更新登录时间
			UserBiz.updateLoginTime(user.getUserId());

			String pwd = UuidUtil.generateShortUuid();
			TokenVO tokenVO = UserBiz.createToken(user.getUserId(),pwd);
			UserValidationBean bean = new UserValidationBean(Tool.getCurrTimeMM(),pwd,user);
			
			UserCache.addValidationBean(user.getUserId(),bean);
			
			response = new GuestLoingAuthResponse(GuestLoingAuthResponse.SUCCESS, tokenVO, request.device_code,bean.getAntiAddictionCode());
			AuthServerHelper.sendResponse(ctx, response);
			// 成功登录！
//			checkAntiAddiction(user, bean);
 			
//			Tool.print_debug_level0(request.device_code, MsgType.USER_LOGIN, "本次登录距离上次登录天数:" + useDay);

			// MailBiz.checkMail(gameData);//登陆时获取新邮件
		}
	}

	/**
	 * 从游戏服GameServer传过来的验证消息
	 * @param ctx
	 * @param obj
	 */
	@Protocol(msgType = MsgTypeEnum.USER_VALIDATION_TOKEN)
	public static void gServerValidationLogin(ChannelHandlerContext ctx, Request obj)
	{
		GServerValidationLoginRequest request = (GServerValidationLoginRequest)obj;
		GServerValidationLoginResponse response = null;
		
		int code = UserBiz.validationSimpleToken(request.tokenVO);
		 switch (code)
		{
			case 0:
				UserValidationBean bean = UserCache.getUserValidation(request.tokenVO.uid);
				
				long time = 0;
				Timestamp timestamp = bean.userInfoModel.getLastLogoutTime();
				if(timestamp != null)
				{
					time = timestamp.getTime();
				}
				
				long antiAddictionTime = 0;
				Timestamp antiAddictionTimestamp = bean.userInfoModel.getLastLogoutTime();
				if(antiAddictionTimestamp != null)
				{
					antiAddictionTime = antiAddictionTimestamp.getTime();
				}
				
				response = new GServerValidationLoginResponse(Response.SUCCESS,bean.userInfoModel,antiAddictionTime,time);
				
				Tool.print_debug_level0(MsgType.USER_VALIDATION_TOKEN,"验证来自游戏服的玩家登陆token成功！"+bean.userInfoModel);
				break;
				
			case 1:
				response = new GServerValidationLoginResponse(GServerValidationLoginResponse.ERROR_TOKEN空);
				Tool.print_debug_level0(MsgType.USER_VALIDATION_TOKEN,"验证来自游戏服的玩家登陆token失败！ERROR_TOKEN空");
				break;
				
			case 2:
				response = new GServerValidationLoginResponse(GServerValidationLoginResponse.ERROR_没有申请过登陆);
				Tool.print_debug_level0(MsgType.USER_VALIDATION_TOKEN,"验证来自游戏服的玩家登陆token失败！ERROR_没有申请过登陆");
				break;
				
			case 3:
				response = new GServerValidationLoginResponse(GServerValidationLoginResponse.ERROR_TOKEN验证失败);
				Tool.print_debug_level0(MsgType.USER_VALIDATION_TOKEN,"验证来自游戏服的玩家登陆token失败！ERROR_TOKEN验证失败");
				break;

			default:
				break;
		}
		 sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.COMMUNICATION_AUTH_玩家登出游戏服)
	public void userAntiaddictionLogout(ChannelHandlerContext ctx, Request obj)
	{
		UserAntiaddictionLogoutRequest request = (UserAntiaddictionLogoutRequest)obj;
		UserDaoImpl.updateLogoutTime(request.userId);
		ctx.close();
	}
}
