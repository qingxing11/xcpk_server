package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.xcpk.classic.AllInRequest;
import com.wt.cmd.xcpk.classic.AllInResponse;
import com.wt.cmd.xcpk.classic.ChangeTableRequest;
import com.wt.cmd.xcpk.classic.ChangeTableResponse;
import com.wt.cmd.xcpk.classic.CheckPokerRequest;
import com.wt.cmd.xcpk.classic.CheckPokerResponse;
import com.wt.cmd.xcpk.classic.ComparerPokerRequest;
import com.wt.cmd.xcpk.classic.ComparerPokerResponse;
import com.wt.cmd.xcpk.classic.EnterBeginnerRequest;
import com.wt.cmd.xcpk.classic.EnterBeginnerResponse;
import com.wt.cmd.xcpk.classic.FoldRequest;
import com.wt.cmd.xcpk.classic.FoldResponse;
import com.wt.cmd.xcpk.classic.FollowBetRequest;
import com.wt.cmd.xcpk.classic.FollowBetResponse;
import com.wt.cmd.xcpk.classic.LeaveClassicGameRequest;
import com.wt.cmd.xcpk.classic.LeaveClassicGameResponse;
import com.wt.cmd.xcpk.classic.PlayerReadyRequest;
import com.wt.cmd.xcpk.classic.PlayerReadyResponse;
import com.wt.cmd.xcpk.classic.RaiseBetRequest;
import com.wt.cmd.xcpk.classic.RaiseBetResponse;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.classic.ClassicGame;
import com.wt.xcpk.classic.ClassicGameService;

import io.netty.channel.ChannelHandlerContext;

/**
 * 经典场api
 * @author WangTuo
 */
@RegisterApi(packagePath = "com.wt.cmd.xcpk.classic")
@Service
public class ApiClassicGame
{
	private boolean isDebug = true;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private ClassicGameService classicGameService;
	
	@Protocol(msgType = MsgTypeEnum.classic_换桌)
	public void changeTable(ChannelHandlerContext ctx,ChangeTableRequest request, MySession session)
	{
		ChangeTableResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		if(player.getClassicGame() == null)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.classic_换桌,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new ChangeTableResponse(ChangeTableResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.changeTable(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家准备)
	public void playerReady(ChannelHandlerContext ctx,PlayerReadyRequest request, MySession session)
	{
		PlayerReadyResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		if(player.getClassicGame() == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家准备,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new PlayerReadyResponse(PlayerReadyResponse.SUCCESS);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.ready(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家全压)
	public void allIn(ChannelHandlerContext ctx,AllInRequest request, MySession session)
	{
		AllInResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		if(player.getClassicGame() == null || !player.getClassicGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new AllInResponse(AllInResponse.SUCCESS);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.allIn(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家弃牌)
	public void fold(ChannelHandlerContext ctx,FoldRequest request, MySession session)
	{
		FoldResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家弃牌,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new FoldResponse(FoldResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if(!classicGame.isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家弃牌,"房间状态错误中,player:"+player.getSimpleData()+",state:"+classicGame.getState());
			response = new FoldResponse(FoldResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.fold(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家比牌)
	public void comparerPoker(ChannelHandlerContext ctx,ComparerPokerRequest request, MySession session)
	{
		ComparerPokerResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null) 
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家比牌,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new ComparerPokerResponse(ComparerPokerResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if(!classicGame.isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家比牌,"房间状态错误中,player:"+player.getSimpleData()+",state:"+classicGame.getState());
			response = new ComparerPokerResponse(ComparerPokerResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.comparerPoker(player,request.pos);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家离开)
	public void leaveClassicGame(ChannelHandlerContext ctx, LeaveClassicGameRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		LeaveClassicGameResponse response = null;
		
		if(player.getClassicGame() == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家离开,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new LeaveClassicGameResponse(LeaveClassicGameResponse.ERROR_不在游戏中);
			player.sendResponse(response);
			return;
		}
		
		response = new LeaveClassicGameResponse(LeaveClassicGameResponse.SUCCESS);
		player.sendResponse(response);
		classicGameService.leaveRoom(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家加注)
	public void raiseBet(ChannelHandlerContext ctx, RaiseBetRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		RaiseBetResponse response = null;
		
		if(player.getClassicGame() == null || !player.getClassicGame().isAction())
		{
			if(isDebug)
			{
				int state = -999;
				if(player.getClassicGame() != null)
				{
					state = player.getClassicGame().getState();
				}
				Tool.print_debug_level0(MsgTypeEnum.classic_玩家加注,"ERROR_不在比赛中,state:"+state+",player:"+player.getSimpleData());
			}
			
			response = new RaiseBetResponse(RaiseBetResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.raiseBet(player,request.betNum);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家跟注)
	public void followBet(ChannelHandlerContext ctx, FollowBetRequest request, MySession session)
	{
		FollowBetResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家跟注,"ERROR_不在比赛中,player:"+player.getSimpleData());
			
			response = new FollowBetResponse(FollowBetResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if(!classicGame.isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家跟注,"房间状态错误中,player:"+player.getSimpleData()+",state:"+classicGame.getState());
			response = new FollowBetResponse(FollowBetResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		classicGameService.playerFollowBet(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_玩家看牌)
	public void checkPoker(ChannelHandlerContext ctx, CheckPokerRequest request, MySession session)
	{
		CheckPokerResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家看牌,"ERROR_不在比赛中,player:"+player.getSimpleData());
			
			response = new CheckPokerResponse(CheckPokerResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if(!classicGame.isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家看牌,"房间状态错误中,player:"+player.getSimpleData()+",state:"+classicGame.getState());
			response = new CheckPokerResponse(CheckPokerResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		classicGameService.checkPoker(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.classic_进入新手场)
	public void enterBeginner(ChannelHandlerContext ctx, EnterBeginnerRequest request, MySession session)
	{
		EnterBeginnerResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}

//		if (player.getCoins() >= 100000)
//		{
//			Tool.print_debug_level0(MsgTypeEnum.classic_进入新手场,"ERROR_金币过多,player:"+player.getSimpleData());
//			response = new EnterBeginnerResponse(EnterBeginnerResponse.ERROR_金币过多);
//			player.sendResponse(response);
//			return;
//		}
		
		int cost = classicGameService.getCost(request.type);
		if (player.getCoins() < cost)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_进入新手场,"ERROR_金币不足,player:"+player.getSimpleData());
			response = new EnterBeginnerResponse(EnterBeginnerResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}

		classicGameService.addBeginnerList(player,request.type);
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