package com.wt.api;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.biz.UserBiz;
import com.wt.cache.UserCache;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.dao.impl.UserDaoImpl;
import com.wt.main.AuthServerHelper;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.yt.cmd.antiaddiction.AntiaddictionNoticeRequest;
import com.yt.cmd.antiaddiction.RealNameAuthenticationRequest;
import com.yt.cmd.antiaddiction.RealNameAuthenticationResponse;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.yt.cmd.antiaddiction")
@Service
public class ApiAntiAddiction extends AuthServerHelper
{
	/**
	 * 玩家处于账号服时进行实名验证
	 * @param ctx
	 * @param obj
	 */
	@Protocol(msgType = MsgTypeEnum.RealNameAuthentication)
	public void realNameAuthentication(ChannelHandlerContext ctx, Request obj)
	{
		RealNameAuthenticationRequest request = (RealNameAuthenticationRequest)obj;
		RealNameAuthenticationResponse response = null;
		
		int tokenValidationCode = UserBiz.validationSimpleToken(request.tokenVO);
		int userId = request.tokenVO.uid;
		if(tokenValidationCode != 0)
		{
			response = new RealNameAuthenticationResponse(RealNameAuthenticationResponse.验证失败);
			sendResponse(ctx, response);
			return;
		}
		UserCache.getUserValidation(userId).setAntiAddictionCode(request.isAdult);
		UserDaoImpl.updateAntiAddiction(request.isAdult,userId);
		
		response = new RealNameAuthenticationResponse(RealNameAuthenticationResponse.SUCCESS);
		sendResponse(ctx, response);
	}
	
	/**
	 * 玩家处于游戏服时，游戏服代为实名验证，并且将结果通知到账号服
	 * @param ctx
	 * @param obj
	 */
	@Protocol(msgType = MsgTypeEnum.COMMUNICATION_AUTH_实名验证)
	public void antiaddictionNotice(ChannelHandlerContext ctx, Request obj)
	{
		AntiaddictionNoticeRequest request = (AntiaddictionNoticeRequest)obj;
		Tool.print_debug_level0("收到更新实名认证,antiAddiction:"+request.antiAddiction+",userId:" +request.userId);
		UserDaoImpl.updateAntiAddiction(request.antiAddiction,request.userId);
		
		ctx.close();
	}
	
	
	@PostConstruct
	private void init()
	{
		try
		{
			ClassTool.registerApi(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
