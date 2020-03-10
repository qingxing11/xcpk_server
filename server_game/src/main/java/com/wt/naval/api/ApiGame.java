package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.game.GClientReconnectValidationRequest;
import com.wt.cmd.game.PlayerReConnectRequest;
import com.wt.cmd.game.PlayerReConnectResponse;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.netty.client.ClientHelper;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@Service
@RegisterApi(packagePath = "com.wt.cmd.game")
public class ApiGame
{
	@Autowired
	private ClientHelper clientHelper;
	
	@Autowired
	private PlayerService playerService;
	
	/**
	 * 玩家掉线重连
	 * @param ctx
	 * @param obj
	 */
	@Protocol(msgType = MsgTypeEnum.GAME_客户端要求断线重连)
	public void playerReConnect(ChannelHandlerContext ctx, PlayerReConnectRequest request)
	{
		Tool.print_debug_level0(MsgTypeEnum.GAME_客户端要求断线重连, "playerReConnect：" + request.isPing);
		PlayerReConnectResponse response = null;
		if(request.isPing)
		{//TODO。。。。。。。。。。。。。。。。
			if(request.tokenVO == null)
			{
				response = new PlayerReConnectResponse(PlayerReConnectResponse.ERROR_SESSION空);
				GameServerHelper.sendResponse(ctx, response);
				Tool.print_debug_level0(MsgTypeEnum.GAME_客户端要求断线重连, "ERROR_SESSION空");
				return;
			}
			
			UserCache.addWaitValidation(request.tokenVO.uid, ctx.channel());

			GClientReconnectValidationRequest gcRequest = new GClientReconnectValidationRequest(request.tokenVO);
			clientHelper.sendRequest(gcRequest);
		}
		else
		{
			MySession mySession = Tool.getSession(ctx);
			if(mySession == null)
			{
				response = new PlayerReConnectResponse(PlayerReConnectResponse.ERROR_SESSION空);
				GameServerHelper.sendResponse(ctx, response);
				Tool.print_debug_level0(MsgTypeEnum.GAME_客户端要求断线重连, "ERROR_SESSION空");
				return;
			}
			Player player = playerService.getPlayerAndCheck(mySession);
			if(player == null)
			{
				return;
			}
			player.online(false);
			response = new PlayerReConnectResponse(PlayerReConnectResponse.SUCCESS,request.isPing);
			player.sendResponse(response);
		}
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