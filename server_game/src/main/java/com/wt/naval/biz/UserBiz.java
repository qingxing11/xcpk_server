package com.wt.naval.biz;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wt.archive.GMMailData;
import com.wt.archive.GameData;
import com.wt.cmd.MsgType;
import com.wt.job.IJob;
import com.wt.job.MyJobTask;
import com.wt.naval.cache.ServerCache;
import com.wt.naval.cache.UserCache;
import com.wt.naval.dao.impl.FriendsDaoImpl;
import com.wt.naval.dao.impl.SignDaoImpl;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.dao.model.user.GameDataModel;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.UuidUtil;
import com.wt.util.security.MyPBKDF2;
import com.wt.util.security.MySession;
import com.wt.util.security.token.MyTokenUtil;
import com.wt.util.security.token.TokenVO;

import io.netty.channel.ChannelHandlerContext;

@Service public class UserBiz
{
	public static void main(String[] args)
	{
		GameDataModel gameDataModel = UserDaoImpl.getUserData(30346);
		System.out.println("gameDataModel:"+gameDataModel);
	}
	
	
	public static UserBiz inst;
	boolean isDebug = true;
	static boolean isDebugState = false;

	/**
	 * 获取玩家选择的国家
	 * 
	 * @param userId
	 * @return
	 */
	public static int getUserSelectCountryId(int userId)
	{
		return UserDaoImpl.getUserCountryId(userId);
	}

	/**
	 * 更新上一次增加人数时间
	 * 
	 * @param userId
	 * @param lastAddPeopleTimer
	 */
	public static boolean updateUserLastAddPeopleTimer(int userId, long lastAddPeopleTimer)
	{
		return UserDaoImpl.updateUserLastAddPeopleTimer(userId, lastAddPeopleTimer);
	}

	/**
	 * 写入国家的cityDatas，此写入只需一次
	 */
//	public static void updateCountryCityDatas()
//	{
//		UserDaoImpl.delectAllCountryData();// 先清空表
//
//		HashMap<Integer, ArrayList<Integer>> countryCity = new HashMap<>();
//		ArrayList<Integer> arrays;
//		for (DataCity city : ServerCache.configs.getDataCity().values())
//		{
//			arrays = new ArrayList<>();
//			if (countryCity.containsKey(city.Country))
//			{
//				arrays = countryCity.get(city.Country);
//				arrays.add(city.Id);
//				countryCity.put(city.Country, arrays);
//			}
//			else
//			{
//				arrays.add(city.Id);
//				countryCity.put(city.Country, arrays);
//			}
//		}
//
//		for (int ci : countryCity.keySet())
//		{
//			DataCountry country = ServerCache.configs.getDataCountry().get(ci);
//			UserDaoImpl.insertCountryData(ci, country.CityNumber, country.Power, country.Power, MySerializerUtil.serializer_Java(countryCity.get(ci)));
//			// UserDaoImpl.updateCountryCityDatas(MySerializerUtil.serializer_Java(countryCity.get(ci)),
//			// ci);
//		}
//	}


	// 保存玩家用户信息
	public static boolean insertUser(String account, String password, String email, String imei, String reg_phone_num)
	{
		return UserDaoImpl.saveUserInfo(account, password, email, imei, reg_phone_num);
	}

	// 保存玩家用户信息
	public static boolean insertUser(String account)
	{
		return UserDaoImpl.saveUserInfo(account);
	}

	public static Integer insertUser(String account, String unionId)
	{
		return UserDaoImpl.saveUserInfo(account, unionId);
	}

	/**
	 * 注册时玩家资源和英雄列表资源初始化
	 * 
	 * @param gameData
	 *                玩家数据类
	 * @param account
	 *                玩家昵称
	 * @return
	 */
	public  boolean userRegisterGuest(GameData gameData, String account)
	{
		FriendsDaoImpl.insetUserFriendsList(gameData.userId, null);
		SignDaoImpl.insetUserSign(gameData.userId,false,false,false,false,false,false,false, MyTimeUtil.getCurrTimeMM());
		return UserDaoImpl.transaction_register(gameData.userId, account,gameData.nickName,gameData.coins,gameData.getLastOutPutTime());
	}

