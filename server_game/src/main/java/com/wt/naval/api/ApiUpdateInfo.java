package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.update.UpdateGarderRequest;
import com.brc.cmd.update.UpdateGarderResponse;
import com.brc.cmd.update.UpdateNickNameRequest;
import com.brc.cmd.update.UpdateNickNameResponse;
import com.brc.cmd.update.UpdateSignRequest;
import com.brc.cmd.update.UpdateSignResponse;
import com.brc.naval.updateinfo.UpdateInfoService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.cmd.update") 
@Service
public class ApiUpdateInfo 
{
	@Autowired
	private PlayerService playerService;
	@Autowired
	private UpdateInfoService updateInfoService;
	
	
	//修改昵称
	@Protocol(msgType = MsgTypeEnum.UPDATENICKNAME)
	public void updateNickName(ChannelHandlerContext ctx, UpdateNickNameRequest request, MySession session)
	{
		UpdateNickNameResponse response=null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		
		if(player.getChangeNameCard() <= 0)
		{
			response=new UpdateNickNameResponse(UpdateNickNameResponse.ERROR_改名卡不足);
			Tool.print_debug_level0(MsgTypeEnum.UPDATENICKNAME,"ERROR_改名卡不足");
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		if (UserDaoImpl.isNicknameExists(request.nickName))
		{
			response = new UpdateNickNameResponse(UpdateNickNameResponse.ERROR_昵称重复);
			Tool.print_debug_level0(MsgTypeEnum.UPDATENICKNAME,"ERROR_昵称重复");
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		player.subChangeNameCard();
		if(!updateInfoService.updateNickname(player, request.nickName))
		{
			response=new UpdateNickNameResponse(Response.FAILED);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		response=new UpdateNickNameResponse(Response.SUCCESS, request.nickName);
		GameServerHelper.sendResponse(ctx, response);
	}
		//修改性别
		@Protocol(msgType = MsgTypeEnum.UPDATEGENDER)
		public void updateGarder(ChannelHandlerContext ctx, UpdateGarderRequest request, MySession session)
		{
			Player player = playerService.getPlayerAndCheck(session);
			if(player == null)
			{
				return;
			}
			UpdateGarderResponse response=null; 
			if(!updateInfoService.updateGarder(player, request.gender))
			{
				response=new UpdateGarderResponse(Response.FAILED);
				GameServerHelper.sendResponse(ctx, response);
				return;
			}
			response=new UpdateGarderResponse(Response.SUCCESS,request.gender);
			GameServerHelper.sendResponse(ctx, response);
		}
	
	//修改签名
	@Protocol(msgType = MsgTypeEnum.UPDATESIGN)
	public void updateSign(ChannelHandlerContext ctx, UpdateSignRequest request, MySession session)
	{
		UpdateSignResponse response=null; 
		Player player=playerService.getPlayer(session.getUserId());
		if(!updateInfoService.updateSign(player,request.sign))
		{
			response=new UpdateSignResponse(Response.FAILED);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		response=new UpdateSignResponse(Response.SUCCESS,request.sign);
		GameServerHelper.sendResponse(ctx, response);
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
