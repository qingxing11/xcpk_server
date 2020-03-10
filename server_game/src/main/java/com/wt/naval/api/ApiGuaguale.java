package com.wt.naval.api;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.ggl.GglBuyOnceRequest;
import com.wt.cmd.ggl.GglBuyOnceRespone;
import com.wt.cmd.ggl.GglCustomizeBuyRequest;
import com.wt.cmd.ggl.GglCustomizeBuyRespone;
import com.wt.cmd.ggl.GglRewardRequest;
import com.wt.cmd.ggl.GglRewardRespone;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.ggl.GglService;
import com.wt.xcpk.vo.GGLLotteryChessVO;

import io.netty.channel.ChannelHandlerContext;

@Service @RegisterApi(packagePath = "com.wt.cmd.ggl") public class ApiGuaguale
{
	private static final boolean isDebug = true;
	@Autowired
	private PlayerService playerService;

	@Autowired
	private GglService gglService;

	@Protocol(msgType = MsgTypeEnum.GGL_自定义购买)
	public void gglCustomizeBuy(ChannelHandlerContext ctx, GglCustomizeBuyRequest request, MySession mySession)
	{
		Player player = playerService.getPlayerAndCheck(mySession);
		if (player == null)
		{
			return;
		}
		GglCustomizeBuyRespone response = null;
		
		response = new GglCustomizeBuyRespone(GglCustomizeBuyRespone.ERROR_购买场次错误);
		player.sendResponse(response);
		return;
		
//		if (request.level < 0 || request.level > 2)
//		{
//			response = new GglCustomizeBuyRespone(GglCustomizeBuyRespone.ERROR_购买场次错误);
//			player.sendResponse(response);
//			return;
//		}
//		
//		int cost = getCost(request.level) * request.num;
//		if(player.getCoins() < cost)
//		{
//			response = new GglCustomizeBuyRespone(GglCustomizeBuyRespone.ERROR_金币不足);
//			player.sendResponse(response);
//			return;
//		}
//		
//		Tool.print_subCoins(player.getNickName(),cost,"ggl",player.getCoins());
//		player.subCoinse(cost);
//		
//		ArrayList<Integer> list_money = gglService.getCustomizeBuy(request.level,request.num);
//		int gift = getGift(list_money);
//		Tool.print_coins(player.getNickName(),gift,"刮刮乐自定义购买",player.getCoins());
//		player.addCoins(gift);
//		response = new GglCustomizeBuyRespone(GglCustomizeBuyRespone.SUCCESS,list_money,cost);
//		player.sendResponse(response);
	}
	
	private int getGift(ArrayList<Integer> list_money)
	{
		int num = 0;
		for (Integer integer : list_money)
		{
			num += integer;
		}
		return num;
	}

	private int getCost(int butType)
	{
		int buyCostCoins = 0;
		switch (butType)
		{
			case 0:
				buyCostCoins = 1000;
				break;
			case 1:
				buyCostCoins = 10000;
				break;
			case 2:
				buyCostCoins = 100000;
				break;
		}
		return buyCostCoins;
	}
	
	@Protocol(msgType = MsgTypeEnum.GGL_兑奖)
	public void gglReward(ChannelHandlerContext ctx, GglRewardRequest request, MySession mySession)
	{
		GglRewardRespone response = null;
		Player player = playerService.getPlayerAndCheck(mySession);
		if (player == null)
		{
			return;
		}
		response = new GglRewardRespone(GglRewardRespone.SUCCESS, player.getGglReward());
		player.sendResponse(response);
		
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.GGL_兑奖,"中奖金币:"+player.getGglReward());
		
		int reward = player.getGglReward();
		Tool.print_coins(player.getNickName(),reward,"刮刮乐购买",player.getCoins());
		player.addCoins(reward);
		player.setGglReward(0);
	}

	@Protocol(msgType = MsgTypeEnum.GGL_单次购买)
	public void gglBuyOnce(ChannelHandlerContext ctx, GglBuyOnceRequest request, MySession mySession)
	{
		
		GglBuyOnceRespone response = null;
		Player player = playerService.getPlayerAndCheck(mySession);
		if (player == null)
		{
			return;
		}

		response = new GglBuyOnceRespone(GglBuyOnceRespone.ERROR_购买场次错误);
		player.sendResponse(response);
		return;
		
//		if (request.level < 0 || request.level > 2)
//		{
//			response = new GglBuyOnceRespone(GglBuyOnceRespone.ERROR_购买场次错误);
//			player.sendResponse(response);
//			return;
//		}
//
//		int cost = gglService.getCost(request.level);
//		if (player.getCoins() < cost)
//		{
//			response = new GglBuyOnceRespone(GglBuyOnceRespone.ERROR_金币不足);
//			player.sendResponse(response);
//			return;
//		}
//
//		/* 幸运棋子2个：决定本次在6个棋子库中中将的棋子 */
//		ArrayList<Integer> list_luckyChess = gglService.getLuckyChess();
//		/* 自己棋子6个：从红黑象棋库中挑选6个棋子作为 */
//		ArrayList<GGLLotteryChessVO> list_myChess = gglService.getLotteryChess(request.level);
//		int reward = gglService.calcReward(list_luckyChess, list_myChess);
//		response = new GglBuyOnceRespone(GglBuyOnceRespone.SUCCESS, list_luckyChess, list_myChess, reward,cost);
//		
//		Tool.print_subCoins(player.getNickName(),cost,"ggl",player.getCoins());
//		player.subCoinse(cost);
//		player.setGglReward(reward);
//		player.sendResponse(response);
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
