package com.wt.naval.api;

import java.util.Calendar;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.brc.cmd.mail.Attach;
import com.brc.naval.mail.MailService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.rank.RankService;
import com.yt.cmd.task.ChouJiangRequest;
import com.yt.cmd.task.ChouJiangResponse;
import com.yt.cmd.task.OnLineRewardRequest;
import com.yt.cmd.task.OnLineRewardResponse;
import com.yt.cmd.task.ReceiveRewardRequest;
import com.yt.cmd.task.ReceiveRewardResponse;
import com.yt.xcpk.data.TaskDetailedInfo;
import com.yt.xcpk.task.TaskService;

import data.data.Configs;
import data.data.DataChouJiang;
import data.data.DataOnLineReward;
import data.data.DataTask;
import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.yt.cmd.task")
@Service
public class ApiTask {

	@Autowired
	private PlayerService playerImpl;

	@Autowired
	private TaskService taskImpl;
	@Autowired
	private RankService rankService;
	@Autowired
	private Configs config;
	@Autowired
	private MailService mailService;

	private boolean IsDebug = false;

	@PostConstruct
	private void init() {
		try {
			ClassTool.registerApi(this);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	@Protocol(msgType = MsgTypeEnum.ReceiveReward)
	public void receiveReward(ChannelHandlerContext ctx, Request obj, MySession session) {
		ReceiveRewardRequest request = (ReceiveRewardRequest) obj;
		ReceiveRewardResponse response = null;

		TaskDetailedInfo taskDetailedInfo = taskImpl.getCurrentRequestTask(session.getUserId(), request.taskId);
		if (taskDetailedInfo == null) {
			response = new ReceiveRewardResponse(ReceiveRewardResponse.当前任务不存在);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (taskDetailedInfo.currentValue < taskDetailedInfo.completeValue) {
			response = new ReceiveRewardResponse(ReceiveRewardResponse.当前任务未到领取条件,taskDetailedInfo);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (taskDetailedInfo.isLingQu) {
			response = new ReceiveRewardResponse(ReceiveRewardResponse.当前任务已领取奖励);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		taskDetailedInfo.isGetReward = false; // 变成不能领取
		taskDetailedInfo.isLingQu = true;// 当前任务状态改为已领取
		DataTask dataTask = config.getDataTask().get(request.taskId);
		Player player = playerImpl.getPlayer(session.getUserId());

		Tool.print_coins(player.getNickName(),dataTask.taskRewardValue,"领取任务奖励",player.getCoins());
		player.addCoins(dataTask.taskRewardValue);
		// rankService.addWinConisNum(player.getUserId(), dataTask.taskRewardValue,
		// player.getNickName());

		taskImpl.updateTaskInfo(taskDetailedInfo.taskBigType, player.getUserId());
		response = new ReceiveRewardResponse(ReceiveRewardResponse.SUCCESS, taskDetailedInfo);
		GameServerHelper.sendResponse(ctx, response);

	}

	@Protocol(msgType = MsgTypeEnum.ChouJiangResult)
	public void chouJiang(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		ChouJiangRequest request = (ChouJiangRequest) obj;
		ChouJiangResponse response = null;
		boolean isFreeChouJiang = taskImpl.requestChouJiang(session.getUserId());
		Player player = playerImpl.getPlayer(session.getUserId());
		if (!isFreeChouJiang)
		{
			if (IsDebug)
				Tool.print_debug_level0(MsgTypeEnum.ChouJiangResult,"isFreeChouJiang:"+isFreeChouJiang);
			if (isFreeChouJiang)
			{
				response = new ChouJiangResponse(ChouJiangResponse.您当前可以免费抽奖一次);
				GameServerHelper.sendResponse(ctx, response);
				return;
			}
			if (IsDebug)
				Tool.print_debug_level0("扣之前 当前钻石数：" + player.gameData.crystals);
			if (player.gameData.crystals < 10)
			{
				if (IsDebug)
					Tool.print_debug_level0("不是免费抽奖 当前钻石数不够 ：" + player.gameData.crystals);
				response = new ChouJiangResponse(ChouJiangResponse.您的钻石不足了);
				GameServerHelper.sendResponse(ctx, response);
				return;
			}
			
			Tool.print_crystals(player.getNickName(), -10, "抽奖钻石");
			player.addCrystals(-10);
			if (IsDebug)
				Tool.print_debug_level0("扣之后 当前钻石数：" + player.gameData.crystals);
		}
		else
		{
			Tool.print_debug_level0("免费抽奖：" + player.getNickName());
		}

		int pathId = taskImpl.getCurrentGetChouJiangInfo();
		DataChouJiang dataChouJiang = null;
		Collection<DataChouJiang> list_chouJiang = config.getDataChouJiang().values();
		for (DataChouJiang chouJiang : list_chouJiang)
		{
			if (chouJiang.Id == pathId)
			{
				dataChouJiang = chouJiang;
				break;
			}
		}

		if (dataChouJiang == null)
		{
			response = new ChouJiangResponse(ChouJiangResponse.没找到该奖项);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}else
		{
			Tool.print_debug_level0("【抽奖】玩家:"+player.getUserId()+",当前抽到的为 ：id="+ dataChouJiang.Id+",奖励名字:"+dataChouJiang.Name+",奖励的值为 ： "+dataChouJiang.getRewardValue);
		}
		// TODO 调用邮件 领取奖励的接口 传值 userId rewardType rewardValue
		String attachContent = null;
		Attach attach = new Attach(dataChouJiang.luckyType, dataChouJiang.getRewardValue);
		attachContent = JSON.toJSONString(attach);

		mailService.sendMail(session.getUserId(), "领取奖励", "恭喜获得奖励", attachContent);
		taskImpl.updateFreeChouJiangState(session.getUserId(), 2);
		response = new ChouJiangResponse(ChouJiangResponse.SUCCESS, pathId);
		GameServerHelper.sendResponse(ctx, response);
		return;
	}

	@Protocol(msgType = MsgTypeEnum.RequestLingQuOnLineReward)
	public void onLineReward(ChannelHandlerContext ctx, Request obj, MySession session) {
		OnLineRewardRequest request = (OnLineRewardRequest) obj;
		OnLineRewardResponse response = null;
		// boolean isHave=taskImpl.judgyIsHavaOnLineReward(request.timeId);
		// if(!isHave) {
		//
		// response=new OnLineRewardResponse(OnLineRewardResponse.没有找到该时间段的奖励);
		// GameServerHelper.sendResponse(ctx, response);
		// return ;
		// }
		Integer isLingQu = taskImpl.requestLingQuOnLineReward(session.getUserId(), request.timeId);
		if (isLingQu == 3) {
			response = new OnLineRewardResponse(OnLineRewardResponse.该时间段的奖励已领取);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		// Date data=new Date();
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd
		// HH:mm:ss");//可以方便地修改日期格式
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		int hour = c.get(Calendar.HOUR_OF_DAY);
		Player player = playerImpl.getPlayer(session.getUserId());
		DataOnLineReward onLineReward = config.getDataOnLineReward().get(request.timeId);
		Tool.print_debug_level0(
				"时间 ：小时 ：" + hour + ",请求的时间段上限" + onLineReward.before + ",请求的时间段下限" + onLineReward.after);
		if (hour < Integer.parseInt(onLineReward.before) || hour > Integer.parseInt(onLineReward.after)) {
			response = new OnLineRewardResponse(OnLineRewardResponse.未到领取该时间段的时间);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		Tool.print_coins(player.getNickName(),onLineReward.onLineRewardValue,"在线奖励",player.getCoins());
		player.addCoins(onLineReward.onLineRewardValue);
		taskImpl.setOnLineRewardInfo(session.getUserId(), request.timeId, 3);
		response = new OnLineRewardResponse(OnLineRewardResponse.SUCCESS, request.timeId);
		GameServerHelper.sendResponse(ctx, response);
	}

}
