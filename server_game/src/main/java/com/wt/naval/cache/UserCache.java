package com.wt.naval.cache;

import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import com.wt.archive.GameData;
import com.wt.naval.biz.UserBiz;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.util.security.MySession.State;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 在线用户列表以及session
 */

@Service 
public class UserCache
{
	private static UserCache inst;

	@PostConstruct
	private void init()
	{
		inst = this;
	}

	private static final boolean isDebug = false;

	/***
	 * 预计单服最大玩家数
	 */
	public static final int MAX_PLAYERS = 1024;

	public static ConcurrentHashMap<Integer, Player> map_onlines = new ConcurrentHashMap<Integer, Player>(MAX_PLAYERS);// 用户在线数据

	public static ConcurrentHashMap<Integer, Channel> map_channel = new ConcurrentHashMap<Integer, Channel>(MAX_PLAYERS);// 用户在线session

	/**
	 * 已建立链接，等待向账号服验证的通道，验证成功或者10分钟后销毁
	 */
	private static ConcurrentHashMap<Integer, Channel> map_waitValidationSessions = new ConcurrentHashMap<>();

	/**
	 * 玩家在线密钥，每次登陆验证后随机生成，用于玩家短线重连等快速验证
	 */
	private static ConcurrentHashMap<Integer, String> map_pwd = new ConcurrentHashMap<>();

	public UserCache()
	{}

	// 添加在线用户
	public static boolean addOnlineUser(Player bean, Channel channel)
	{
		// System.out.println("bean:"+bean+",channel:"+channel);
		map_onlines.put(bean.getUserId(), bean);
		map_channel.put(bean.getUserId(), channel);

		MySession session = new MySession(channel, bean.getUserId());
		channel.attr(MySession.attr_session).set(session);// 通过合法途径建立的链接,为channel附加session
		bean.setSession(session);
		bean.online(true);

		Tool.print_debug_level0("玩家" + bean.getUserId() + "上线,当前在线人数:" + map_onlines.size());
	 
		// if (map_onlines.size() >
		// GameServerImpl.instance.maxPlayerNum)
		// {
		// GameServerImpl.instance.maxPlayerNum = map_onlines.size();
		// GameServerImpl.instance.maxPlayerTime = Tool.getCurrTimeMM();
		//
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");// 设置日期格式
		// Tool.print_debug_level0("当前玩家数为历史最高，人数：" + map_onlines.size()
		// + ",当前系统时间 ：" + df.format(new Date()));
		// }
		return true;
	}

	private static void initRsa()
	{
		// try
		// {
		// Map<String, Object> keyMap = RSAUtils.genKeyPair();
		// String publicKey = RSAUtils.getPublicKey(keyMap);
		// String privateKey = RSAUtils.getPrivateKey(keyMap);
		// keys.put(channel, privateKey);// 保存密钥
		//
		// byte[] temp1 = RSAJavaToCSharp.b64decode(publicKey);
		// publicKey =
		// RSAJavaToCSharp.getRSAPublicKeyAsNetFormat(temp1);// 转换公钥
		// // System.out.println("publicKey:"+publicKey);
		// bean.setPublicKey(publicKey);// 保存公钥
		// }
		// catch (Exception e)
		// {
		// System.out.println("生成RSA钥匙对出错");
		// e.printStackTrace();
		// return false;
		// }
	}

	/**
	 * 使userId离线
	 * 
	 * @param userId
	 */
	public static void offline(int userId)
	{// 通道关闭
		Channel channel = map_channel.get(userId);
		if (channel != null)
		{
			channel.attr(MySession.attr_session).set(null);

			if (channel.isOpen())
			{
				channel.disconnect();
				channel.close();
			}
		}

		Tool.print_debug_level0("玩家通道关闭，保持存" + MySession.OFFLINE_TIME / 1000 + "s，当前在线人数：" + map_onlines.size());

		map_channel.remove(userId);
		map_pwd.remove(userId);
		Player bean = map_onlines.remove(userId);
		if (bean == null)
		{
			return;
		}
		WorldMapUnitEvent.playerOfflineOnServer(bean);
	
		// UserBiz.updateLogOutTime(userId, Tool.getCurrTimeMM());
	}

	/**
	 * 使通道断线，主动断开和心跳超时调用
	 * 
	 * @param c
	 */
	public static void autoOffline(Channel c)
	{
		MySession session = c.attr(MySession.attr_session).get();
		if (session != null)
		{
			session.setState(State.State_offline);
			gamerOffline(session.getUserId());
		}
		c.close();
		c.disconnect();
	}

