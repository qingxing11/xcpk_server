package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.xcpk.manypepol.EnterManyPepolRoomRequest;
import com.wt.cmd.xcpk.manypepol.LeaveManyPepolRoomRequest;
import com.wt.cmd.xcpk.manypepol.LeaveManyPepolRoomResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolFollowBetRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolFollowBetResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomAllInRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomAllInResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomCheckPokerRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomCheckPokerResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomComparerPokerRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomComparerPokerResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomFoldRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomFoldResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomPayBetRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomPayBetResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomRaiseBetRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomRaiseBetResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomSitdownRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomSitdownResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomStandUpRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomStandUpResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomTableListRequest;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomTableListResponse;
import com.wt.cmd.xcpk.manypepol.MprGetJackpotRequest;
import com.wt.cmd.xcpk.manypepol.MprGetJackpotResponse;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.manypepol.ManyPepolService;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.xcpk.manypepol") 
@Service
public class ApiManyPepolGame
{
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private ManyPepolService manyPepolService;
	
	@Protocol(msgType = MsgTypeEnum.manypepol_获取奖池)
	public void mprGetJackpot(ChannelHandlerContext ctx,MprGetJackpotRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		MprGetJackpotResponse response = new MprGetJackpotResponse(MprGetJackpotResponse.SUCCESS, manyPepolService.getJackpotData());
		player.sendResponse(response);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家离开)
	public void leaveManyPepolRoom(ChannelHandlerContext ctx,LeaveManyPepolRoomRequest request, MySession session)
	{
		LeaveManyPepolRoomResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		manyPepolService.leaveRoom(player,request.isTV);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_申请下桌)
	public void manyPepolRoomStandUp(ChannelHandlerContext ctx,ManyPepolRoomStandUpRequest request, MySession session)
	{
		ManyPepolRoomStandUpResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		if(player.getManyPepolGame() == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new ManyPepolRoomStandUpResponse(ManyPepolRoomStandUpResponse.ERROR_不在游戏中);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.mGameStandUp(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家全压)
	public void manyPepolRoomAllIn(ChannelHandlerContext ctx,ManyPepolRoomAllInRequest request, MySession session)
	{
		ManyPepolRoomAllInResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		if(player.getManyPepolGame() == null  || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new ManyPepolRoomAllInResponse(ManyPepolRoomAllInResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.allIn(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家弃牌)
	public void manyPepolRoomFold(ChannelHandlerContext ctx,ManyPepolRoomFoldRequest request, MySession session)
	{
		ManyPepolRoomFoldResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		if(player.getManyPepolGame() == null || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家弃牌,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new ManyPepolRoomFoldResponse(ManyPepolRoomFoldResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if(player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家弃牌,"ERROR_没轮到行动,isDie,player:"+player.getSimpleData());
			response = new ManyPepolRoomFoldResponse(ManyPepolRoomFoldResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		manyPepolService.fold(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家比牌)
	public void manyPepolRoomComparerPoker(ChannelHandlerContext ctx, ManyPepolRoomComparerPokerRequest request, MySession session)
	{
		ManyPepolRoomComparerPokerResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		
		if(player.getManyPepolGame() == null || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家比牌,"ERROR_不在游戏中,player:"+player.getSimpleData());
			response = new ManyPepolRoomComparerPokerResponse(ManyPepolRoomComparerPokerResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if(player.getManyGamePos() != player.getManyPepolGame().getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家比牌,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomComparerPokerResponse(ManyPepolRoomComparerPokerResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.comparerPoker(player,request.pos,false);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_上桌列表)
	public void manyPepolRoomTableList(ChannelHandlerContext ctx, ManyPepolRoomTableListRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		ManyPepolRoomTableListResponse response = null;
		
		if(player.getManyPepolGame() == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_上桌列表,"ERROR_不在比赛中,player:"+player.getSimpleData());
			response = new ManyPepolRoomTableListResponse(ManyPepolRoomTableListResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		response = new ManyPepolRoomTableListResponse(ManyPepolRoomTableListResponse.SUCCESS, manyPepolService.getTableList());
		player.sendResponse(response);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_闲家下注)
	public void manyPepolRoomPayBet(ChannelHandlerContext ctx, ManyPepolRoomPayBetRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		ManyPepolRoomPayBetResponse response = null;
		
		if(player.getManyPepolGame() == null || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_闲家下注,"ERROR_不在比赛中,player:"+player.getSimpleData());
			response = new ManyPepolRoomPayBetResponse(ManyPepolRoomPayBetResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.payBet(player,request.pos,request.betNum);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家加注)
	public void manyPepolRoomRaiseBet(ChannelHandlerContext ctx, ManyPepolRoomRaiseBetRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		ManyPepolRoomRaiseBetResponse response = null;
		
		if(player.getManyPepolGame() == null || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家加注,"ERROR_不在比赛中,player:"+player.getSimpleData());
			response = new ManyPepolRoomRaiseBetResponse(ManyPepolRoomRaiseBetResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.raiseBet(player,request.betNum);
	
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家跟注)
	public void manyPepolFollowBet(ChannelHandlerContext ctx, ManyPepolFollowBetRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		ManyPepolFollowBetResponse response = null;
		
		if(player.getManyPepolGame() == null || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家跟注,"ERROR_不在比赛中,player:"+player.getSimpleData());
			
			response = new ManyPepolFollowBetResponse(ManyPepolFollowBetResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.playerFollowBet(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家看牌)
	public void manyPepolRoomCheckPoker(ChannelHandlerContext ctx, ManyPepolRoomCheckPokerRequest request, MySession session)
	{
		ManyPepolRoomCheckPokerResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		if(player.getManyPepolGame() == null || !player.getManyPepolGame().isAction())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家看牌,"ERROR_不在比赛中,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomCheckPokerResponse(ManyPepolRoomCheckPokerResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		
		if( player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家看牌,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomCheckPokerResponse(ManyPepolRoomCheckPokerResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		manyPepolService.checkPoker(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_玩家进入)
	public void enterManyPepolRoom(ChannelHandlerContext ctx, EnterManyPepolRoomRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		
		manyPepolService.enter(player);
	}
	
	@Protocol(msgType = MsgTypeEnum.manypepol_申请上桌)
	public void manyPepolRoomSitdown(ChannelHandlerContext ctx, ManyPepolRoomSitdownRequest request, MySession session)
	{
		ManyPepolRoomSitdownResponse response = null;
		
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}

//		int cost = 0;//上桌要求
		if(player.getCoins() < manyPepolService.getWanrenConfig().getApplyCoins())
		{
			response = new ManyPepolRoomSitdownResponse(ManyPepolRoomSitdownResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}
		
		Tool.print_debug_level0(MsgTypeEnum.manypepol_申请上桌,player.getNickName()+",state:"+player.getState());
		manyPepolService.sitdown(player);
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
