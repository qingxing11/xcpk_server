package com.wt.naval.api;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.Response;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;
import com.wt.xcpk.killroom.ApplicationKillRoomBankerRequest;
import com.wt.xcpk.killroom.ApplicationKillRoomBankerResponse;
import com.wt.xcpk.killroom.BankerListResponse;
import com.wt.xcpk.killroom.EnterKillRoomResponse;
import com.wt.xcpk.killroom.GetJackpotRequest;
import com.wt.xcpk.killroom.GetJackpotResponse;
import com.wt.xcpk.killroom.KillRoomGardRedEnvelopeRequest;
import com.wt.xcpk.killroom.KillRoomGardRedEnvelopeResponse;
import com.wt.xcpk.killroom.KillRoomGetAllRedRequest;
import com.wt.xcpk.killroom.KillRoomPayBetRequest;
import com.wt.xcpk.killroom.KillRoomPayBetResponse;
import com.wt.xcpk.killroom.KillRoomSendRedEnvelopeOverResponse;
import com.wt.xcpk.killroom.KillRoomSendRedEnvelopeRequest;
import com.wt.xcpk.killroom.KillRoomSendRedEnvelopeResponse;
import com.wt.xcpk.killroom.KillRoomSitDownRequest;
import com.wt.xcpk.killroom.KillRoomSitDownResponse;
import com.wt.xcpk.killroom.KillRoomStandUpResponse;
import com.wt.xcpk.killroom.LeaveKillRoomResponse;
import com.wt.xcpk.killroom.RedEnvelopeInfo;
import com.wt.xcpk.killroom.ResignKillRoomBankerResponse;
import com.wt.xcpk.killroom.push.Push_killRoomOtherPlayerResignBanker;
import com.wt.xcpk.killroom.push.Push_killRoomRedEnvelope;
import com.wt.xcpk.zjh.killroom.KillRoomService;
import com.yt.xcpk.killroomSendRedEnvelope.KillRoomSendRedEnvelopeService;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.xcpk.killroom") @Service public class ApiKillRoom
{
	private static boolean isDebug = false;
	@Autowired
	private KillRoomService killRoomService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private KillRoomSendRedEnvelopeService sendRedEmvelope;

	@Protocol(msgType = MsgTypeEnum.KillRoom_获取奖池)
	public void getJackpot(ChannelHandlerContext ctx, GetJackpotRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		GetJackpotResponse response = new GetJackpotResponse(GetJackpotResponse.SUCCESS, killRoomService.getJackpotData());
		player.sendResponse(response);
	}
	
	@Protocol(msgType = MsgTypeEnum.KillRoom_进入通杀场)
	public void enterKillRoom(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		EnterKillRoomResponse response = null;
		Player player = playerService.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new EnterKillRoomResponse(EnterKillRoomResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		// * 房间状态
		// * 当前庄家
		// * 座位玩家
		// * 上庄列表
		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.KillRoom_进入通杀场, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "进入通杀场");
		killRoomService.enterRoom(player);
		
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_离开通杀场)
	public void leaveKillRoom(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		LeaveKillRoomResponse response = null;
		Player player = playerService.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new LeaveKillRoomResponse(LeaveKillRoomResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.KillRoom_离开通杀场, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "离开通杀场");
		if (!killRoomService.leaveRoom(player))
		{
			if (isDebug || true)
				Tool.print_debug_level0(MsgTypeEnum.KillRoom_离开通杀场, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "离开通杀场,不在房间中");
			response = new LeaveKillRoomResponse(LeaveKillRoomResponse.ERROR_不在房间中);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		response = new LeaveKillRoomResponse(LeaveKillRoomResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_通杀场下注)
	public void killRoomPayBet(ChannelHandlerContext ctx, KillRoomPayBetRequest request, MySession session)
	{
		// KillRoomPayBetRequest request = (KillRoomPayBetRequest)obj;
		KillRoomPayBetResponse response = null;

		Player player = playerService.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new KillRoomPayBetResponse(KillRoomPayBetResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (player.getCoins() < request.chipNum)
		{
			response = new KillRoomPayBetResponse(KillRoomPayBetResponse.ERROR_金币不足);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (player.getUserId() == killRoomService.getRoom().getBankerPlayer().getUserId())
		{
			response = new KillRoomPayBetResponse(KillRoomPayBetResponse.ERROR_庄家不能下注);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下注, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",通杀场下注,chip:" + request.chipNum + ",位置:" + request.pos);
		boolean isSuccess = killRoomService.payChip(player, request.pos, request.chipNum);
		if (!isSuccess)
		{
			response = new KillRoomPayBetResponse(KillRoomPayBetResponse.ERROR_单边下注超标);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		response = new KillRoomPayBetResponse(KillRoomPayBetResponse.SUCCESS, request.pos, request.chipNum);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_选座坐下)
	public void killRoomSitDown(ChannelHandlerContext ctx, KillRoomSitDownRequest request, MySession session)
	{
		KillRoomSitDownResponse response = null;

		Player player = playerService.getPlayer(session.getUserId());
		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.KillRoom_选座坐下, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",选座坐下,pos:" + request.pos);

		if (player.getInsideRoom().isBanker(player.getUserId()))
		{
			response = new KillRoomSitDownResponse(KillRoomSitDownResponse.ERROR_庄家不能坐下);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		int code = killRoomService.sitDown(player, request.pos);
		switch (code)
		{
			case Response.SUCCESS:
				response = new KillRoomSitDownResponse(KillRoomSitDownResponse.SUCCESS, request.pos);
				GameServerHelper.sendResponse(ctx, response);
				break;

			default:
				response = new KillRoomSitDownResponse(code, request.pos);
				GameServerHelper.sendResponse(ctx, response);
				break;
		}
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_从座位站起)
	public void killRoomStandUp(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		KillRoomStandUpResponse response = null;

		Player player = playerService.getPlayer(session.getUserId());
		int pos = 0;
		if (player != null)
		{
			pos = player.getKillRoomPos();
		}
		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.KillRoom_从座位站起, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",站起,pos:" + player.getKillRoomPos());
		boolean isSuccess = killRoomService.standUp(player);
		if (isSuccess)
		{
			response = new KillRoomStandUpResponse(KillRoomStandUpResponse.SUCCESS, pos);
			GameServerHelper.sendResponse(ctx, response);
		}
		else
		{
			response = new KillRoomStandUpResponse(KillRoomStandUpResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
		}
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_通杀场上庄)
	public void applicationKillRoomBanker(ChannelHandlerContext ctx, ApplicationKillRoomBankerRequest request, MySession session)
	{
		ApplicationKillRoomBankerResponse response = null;

		Player player = playerService.getPlayer(session.getUserId());
		if (player.getCoins() < killRoomService.getConfigKillroom().getApplicationBankerCoins())
		{
			response = new ApplicationKillRoomBankerResponse(ApplicationKillRoomBankerResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}

		int code = killRoomService.applicationKillRoomBanker(player);
		if (code != Response.SUCCESS) {
			response = new ApplicationKillRoomBankerResponse(code);
			return;
		}

		ArrayList<PlayerSimpleData> list = killRoomService.getRoom().getBankerList();
		response = new ApplicationKillRoomBankerResponse(ApplicationKillRoomBankerResponse.SUCCESS, list);
		player.sendResponse(response);
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_通杀场下庄)
	public void resignKillRoomBanker(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		ResignKillRoomBankerResponse response = null;
		Player player = playerService.getPlayer(session.getUserId());

		Room room = killRoomService.getRoom();
		// if(room.getBankerPlayer().getUserId() != player.getUserId())
		// {
		// Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下庄,"玩家:"+player.getNickName()+",userId:"+player.getUserId()+",不是庄家,现在庄家id:"+room.getBankerPlayer().getUserId());
		// response = new
		// ResignKillRoomBankerResponse(ResignKillRoomBankerResponse.ERROR_不是庄家);
		// GameServerHelper.sendResponse(ctx, response);
		// return;
		// }

		if (room.isBanker(player.getUserId()))
		{
			Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下庄, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",已经是庄家,下回合下庄!");
			room.setResignBanker();
			response = new ResignKillRoomBankerResponse(ResignKillRoomBankerResponse.SUCCESS);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		else
		{
			if (room.removeBankerListExist(player.getUserId()))
			{
				for (Player iterable_element : room.getAllPlayer())
				{
					iterable_element.sendResponse(new Push_killRoomOtherPlayerResignBanker(player.getUserId()));
				}
				
				Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下庄, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",不是庄家,在上庄列表!");
				response = new ResignKillRoomBankerResponse(ResignKillRoomBankerResponse.SUCCESS);
			}
			else
			{
				Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下庄, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",不是庄家,不在上庄列表!");
				response = new ResignKillRoomBankerResponse(ResignKillRoomBankerResponse.ERROR_不是庄家);
			}
			GameServerHelper.sendResponse(ctx, response);
		}
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_庄家列表)
	public void bankerList(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		BankerListResponse response = null;
		ArrayList<PlayerSimpleData> list = killRoomService.getRoom().getBankerList();
		response = new BankerListResponse(Response.SUCCESS, list);
		GameServerHelper.sendResponse(ctx, response);
	}
	@Protocol(msgType = MsgTypeEnum.KillRoom_获取所有红包)
	public void  killRoomGetAllRed(ChannelHandlerContext ctx, KillRoomGetAllRedRequest request, MySession session)
	{
		Player player=playerService.getPlayer(session.getUserId());
		sendRedEmvelope.giveEnterRoomPlayerSendShengYuRedEnvelopeInfo(player);
	}
	
	
	@Protocol(msgType = MsgTypeEnum.KillRoom_发红包)
	public void killRoomSendRedEnvelope(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		KillRoomSendRedEnvelopeRequest request = (KillRoomSendRedEnvelopeRequest) obj;
		KillRoomSendRedEnvelopeResponse response = null;
		Player banker = killRoomService.getRoom().getBankerPlayer();
		if (banker == null)
		{
			response = new KillRoomSendRedEnvelopeResponse(KillRoomSendRedEnvelopeResponse.您不是当前庄家_不能发红包);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (banker.getUserId() != request.userId)
		{
			response = new KillRoomSendRedEnvelopeResponse(KillRoomSendRedEnvelopeResponse.您不是当前庄家_不能发红包);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (banker.getCoins() < request.redEnvelopeValue)
		{
			response = new KillRoomSendRedEnvelopeResponse(KillRoomSendRedEnvelopeResponse.玩家金币不足);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		int redState = sendRedEmvelope.returnSendRedState();
		if (redState == 2)
		{
			response = new KillRoomSendRedEnvelopeResponse(KillRoomSendRedEnvelopeResponse.本轮红包未结束_不能发红包);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		Tool.print_subCoins(banker.getNickName(),request.redEnvelopeValue,"发红包",banker.getCoins());
		banker.subCoinse(request.redEnvelopeValue);
		sendRedEmvelope.setSendRedStateToNotCanSend();
		ArrayList<RedEnvelopeInfo> redInfo = sendRedEmvelope.getRenEnvelopeInfo(request.userId, request.redEnvelopeValue, 30, 50000, 5000);
		response = new KillRoomSendRedEnvelopeResponse(KillRoomSendRedEnvelopeResponse.SUCCESS, redInfo, redState,
				session.getUserId());
		Collection<Player> list_roomPlayers = killRoomService.getRoom().getAllPlayer();
		if (list_roomPlayers != null)
		{
			for (Player roomPlayer : list_roomPlayers)
			{
				// 红包信息计算成功后发送给所有玩家
				roomPlayer.sendResponse(response);
			}
		}
		
		playerService.sendToAll(new Push_killRoomRedEnvelope(banker.getNickName(),request.redEnvelopeValue));
	}

	@Protocol(msgType = MsgTypeEnum.KillRoom_抢红包)
	public void killRoomGardRedEnvelope(ChannelHandlerContext ctx, KillRoomGardRedEnvelopeRequest request, MySession session)
	{
		KillRoomGardRedEnvelopeResponse response = null;
		Player player = playerService.getPlayer(session.getUserId());
//		
//		if (player.getUserId() == killRoomService.getRoom().getBankerPlayer().getUserId())
//		{
//			response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.庄家不能抢红包);
//			GameServerHelper.sendResponse(ctx, response);
//			return;
//		}
		ArrayList<RedEnvelopeInfo> list_alreadyHave = sendRedEmvelope.getCurrentShengYuRedEnvelope();
		if (list_alreadyHave == null || list_alreadyHave.size() <= 0)
		{
			response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.您的手慢了_红包已被抢完);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		Tool.print_debug_level0("剩余可抢红包为   list_alreadyHave :" + list_alreadyHave.size());

		ArrayList<RedEnvelopeInfo> list_alreadyWasRobbed = sendRedEmvelope.getCurrentWasRobbedRedEnvelope();

		if (list_alreadyWasRobbed != null && list_alreadyWasRobbed.size() > 0)
		{
			Tool.print_debug_level0("已经被抢红包为   list_alreadyWasRobbed :" + list_alreadyWasRobbed.size());
			synchronized (list_alreadyWasRobbed)
			{				
				for (RedEnvelopeInfo redEnvelope : list_alreadyWasRobbed)
				{
					if (redEnvelope.redEnvelopeIndex == request.redEnvelopeIndex && redEnvelope.redEnvelopeValue == request.redEnvelopeValue)
					{
						response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.您的手慢了_当前红包被抢走了);
						GameServerHelper.sendResponse(ctx, response);
						return;
					}
				}
			}
			
		}

		ArrayList<RedEnvelopeInfo> redInfo = sendRedEmvelope.getCurrentBureauAllRedEnvelopeInfo();

		RedEnvelopeInfo requsetRedEnvelope = null;
		for (RedEnvelopeInfo redEnvelope : redInfo)
		{
			if (redEnvelope.redEnvelopeIndex == request.redEnvelopeIndex && redEnvelope.redEnvelopeValue == request.redEnvelopeValue)
			{
				requsetRedEnvelope = redEnvelope;
				break;
			}
		}

		if (requsetRedEnvelope == null)
		{
			response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.请求错误_您当前抢的红包不存在);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		sendRedEmvelope.GrabRedEnvelope(requsetRedEnvelope);
		Tool.print_coins(player.getNickName(),request.redEnvelopeValue,"抢红包",player.getCoins());
		player.addCoins(request.redEnvelopeValue);
		response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.SUCCESS, player.getUserId(), requsetRedEnvelope);
		Collection<Player> list_roomPlayers = killRoomService.getRoom().getAllPlayer();
		if (list_roomPlayers != null)
		{
			for (Player roomPlayer : list_roomPlayers)
			{
				// 红包信息计算成功后发送给所有玩家
				if (roomPlayer.isOnline())
				{
					roomPlayer.sendResponse(response);
				}
			}
		}

		if (list_alreadyHave != null && list_alreadyWasRobbed != null && redInfo != null)
		{
			if (list_alreadyWasRobbed.size() == redInfo.size())
			{
				sendRedEmvelope.setSendRedStateToCanSend();
				KillRoomSendRedEnvelopeOverResponse sendRedEnvelopeOverResponse = new KillRoomSendRedEnvelopeOverResponse();
				if (list_roomPlayers != null)
				{
					for (Player roomPlayer : list_roomPlayers)
					{
						// 红包信息计算成功后发送给所有玩家
						if (roomPlayer.isOnline())
						{
							roomPlayer.sendResponse(sendRedEnvelopeOverResponse);
						}
					}
				}
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