	// 是否在黑名单中
	public static boolean isInTheBlackList(int userId)
	{
		return UserDaoImpl.isInTheBlackList(userId);
	}

	// 获取用户基本信息
	public static UserInfoModel getUserInfo(String account)
	{
		if (account == null || "".equals(account))
		{
			return null;
		}
		UserInfoModel infoModel = UserDaoImpl.getUser(account);
		return infoModel;
	}

	/**
	 * 登录时初始化玩家数据并赋值
	 * 
	 * @param userId
	 *                玩家Id
	 * @return
	 */
	// 获取用户游戏内信息
	public static Player getPlayerData(int userId)
	{
		GameDataModel gameDataModel = UserDaoImpl.getUserData(userId);
		if (gameDataModel == null)
		{
			return null;
		}
		GameData gameData = new GameData(gameDataModel); // 初始化user_data表的数据
		initGameData(userId, gameData); // 初始化该玩家的其他表的数据（英雄列表数据、）

		Player player = new Player(gameData); // 玩家数据赋值
		return player;
	}


	// 获取玩家id
	public static int getUserId(String account)
	{
		if (account == null || account.equals(""))
		{
			Tool.print_debug_level0("account不能为空!");
			return 0;
		}
		return UserDaoImpl.getUserId(account);
	}

	// 更新登录时间
	public static void updateLoginTime(int userId)
	{
		UserDaoImpl.updateLoginTime(userId);
	}

	// 更新登出时间
	public static void updateLogOutTime(int userId, long time)
	{
		UserDaoImpl.updateLogOutTime(userId);
	}

	/**
	 * 检查玩家对全服邮件的领取情况
	 *
	 * @param gameData
	 */
	public static void checkServerMaill(Player gameData)
	{
		ArrayList<GMMailData> arrayList_serverMails = ServerCache.arrayList_serverMail;

		String[] mailIds = null;

//		if (!gameData.serverMail.equals(""))
//		{
//			mailIds = gameData.serverMail.split(",");// 玩家已有全服邮件
//		}

		for (int i = 0 ; i < arrayList_serverMails.size() ; i++)
		{
			GMMailData gmMailData = arrayList_serverMails.get(i);
			String mailId = String.valueOf(gmMailData.mailId);

			if (gmMailData.status == GMMailData.status_无效)
				continue;
			if (gmMailData.time + gmMailData.duration < System.currentTimeMillis() / 1000)
				continue;
			if (ArrayUtils.contains(mailIds, mailId))
				continue;

		
			// 增加全服邮件领取记录
			ArrayUtils.add(mailIds, mailId);
		}

		if (mailIds == null)
			return;

		// 更新全服邮件领取记录
		StringBuilder sb = new StringBuilder();
		for (String mailId : mailIds)
		{
			sb.append(mailId);
		}
		//gameData.serverMail = sb.toString();

		// for (int i = 0 ; i < arraylist_obj.size() ; i++)
		// {
		// ServerMail array_element = arraylist_obj.get(i);// 一个全服邮件
		// boolean isHaveMail = false;
		// if (maill_id != null)
		// {
		// for (int j = 0 ; j < maill_id.length ; j++)
		// {
		// String string = maill_id[j];
		// int checkId = Integer.parseInt(string);
		// if (checkId == array_element.mail_id)// 玩家已有邮件
		// {
		// isHaveMail = true;
		// break;
		// }
		// }
		// }
		//
		// if (!isHaveMail)
		// {
		// MailBiz.sendMail(gameData, array_element, MailData.TYPE_系统);
		// }
		// }
	}

	public static void addUserCrystals_add(int userId, int num)
	{
		MyJobTask.addTask(new IJob()
		{
			public void execute()
			{
				if (num <= 0)
					return;
				Tool.print_debug_level0(MsgType.LOG_玩家增加金币, "玩家数据库中增加金币:" + num + ",userid:" + userId);
				UserDaoImpl.addUserCrystals(num, userId);
			}
		});
	}

	public static String createRandomName(int userId)
	{
		StringBuffer name = new StringBuffer();
		for (int i = 0 ; i < 4 ; i++)
		{
			name.append(Tool.getAbc(Tool.getRandom(26)));
		}
		return name.toString() + userId;
	}

	public static void checkGamedate(GameData gameData)
	{

	}

