package com.wt.xcpk.reconnect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.game.GClientReconnectValidationResponse;
import com.wt.cmd.game.PlayerReConnectResponse;
import com.wt.naval.biz.UserBiz;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;
import com.wt.xcpk.classic.ClassicGameService;
import com.wt.xcpk.manypepol.ManyPepolService;
import com.wt.xcpk.zjh.killroom.KillRoomService;
import com.yt.xcpk.fruitMachine.FruitMachineService;

import io.netty.channel.Channel;

@Service
public class ReconnectImpl implements ReconnectService
{
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private KillRoomService killRoomService;
	
	@Autowired
	private ClassicGameService classicGameService;
	
	@Autowired
	private FruitMachineService fruitMachineService;
	
	@Autowired
	private ManyPepolService manyPepolService;
	
	@Override
	public void reconnect(int userId,int code)
	{
		Channel channel = UserCache.getWaitValidationChannel(userId);
		if(channel == null)
		{
			Tool.print_error(MsgTypeEnum.GAME_客户端要求断线重连,"channel空，userId:"+userId);
			return;
		}
		
		switch (code)
		{
			case GClientReconnectValidationResponse.SUCCESS:
				Player player = playerService.getPlayer(userId);
				if (player != null)
				{
//					GameServerHelper.sendResponse(channel,new PlayerReConnectResponse(PlayerReConnectResponse.SUCCESS,player.gameData,true));
					Tool.print_debug_level0(MsgTypeEnum.GAME_客户端要求断线重连, "用户存在 : " + player.getNickName());

					/**
					 * 玩家已经在线，检查玩家是否在比赛中，如在比赛中则需要断线重连
					 * 重新返回房间和比赛信息
					 */
					synchronized (UserCache.map_channel)
					{
						UserCache.closeExistsChannel(player.getUserId());
						Tool.print_debug_level0(player.getNickName(), MsgTypeEnum.GAME_客户端要求断线重连, "已经在线了");
						UserCache.updateSessions(userId, channel);
						UserCache.addOnlineUser(player, channel);

						UserBiz.playerOnline(player);
					}

					UserBiz.updateLoginTime(player.getUserId());
					
					onReconnect(channel, player);
				}
				else
				{
					Tool.print_debug_level0(MsgTypeEnum.GAME_客户端要求断线重连, "用户不存在 : " + userId);
					GameServerHelper.sendResponse(channel,new PlayerReConnectResponse(PlayerReConnectResponse.ERROR_不存在的用户));
				}
			
				break;

			default:
				Tool.print_error(MsgTypeEnum.GAME_客户端要求断线重连,"错误，userId:"+userId+",code:"+code);
				GameServerHelper.sendResponse(channel,new PlayerReConnectResponse(code));
				break;
		}
	}

	private void onReconnect(Channel channel, Player player)
	{
		boolean isClassicReconnect = classicGameService.reconnect(player);
		boolean isManypepolReconnect = manyPepolService.reconnect(player,false);
		boolean isFruitReconnect = fruitMachineService.reconnect(player);
		if(!isClassicReconnect && !isFruitReconnect && !isManypepolReconnect)
		{
			Tool.print_debug_level0(MsgTypeEnum.GAME_客户端要求断线重连, "ERROR_不在任何房间中");
			GameServerHelper.sendResponse(channel,new PlayerReConnectResponse(PlayerReConnectResponse.ERROR_不在任何房间中,player.gameData));
		}
		else
		{
			GameServerHelper.sendResponse(channel,new PlayerReConnectResponse(PlayerReConnectResponse.SUCCESS,player.gameData,true));
		}
	}
}
