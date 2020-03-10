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
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;
import com.yt.cmd.fruitMachine.CancleContinueXiaZhuResponse;
import com.yt.cmd.fruitMachine.EnterFruitMachineResponse;
import com.yt.cmd.fruitMachine.FruitBankerListResponse;
import com.yt.cmd.fruitMachine.FruitDownBankerResponse;
import com.yt.cmd.fruitMachine.FruitMachineXiaZhuRequest;
import com.yt.cmd.fruitMachine.FruitMachineXiaZhuResponse;
import com.yt.cmd.fruitMachine.FruitUpBankerResponse;
import com.yt.cmd.fruitMachine.IsContinueXiaZhuResponse;
import com.yt.cmd.fruitMachine.LeaveFruitMachineResponse;
import com.yt.xcpk.fruitMachine.FruitMachineService;

import data.define.UpBankerCoin;
import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.yt.cmd.fruitMachine") @Service public class ApiFruitMachine
{
	private static final boolean isDebugBankerDown = true;
	private static final boolean isDebug = false;

	@Autowired
	private FruitMachineService fruitMachineService;

	@Autowired
	private PlayerService playerImpl;

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

	@Protocol(msgType = MsgTypeEnum.EnterFruitMechine)
	public void enterFruitMachine(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		EnterFruitMachineResponse response = null;
		Player player = playerImpl.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new EnterFruitMachineResponse(EnterFruitMachineResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.EnterFruitMechine, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "进入水果机");
		fruitMachineService.enterRoom(player);
		Room room = fruitMachineService.getRoom();
		Player playerBanker = room.getBankerPlayer();
		int state = fruitMachineService.getRoomState();
		long stateTime = fruitMachineService.getStateTime();
		long jiangPoolCoins = room.getJackpot();
		Collection<Integer> list_xiaZhuKey = fruitMachineService.getCurrentAllXiaZhuKey();
		Collection<Integer> list_xiaZhuValue = fruitMachineService.getCurrentAllXiaZhuValue();
		ArrayList<String> list_history = fruitMachineService.getCurrentHistory();
		if (playerBanker == null)
		{
			response = new EnterFruitMachineResponse(EnterFruitMachineResponse.SUCCESS, null, state, stateTime, jiangPoolCoins, list_xiaZhuKey, list_xiaZhuValue, list_history,fruitMachineService.getRoundIndex());
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		PlayerSimpleData playerBankerData = playerBanker.getSimpleData();
		response = new EnterFruitMachineResponse(EnterFruitMachineResponse.SUCCESS, playerBankerData, state, stateTime, jiangPoolCoins, list_xiaZhuKey, list_xiaZhuValue, list_history,fruitMachineService.getRoundIndex());
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.LeaveFruitMechine)
	public void leaveFruitMachine(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		LeaveFruitMachineResponse response = null;
		Player player = playerImpl.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new LeaveFruitMachineResponse(LeaveFruitMachineResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.LeaveFruitMechine, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "离开水果机");
		fruitMachineService.leaveRoom(player);
		response = new LeaveFruitMachineResponse(LeaveFruitMachineResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.Fruit_ContinueXiaZhu)
	public void isContinueXiaZhu(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		IsContinueXiaZhuResponse response = null;
		Player player = playerImpl.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new IsContinueXiaZhuResponse(IsContinueXiaZhuResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.Fruit_ContinueXiaZhu, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "水果机请求续押");
		fruitMachineService.setPlayerContinueXiaZhu(player, true);
		response = new IsContinueXiaZhuResponse(IsContinueXiaZhuResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.Fruit_CancelContinueXiaZhu)
	public void cancleContinueXiaZhu(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		CancleContinueXiaZhuResponse response = null;
		Player player = playerImpl.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new CancleContinueXiaZhuResponse(CancleContinueXiaZhuResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.Fruit_CancelContinueXiaZhu, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "水果机取消续押");
		fruitMachineService.setPlayerContinueXiaZhu(player, false);
		response = new CancleContinueXiaZhuResponse(CancleContinueXiaZhuResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.Fruit_requestBankerList)
	public void fruitBankerList(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		FruitBankerListResponse response = null;
		Player player = playerImpl.getPlayer(session.getUserId());
		if (player == null)
		{
			response = new FruitBankerListResponse(FruitBankerListResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.Fruit_requestBankerList, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "请求庄家列表");
		ArrayList<PlayerSimpleData> list_banker = fruitMachineService.getRoom().getBankerList();
		response = new FruitBankerListResponse(FruitBankerListResponse.SUCCESS, list_banker);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.FruitUpBanker)
	public void fruitUpBanker(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		FruitUpBankerResponse response = null;
		Player player = playerImpl.getPlayer(session.getUserId());
		if (player == null)
		{
			// 玩家不在线/不存在
			response = new FruitUpBankerResponse(FruitUpBankerResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.FruitUpBanker, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + "下注");

		Room room = fruitMachineService.getRoom();
		Player playerBanker = room.getBankerPlayer();
		if (playerBanker != null && playerBanker.getUserId() == player.getUserId())
		{
			if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.FruitUpBanker, "玩家:" + player.getNickName() + "，您已是当前房价庄家！");
			response = new FruitUpBankerResponse(FruitUpBankerResponse.您已是当前房间庄家);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		long playerCoin = player.getCoins();
		if (playerCoin < UpBankerCoin.Up)
		{
			if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.FruitUpBanker, "玩家:" + player.getNickName() + "，您未到达上庄条件！金币大于5000万");
			response = new FruitUpBankerResponse(FruitUpBankerResponse.未达到上装条件);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		ArrayList<PlayerSimpleData> list_bankers = room.getBankerList();
		if (list_bankers != null && list_bankers.size() == fruitMachineService.getRoom().getBankerListMaxNum())
		{
			if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.FruitUpBanker, "玩家:" + player.getNickName() + "，申请上庄失败");
			response = new FruitUpBankerResponse(FruitUpBankerResponse.申请人数已达上限);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		boolean isSuccess = fruitMachineService.requestBanker(player);
		if (isSuccess == false)
		{
			if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.FruitUpBanker, "玩家:" + player.getNickName() + "，申请上庄失败");
			response = new FruitUpBankerResponse(FruitUpBankerResponse.申请上庄失败);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		PlayerSimpleData banker = null;
		room.sortBankerList();
		if (room.getAllPlayer() != null && room.getAllPlayer().size() != 0)
		{

			// if (room.getBankerPlayer() == null) {
			// room.changeBankerByList();
			// room.changeBanker(player);
			// }
			// if (room.getBankerPlayer().getUserId() ==
			// player.getUserId()) {
			// banker = player.getSimpleData();
			// } else {
			// banker = room.getBankerPlayer().getSimpleData();
			// }
			Collection<Player> list_roomPlayers = room.getAllPlayer();
			if (list_roomPlayers != null)
			{
				for (Player roomPlayer : list_roomPlayers)
				{
					if (room.getBankerPlayer() == null)
					{
						banker = null;
					}
					else
					{
						banker = room.getBankerPlayer().getSimpleData();
					}
					fruitMachineService.push_changeBankerListInfo(roomPlayer, room.getBankerList(), banker);
					// 上庄成功后给房间内所有玩家推送消息
					if (isDebug)
						Tool.print_debug_level0(MsgTypeEnum.FruitUpBanker, "庄家为 ：" + player.getSimpleData());
				}
			}
		}
		// PlayerSimpleData playerSelf = player.getSimpleData();
		response = new FruitUpBankerResponse(FruitUpBankerResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);

	}

	/**
	 * 下注
	 * 
	 * @param ctx
	 * @param obj
	 * @param session
	 */
	@Protocol(msgType = MsgTypeEnum.FruitDownBanker)
	public void fruitDownBanker(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		FruitDownBankerResponse response = null;
		Player player = playerImpl.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		// TODO 数据处理 结算
 		
		if (isDebugBankerDown)Tool.print_debug_level0(MsgTypeEnum.FruitDownBanker, "玩家請求下莊:" + player.getNickName());
		
		Room room = fruitMachineService.getRoom();
		
		Player banker = room.getBankerPlayer();
		if (banker == null || banker.getUserId() != player.getUserId())//不是庄家
		{
			ArrayList<PlayerSimpleData> list_banker = room.getBankerList();//判断是否在庄家列表
			if (isDebugBankerDown)Tool.print_debug_level0(MsgTypeEnum.FruitDownBanker, "不是庄家,userId:"+player.getUserId()+",list_banker.size():"+list_banker.size());
			if(list_banker.size() > 0)
			{
				synchronized (list_banker)
				{
					for (int j = 0 ; j < list_banker.size() ; j++)
					{
						PlayerSimpleData playerSimpleData = list_banker.get(j);
						if(player.getUserId() == playerSimpleData.getUserId())
						{
							room.removeBankerListExist(player.getUserId());
							 
							Collection<Player> list_allPlayer = room.getAllPlayer();
							if(list_allPlayer != null && list_allPlayer.size() > 0)
							{
								for (Player roomPlayer : list_allPlayer)
								{
									roomPlayer.sendResponse(new FruitBankerListResponse(FruitBankerListResponse.SUCCESS, list_banker));
								}
							}
							
							response = new FruitDownBankerResponse(FruitDownBankerResponse.SUCCESS);
							player.sendResponse(response);
							return;
						}
					}
				}
			}
			
			response = new FruitDownBankerResponse(FruitDownBankerResponse.你不是当前庄家_不能申请下庄);
			player.sendResponse(response);
			return;
		}
		room.setResignBanker();
		// 下庄后重新设置的庄家 推送给所有玩家 同时从庄家列表中移除
		Collection<Player> list_roomPlayers = room.getAllPlayer();
		if (list_roomPlayers != null)
		{
			for (Player roomPlayer : list_roomPlayers)
			{
				// 上庄成功后给房间内所有玩家推送消息
				fruitMachineService.push_changeBankerListInfo(roomPlayer, room.getBankerList(), banker.getSimpleData());
			}
		}
		response = new FruitDownBankerResponse(FruitDownBankerResponse.SUCCESS);
		player.sendResponse(response);
	}

	/**
	 * 下注
	 * 
	 * @param ctx
	 * @param obj
	 * @param session
	 */
	@Protocol(msgType = MsgTypeEnum.FruitMechine)
	public void fruitMachineXiaZhu(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		FruitMachineXiaZhuRequest request = (FruitMachineXiaZhuRequest) obj;
		FruitMachineXiaZhuResponse response = null;
		Player player = UserCache.getPlay(session.getUserId());
		if (player == null)
		{
			response = new FruitMachineXiaZhuResponse(FruitMachineXiaZhuResponse.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (player.getCoins() < request.fruitMachineValue)
		{
			response = new FruitMachineXiaZhuResponse(FruitMachineXiaZhuResponse.CoinNotEnough);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		// TODO 数据处理 结算
		if (isDebug)
			Tool.print_debug_level0(MsgTypeEnum.FruitMechine, "玩家:" + player.getNickName() + ",request.type:" + request.fruitMachineType + ",request.value:" + request.fruitMachineValue);

		fruitMachineService.payXiaZhuValue(player, request.fruitMachineType, request.fruitMachineValue);
		response = new FruitMachineXiaZhuResponse(FruitMachineXiaZhuResponse.SUCCESS);
		player.sendResponse(response);
	}
}
