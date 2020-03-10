package com.yt.xcpk.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.dao.impl.YTDaoImpl;
import com.wt.naval.event.timer.NewDayEventListener;
import com.wt.naval.event.timer.TimerEvent;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.io.MySerializerUtil;
import com.yt.cmd.task.PushPlayerTaskEveryTimeCompleteResponse;
import com.yt.cmd.task.PushPlayerTaskInfoResponse;
import com.yt.cmd.task.PushTodayIsFreeChouJiangResponse;
import com.yt.cmd.task.Push_OnLineRewardInfo;
import com.yt.xcpk.data.TaskDetailedInfo;
import com.yt.xcpk.data.TaskInfo;

import data.data.Configs;
import data.data.DataChouJiang;
import data.data.DataTask;
import data.define.LuckyType;
import data.define.TaskSmallType;
import data.define.TaskType;
import model.UserTask;

@Service public class TaskImpl implements TaskService, NewDayEventListener
{

	public static TaskImpl inst;

	@PostConstruct // 初始化方法 服务器启动的时候调用
	private void init()
	{
		inst = this;
		TimerEvent.addEventListener(this);
		try
		{}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 所有玩家的任务信息
	 */
	private ConcurrentHashMap<String, TaskInfo> map_allPlayerTaskInfo = new ConcurrentHashMap<String, TaskInfo>();

	// private ArrayList<Integer> list_LineRewardInfo = new
	// ArrayList<Integer>();
	// 用于纪录当前玩家今天有没有免费抽过奖 1 为可免费抽一次 2 为不可免费抽 需付费
	// private int freeChouJiang;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private Configs config;

	@Override
	public void registerPlayerSetTaskInfo(int userId)
	{
//		Tool.print_debug_level0("注册时设置玩家的任务信息：" + map_allPlayerTaskInfo);

		TaskInfo taskInfo = new TaskInfo();
		Collection<DataTask> list_dataTask = (Collection<DataTask>) config.getDataTask().values();
//		Tool.print_debug_level0("注册时设置玩家的任务信息：任务配表信息为 ：" + list_dataTask);
		taskInfo.userId = userId;
		taskInfo.freeChouJiang = 1;
		for (DataTask task : list_dataTask)
		{
			if (task.taskType == 1)
			{
				if (taskInfo.getDayTaskInfo() == null)
				{
					taskInfo.list_dayTaskInfo = new ArrayList<TaskDetailedInfo>();
				}
				TaskDetailedInfo taskDetailedInfo = new TaskDetailedInfo(task.Id, task.taskType, task.taskSmallType, 0, task.taskLimit, false, false);
				taskInfo.list_dayTaskInfo.add(taskDetailedInfo);
			}
			else if (task.taskType == 2)
			{
				if (taskInfo.getPersonSelfTaskInfo() == null)
				{
					taskInfo.list_personSelfTaskInfo = new ArrayList<TaskDetailedInfo>();
				}
				TaskDetailedInfo taskDetailedInfo = new TaskDetailedInfo(task.Id, task.taskType, task.taskSmallType, 0, task.taskLimit, false, false);
				taskInfo.list_personSelfTaskInfo.add(taskDetailedInfo);
			}
			else
			{
				if (taskInfo.getSystemTaskInfo() == null)
				{
					taskInfo.list_systemTaskInfo = new ArrayList<TaskDetailedInfo>();
				}
				TaskDetailedInfo taskDetailedInfo = new TaskDetailedInfo(task.Id, task.taskType, task.taskSmallType, 0, task.taskLimit, false, false);
				taskInfo.list_systemTaskInfo.add(taskDetailedInfo);
			}
		}
		Tool.print_debug_level0("注册时设置玩家的日常任务信息：" + taskInfo.list_dayTaskInfo);
		Tool.print_debug_level0("注册时设置玩家的个人任务信息：" + taskInfo.list_personSelfTaskInfo);
		Tool.print_debug_level0("注册时设置玩家的系统任务信息：" + taskInfo.list_systemTaskInfo);
		String user = "" + userId;
		map_allPlayerTaskInfo.put(user, taskInfo);
		byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
		byte[] bytePersonSelfTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
		byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
		ArrayList<Integer> list_onLineRewardInfo = new ArrayList<Integer>();
		list_onLineRewardInfo.add(1);
		list_onLineRewardInfo.add(1);
		list_onLineRewardInfo.add(1);
		taskInfo.list_onLineReward = list_onLineRewardInfo;
		byte[] byteOnLine = MySerializerUtil.serializer_Java(taskInfo.list_onLineReward);
		YTDaoImpl.insertServerTaskInfo(userId, byteDayTask, bytePersonSelfTask, byteSystemTask, taskInfo.freeChouJiang, byteOnLine);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void loadPlayerTaskInfo(Integer userId)
	{
		Player player = playerService.getPlayer(userId);
		// player.gameData.crystals = 50;
		UserTask userTask = YTDaoImpl.getUserTask(userId);
		if (userTask==null)
			return;
		
		Tool.print_debug_level0("loadPlayerTaskInfo,nickName:"+player.getNickName()+",userTask:"+userTask);
		byte[] byteDayTask = userTask.getDayTask();
		byte[] bytePersonSelfTask = userTask.getPersonSelfTask();
		byte[] byteSystemTask = userTask.getSystemTask();
		byte[] byteOnLineTask = userTask.getOnLineTask();
		int freeChouJiang = userTask.getFreeChouJiang();
		if (byteDayTask == null || (bytePersonSelfTask == null && byteSystemTask == null))
		{
			Tool.print_debug_level0("出错了 注册时信息未存到数据库中！！！！！");
			registerPlayerSetTaskInfo(userId);
			userTask = YTDaoImpl.getUserTask(userId);
			
			byteDayTask = userTask.getDayTask();
			bytePersonSelfTask = userTask.getPersonSelfTask();
			byteSystemTask = userTask.getSystemTask();
			byteOnLineTask = userTask.getOnLineTask();
			freeChouJiang = userTask.getFreeChouJiang();
			// return;
		}
		ArrayList<Integer> list_onLineRewardInfo = new ArrayList<Integer>();
		if (byteOnLineTask == null)
		{
			list_onLineRewardInfo.add(1);
			list_onLineRewardInfo.add(1);
			list_onLineRewardInfo.add(1);
			byteOnLineTask = MySerializerUtil.serializer_Java(list_onLineRewardInfo);
			YTDaoImpl.updateOnLineTaskData(byteOnLineTask, userId);
		}
		ArrayList<TaskDetailedInfo> list_dayTask = (ArrayList<TaskDetailedInfo>) MySerializerUtil.deserializer_Java(byteDayTask);
		ArrayList<TaskDetailedInfo> list_personSelfTask = (ArrayList<TaskDetailedInfo>) MySerializerUtil.deserializer_Java(bytePersonSelfTask);
		ArrayList<TaskDetailedInfo> list_systemTask = (ArrayList<TaskDetailedInfo>) MySerializerUtil.deserializer_Java(byteSystemTask);
		list_onLineRewardInfo = (ArrayList<Integer>) MySerializerUtil.deserializer_Java(byteOnLineTask);
		String user = "" + userId;
		if (map_allPlayerTaskInfo.containsKey(user))
		{
			TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
			taskInfo.userId = userId;
			taskInfo.list_dayTaskInfo = list_dayTask;
			taskInfo.list_personSelfTaskInfo = list_personSelfTask;
			taskInfo.list_systemTaskInfo = list_systemTask;
			taskInfo.freeChouJiang = freeChouJiang;
			taskInfo.list_onLineReward = list_onLineRewardInfo;
			Tool.print_debug_level0("进入游戏推送任务信息：" + taskInfo);
			Tool.print_debug_level0("缓存中该玩家的任务数据是否为空 ：" + map_allPlayerTaskInfo.containsKey(user) + ",taskInfo:" + map_allPlayerTaskInfo.get(user));
			PushPlayerTaskInfoResponse response = new PushPlayerTaskInfoResponse(PushPlayerTaskInfoResponse.SUCCESS, taskInfo);
			player.sendResponse(response);
			PushTodayIsFreeChouJiangResponse responseChouJiang = new PushTodayIsFreeChouJiangResponse(PushTodayIsFreeChouJiangResponse.SUCCESS, freeChouJiang);
			player.sendResponse(responseChouJiang);
			Push_OnLineRewardInfo responseOnLineReward = new Push_OnLineRewardInfo(Push_OnLineRewardInfo.SUCCESS, taskInfo.list_onLineReward);
			player.sendResponse(responseOnLineReward);
			return;
		}

		TaskInfo taskInfo = new TaskInfo();
		taskInfo.userId = userId;
		taskInfo.list_dayTaskInfo = list_dayTask;
		taskInfo.list_personSelfTaskInfo = list_personSelfTask;
		taskInfo.list_systemTaskInfo = list_systemTask;
		taskInfo.freeChouJiang = freeChouJiang;
		taskInfo.list_onLineReward = list_onLineRewardInfo;
		map_allPlayerTaskInfo.put(user, taskInfo);
		PushPlayerTaskInfoResponse response = new PushPlayerTaskInfoResponse(PushPlayerTaskInfoResponse.SUCCESS, taskInfo);
		player.sendResponse(response);
		PushTodayIsFreeChouJiangResponse responseChouJiang = new PushTodayIsFreeChouJiangResponse(PushTodayIsFreeChouJiangResponse.SUCCESS, taskInfo.freeChouJiang);
		player.sendResponse(responseChouJiang);

		Push_OnLineRewardInfo responseOnLineReward = new Push_OnLineRewardInfo(Push_OnLineRewardInfo.SUCCESS, list_onLineRewardInfo);
		player.sendResponse(responseOnLineReward);

	}

	/**
	 * 隔日清空玩家日常任务信息
	 */
	public void clearPlayerDayTaskInfo()
	{
		if (map_allPlayerTaskInfo == null || map_allPlayerTaskInfo.size() <= 0)
		{
			return;
		}
		Tool.print_debug_level0("clearPlayerDayTaskInfo---->begin:");
//		ArrayList<TaskInfo> list_taskInfo = (ArrayList<TaskInfo>) map_allPlayerTaskInfo.values();
		Collection<TaskInfo> list_taskInfo = map_allPlayerTaskInfo.values();
		for (TaskInfo taskInfo : list_taskInfo)//TODO 严重性能问题待优化
		{
			if (taskInfo.getDayTaskInfo() != null)
			{
				for (TaskDetailedInfo taskDetailedInfo : taskInfo.getDayTaskInfo())
				{
					taskDetailedInfo.SetCurrentValue(0);
				}
				byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
				YTDaoImpl.updateDayTaskData(byteDayTask, taskInfo.userId);
			}
			if (taskInfo.list_onLineReward != null)
			{
				for (int i = 0; i <  taskInfo.list_onLineReward.size(); i++) {
					taskInfo.list_onLineReward.set(i, 1);
				}
				byte[] byteOnLineTask = MySerializerUtil.serializer_Java(taskInfo.list_onLineReward);
				YTDaoImpl.updateOnLineTaskData(byteOnLineTask, taskInfo.userId);
			}
			updateFreeChouJiangState(taskInfo.userId, 1);// 每日更新
			// 可免费抽奖的状态
		
		}
		
		Tool.print_debug_level0("clearPlayerDayTaskInfo---->end:");
	}

	@Override
	public void newDayEvent()
	{
		try
		{
			clearPlayerDayTaskInfo();
		}
		catch (Exception e)
		{
			Tool.print_error("清理每日任务时错误:",e);
		}
	}

	@Override
	public TaskDetailedInfo getCurrentRequestTask(int userId, int taskId)
	{
		String user = "" + userId;
		TaskDetailedInfo taskDetailedInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			Tool.print_debug_level0("请求领取奖励时 ：存在该玩家的任务信息 为空：" + map_allPlayerTaskInfo.get(user) + ",taskInfo:" + map_allPlayerTaskInfo.get(user));
			return taskDetailedInfo;
		}
		Tool.print_debug_level0("请求领取奖励时 ：存在该玩家的任务信息不 为空：" + map_allPlayerTaskInfo.get(user) + ",taskInfo:" + map_allPlayerTaskInfo.get(user));
		TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
		ArrayList<TaskDetailedInfo> list_dayTask = taskInfo.getDayTaskInfo();
		ArrayList<TaskDetailedInfo> list_personSelfTask = taskInfo.getPersonSelfTaskInfo();
		ArrayList<TaskDetailedInfo> list_systemTask = taskInfo.getSystemTaskInfo();

		for (TaskDetailedInfo task : list_dayTask)
		{
			if (task.taskId == taskId)
			{
				taskDetailedInfo = task;
				break;
			}
		}

		for (TaskDetailedInfo task : list_personSelfTask)
		{
			if (task.taskId == taskId)
			{
				taskDetailedInfo = task;
				break;
			}
		}

		for (TaskDetailedInfo task : list_systemTask)
		{
			if (task.taskId == taskId)
			{
				taskDetailedInfo = task;
				break;
			}
		}
		return taskDetailedInfo;
	}

	@Override
	public void updateFreeChouJiangState(int userId, int freeChouJiang)
	{
		String user = "" + userId;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
		taskInfo.freeChouJiang = freeChouJiang;
		YTDaoImpl.updateFreeChouJiangData(freeChouJiang, userId);
	}

	@Override
	public boolean requestChouJiang(int userId)
	{
		String user = "" + userId;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return false;
		}
		
		TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
		if (taskInfo.freeChouJiang == 1)
		{
			return true;
		}
		return false;
	}

