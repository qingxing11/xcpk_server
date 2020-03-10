package com.wt.naval.api;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.user.GServerValidationLoginRequest;
import com.wt.cmd.user.GetPlayerNicknameRequest;
import com.wt.cmd.user.GetPlayerNicknameResponse;
import com.wt.cmd.user.PlayerNicknameVO;
import com.wt.cmd.user.UpdateHeadIconRequest;
import com.wt.cmd.user.UpdateHeadIconResponse;
import com.wt.cmd.user.UserValidationLoginRequest;
import com.wt.cmd.user.UserValidationLoginResponse;
import com.wt.naval.cache.UserCache;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.netty.client.ClientHelper;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.util.server.OSSUtils;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.user") @Service public class ApiUser
{
	@Autowired
	private ClientHelper clientHelper;

	@Autowired
	private PlayerService playerService;

	@Protocol(msgType = MsgTypeEnum.GAME_GETPLAYERNICKNAMES)
	public void getPlayerNickname(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		int userId = session.getUserId();
		Player player = playerService.getPlayer(userId);

		GetPlayerNicknameRequest request = (GetPlayerNicknameRequest) obj;
		if (request.list_playerIds == null || request.list_playerIds.size() <= 0)
		{
			GetPlayerNicknameResponse res = new GetPlayerNicknameResponse(GetPlayerNicknameResponse.EROOR_请求信息为空);
			player.sendResponse(res);
		}

		ArrayList<PlayerNicknameVO> list_nicknames = playerService.getPlayersNickname(request.list_playerIds);
		GetPlayerNicknameResponse response = new GetPlayerNicknameResponse(list_nicknames);
		player.sendResponse(response);

	}

	// 验证token,登陆到游戏服
	@Protocol(msgType = MsgTypeEnum.USER_VALIDATION_TOKEN)
	public void userValidationLogin(ChannelHandlerContext ctx, Request obj)
	{
		UserValidationLoginRequest request = (UserValidationLoginRequest) obj;

		Tool.print_debug_level0(MsgType.USER_VALIDATION_TOKEN, "进入游戏服验证：" + request.tokenVO);
		
		if(PlayerDaoImpl.isBanned(request.tokenVO.uid))
		{
			GameServerHelper.sendResponse(ctx, new UserValidationLoginResponse(UserValidationLoginResponse.ERROR_已被封号));
			return;
		}
		
		UserCache.addWaitValidation(request.tokenVO.uid, ctx.channel());

		GServerValidationLoginRequest gServerValidationLoginRequest = new GServerValidationLoginRequest(request.tokenVO);
		clientHelper.sendRequest(gServerValidationLoginRequest);
	}

	@Protocol(msgType = MsgTypeEnum.USER_上传自定义头像)
	public void updateHeadIcon(ChannelHandlerContext ctx, UpdateHeadIconRequest request,MySession session)
	{
		UpdateHeadIconResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		
		if(request.headIcon == null || request.headIcon.length == 0)
		{
			Tool.print_error(MsgTypeEnum.USER_上传自定义头像, "ERROR_头像数据错误");
			player.sendResponse(new UpdateHeadIconResponse(UpdateHeadIconResponse.ERROR_头像数据错误));
			return;
		}
		
		Tool.print_debug_level0(MsgTypeEnum.USER_上传自定义头像, "上传自定义头像,长度:"+request.headIcon.length);
		
		String fileName = "headIcon/"+player.getUserId()+".jpg";
		OSSUtils.uploadFile(fileName, request.headIcon);

		player.setHeadIconUrl("http://xcpk-game.oss-cn-shanghai.aliyuncs.com/"+fileName);
		PlayerDaoImpl.updateHeadImageUrl(player.getUserId(),player.getHeadIconUrl());
		response = new UpdateHeadIconResponse(UpdateHeadIconResponse.SUCCESS,player.getHeadIconUrl());
		player.sendResponse(response);
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