	/**
	 * 玩家离线
	 * 
	 * @param uid
	 */
	private static void gamerOffline(int uid)
	{
		Player player = null;
		if ((player = getPlay(uid)) != null)
		{
			Tool.print_debug_level0(player.getNickName() + "离线");
//			gameOfflineAction(uid);
			WorldMapUnitEvent.playerOfflineOnGame(player);
			player.online(false);
			
		}
	}

	/**
	 * 获取在线用户信息
	 * 
	 * @deprecated
	 */
	public static Player getPlay(int userId)
	{
		return map_onlines.get(userId);
	}

	/**
	 * @deprecated
	 * @param userId
	 * @return
	 */
	public static GameData getGameData(int userId)
	{
		return map_onlines.get(userId).gameData;
	}

	public static Channel getChannel(int userId)
	{
		return map_channel.get(userId);
	}

	// 获取session
	public static boolean isSessionExists(int userId)
	{
		if (map_channel.get(userId) != null)
		{
			return true;
		}
		return false;
	}

	// public static String getRSAKey(Channel c)
	// {
	// String key = keys.get(c);
	//
	// if (key == null)
	// {
	// return ServerStatics.privateKey;
	// }
	// else
	// {
	// return key;
	// }
	// }

	/**
	 * 玩家掉线重连时更新玩家通道信息
	 * 
	 * @param userId
	 * @param channel
	 */
	public static void updateSessions(int userId, Channel channel)
	{
		map_channel.put(userId, channel);
		// sessions_uid.put(channel.id().asShortText(),userId);
	}

	public static boolean isOnline(int userId)
	{
		Channel channel = UserCache.map_channel.get(userId);
		if (channel == null || !channel.isActive())
		{
			return false;
		}
		MySession session = channel.attr(MySession.attr_session).get();
		if (session == null || session.isOffline())
		{
			return false;
		}
		return true;
	}

	public static void offlineAll()
	{
		for (Integer userId : UserCache.map_channel.keySet())
		{
			offline(userId);
		}
	}

	/**
	 * 关闭上个通道
	 * 
	 * @param userId
	 */
	public static void closeExistsChannel(int userId)
	{
		Channel channel = UserCache.map_channel.get(userId);
		if (channel != null)
		{
			channel.attr(MySession.attr_session).set(null);

			channel.disconnect();
			channel.close();

			UserCache.map_channel.remove(userId);
		}
	}

	/**
	 * 登陆成功后添加在线密钥
	 * 
	 * @param userId
	 * @param pwd
	 */
	public static void addPwd(int userId, String pwd)
	{
		map_pwd.put(userId, pwd);
	}

	public static String getPwd(int uid)
	{
		return map_pwd.get(uid);
	}

	public void removePwd(int uid)
	{
		map_pwd.remove(uid);
	}

	/**
	 * 获取当前通道对应的userid
	 * 
	 * @param channelId
	 * @return
	 */
	// private static int getUserIdByChannelId(String channelId)
	// {
	// if(sessions_uid.get(channelId) == null)
	// {
	// return -1;
	// }
	// return sessions_uid.get(channelId);
	// }

	/**
	 * 检查当前通道是否通过允许的合法验证指令存储过
	 * 
	 * @param ctx
	 * @param userId_list
	 * @return
	 */
	// public static boolean checkChanelMatchUid(ChannelHandlerContext ctx,
	// int userId)
	// {
	// if(ctx == null)
	// return false;
	//
	// String channelId = ctx.channel().id().asShortText();
	// int sessions_uid = getUserIdByChannelId(channelId);
	// if(sessions_uid == -1)
	// {
	// return false;
	// }
	// return sessions_uid == userId;
	// }

	public static int getUserId(ChannelHandlerContext ctx)
	{
		MySession session = ctx.channel().attr(MySession.attr_session).get();
		if (!UserBiz.checkSessionAndOnline(ctx, session))
		{
			return 0;
		}

		return session.getUserId();
	}

	public static void addWaitValidation(int userId, Channel channel)
	{
		map_waitValidationSessions.put(userId, channel);
	}

	public static Channel getWaitValidationChannel(int userId)
	{
		return map_waitValidationSessions.get(userId);
	}

	public static Channel removeWaitValidationChannel(int userId)
	{
		return map_waitValidationSessions.remove(userId);
	}

	public static String getNickName(Integer userId)
	{
		Player player = getPlay(userId);
		if (player == null)
		{
			Tool.print_error("获取:[" + userId + "]时空");
			return null;
		}
		return player.getNickName();
	}

	public static ConcurrentHashMap<Integer, Channel> getAllChannel()
	{
		return map_channel;
	}
}
