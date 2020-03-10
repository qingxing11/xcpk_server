package com.wt.naval.api;

import com.gjc.cmd.lottery.AskAutoLotteryRequest;
import com.gjc.cmd.lottery.AskLotteryRequest;
import com.gjc.cmd.lottery.AskLotteryTimeRequest;
import com.gjc.naval.lottery.LotteryService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RegisterApi(packagePath = "com.gjc.cmd.lottery") @Service public class ApiLottery
{
	@Autowired
	private LotteryService lotterySerivice;

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

	@Protocol(msgType = MsgTypeEnum.Lottery_下注)
	public void askLottery(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		AskLotteryRequest request = (AskLotteryRequest) obj;
		lotterySerivice.askLottery(request.userId, request.type, request.number);
		Tool.print_debug_level0("收到下注消息：" + request);
	}

	@Protocol(msgType = MsgTypeEnum.Lottery_自动下注)
	public void askAutoLottery(ChannelHandlerContext ctx, AskAutoLotteryRequest request, MySession session)
	{
		Tool.print_debug_level0("收到自动下注消息：" + request);
		lotterySerivice.askAutoLottery(request.userId, request.num);
	}

	@Protocol(msgType = MsgTypeEnum.Lottery_同步时间)
	public void askLotteryTime(ChannelHandlerContext ctx, AskLotteryTimeRequest request, MySession session)
	{
		lotterySerivice.askLotteryTime(session.getUserId());
		Tool.print_debug_level0("收到同步时间消息：" + request);
	}
}
