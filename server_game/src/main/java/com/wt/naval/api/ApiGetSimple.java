package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wt.xcpk.PlayerSimpleData;
import com.brc.naval.getsimple.GetSimpleRequest;
import com.brc.naval.getsimple.GetSimpleResponse;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.security.MySession;
import com.wt.xcpk.killroom.EnterKillRoomResponse;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.naval.getsimple")
@Service
public class ApiGetSimple 
{
	@Autowired
	private PlayerService playerService;
	
	
	
	@Protocol(msgType = MsgTypeEnum.GET_SIMPLEDATA)
	public void getSimple(ChannelHandlerContext ctx, GetSimpleRequest obj, MySession session)
	{
		GetSimpleResponse response = null;
		Player player = playerService.getPlayer(obj.userId);
		PlayerSimpleData playerSimpleData;
		
		if (player == null)
		{
			response = new GetSimpleResponse(GetSimpleResponse.ERROR_玩家为空);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		else {
			playerSimpleData=player.getSimpleData();
			if(playerSimpleData==null)
			{
				response = new GetSimpleResponse(GetSimpleResponse.ERROR_数据为空);
				GameServerHelper.sendResponse(ctx, response);
				return;
			}
			else {
				response = new GetSimpleResponse(GetSimpleResponse.SUCCESS,playerSimpleData);
				GameServerHelper.sendResponse(ctx, response);
			}
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