	@Override
	public int getCurrentGetChouJiangInfo()
	{
		Collection<DataChouJiang> list_chouJiang = config.getDataChouJiang().values();
		int ran = MyUtil.getRandom(0, 1000);
		
//		ArrayList<DataChouJiang> list_values = new ArrayList<DataChouJiang>();
		DataChouJiang chouJiang = new DataChouJiang();
		chouJiang.Id = 1;
		chouJiang.luckyType = 6;
		chouJiang.getRewardValue = 1;
		int rate = 0;
		for (DataChouJiang item : list_chouJiang)
		{
			int nowRate = item.luckyDrawMin+rate;
//			Tool.print_debug_level0("抽奖计算:"+nowRate+",取得概率:"+ran+",item.luckyDrawMin:"+item.luckyDrawMin);
			if (ran < nowRate)
			{
				chouJiang = item;
				break;
			}
			rate += item.luckyDrawMin;
		}
		return chouJiang.Id;
	}
	

	@Override
	public Integer requestLingQuOnLineReward(int userId, int timeId)
	{
		String user = "" + userId;
		TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
		return taskInfo.list_onLineReward.get(timeId - 1);
	}

	/// 每日任务
	@Override
	public void loudSpeakersTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_dayTaskInfo == null || taskInfo.list_dayTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_dayTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.大喇叭发言)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
		YTDaoImpl.updateDayTaskData(byteDayTask, userId);
	}

	@Override
	public void sendEnjoyTask(int userId, int addValue)
	{
		Tool.print_debug_level0("发表情任务！！！");
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_dayTaskInfo == null || taskInfo.list_dayTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		Tool.print_debug_level0("日常任务 ：进入发表情任务 ：" + taskInfo.list_dayTaskInfo);
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_dayTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.发表情)
			{
				Tool.print_debug_level0("发表情任务 ：" + taskDetailInfo);
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
				Tool.print_debug_level0("发送发表情任务推送！！！");
			}
		}
		byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
		YTDaoImpl.updateDayTaskData(byteDayTask, userId);
	}

	@Override
	public void classicWinTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_dayTaskInfo == null || taskInfo.list_dayTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_dayTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.经典获胜)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
		YTDaoImpl.updateDayTaskData(byteDayTask, userId);
	}

	@Override
	public void everyDayUpBankerInKillRoom(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_dayTaskInfo == null || taskInfo.list_dayTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_dayTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.每日通杀上庄)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
		YTDaoImpl.updateDayTaskData(byteDayTask, userId);
	}

	@Override
	public void everyDayUpBankerInWanRenRoom(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_dayTaskInfo == null || taskInfo.list_dayTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_dayTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.每日万人上庄)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
		YTDaoImpl.updateDayTaskData(byteDayTask, userId);
	}
	/// 每日任务

	/// 个人任务
	@Override
	public void classicWinJinHuaTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_personSelfTaskInfo == null || taskInfo.list_personSelfTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_personSelfTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.经典场获得金花)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] bytePersonSelfTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
		YTDaoImpl.updatePersonSelfTaskData(bytePersonSelfTask, userId);

	}

	@Override
	public void classicWinShunJinTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_personSelfTaskInfo == null || taskInfo.list_personSelfTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_personSelfTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.经典场获得顺金)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] bytePersonSelfTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
		YTDaoImpl.updatePersonSelfTaskData(bytePersonSelfTask, userId);

	}

	@Override
	public void classicWinBaoZiTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_personSelfTaskInfo == null || taskInfo.list_personSelfTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_personSelfTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.经典场获得豹子)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] bytePersonSelfTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
		YTDaoImpl.updatePersonSelfTaskData(bytePersonSelfTask, userId);

	}

	@Override
	public void killRoomAndInTheBaoZiTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.通杀场压中豹子)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}

		byte[] bytePersonSelfTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
		YTDaoImpl.updatePersonSelfTaskData(bytePersonSelfTask, userId);
	}

	@Override
	public void wanRenRoomAndInTheBaoZiTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.万人场获取豹子)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}

		byte[] bytePersonSelfTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
		YTDaoImpl.updatePersonSelfTaskData(bytePersonSelfTask, userId);
	}
	/// 个人任务

	/// 系统任务
	@Override
	public void playerLevelTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.玩家等级)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}
				int time = taskDetailInfo.currentValue % 5;
				taskDetailInfo.currentValue += addValue;
				int timeAfter = taskDetailInfo.currentValue % 5;
				if (timeAfter > time)
				{
					taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
					taskDetailInfo.isGetReward = true;
					PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
					player.sendResponse(response);
					break;
				}
				else
				{
					taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
					taskDetailInfo.isGetReward = false;
				}
			}
		}
		byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
		YTDaoImpl.updateSystemTaskData(byteSystemTask, userId);
	}

	@Override
	public void playerOnLineTimeTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.累积在线)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
		YTDaoImpl.updateSystemTaskData(byteSystemTask, userId);
	}

	@Override
	public void playerPaymentTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.累积充值)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
		YTDaoImpl.updateSystemTaskData(byteSystemTask, userId);
	}

	@Override
	public void WinRankFirstTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.累积赢金榜排名第一名)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
		YTDaoImpl.updateSystemTaskData(byteSystemTask, userId);
	}

	@Override
	public void paymentFristTask(int userId, int addValue)
	{
		String user = "" + userId;
		TaskInfo taskInfo = null;
		if (!map_allPlayerTaskInfo.containsKey(user))
		{
			return;
		}
		taskInfo = map_allPlayerTaskInfo.get(user);
		Player player = playerService.getPlayer(userId);
		if (taskInfo.list_systemTaskInfo == null || taskInfo.list_systemTaskInfo.size() <= 0)
		{
			Tool.print_debug_level0("出错了 数据没有！！！");
			return;
		}
		for (TaskDetailedInfo taskDetailInfo : taskInfo.list_systemTaskInfo)
		{
			if (taskDetailInfo.taskSmallType == TaskSmallType.累积充值榜排名第一名)
			{
				if (taskDetailInfo.isJudgyTaskAlreadyComplete())
				{
					continue;
				}

				taskDetailInfo.currentValue += addValue;
				taskDetailInfo.SetCurrentValue(taskDetailInfo.currentValue);
				taskDetailInfo.isJudgyTaskComplete();
				PushPlayerTaskEveryTimeCompleteResponse response = new PushPlayerTaskEveryTimeCompleteResponse(PushPlayerTaskInfoResponse.SUCCESS, taskDetailInfo);
				player.sendResponse(response);
			}
		}
		byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
		YTDaoImpl.updateSystemTaskData(byteSystemTask, userId);
	}
	/// 系统任务

	@Override
	public void setOnLineRewardInfo(int userId, int timeId, Integer isLingQu)
	{
		String user = "" + userId;
		TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
		for (int i = 0 ; i < taskInfo.list_onLineReward.size() ; i++)
		{
			if (i == timeId - 1)
			{
				taskInfo.list_onLineReward.set(i, isLingQu);
				byte[] byteOnLineTask = MySerializerUtil.serializer_Java(taskInfo.list_onLineReward);
				YTDaoImpl.updateOnLineTaskData(byteOnLineTask, taskInfo.userId);
				break;
			}
		}
		
	}

	@Override
	public void setPlayerAttachRewardInfo(int userId, int type, int num)
	{
		Player player = playerService.getPlayer(userId);
		if (player != null)
		{
			switch (type)
			{
				case LuckyType.VIP1:
				case LuckyType.VIP2:
				case LuckyType.VIP3:
				case LuckyType.VIP4:
				case LuckyType.VIP5:
					if (player.gameData.vipLv >= type)
					{
						Tool.print_coins(player.getNickName(),10000, "附件，vip转换",player.getCoins());
						player.addCoins(10000);
					}
					else
					{
						player.setVipLv(type);
						player.setVipTime(MyTimeUtil.getCurrTimeMM());
						UserDaoImpl.updateUserVIPLv(userId,type);
						
						Tool.print_debug_level0("抽奖抽到vip,nickName:"+player.getNickName()+",当前自己VIP等级:"+player.getVipLv()+",抽到等级:"+type);
					}

					break;
				case LuckyType.增时卡:
					player.addTimeNum(1);
					break;
				case LuckyType.抢座卡:
					player.addRobPosNum(1);
					break;
				case LuckyType.改名卡:
					player.addModifyNickName(1);
					break;
				case LuckyType.金币:
					Tool.print_coins(player.getNickName(),num, "附件",player.getCoins());
					player.addCoins(num);
					break;
				case LuckyType.钻石:
					Tool.print_crystals(player.getNickName(),num, "附件");
					player.addCrystals(num);
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void updateTaskInfo(int taskBigType, int userId)
	{
		String user = "" + userId;
		TaskInfo taskInfo = map_allPlayerTaskInfo.get(user);
		if (taskInfo == null)
		{
			return;
		}
		if (taskBigType == TaskType.DayTask)
		{
			byte[] byteDayTask = MySerializerUtil.serializer_Java(taskInfo.getDayTaskInfo());
			YTDaoImpl.updateDayTaskData(byteDayTask, userId);

		}
		else if (taskBigType == TaskType.PersonSelfTask)
		{
			byte[] bytePersonTask = MySerializerUtil.serializer_Java(taskInfo.getPersonSelfTaskInfo());
			YTDaoImpl.updatePersonSelfTaskData(bytePersonTask, userId);

		}
		else
		{
			byte[] byteSystemTask = MySerializerUtil.serializer_Java(taskInfo.getSystemTaskInfo());
			YTDaoImpl.updateSystemTaskData(byteSystemTask, userId);
		}
	}

}
