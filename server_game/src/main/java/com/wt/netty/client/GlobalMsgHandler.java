package com.wt.netty.client;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.brc.cmd.set.ChangeAccountResponse;
import com.brc.cmd.set.FindPasswordResponse;
import com.brc.cmd.set.ModifyPwdResponse;
import com.brc.cmd.set.SupplementaryAccountResponse;
import com.gjc.naval.vo.chat.FriendsDataVO;
import com.wt.archive.GameData;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.cmd.game.GClientReconnectValidationResponse;
import com.wt.cmd.user.GServerValidationLoginResponse;
import com.wt.cmd.user.UserValidationLoginResponse;
import com.wt.cmd.user.push.Push_otherDeviceLogin;
import com.wt.naval.biz.UserBiz;
import com.wt.naval.cache.UserCache;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.Tool;
import com.wt.util.io.MySerializerUtil;
import com.wt.xcpk.manypepol.ManyPepolService;
import com.wt.xcpk.reconnect.ReconnectService;
import com.yt.xcpk.fruitMachine.FruitMachineImpl;
import com.yt.xcpk.task.TaskService;

import io.netty.channel.Channel;

@Service
public class GlobalMsgHandler {
	private static final boolean isDebug = true;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private ManyPepolService manyPepolService;

	@Autowired
	private TaskService taskImpl;
	
	@Autowired
	private ReconnectService reconnectService;

	public void bufToRequest(int msgType, byte[] data) {
		try
		{
			switch (msgType)// 协议号
			{
				case MsgType.USER_VALIDATION_TOKEN:
					validationTokeLogin(data);
					break;
					
				default:
					if (msgType == MsgTypeEnum.SET_完善账号.getType())
					{
						supplementaryAccount(data);
					}
					else if(msgType == MsgTypeEnum.SET_切换账号.getType())
					{
						changeAccount(data);
					}
					else if(msgType == MsgTypeEnum.MODIFYPWD.getType())
					{
						changePassword(data);
					}
					else if(msgType == MsgTypeEnum.SET_找回密码.getType())
					{
						findPassoword(data);
					}
					else if(msgType == MsgTypeEnum.GAME_游戏服断线重连验证.getType())
					{
						validationReconnect(data);
					}
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Tool.print_error("msgType:" + msgType + "执行出错!", e);
		}
		 
		ClientHelper.close();
	}

	private void validationReconnect(byte[] data)
	{
		GClientReconnectValidationResponse response = MySerializerUtil.deserializer_protobufIOUtil(data, GClientReconnectValidationResponse.class);
		reconnectService.reconnect(response.userId,response.code);
	}

	private void findPassoword(byte[] data)
	{
		FindPasswordResponse response = MySerializerUtil.deserializer_protobufIOUtil(data, FindPasswordResponse.class);
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码,"response:"+response);
		Player  player = playerService.getPlayer(response.userId);
		if(player != null)
		{
			player.sendResponse(response);
		}
	}

	private void changePassword(byte[] data)
	{
		ModifyPwdResponse response = MySerializerUtil.deserializer_protobufIOUtil(data, ModifyPwdResponse.class);
		Tool.print_debug_level0("changePassword,response:"+response);
		Player  player = playerService.getPlayer(response.userId);
		if(player != null)
		{
			player.sendResponse(response);
		}
	}

	private void changeAccount(byte[] data)
	{
		ChangeAccountResponse response = MySerializerUtil.deserializer_protobufIOUtil(data, ChangeAccountResponse.class);
		
		Tool.print_debug_level0("切换账号回应:"+response);
		Player  player = playerService.getPlayer(response.userId);
		if(player != null)
		{
			player.sendResponse(response);
		}
	}

	private void supplementaryAccount(byte[] data)
	{
		SupplementaryAccountResponse response = MySerializerUtil.deserializer_protobufIOUtil(data, SupplementaryAccountResponse.class);
		Tool.print_debug_level0("完善账号:"+response);
		if(response.code == 1000)
		{
			PlayerDaoImpl.updatePlayerSupplementaryAccount(response.account,response.nickName,response.userId);
		}
		Player player = playerService.getPlayer(response.userId);
		
		if(player != null)
		{
			player.setAccountsupplementary();
			player.setNickName(response.nickName);
			player.setAccount(response.account);
			player.sendResponse(response);
		}
	}

