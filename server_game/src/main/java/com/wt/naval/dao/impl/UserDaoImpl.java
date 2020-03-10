package com.wt.naval.dao.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.wt.archive.MailData;
import com.wt.db.RedisUtil;
import com.wt.db.SqlSimpleUtil;
import com.wt.job.MyJobTask;
import com.wt.naval.dao.model.user.GameDataModel;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.server.DBUtil;
import com.wt.util.timetask.SimpleTaskUtil;
import com.wt.xcpk.PlayerSimpleData;

public class UserDaoImpl
{
	public static boolean transaction_testId()
	{
		String inert = "INSERT INTO `test`(`test`) VALUES(1)";
		
		boolean isSuccess = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
			conn = DBUtil.openConnection();
			conn.setAutoCommit(false);

			pst = conn.prepareStatement(inert);
			pst.executeUpdate();
			pst.close();

			int key = 0;
			pst = conn.prepareStatement("SELECT LAST_INSERT_ID()");
			rs = pst.executeQuery();
			
			if (rs.next())
			{
				key = rs.getInt(1);
			}
			pst.close();
			System.out.println("key:"+key);
			
			isSuccess = true;
		}
		catch (SQLException e)
		{
			isSuccess = false;
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		finally
		{
			try
			{
				conn.setAutoCommit(true);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			DBUtil.closeConnection(rs, pst);
		}
		return isSuccess;
	}
	
	// 更新上一次增加人数时间
	private static final String UPDATE_USER_DATA_PEOPLE = "UPDATE `user_data` SET  `lastAddPeopleTimer`=?  WHERE `userId`=?";

	public static boolean updateUserLastAddPeopleTimer(int userId, long lastAddPeopleTimer)
	{
		return SqlSimpleUtil.update(UPDATE_USER_DATA_PEOPLE, lastAddPeopleTimer, userId) > 0;
	}

	// 更新world_countrydata表里的cityDatas数据
	private static final String UPDATE_COUNTRY_CITYDATAS = "UPDATE `world_countrydata` SET `cityDatas` = ? WHERE `srcId` = ?";

	public static void updateCountryCityDatas(byte[] citydatas, int srcId)
	{
		SqlSimpleUtil.update(UPDATE_COUNTRY_CITYDATAS, citydatas, srcId);
	}

	// 插入国家的信息
	private static final String INSERT_COUNTRYDATA = "INSERT INTO `world_countrydata`(`srcId`,`cityNum`,`power`,`tradeNum`,`cityDatas`) VALUES (?,?,?,?,?)";

	

	// 删除整个国家的表的内容
	private static final String DELETE_COUNTRYDADA_ALL = "DELETE FROM `world_countrydata`";

	public static void delectAllCountryData()
	{
		SqlSimpleUtil.update(DELETE_COUNTRYDADA_ALL);
	}

	// 插入玩家反馈消息
	private static final String INSERT_FEEDBACK = "INSERT INTO `feedback`(`id`,`name`,`content`) VALUES (?,?,?)";

	public static final boolean AddFeedBack(int userId, String name, String content)
	{
		return SqlSimpleUtil.insert(INSERT_FEEDBACK, userId, name, content) != null;
	}

	private static final String Get_USER_COUNTRYID = "SELECT `countryId` FROM `user_data` WHERE `userId`=?";

	/**
	 * 获取玩家选择的国家
	 * @param userId
	 * @return
	 */
	public static int getUserCountryId(int userId)
	{
		return SqlSimpleUtil.selectSingleObject(Get_USER_COUNTRYID, userId);
	}

	// 保存用户信息
	private static final String INSERT_USER_INFO_FAST = "INSERT INTO `user`(`account`) VALUES (?)";

	public static boolean saveUserInfo(String account)
	{
		return SqlSimpleUtil.insert(INSERT_USER_INFO_FAST, account) != null;
	}

	// 保存用户信息
	private static final String INSERT_USER_INFO_WX = "INSERT INTO `user`(`account`,`unionID`) VALUES (?,?)";

	public static Integer saveUserInfo(String account, String unionID)
	{
		return SqlSimpleUtil.insert(INSERT_USER_INFO_WX, account, unionID);
	}

	// 保存用户unionID
	private static final String INSERT_USER_UNION = "INSERT INTO `user`(`unionID`) VALUES (?)";

	public static boolean saveUserUnion(String unoindID)
	{
		return SqlSimpleUtil.insert(INSERT_USER_UNION, unoindID) != null;
	}

	// 更新玩家金币数
	private static final String UPDATE_USER_COINS = "UPDATE `user_data` SET `coins` = ? WHERE `userId` = ?";
	/**更新玩家金币数*/
	public static void updateUserCoins(long num, long userId)
	{
		SqlSimpleUtil.update(UPDATE_USER_COINS, num, userId);

	}
	
	// 更新玩家金币数
	private static final String UPDATE_USER_COINSAll = "UPDATE `user_data` SET `coins` = ? WHERE `userId` = ?";

	/** 更新玩家金币数 */
	public static void updateUserCoinsBatch(Object[][] object)
	{
		SqlSimpleUtil.updateBatch(UPDATE_USER_COINSAll, object);
	}
	// 更新玩家钻石数
	private static final String UPDATE_USER_CRYSTALS = "UPDATE `user_data` SET `crystals` = ? WHERE `userId` = ?";

	public static void updateUserCrystals(int num, int userId)
	{
		SqlSimpleUtil.update(UPDATE_USER_CRYSTALS, num, userId);
	}

	private static final String ADD_USER_CRYSTALS = "UPDATE `user_data` SET `crystals` = `crystals` + ? WHERE `userId` = ?";

	public static void addUserCrystals(int num, int userId)
	{
		SqlSimpleUtil.update(ADD_USER_CRYSTALS, num, userId);
	}

	private static final String UPDATE_USER_CRYSTALS_ADD = "UPDATE `user_data` SET `crystals` = `crystals` + ? WHERE `userId` = ?";

	public static void updateUserCrystalsAdd(int num, int userId)
	{
		SqlSimpleUtil.update(UPDATE_USER_CRYSTALS_ADD, num, userId);
	}

	// 查找用户账号是否已存在
	private static final String GET_ACCOUNT_COUNT = "SELECT COUNT(`account`) FROM `user` WHERE `account`=?";

	public static boolean isAccountExists(String account)
	{
		return SqlSimpleUtil.selectCount(GET_ACCOUNT_COUNT, account) > 0;
	}

	private static final String GET_USERID_COUNT = "SELECT COUNT(`userId`) FROM `user` WHERE `userId`=?";

	public static boolean isUserIdExists(int userId)
	{
		return SqlSimpleUtil.selectCount(GET_USERID_COUNT, userId) > 0;
	}

	// 检查兑换码是否存在
	private static final String GET_REDEEM_EXISTS = "SELECT COUNT(`code`) FROM `redeem` WHERE `code`=?";

	public static boolean isRedeemExists(String code)
	{
		return SqlSimpleUtil.selectCount(GET_REDEEM_EXISTS, code) > 0;
	}

	// 玩家是否在黑名单
	private static final String USER_IS_BLOCKED = "SELECT COUNT(`userId`) FROM `user_blacklist` WHERE `userId`=?";

	public static boolean isInTheBlackList(int userId)
	{
		return SqlSimpleUtil.selectCount(USER_IS_BLOCKED, userId) > 0;
	}

	// 添加用户信息（注册用）
	private static final String INSERT_USER_INFO = "INSERT INTO `user`(`account`,`password`,`email`,`imei`,`reg_phone_num`) VALUES (?,?,?,?,?)";

	// 保存用户信息
	public static boolean saveUserInfo(String account, String password, String email, String imei, String reg_phone_num)
	{
		return SqlSimpleUtil.insert(INSERT_USER_INFO, account, password, email, imei, reg_phone_num) != null;
	}

	// 获取玩家信息
	private static final String GET_USER_BY_NAME = "SELECT * FROM `user` WHERE `account`=?";

	public static UserInfoModel getUser(String account)
	{
		return SqlSimpleUtil.selectBean(GET_USER_BY_NAME, UserInfoModel.class, account);

	}

	// 获取玩家信息
	private static final String GET_USER_BY_USERID = "SELECT * FROM `user` WHERE `userId`=?";

	public static UserInfoModel getUser(int user_id)
	{
		return SqlSimpleUtil.selectBean(GET_USER_BY_USERID, UserInfoModel.class, user_id);
	}

	// 更新手机号码
	private static final String UPDATE_USER_DATA_MOBILENUM = "UPDATE `user_data` SET  `phone_num`=?  WHERE `userId`=?";

	public static boolean updateUserMobileNum(int userId, String mobilenum)
	{
		return SqlSimpleUtil.update(UPDATE_USER_DATA_MOBILENUM, mobilenum, userId) > 0;
	}

	// 检查昵称是否存在（如何需要测试昵称去重的话）
	private static final String GET_NICKNAME_COUNT = "SELECT COUNT(`nickName`) FROM `user_data` WHERE `nickName`=?";
	public static boolean isNicknameExists(String nick_name)
	{
		return SqlSimpleUtil.selectCount(GET_NICKNAME_COUNT, nick_name) > 0;
	}
	
	private static final String CHECK_ACCOUT = "SELECT COUNT(`userId`) FROM `user` WHERE `account`=? AND `password` = ? AND `userId` = ?";
	public static boolean checkAccount(String accout,String password,int userId)
	{
		return SqlSimpleUtil.selectCount(CHECK_ACCOUT, accout,password,userId) > 0;
	}

	// 获取用户数量
	private static final String GET_USER_COUNT = "SELECT COUNT(`userId`) FROM `user`";

	public static int getUserDataCount()
	{
		return (int) SqlSimpleUtil.selectCount(GET_USER_COUNT);
	}

	// 获取用户id
	private static final String GET_USERID_BY_ACCOUNT = "SELECT `userId` FROM `user` WHERE `account`=?";

	public static int getUserId(String account)
	{
		return SqlSimpleUtil.selectSingleObject(GET_USERID_BY_ACCOUNT, account);
	}

	// 获取用户数据
	private static final String GET_USERDATA_BY_ID = "SELECT * FROM `user_data` WHERE `userId`=?";

	public static GameDataModel getUserData(int userId)
	{
		GameDataModel playerModel = SqlSimpleUtil.selectBean(GET_USERDATA_BY_ID, GameDataModel.class, userId);
		return playerModel;
	}

	// 更新登录时间
	private static final String UPDATE_LOGIN_TIME = "UPDATE `user_data` SET `lastLoginTime` = ? WHERE `userId` = ?";

	public static void updateLoginTime(int userId)
	{
		SqlSimpleUtil.update(UPDATE_LOGIN_TIME, new Timestamp(Tool.getCurrTimeMM()), userId);
	}

	// 更新登出时间
	private static final String UPDATE_LOGOUT_TIME = "UPDATE `user_data` SET `lastLogoutTime` = ? WHERE `userId` = ?";

	public static void updateLogOutTime(int userId)
	{
		SqlSimpleUtil.update(UPDATE_LOGOUT_TIME, new Timestamp(Tool.getCurrTimeMM()), userId);
	}

	// 删除一个黑名单
	private static final String DELETE_BLACKLIST = "DELETE FROM `user_blacklist` WHERE `userId` = ?";

	public static void deleteUserBlackList(int userId)
	{
		SqlSimpleUtil.update(DELETE_BLACKLIST, userId);
	}

	// 增加一个黑名单
	private static final String INSERT_BLACKLIST = "INSERT INTO `user_blacklist` (`userId`) VALUES (?)";

	public static void addUserBlackList(int userId)
	{
		SqlSimpleUtil.insert(INSERT_BLACKLIST, userId);
	}

	// 获取用户注册时间
	private static final String GET_USER_REGTIME = "SELECT `regTime` FROM `user` WHERE `userId`=?";

	public static long getRegTime(int userId)
	{
		return SqlSimpleUtil.selectSingleObject(GET_USER_REGTIME, "reg_phone_num", userId);
	}

	// 获取用户钻石数
	private static final String GET_CRYSTALS_BY_USERID = "SELECT `crystals` FROM `user_data` WHERE `userId`=?";

	public static int getUserCrystals(int userId)
	{
		return SqlSimpleUtil.selectSingleObject(GET_CRYSTALS_BY_USERID, "crystals", userId);
	}

	/****************** 邮件 *********************************************/
	// 发送一封邮件给玩家
	private static final String INSERT_USER_MAIL = "INSERT INTO `mail`(`toUserId`,`type`,`fromNick`, `time`, `title`, `content`,`status`, `attachmentType`, `itemType`,`itemNum`) VALUES (?,?,?,?,?,?,?,?,?,?)";

	public static boolean sendMail(int toUserId, byte type, String fromNick, String title, String content, String item_type, String item_num)
	{
		return SqlSimpleUtil.insert(INSERT_USER_MAIL, toUserId, type, fromNick, new Time(System.currentTimeMillis()), title, content, MailData.STATUS_状态未读, item_type, item_num) != null;
	}

	/**
	 * 离线邮件插入数据库
	 * 
	 * @param toUserId
	 * @param mailData
	 * @return 发送是否成功
	 */
	public static boolean sendMail(int toUserId, MailData mailData)
	{
		return SqlSimpleUtil.insert(INSERT_USER_MAIL, toUserId, mailData.type, mailData.fromNick, new Time(System.currentTimeMillis()), mailData.title, mailData.content, MailData.STATUS_状态已读, mailData.itemType, mailData.itemNum) != null;
	}

	// 获取玩家的邮件列表
	private static final String GET_USER_MAIL = "SELECT * FROM `user_mail` WHERE `toUserId` = ? AND `status` != ?";

	public static HashMap<Integer, MailData> getUserMail(Player playerData)
	{
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap<Integer, MailData> hashMap_mailData = new HashMap<>();
		try
		{
			pst = DBUtil.openConnection().prepareStatement(GET_USER_MAIL);
			pst.setInt(1, playerData.getUserId());
			pst.setInt(2, MailData.STATUS_状态已删除);
			rs = pst.executeQuery();

			while (rs.next())
			{
				MailData mailData = new MailData();
				for (int i = 0 ; i < rs.getMetaData().getColumnCount() ; i++)
				{
					try
					{
						String name = rs.getMetaData().getColumnName(i + 1);
						Field field = mailData.getClass().getField(name);
						Object obj;

						switch (name)
						{
							case "type":
							case "status":
							case "attachmentType":
								obj = rs.getByte(name);
								break;
							case "time":
								obj = rs.getTimestamp(name).getTime() / 1000;
								break;
							default:
								obj = rs.getObject(i + 1);
								break;
						}

						field.set(mailData, obj);

					}
					catch (NoSuchFieldException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
				hashMap_mailData.put(mailData.mailId, mailData);
				playerData.mailIndex = mailData.mailId > playerData.mailIndex ? mailData.mailId : playerData.mailIndex;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			DBUtil.closeConnection(rs, pst);
		}

		playerData.mailIndex++;
		return hashMap_mailData;
	}

	private static final String UPDATE_USER_RENAME = "UPDATE `user_data` SET  `reUserName`=?  WHERE `userId`=?";

	public static boolean updateUserReName(int userId, String re_user_name)
	{
		return SqlSimpleUtil.update(UPDATE_USER_RENAME, re_user_name, userId) > 0;
	}

	// 更新玩家headid
	private static final String UPDATE_PLAYER_HEADID = "UPDATE `user_data` SET `headid` = ? WHERE `userId` = ?";

	public static boolean update_Player_HeadId(int userId, int headid)
	{
		return SqlSimpleUtil.update(UPDATE_PLAYER_HEADID, headid, userId) > 0;
	}

	// 获取资源信息
	private static final String GET_USER_RESOURCES_BY_USERID = "SELECT * FROM `user_data` WHERE `userId`=?";

	public static GameDataModel selectUserResourcesDat(int userId)
	{
		return SqlSimpleUtil.selectBean(GET_USER_RESOURCES_BY_USERID, GameDataModel.class, userId);
	}

	// 添加用户数据（注册用）
	private static final String INSERT_USERDATA = "INSERT INTO `user_data`(`userId`,`account`,`nickName`,`coins`,`lastOutPutTime`,`startOutPutTime`) VALUES (?,?,?,?,?,?)";
	public static final String INSERT_USERFRIENDSINFO = "INSERT INTO `user_friendsinfo`(`userId`) VALUES (?)";
	public static final String INSERT_添加好友事件列表 = "INSERT INTO `user_friendevent`(`userId`) VALUES (?)";
	
	public static boolean transaction_register(int userId, String account,String nickName,long coins,long curTime)
	{
		System.out.println("account:"+account+",nickName:"+nickName);
		boolean isSuccess = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
			conn = DBUtil.openConnection();
			conn.setAutoCommit(false);

			pst = conn.prepareStatement(INSERT_USERDATA);
			int index = 0;
			pst.setInt(++index, userId);
			pst.setString(++index, Tool.filterEmoji(account));
			pst.setString(++index, Tool.filterEmoji(nickName));
			pst.setLong(++index, coins);
			pst.setLong(++index, curTime);
			pst.setLong(++index, curTime);
			pst.executeUpdate();
			pst.close();
			
			isSuccess = true;
		}
		catch (SQLException e)
		{
			isSuccess = false;
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
				Tool.print_dbError("注册时错误，回滚失败", e1);
			}
			e.printStackTrace();
			Tool.print_dbError("注册时错误", e);
		}
		finally
		{
			try
			{
				conn.setAutoCommit(true);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			DBUtil.closeConnection(rs, pst);
		}
		return isSuccess;
	}

	private static final String UPDATE_PLAYER_TIMERPEOPLE = "UPDATE `user_data` SET `lastAddPeopleTimer` = ? WHERE `userId` = ?";

	public static boolean UpdataLastAddPeopleTimer(int userId, int temp)
	{
		return SqlSimpleUtil.update(UPDATE_PLAYER_TIMERPEOPLE, temp, userId) > 0;
	}

	private static final String UPDATE_PLAYER_TIMERLOYAL = "UPDATE `user_data` SET `lastAddPeopleLoyalTimer` = ? WHERE `userId` = ?";

	public static boolean UpdataLastAddPeopleLoyalTimer(int userId, int temp)
	{
		return SqlSimpleUtil.update(UPDATE_PLAYER_TIMERLOYAL, temp, userId) > 0;
	}

//	public int userId;
//	public String nickName;
//	public String headIconUrl;
//	public int conis;
//	public int pos;
	private static final String GET_PLAYER_SIMPLEDATA = "SELECT `nickName`,`headIconUrl`,`coins`,`roleId`,`vipLv`,`playerLevel`,`crystals`,`sign`,`victoryNum`,`failureNum`,`headIcon` FROM `user_data` WHERE `userId`=?";

	public static PlayerSimpleData getPlayerSimpleData(int userId)
	{
		HashMap<String, Object> map_bean = SqlSimpleUtil.selectObject(GET_PLAYER_SIMPLEDATA, userId);
		PlayerSimpleData playerSimpleData = null;
		if (map_bean != null)
		{
			try
			{
				String nickName = (String) map_bean.get("nickName");
				String headIconUrl = (String) map_bean.get("headIconUrl");
				long conis = (long) map_bean.get("coins");
				int roleId = (int) map_bean.get("roleId");
				int vipLv = (int) map_bean.get("vipLv");
				int lv = (int) map_bean.get("playerLevel");

				// 砖石
				int crystals = (int) map_bean.get("crystals");
				// 签名
				String sign = (String) map_bean.get("sign");
				// 胜场
				int victoryNum = (int) map_bean.get("victoryNum");
				// 负场
				int failureNum = (int) map_bean.get("failureNum");

				playerSimpleData = new PlayerSimpleData(userId, nickName, headIconUrl, conis, 0,0,0, roleId, vipLv, lv, crystals, sign, victoryNum, failureNum, 0, 0);
			}
			catch (Exception e)
			{
				Tool.print_error("转换PlayerSimpleData时出错", e);
			}
		}

		return playerSimpleData;
	}
	
	
		private static final String UPDATE_USER_DATA_OutPut_Money = "UPDATE `user_data` SET  `lastOutPutTime`=?,`monetTree`=?  WHERE `userId`=?";
		/**产出时间*产出金币*/
		public static int updateUserLastOutPutTimeAndMoneyTree(int userId, long lastOutPutTime,long moneyTree)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_OutPut_Money, lastOutPutTime,moneyTree, userId);
		}
		
		private static final String UPDATE_USER_DATA_OutPut = "UPDATE `user_data` SET  `lastOutPutTime`=?  WHERE `userId`=?";
		/**产出时间*/
		public static int updateUserLastOutPutTime(int userId, long lastOutPutTime)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_OutPut, lastOutPutTime,userId);
		}
		
		private static final String UPDATE_USER_DATA_Money = "UPDATE `user_data` SET  `monetTree`=?  WHERE `userId`=?";
		/**产出金币*/
		public static int updateUserMoneyTree(int userId, long moneyTree)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_Money,moneyTree, userId);
		}
		
		private static final String UPDATE_USER_DATA_StartOutPutTime = "UPDATE `user_data` SET  `startOutPutTime`=?  WHERE `userId`=?";
		/**产出开始时间*/
		public static int updateUserStartOutPutTime(int userId, long startOutPutTime)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_StartOutPutTime,startOutPutTime, userId);
		}
		
		private static final String UPDATE_USER_DATA_TreeLv = "UPDATE `user_data` SET  `treeLv`=?  WHERE `userId`=?";
		/**摇钱树等级*/
		public static int updateUserTreeLv(int userId, int treeLv)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_TreeLv,treeLv, userId);
		}
		
		private static final String UPDATE_USER_DATA_AddTreeLv = "UPDATE `user_data` SET  `curMonthAddTreeLv`=`curMonthAddTreeLv`+?  WHERE `userId`=?";
		/**摇钱树当月增加等级*/
		public static int updateUserAddTreeLv(int userId, int addMonthAddTreeLv)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_AddTreeLv,addMonthAddTreeLv, userId);
		}
		
		private static final String UPDATE_USER_DATA_AddCoin = "UPDATE `user_data` SET  `coins`=`coins`+?  WHERE `userId`=?";
		/**增加金币*/
		public static int updateUserAddCoin(int userId, int coin)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_AddCoin,coin, userId);
		}
		
		private static final String UPDATE_USER_DATA_ResAddTreeLv = "UPDATE `user_data` SET  `curMonthAddTreeLv`=?  WHERE `userId`=?";
		/**摇钱树当月增加等级重置*/
		public static int updateUserResAddTreeLv(int userId)
		{
			return SqlSimpleUtil.update(UPDATE_USER_DATA_ResAddTreeLv,0, userId);
		}
		
		private static final String UPDATE_USER_DATA_VIPLV = "UPDATE `user_data` SET  `vipLv`=?,vipTime =?  WHERE `userId`=?";
		/**VIP等级*/
		public static void updateUserVIPLv(int userId, int vipLv)
		{
			SqlSimpleUtil.update(UPDATE_USER_DATA_VIPLV, vipLv,new Timestamp(MyTimeUtil.getCurrTimeMM()), userId);
		}
		
		private static final String UPDATE_USER_DATA_topUp = "UPDATE `user_data` SET  `topUp`=`topUp`+?  WHERE `userId`=?";
		/**充值金额*/
		public static void updateUserTopUp(int userId, int addTopUp)
		{
			SqlSimpleUtil.update(UPDATE_USER_DATA_topUp, addTopUp, userId);
		}
		
		private static final String UPDATE_USER_DATA_vipPayNum = "UPDATE `user_data` SET  `vipPayNum`=`vipPayNum`+?  WHERE `userId`=?";
		/**充值金额*/
		public static void updateUserVipPayNum(int userId, int addTopUp)
		{
			SqlSimpleUtil.update(UPDATE_USER_DATA_vipPayNum, addTopUp, userId);
		}

		private static final String UPDATE_USER_CHANGENAME_CARD = "UPDATE `user_data` SET  `modifyNickName`=`modifyNickName` - 1  WHERE `userId`=?";
		public static void subUserChangeNameCard(int userId)
		{
			SqlSimpleUtil.update(UPDATE_USER_CHANGENAME_CARD, userId);
		}

		private static final String UPDATE_USER_DATA_ClearVip = "UPDATE `user_data` SET  `vipPayNum`= 0,vipLv = 0  WHERE `userId`=?";
		public static void resetVip(int userId)
		{
			SqlSimpleUtil.update(UPDATE_USER_DATA_ClearVip, userId);
		}
}