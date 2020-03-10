package com.wt.naval.api;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.ranking.LowRequest;
import com.brc.cmd.ranking.LowResponse;
import com.brc.cmd.ranking.RankRewardRequest;
import com.brc.cmd.ranking.RankRewardResponse;
import com.brc.cmd.ranking.RankingResponse;
import com.brc.naval.ranking.RankingService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.RankVO;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.cmd.ranking") @Service public class ApiRanking
{
	@Autowired
	private PlayerService playerService;

	@Autowired
	private RankingService rankingService;

	@Autowired
	private RankService rankService;

	// 排行榜领取奖励
	@Protocol(msgType = MsgTypeEnum.RANKING)
	public void ranking(ChannelHandlerContext ctx, Request request, MySession session)
	{
		Player player = playerService.getPlayer(session.getUserId());
		RankingResponse response = null;
		ArrayList<RankVO> payRank = rankService.getPayRank();
		ArrayList<RankVO> coinsRank = rankService.getCoinsRank();
		ArrayList<RankVO> bigWinRank = rankService.getBigWinRank();

		boolean isGetWin = rankingService.isGeting(player.getUserId(), 0);
		boolean isGetPay = rankingService.isGeting(player.getUserId(), 1);
		response = new RankingResponse(RankingResponse.SUCCESS, payRank, coinsRank, bigWinRank, isGetWin, isGetPay);
		player.sendResponse(response);
	}

	// 排行榜领取奖励
	@Protocol(msgType = MsgTypeEnum.RANKINGREWARD)
	public void rankReward(ChannelHandlerContext ctx, RankRewardRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		RankRewardResponse response = null;
		
		long curTime = MyTimeUtil.getDayPassTime();
		
		if(curTime<=600)
		{
			response = new RankRewardResponse(RankRewardResponse.ERROR_统计中, request.type);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		Tool.print_debug_level0("AAAAAAAAAAAAAAAAAAA    领取排行榜：type:" + request.type);
		if (!rankingService.isFirst(player.getUserId(), request.type))
		{
			Tool.print_debug_level0("BBBBBBBBBBBBBBBBB    领取排行榜：type:" + request.type);
			response = new RankRewardResponse(RankRewardResponse.ERROR_未上榜, request.type);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (rankingService.isGeting(player.getUserId(), request.type))
		{
			response = new RankRewardResponse(RankRewardResponse.ERROR_已领取, request.type);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		long coins = rankingService.getReward(player, request.type);
		response = new RankRewardResponse(RankRewardResponse.SUCCESS, coins, request.type);
		GameServerHelper.sendResponse(ctx, response);
	}

	// 低保
	@Protocol(msgType = MsgTypeEnum.LOW)
	public void low(ChannelHandlerContext ctx, LowRequest request, MySession session)
	{
		int userId = session.getUserId();
		Player player = playerService.getPlayer(userId);

		LowResponse response;

		if ((player.gameData.coins + player.gameData.bankCoins) > 3000)
		{
			response = new LowResponse(LowResponse.ERROR_金币较多);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (!rankingService.canLow(userId))
		{
			response = new LowResponse(LowResponse.ERROR_次数不足);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		int num = rankingService.getLow(player);
		response = new LowResponse(LowResponse.SUCCESS, num);
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
