package com.wt.api;

import java.sql.Timestamp;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.biz.UserBiz;
import com.wt.cache.UserCache;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.cmd.game.GClientReconnectValidationRequest;
import com.wt.cmd.game.GClientReconnectValidationResponse;
import com.wt.cmd.user.GServerValidationLoginResponse;
import com.wt.iserver.ServerHelper;
import com.wt.pojo.user.UserValidationBean;
import com.wt.util.Tool;
import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.game") 
public class ApiGame extends ServerHelper
{
	@Protocol(msgType = MsgTypeEnum.GAME_游戏服断线重连验证)
	public static void gClientReconnectValidation(ChannelHandlerContext ctx, GClientReconnectValidationRequest request)
	{
		GClientReconnectValidationResponse response = null;
		
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
				
				response = new GClientReconnectValidationResponse(Response.SUCCESS,bean.userInfoModel.getUserId(),time);
				
				Tool.print_debug_level0(MsgTypeEnum.GAME_游戏服断线重连验证,"验证来自游戏服的玩家登陆token成功！"+bean.userInfoModel);
				break;
				
			case 1:
				response = new GClientReconnectValidationResponse(GServerValidationLoginResponse.ERROR_TOKEN空);
				Tool.print_debug_level0(MsgTypeEnum.GAME_游戏服断线重连验证,"验证来自游戏服的玩家登陆token失败！ERROR_TOKEN空");
				break;
				
			case 2:
				response = new GClientReconnectValidationResponse(GServerValidationLoginResponse.ERROR_没有申请过登陆);
				Tool.print_debug_level0(MsgTypeEnum.GAME_游戏服断线重连验证,"验证来自游戏服的玩家登陆token失败！ERROR_没有申请过登陆");
				break;
				
			case 3:
				response = new GClientReconnectValidationResponse(GServerValidationLoginResponse.ERROR_TOKEN验证失败);
				Tool.print_debug_level0(MsgTypeEnum.GAME_游戏服断线重连验证,"验证来自游戏服的玩家登陆token失败！ERROR_TOKEN验证失败");
				break;

			default:
				break;
		}
		 sendResponse(ctx, response);
	}
}
