package com.wt.naval.api;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.lucky.LuckyBoxRequest;
import com.brc.cmd.lucky.LuckyBoxResponse;
import com.brc.cmd.lucky.LuckyRequest;
import com.brc.cmd.lucky.LuckyResponse;
import com.brc.cmd.mail.Attach;
import com.brc.naval.lucky.LuckyService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.security.MySession;
import com.wt.xcpk.Room;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.cmd.lucky") @Service public class ApiLucky
{
	@Autowired
	private PlayerService playerService;
	@Autowired
	private LuckyService luckyService;

	@Protocol(msgType = MsgTypeEnum.LUCKY)
	public void lucky(ChannelHandlerContext ctx, LuckyRequest request, MySession session)
	{
		LuckyResponse response = null;
		Player player = playerService.getPlayer(session.getUserId());

		if (player.gameData.isLucky)
		{
			response = new LuckyResponse(LuckyResponse.ERROR_当月已经抽取,player.gameData.luckyNum);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		int luckyNum = luckyService.getLucky(player);
		response = new LuckyResponse(LuckyResponse.SUCCESS, luckyNum);
		GameServerHelper.sendResponse(ctx, response);
	}

	// 宝箱
	@Protocol(msgType = MsgTypeEnum.LUCKY_BOX)
	public void luckyBox(ChannelHandlerContext ctx, LuckyBoxRequest request, MySession session)
	{
		LuckyBoxResponse response = null;
	
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		if (player.gameData.vipLv != request.index)
		{
			response = new LuckyBoxResponse(LuckyBoxResponse.ERROR_不是这个宝箱);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		ArrayList<Attach> attachs = luckyService.getLuckyBox(player);
		response = new LuckyBoxResponse(LuckyBoxResponse.SUCCESS, player.getUserId(),attachs);
		
		Room room = player.getInsideRoom();
		if(room != null)
		{
			room.broadResponseToAll(response);
		}
//		GameServerHelper.sendResponse(ctx, response);
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
