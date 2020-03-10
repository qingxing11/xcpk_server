package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gjc.cmd.horn.SendHornInfoRequest;
import com.gjc.cmd.horn.SendHornInfoResponse;
import com.gjc.naval.horn.HornInfoVO;
import com.gjc.naval.vip.VipService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.gjc.cmd.horn") @Service
public class ApiHorn 
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
	private VipService vipService;
	
	
	@Protocol(msgType = MsgTypeEnum.Horn_喇叭消息)
	/**喇叭消息*/
	public void sendHornInfo(ChannelHandlerContext ctx, SendHornInfoRequest obj, MySession session)
	{
		int userId=session.getUserId();
		Player player=playerService.getPlayer(userId);
		int vipLv=player.getVipLv();
		int needMoney=vipService.typhonChatMoney(vipLv);
		Tool.print_debug_level0("广播金币小喇叭：needMoney="+needMoney);
		if (player.getCoins()-needMoney>=0) 
		{
			Tool.print_subCoins(player.getNickName(),needMoney,"发送喇叭",player.getCoins());
			player.subCoinse(needMoney);
		}
		else
		{
			SendHornInfoResponse response=new SendHornInfoResponse(SendHornInfoResponse.Error_金币不足);
			playerService.sendMsg(response, userId);
			return;
		}
		
		String nikeName=player.getNickName();
		String info=obj.info;
		int role=player.getRoleId();
		HornInfoVO vo=new HornInfoVO(userId,nikeName,vipLv,info,role);
		
		SendHornInfoResponse response=new SendHornInfoResponse(SendHornInfoResponse.SUCCESS,vo,needMoney);
		for (Player play : playerService.getAllPlayer()) 
		{
			//Tool.print_debug_level0("广播金币小喇叭：给玩家："+play.getUserId()+play.getNickName()+"\n消息是："+response);
			play.sendResponse(response);
		}
	}
}