	private void validationTokeLogin(byte[] data)
	{
		GServerValidationLoginResponse response = MySerializerUtil.deserializer_protobufIOUtil(data, GServerValidationLoginResponse.class);
		UserInfoModel userInfoModel = response.userModel;
		Channel channel = UserCache.getWaitValidationChannel(userInfoModel.getUserId());
		switch (response.code)
		{
			case Response.SUCCESS:// 向账号服验证成功，在游戏服中注册或者上线该玩家
				UserValidationLoginResponse guestLoingResponse = null;
				if (channel == null)
				{
					Tool.print_debug_level0(response.msgType, "通道不存在,account:" + userInfoModel.getAccount() + ",user_id:" + userInfoModel.getUserId()+"nickName:"+userInfoModel.getNickName());
					return;
				}

				Player playerData = playerService.getPlayer(userInfoModel.getUserId());// 存档在线
				if (playerData != null)
				{
					if (isDebug)
						Tool.print_debug_level0("验证登陆时获取player,player在线,userInfoModel:" + userInfoModel);
					playerData.sendResponse(new Push_otherDeviceLogin());
					
					
					guestLoingResponse = new UserValidationLoginResponse(UserValidationLoginResponse.SUCCESS, playerData.gameData);
					playerData.setLastLogoutTime(response.lastLogoutTime);
					WorldMapUnitEvent.playerOnline(playerData, channel);
					//fruitMachineImpl.setFruitMachineInterval();
					GameServerHelper.sendResponse(channel, guestLoingResponse);
					Tool.print_debug_level0(playerData.getNickName(), guestLoingResponse.msgType, "已在线玩家登陆成功");
					UserCache.removeWaitValidationChannel(userInfoModel.getUserId());
					taskImpl.loadPlayerTaskInfo(playerData.getUserId());
					
					boolean isConnect = manyPepolService.reconnect(playerData,true);
					Tool.print_debug_level0("登陆成功,"+playerData.getNickName(), guestLoingResponse.msgType, "是否断线重连进万人场："+isConnect);
					return;
				}

				playerData = UserBiz.getPlayerData(userInfoModel.getUserId());
				if (playerData == null)// 注册并上线
				{
					if (isDebug)
						Tool.print_debug_level0("验证登陆时获取player,player不在线并且不存在,userInfoModel:" + userInfoModel);

					playerData = new Player();
					Tool.print_debug_level0(response.msgType, "验证成功，玩家不存在，准备注册:" + userInfoModel.getUserId());

					GameData gameData = new GameData();
					playerData.gameData = gameData;

					gameData.initGuest(userInfoModel);

					ArrayList<FriendsDataVO> friendsData_list = new ArrayList<FriendsDataVO>();
					playerData.initFriendsData(friendsData_list);

					boolean isSuccess = UserBiz.inst.userRegisterGuest(gameData, userInfoModel.getAccount());
					playerData.initSign();

					Tool.print_debug_level0(response.msgType, "验证成功，玩家不存在，准备注册,isSuccess:" + isSuccess);
					if (!isSuccess)
					{
						guestLoingResponse = new UserValidationLoginResponse(UserValidationLoginResponse.ERROR_更新数据库失败);
						GameServerHelper.sendResponse(channel, guestLoingResponse);
						Tool.print_debug_level0(userInfoModel.getAccount(), guestLoingResponse.msgType, "游客注册失败，更新数据库失败");

						UserCache.removeWaitValidationChannel(userInfoModel.getUserId());
						return;
					}
					playerData.setLastLogoutTime(response.lastLogoutTime);
					WorldMapUnitEvent.playerOnline(playerData, channel);// UserCache.addOnlineUser(player,
					// channel);
					//fruitMachineImpl.setFruitMachineInterval();
					Tool.print_debug_level0(response.msgType, "验证成功，玩家不存在，准备注册,addOnlineUser");
					taskImpl.registerPlayerSetTaskInfo(playerData.getUserId());

					guestLoingResponse = new UserValidationLoginResponse(UserValidationLoginResponse.SUCCESS, playerData.gameData);
					GameServerHelper.sendResponse(channel, guestLoingResponse);
					Tool.print_debug_level0(playerData.getNickName(), guestLoingResponse.msgType, "游客注册成功");
					UserCache.removeWaitValidationChannel(userInfoModel.getUserId());
					taskImpl.loadPlayerTaskInfo(playerData.getUserId());
				}
				else// 已有玩家,上线
				{
					Tool.print_debug_level0(response.msgType, "验证成功，玩家已注册，读取存档:" + userInfoModel.getUserId()+",nickName:"+userInfoModel.getNickName());
					playerData.init(userInfoModel);
					guestLoingResponse = new UserValidationLoginResponse(UserValidationLoginResponse.SUCCESS, playerData.gameData);

					playerData.setLastLogoutTime(response.lastLogoutTime);
					WorldMapUnitEvent.playerOnline(playerData, channel);
					//fruitMachineImpl.setFruitMachineInterval();
					GameServerHelper.sendResponse(channel, guestLoingResponse);

					Tool.print_debug_level0(playerData.getNickName(), guestLoingResponse.msgType, "登陆成功");
					UserCache.removeWaitValidationChannel(userInfoModel.getUserId());

					taskImpl.loadPlayerTaskInfo(playerData.getUserId());
				}
				break;

			case GServerValidationLoginResponse.ERROR_TOKEN空:
				guestLoingResponse = new UserValidationLoginResponse(UserValidationLoginResponse.ERROR_登陆失败);
				GameServerHelper.sendResponse(channel, guestLoingResponse);
				Tool.print_debug_level0(userInfoModel.getAccount(), guestLoingResponse.msgType, "ERROR_登陆失败");
				break;

			default:
				break;
		}
	}
}