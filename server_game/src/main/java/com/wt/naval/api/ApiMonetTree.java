package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.lottery.AskLotteryRequest;
import com.gjc.cmd.monetTree.AskMonetTreeRequest;
import com.gjc.cmd.monetTree.GetMoneyTreeRequest;
import com.gjc.naval.moneytree.MoneyTreeService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.gjc.cmd.monetTree") 
@Service
public class ApiMonetTree
{
	@Autowired
	private MoneyTreeService moneyTreeService;
	
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
	
	@Protocol(msgType = MsgTypeEnum.MoneyTree_领取)
	public void getMoneyTree(ChannelHandlerContext ctx, GetMoneyTreeRequest obj, MySession session)
	{
			Tool.print_debug_level0("收到领取消息："+obj);
			int userId=session.getUserId();
			moneyTreeService.getMoney(userId);
			
	}
	@Protocol(msgType = MsgTypeEnum.MoneyTree_同步)
	public void askMonetTree(ChannelHandlerContext ctx, AskMonetTreeRequest obj, MySession session)
	{
		Tool.print_debug_level0("收到同步消息：" + obj);
		int userId = session.getUserId();
		moneyTreeService.askMonetTree(userId);

	}
}