	public static void initNextDayData(Player gameData, int useDay)
	{
		// 救济金
	}

	public static void playerOnline(Player gameData)
	{
		gameData.setDetection(false);
	}

	public static JSONObject GetUserWXInfo(String openId, String token)
	{
		String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s", token, openId);

		String result = null;
		HttpGet httpGet = new HttpGet(url);
		try
		{
			CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);
			HttpEntity entity = response.getEntity();

			if (entity == null)
			{
				Tool.print_debug_level0("微信获取个人信息回复为空!");
				return null;
			}

			result = EntityUtils.toString(entity, "UTF-8");

			if (result.contains("errcode"))
			{
				Tool.print_debug_level0("微信获取个人信息错误:" + result);
				return null;
			}
			Tool.print_debug_level0("接收微信获取个人信息回复:" + result);

			return JSON.parseObject(result);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public static boolean ChkToken(String openId, String token)
	{
		String url = String.format("https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s", token, openId);
		String result = null;
		HttpGet httpGet = new HttpGet(url);
		try
		{
			CloseableHttpResponse response = HttpClients.createDefault().execute(httpGet);
			HttpEntity entity = response.getEntity();

			if (entity == null)
			{
				Tool.print_debug_level0("ChkToken===>微信token回复为空!");
				return false;
			}

			result = EntityUtils.toString(entity, "UTF-8");
			Tool.print_debug_level0("接收微信登录token回复:" + result);
			return result.contains("{\"errcode\":0,\"errmsg\":\"ok\"}");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 玩家离线重连时快速登陆，校验玩家令牌是否有效
	 * 
	 * @param tokenVO
	 * @return 0:success , 1:token空 ,2:不是在线玩家 ,3:检验失败,令牌错误
	 */
	public static int checkSimpleToken(TokenVO tokenVO)
	{
		if (tokenVO == null)
		{
			return 1;
		}

		String pwd = UserCache.getPwd(tokenVO.uid);
		if (pwd == null)
		{
			return 2;
		}
		boolean isSuccess = false;
		try
		{
			isSuccess = MyPBKDF2.validatePassword(pwd, tokenVO.pwd);
		}
		catch (Exception e)
		{
			Tool.print_error("验证simple token时错误", e);
		}
		if (isSuccess)
		{
			return 0;
		}
		return 3;
	}

	public static int getUserNum()
	{
		return UserDaoImpl.getUserDataCount();
	}

	public static void updateUserReName(int userId, String re_user_name)
	{
		UserDaoImpl.updateUserReName(userId, re_user_name);
	}

	public static boolean checkSessionAndOnline(ChannelHandlerContext ctx, MySession session)
	{
		if (session == null)
		{
			GameServerHelper.sendMessageBySessionErr(ctx);
			return false;
		}
		return true;
	}

	public static boolean isUserIdExists(int userId)
	{
		return UserDaoImpl.isUserIdExists(userId);
	}

	/**
	 * 为玩家创建一个简单token，并保存到缓存
	 * 
	 * @param userId
	 * @return
	 */
	public static TokenVO createToken(int userId)
	{
		String pwd = UuidUtil.generateShortUuid();
		UserCache.addPwd(userId, pwd);
		TokenVO tokenVO = MyTokenUtil.createSimpleToken(userId, pwd);
		return tokenVO;
	}

	/**
	 * 登录时初始化数据
	 * 
	 * @param gameData
	 *                玩家数据类
	 * @return
	 */
	public static boolean initGameData(int userId, GameData gameData)
	{
		return true;
	}

	

	/**
	 * 人口时间
	 * 
	 * @param userId
	 * @param temp
	 * @return
	 */
	public static boolean UpdataLastAddPeopleTimer(int userId, int temp)
	{
		return UserDaoImpl.UpdataLastAddPeopleTimer(userId, temp);
	}

	/**
	 * 民忠时间
	 * 
	 * @param userId
	 * @param temp
	 * @return
	 */
	public static boolean UpdataLastAddPeopleLoyalTimer(int userId, int temp)
	{
		return UserDaoImpl.UpdataLastAddPeopleLoyalTimer(userId, temp);
	}

	@PostConstruct
	private void init()
	{
		inst = this;
	}
}