package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.ranking.RankRewardResponse;
import com.gjc.cmd.safebox.AskPlayerInGameRequest;
import com.gjc.cmd.sign.SignDayRequest;
import com.gjc.cmd.sign.SignDayResponse;
import com.gjc.naval.sign.SignService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.tool.ClassTool;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.gjc.cmd.sign") @Service
public class ApiSign 
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
	private SignService signService;
	
	@Protocol(msgType = MsgTypeEnum.Sign_签到)
	/**签到*/
	public void signDay(ChannelHandlerContext ctx, SignDayRequest obj, MySession session)
	{
		int userId=session.getUserId();
		int day=obj.day;
		
		long curTime = MyTimeUtil.getDayPassTime();
		
		if(curTime<=600)
		{
			return;
		}
		
		//Tool.print_debug_level0(obj.toString());
		boolean sign=signService.sign(userId, day);
		SignDayResponse response=new SignDayResponse(SignDayResponse.SUCCESS,sign,day);
		playerService.sendMsg(response, userId);
	}
}
