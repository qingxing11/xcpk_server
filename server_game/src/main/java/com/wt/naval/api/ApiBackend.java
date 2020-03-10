package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.backend.PaySuccessRequest;
import com.wt.cmd.backend.PaySuccessResponse;
import com.wt.cmd.user.push.Push_GameMoneyChange;
import com.wt.naval.biz.UserBiz;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;


@Service
@RegisterApi(packagePath = "com.wt.cmd.backend")
public class ApiBackend
{
	@Protocol(msgType = MsgTypeEnum.BACKEND_购买金币成功)
	public static void paySuccess(ChannelHandlerContext ctx,Request obj)
	{
		PaySuccessRequest request = (PaySuccessRequest)obj;
		Player player = UserCache.getPlay(request.userId);
		Channel channel = UserCache.getChannel(request.userId);
		if(player != null && channel.isOpen())//在线
		{
			Push_GameMoneyChange response = new Push_GameMoneyChange(Push_GameMoneyChange.MONEYTYPE_CRYTSTAL, Push_GameMoneyChange.STATE_ADD, Math.abs(request.giftNum), player.gameData.crystals);
			GameServerHelper.sendResponse(channel, response);
			
			player.gameData.crystals += request.giftNum;
		}
		else//离线 
		{
			if(!UserBiz.isUserIdExists(request.userId))
			{
				Tool.print_error(MsgType.BACKEND_购买金币成功,"没有这个玩家,userId:"+request.userId);
				
				PaySuccessResponse response = new PaySuccessResponse(PaySuccessResponse.ERROR_没有这个玩家,request.callBackId);
				response.callBackId = request.callBackId;
				GameServerHelper.sendResponse(ctx, response);
				return;
			}
		}
		Tool.print_error(MsgType.BACKEND_购买金币成功,"玩家增加金币："+request.giftNum);
		UserBiz.addUserCrystals_add(request.userId,request.giftNum );
		
		PaySuccessResponse response = new PaySuccessResponse(PaySuccessResponse.SUCCESS,request.callBackId);
		response.callBackId = request.callBackId;
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
