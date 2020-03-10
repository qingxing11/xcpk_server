package com.wt.naval.dao.impl;

import java.util.ArrayList;

import com.wt.db.SqlSimpleUtil;
import com.wt.job.MyJobTask;
import com.wt.util.Tool;

import model.ConfigFruitmechinewining;
import model.UserTask;

public class YTDaoImpl {

	/**
	 * 获取所有的水果机相关的功能
	 */
	private static final String GETALL_SERVER_FruitMachine = "SELECT * FROM `config_FruitMechineWining` ";

	public static ArrayList<ConfigFruitmechinewining> getRewardOdds() {
		return (ArrayList<ConfigFruitmechinewining>) SqlSimpleUtil.selectBeanList(GETALL_SERVER_FruitMachine,
				ConfigFruitmechinewining.class);
		
	}


	/*
	 * 更新日常任务信息
	 */
	private static final String UPDATE_DATATASKINFO = "Update `user_task` Set `dayTask` = ? Where `userId` = ?";

	public static final void updateDayTaskData(byte[] day_task, int userId) {
		MyJobTask.addTask(()->SqlSimpleUtil.update(UPDATE_DATATASKINFO, day_task, userId));
	}

	/*
	 * 更新个人任务信息
	 */
	private static final String UPDATE_PersonSelfTASKINFO = "Update `user_task` Set `personSelfTask` = ? Where `userId` = ?";

	public static final void updatePersonSelfTaskData(byte[] personSelf_task, int userId) {
		SqlSimpleUtil.update(UPDATE_PersonSelfTASKINFO, personSelf_task, userId);
	}

	/*
	 * 更新系统任务信息
	 */
	private static final String UPDATE_SystemTASKINFO = "Update `user_task` Set `systemTask` = ? Where `userId` = ?";

	public static final void updateSystemTaskData(byte[] system_task, int userId) {
		SqlSimpleUtil.update(UPDATE_SystemTASKINFO, system_task, userId);
	}

	/*
	 * 更新系统任务信息
	 */
	private static final String UPDATE_OnLine = "Update `user_task` Set `onLineTask` = ? Where `userId` = ?";

	public static final void updateOnLineTaskData(byte[] system_task, int userId) {
		MyJobTask.addTask(()->SqlSimpleUtil.update(UPDATE_OnLine, system_task, userId));
	}
	
	private static final String INSERT_TASK = "INSERT INTO `user_task`(`userId`,`dayTask`,`personSelfTask`,`systemTask`,`freeChouJiang`,`onLineTask`) VALUES (?,?,?,?,?,?)";

	/**
	 * 添加一封新的全服邮件 在全服邮件表
	 * 
	 * @param title
	 *            邮件标题
	 * @param content
	 *            邮件内容
	 * @param sendtime
	 *            发送日期
	 * @param attachContent
	 *            附件内容
	 * @return 邮件的唯一id
	 */
	public static int insertServerTaskInfo(int userId, byte[] byteDayTask, byte[] bytePersonSelfTask,
			byte[] byteSystemTask, int freeChouJiang,byte[] byteOnLine) {
		Tool.print_debug_level0("userId:" + userId + ",byteDayTask:" + byteDayTask + ",bytePersonSelfTask:"
				+ bytePersonSelfTask + ",byteSystemTask:" + byteSystemTask + ",freeChouJiang:" + freeChouJiang+",byteOnLine:"+byteOnLine);
		return SqlSimpleUtil.insert(INSERT_TASK, userId, byteDayTask, bytePersonSelfTask, byteSystemTask,
				freeChouJiang,byteOnLine);
	}

	/*
	 * 更新抽奖任务信息
	 */
	private static final String UPDATE_FreeChouJiang = "Update `user_task` Set `freeChouJiang` = ? Where `userId` = ?";
	public static final void updateFreeChouJiangData(int freeChouJiang, int userId) {
		SqlSimpleUtil.update(UPDATE_FreeChouJiang, freeChouJiang, userId);
	}

	private static final String SELECT_UserTask = "Select * from `user_task` Where `userId`=?";
	public static  <T>  UserTask getUserTask(int userId)
	{
		return SqlSimpleUtil.selectBean(SELECT_UserTask, UserTask.class, userId);
	}


}
