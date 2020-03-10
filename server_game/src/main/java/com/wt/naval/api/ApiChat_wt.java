package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.chat.ChargesEmojiRequest;
import com.wt.cmd.chat.ChargesEmojiResponse;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.Room;
import com.yt.xcpk.task.TaskService;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.chat") 
@Service
public class ApiChat_wt
{
	@Autowired
	private PlayerService playerService;
	@Autowired
	private TaskService taskImpl;
	@Protocol(msgType = MsgTypeEnum.CHAT_收费表情)
	public void chargesEmoji(ChannelHandlerContext ctx, ChargesEmojiRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.CHAT_收费表情,"错误，玩家不存在");
			return;
		}
		
		Room room = player.getInsideRoom();
		if(room == null)
		{
			player.sendResponse(new ChargesEmojiResponse(ChargesEmojiResponse.ERROR_不在游戏中));
			Tool.print_debug_level0(player.getNickName(),MsgTypeEnum.CHAT_收费表情,"错误，不在游戏中");
			return;
		}
		
		int costConis = getEmojiCost(request.emojiId);
		
		if(player.getCoins() < costConis)
		{
			player.sendResponse(new ChargesEmojiResponse(ChargesEmojiResponse.ERROR_金币不足));
			Tool.print_debug_level0(player.getNickName(),MsgTypeEnum.CHAT_收费表情,"ERROR_金币不足");
			return;
		}
		Tool.print_debug_level0(player.getNickName(),MsgTypeEnum.CHAT_收费表情,"玩家发送收费表情，此时金币:"+player.getCoins());
		room.chargesEmoji(player.getUserId(),request.emojiId,request.toUserId,request.roomId);
		
		Tool.print_subCoins(player.getNickName(),costConis,"收费表情",player.getCoins());
		player.subCoinse(costConis);
		int getCoinse = (int) (costConis * 0.8f);
		
		Player toPlayer = playerService.getPlayer(request.toUserId);
		if(toPlayer == null || !toPlayer.isOnline())
		{
			Tool.print_debug_level0(player.getNickName(),MsgTypeEnum.CHAT_收费表情,"目标玩家不在线，toUserId:"+request.toUserId+",获得金币:"+getCoinse);
			PlayerDaoImpl.addPlayerCoins(getCoinse, request.toUserId);
		}
		else
		{
			Tool.print_coins(player.getNickName(),getCoinse,"收费表情",player.getCoins());
			toPlayer.addCoins(getCoinse);
			Tool.print_debug_level0(player.getNickName(),MsgTypeEnum.CHAT_收费表情,"目标玩家在线，toUserId:"+request.toUserId+",获得金币:"+getCoinse);
		}
		taskImpl.sendEnjoyTask(player.getUserId(), 1);
		Tool.print_debug_level0(player.getNickName(),MsgTypeEnum.CHAT_收费表情,"表情:"+request.emojiId+",目标玩家:"+request.toUserId+",当前金币:"+player.getCoins());
		
		player.sendResponse(new ChargesEmojiResponse(ChargesEmojiResponse.SUCCESS,request.emojiId,request.toUserId));
	}

	private int getEmojiCost(int emojiId)
	{
		int costConis = 0;	
		switch (emojiId)
		{
			case 0:
			case 1:
				costConis = 50000;
				break;

			case 2:
			case 3:
				costConis = 100000;
				break;
				
			case 4:
			case 5:
				costConis = 200000;
				break;
				
			default:
				break;
		}
		return costConis;
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
