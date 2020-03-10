package com.wt.naval.api;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.chat.AskFenestraeChatRequest;
import com.gjc.cmd.chat.AskFenestraeChatResponse;
import com.gjc.cmd.chat.AskShortCutRequest;
import com.gjc.cmd.chat.AskShortCutResponse;
import com.gjc.naval.vip.VipService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.Room;

import data.data.Configs;
import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.gjc.cmd.chat") @Service public class ApiChat
{

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

	@Autowired
	private PlayerService playerService;

	@Autowired
	private Configs configs;

	@Autowired
	private VipService vipService;

	@Protocol(msgType = MsgTypeEnum.Chat_快捷回复)
	public void askShortCut(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		AskShortCutRequest request = (AskShortCutRequest) obj;
		Player player = playerService.getPlayer(session);
		int userId = player.getUserId();
		String nikeName = player.getNickName();
		int vipLv = player.getVipLv();// 默认没有(0)

		if (!vipService.haveRoonChat(vipLv))
		{
			AskShortCutResponse response = new AskShortCutResponse(AskShortCutResponse.Error_暂无聊天功能);
			playerService.sendMsg(response, userId);
			return;
		}

		String info;
		if (configs.getDataShortCut().containsKey(request.infoIndex))
		{
			info = configs.getDataShortCut().get(request.infoIndex).Info;
		}
		else
		{
			Tool.print_error("没有找到配置表：" + request.infoIndex);
			info = "";
		}

		AskShortCutResponse response = new AskShortCutResponse(AskShortCutResponse.SUCCESS, userId, nikeName, vipLv, info);

		Room room = player.getInsideRoom();
		if (room != null && room.getAllPlayer() != null)
		{
			Collection<Player> list_allPlayers = room.getAllPlayer();
			for (Player other : list_allPlayers)
			{
				Tool.print_debug_level0("快捷回复，发送消息给：" + other.getNickName() + ",消息" + response);
				other.sendResponse(response);
			}
		}
		else
		{
			Tool.print_debug_level0("room!=null&&room.getAllPlayer()!=null");
		}
	}

	private int timeLimit = 6000;// 单位毫秒

	@Protocol(msgType = MsgTypeEnum.Chat_小窗聊天)
	public void askFenestraeChat(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		AskFenestraeChatRequest request = (AskFenestraeChatRequest) obj;

		Player player = playerService.getPlayer(session);
		int userId = player.getUserId();
		boolean enjoy = request.enjoy;
		boolean shutCut = request.shutCut;
		int shutCutIndex = request.shutCutIndex;
		int curRoom = request.curRoom;

		Tool.print_debug_level0("enjoy" + enjoy);
		// 时间限制
		long curTime = MyTimeUtil.getCurrTimeMM();
		// Tool.print_debug_level0("当前聊天时间："+player.getChatTime()+"，相差："+(curTime-player.getChatTime()));
		if (curTime - player.getChatTime() >= timeLimit)
		{
			player.setChatTime(curTime);
		}
		else
		{
			AskFenestraeChatResponse response = new AskFenestraeChatResponse(AskFenestraeChatResponse.Error_频繁发送);
			playerService.sendMsg(response, userId);
			return;
		}

		String nikeName = player.getNickName();
		int vipLv = player.getVipLv();// 默认没有(0)

		if (!vipService.haveRoonChat(vipLv))
		{
			AskFenestraeChatResponse response = new AskFenestraeChatResponse(AskFenestraeChatResponse.Error_暂无聊天功能);
			playerService.sendMsg(response, userId);
			return;
		}

		String info = request.info;
		int lv = player.gameData.playerLevel;
		int headId = player.gameData.headid;
		int roelId = player.gameData.getRoleId();

		AskFenestraeChatResponse response = new AskFenestraeChatResponse(AskFenestraeChatResponse.SUCCESS, userId, nikeName, vipLv, info, player.gameData.headIconUrl, lv, headId, roelId, enjoy, shutCut, shutCutIndex, curRoom,request.roomType);
		
//		Tool.print_debug_level0("聊天时获取房间,roomType:"+request.roomType);
		
		Room room = getRoom(request.roomType, player);
		
		if (room != null && room.getAllPlayer() != null)
		{
			Collection<Player> list_allPlayers = room.getAllPlayer();
			if (list_allPlayers.size() == 0)
			{
				Tool.print_error("房间竟然没有玩家 ！！！！！ list_allPlayers.size()==0");
			}
			
			for (Player other : list_allPlayers)
			{
//				Tool.print_debug_level0("小窗聊天，发送消息给：" + other.getNickName() + ",消息" + response);
				other.sendResponse(response);
			}
		}
		else
		{
			Tool.print_error("room==null&&room.getAllPlayer()==null");
		}
	}

	private Room getRoom(int roomType, Player player)
	{
		Room room = null;
		switch (roomType)
		{
			case AskFenestraeChatRequest.ROOM_万人场:
				room = player.getManyPepolGame().getRoom();
				if(player.getManyPepolGame() == null)
				{
					Tool.print_error("万人场聊天获取房间错误，房间空,player:"+player.getNickName());
				}
				else
				{
					room = player.getManyPepolGame().getRoom();
				}
				break;
				
			case AskFenestraeChatRequest.ROOM_经典:
				if(player.getClassicGame() == null)
				{
					Tool.print_error("经典场聊天获取房间错误，房间空,player:"+player.getNickName());
				}
				else
				{
					room = player.getClassicGame().getRoom();
				}
				break;
				
			case AskFenestraeChatRequest.ROOM_通杀:
			case AskFenestraeChatRequest.ROOM_水果机:
				room = player.getInsideRoom();
				break;
				

			default:
				break;
		}
		return room;
	}

}
