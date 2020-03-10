package com.yt.xcpk.task;

import com.yt.xcpk.data.TaskDetailedInfo;

public interface TaskService {

	/**
	 * 注册时设置玩家的任务信息
	 * 
	 * @param userId
	 */
	void registerPlayerSetTaskInfo(int userId);

	/**
	 * 进入游戏时 加载玩家的任务信息 并推送给客户端
	 * 
	 * @param userId
	 */
	void loadPlayerTaskInfo(Integer userId);	
	
	/**
	 * 获取请求领取奖励的任务
	 * 
	 * @param userId
	 * @param taskId
	 * @return
	 */
	TaskDetailedInfo getCurrentRequestTask(int userId, int taskId);

	
	/**
	 * 判断是否可以免费抽奖
	 * @return
	 */
	boolean requestChouJiang(int userId);
	/**
	 * 更新免费抽奖的状态  是否可免费抽
	 * @param freeChouJiang
	 */
	void updateFreeChouJiangState(int userId,int freeChouJiang);
	/**
	 * 获取本次抽奖的结果
	 * @return
	 */
	int getCurrentGetChouJiangInfo();
	
	// /**
	// * 每次完成任务时调用
	// * @param userId
	// * @param taskType 任务大类型 每日任务/个人任务/系统任务
	// * @param taskSmallType 任务小类型 每日任务之一/个人任务之一/系统任务之一
	// * @param addValue 任务增加的数值
	// */
	// void setPlayerSendTask(int userId, int taskType, int taskSmallType, int
	// addValue);

	/**
	 * 大喇叭发言 任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void loudSpeakersTask(int userId, int addValue);

	/**
	 * 发表情任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void sendEnjoyTask(int userId, int addValue);

	/**
	 * 经典场获胜 任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void classicWinTask(int userId, int addValue);

	/**
	 * 每日通杀场上庄任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void everyDayUpBankerInKillRoom(int userId, int addValue);

	/**
	 * 每日万人场上庄任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void everyDayUpBankerInWanRenRoom(int userId, int addValue);

	/**
	 * 经典场获得金花任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void classicWinJinHuaTask(int userId, int addValue);

	/**
	 * 经典场获得顺金任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void classicWinShunJinTask(int userId, int addValue);

	/**
	 * 经典场获得豹子 任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void classicWinBaoZiTask(int userId, int addValue);

	/**
	 * 玩家等级任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void playerLevelTask(int userId, int addValue);

	/**
	 * 玩家在线时间任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void playerOnLineTimeTask(int userId, int addValue);

	/**
	 * 玩家充值任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void playerPaymentTask(int userId, int addValue);

	/**
	 * 通杀场押中豹子任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void killRoomAndInTheBaoZiTask(int userId, int addValue);

	/**
	 * 万人场押中豹子任务
	 * 
	 * @param userId
	 * @param addValue
	 */
	void wanRenRoomAndInTheBaoZiTask(int userId, int addValue);

	/**
	 * 玩家赢金榜排名第一 任务
	 * @param userId
	 * @param addValue
	 */
	void WinRankFirstTask(int userId, int addValue);

	/**
	 * 玩家充值榜排名第一任务
	 * @param userId
	 * @param addValue
	 */
	void paymentFristTask(int userId, int addValue);
	
	/**
	 * 领取在线奖励
	 * @param timeId
	 */	
	Integer requestLingQuOnLineReward(int userId,int timeId);
	
	/**
	 * 设置当前请求领取的在线奖励
	 * @param timeId
	 * @param isLingQu
	 */
	void setOnLineRewardInfo(int userId,int timeId,Integer isLingQu);
	
	/**
	 * 玩家领取奖励
	 * @param userId
	 * @param type
	 * @param num
	 */
	void setPlayerAttachRewardInfo(int userId,int type,int num);
	/**
	 * 更新任务信息
	 * @param taskBigType
	 * @param userId
	 */
	void updateTaskInfo(int taskBigType, int userId);
}
