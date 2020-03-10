package com.wt.naval.module.player;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.naval.friends.FriendsService;
import com.gjc.naval.lottery.LotteryService;
import com.gjc.naval.moneytree.MoneyTreeService;
import com.gjc.naval.safebox.SafeBoxService;
import com.gjc.naval.sign.SignService;
import com.gjc.naval.vip.VipService;
import com.wt.archive.GameData;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.cmd.user.PlayerNicknameVO;
import com.wt.cmd.user.UserAntiaddictionLogoutRequest;
import com.wt.cmd.user.push.Push_LevelUP;
import com.wt.event.server.shutdown.IShutDown;
import com.wt.event.server.shutdown.ShutDownEvent;
import com.wt.naval.cache.UserCache;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.event.timer.FiveMinuteEventListener;
import com.wt.naval.event.timer.TimerEvent;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.netty.client.ClientHelper;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.PlayerSimpleData;

import io.netty.channel.Channel;

@Service public class PlayerImpl implements PlayerService, PlayerListener, FiveMinuteEventListener, IShutDown
{
	private static final boolean isDebug = false;
	private static final boolean isExpDebug = false;

	public static PlayerImpl inst;

	/** 所有在线玩家 */
	private ConcurrentHashMap<Integer, Player> map_players;

	/**
	 * TODO 超时机制
	 */
	private ConcurrentHashMap<Integer, PlayerSimpleData> map_playerSimpleData = new ConcurrentHashMap<Integer, PlayerSimpleData>();

	@Autowired
	private ClientHelper clientHelper;

	@Autowired
	private FriendsService friendsService;

	@Autowired
	private LotteryService lotteryService;

	@Autowired
	private MoneyTreeService moneyTreeService;

	@Autowired
	private SignService signService;
	
	@Autowired
	private VipService vipService;
	/**
	 * 军队对应的Buff/Debuff
	 */
	private HashMap<Integer, HashMap<Integer, Integer>> map_BuffDebuff = new HashMap<>();

	private ArrayList<Player> list_robotPlayer = new ArrayList<Player>();

	private ArrayList<Player> list_save = new ArrayList<Player>();

	@Autowired
	private SafeBoxService safeBoxService;
	
	@PostConstruct // 初始化方法 服务器启动的时候调用
	private void init()
	{
		inst = this;
		map_players = UserCache.map_onlines;
		initRobotBanker();

		WorldMapUnitEvent.addEventListener(this);
		TimerEvent.addEventListener(this);
		ShutDownEvent.addShutDownTask(this);
	}

	private void initRobotBanker()
	{
		Player player = new Player();
		player.initRobotBanker("系统女神", 1000000000);

		list_robotPlayer.add(player);
	}

	public Player getPlayer(int userId)
	{
		return map_players.get(userId);
	}

	public GameData getGameData(int userId)
	{
		Player player = getPlayer(userId);
		if (player == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取GameData, "获取数据时玩家不在线,player为空,userId:" + userId);
			return null;
		}
		return player.gameData;
	}

	@Override
	public Player getPlayer(MySession session)
	{
		if (session == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取Player, "获取player时session为空,未知玩家");
			return null;
		}
		return getPlayer(session.getUserId());
	}

	public Player getPlayerAndCheck(MySession session)
	{
		Player player = getPlayer(session);
		if (player == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取Player, "获取getPlayerAndCheck时player为空,未知玩家:" + session);
			GameServerHelper.sendPlayerNotFound(session.getChannel());
		}
		return player;
	}

	@Override
	public GameData getGameData(MySession session)
	{
		if (session == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取GameData, "获取player时session为空,未知玩家");
			return null;
		}
		return getGameData(session.getUserId());
	}

	// 玩家上线
	@Override
	public void online(Player player, Channel channel)
	{
		 InetSocketAddress ipSocket = (InetSocketAddress)channel.remoteAddress();
		 String clientIp = ipSocket.getAddress().getHostAddress();
		 
		 PlayerDaoImpl.updatePlayerIP(player.getUserId(),clientIp);
		// Tool.print_debug_level0("玩家上线！！！");
		friendsService.friendIsOnLine(player, true);
		
		UserCache.addOnlineUser(player, channel);
		addPlayerSimpleData(player);

		friendsService.playState(player.getUserId(), true);
		loginCheckVip(player);
//		long treeMoney=	moneyTreeService.calculateMoney(27, 3,1);
//		Tool.print_debug_level0("每小时产出金币数："+treeMoney+",8小时产出金币数："+treeMoney*8);
//		resCurMonthAddTreeLv(player);
//		vipService.TopUp(player.getUserId(),1000);//测试
//		moneyTreeService.addTreeLv(player.getUserId(), 1000);
	}

	private void loginCheckVip(Player player)
	{
		if(player.getVipLv() > 0)
		{
			 long vipTime = player.getVipTime();
			 boolean isSamemonth = MyTimeUtil.isSameMonth(vipTime, MyTimeUtil.getCurrTimeMM());
			 Tool.print_debug_level0("玩家:"+player.getNickName()+",获得vip时间:"+vipTime+",当前时间:"+MyTimeUtil.getCurrTimeMM()+",是否同一个月份:"+isSamemonth);
			 if(!isSamemonth)
			 {
				 //隔月，清空vip等级
				 player.setVipLv(0);
				 player.setVipPayNum(0);
				 
				 UserDaoImpl.resetVip(player.getUserId());
			 }
		}
	}

	private void onLineFriends(Player player)
	{
		friendsService.addFriendsEvent(player);
		friendsService.CancelFriendsEvent(player);
		friendsService.CancelFriendsInfo(player);
	}


	private void addPlayerSimpleData(Player player)
	{
		PlayerSimpleData playerSimpleData = player.getSimpleData();
		map_playerSimpleData.put(player.getUserId(), playerSimpleData);
	}

	@Override
	public void offlineOnGame(Player player)
	{
		if (isDebug || true)
			Tool.print_debug_level0("玩家从游戏服离线,userId:" + player.getUserId() + ",nickName:" + player.getNickName());
		noticeAuthServer(player);

		friendsService.friendIsOnLine(player, false);
		
		lotteryService.removeUserList(player.getUserId());
		//moneyTreeService.calculate(player);

//		friendsService.saveFriendChatVO(player);
//
//		friendsService.playState(player.getUserId(), false);
	}

	private void noticeAuthServer(Player player)
	{
		UserAntiaddictionLogoutRequest logoutRequest = new UserAntiaddictionLogoutRequest(player.getUserId());
		clientHelper.sendRequest(logoutRequest);
	}

	@Override
	public void offlineOnServer(Player player)
	{
		player.updatePlayerData();
		player.setDetection(true);
		
		friendsService.saveFriendChatVO(player);

		friendsService.playState(player.getUserId(), false);
		
		friendsService.updataUserFriendsInfo(player);// 定时更新玩家好友信息
		
		player.clear();// 清理内存
	}

	@Override
	public PlayerSimpleData getPlayerSimpleData(int userId)
	{
		PlayerSimpleData playerSimpleData = map_playerSimpleData.get(userId);
		if (playerSimpleData == null)
		{
			Player player = getPlayer(userId);
			if(player == null)
			{
				playerSimpleData = UserDaoImpl.getPlayerSimpleData(userId);// 数据库
				if (playerSimpleData != null)
				{
					map_playerSimpleData.put(userId, playerSimpleData);
				}
			}
			else
			{
				playerSimpleData = player.getSimpleData();
			}
		}
		if(playerSimpleData == null)
		{
			Tool.print_error("获取玩家简单数据时错误，userId:"+userId+",空");
		}
		return playerSimpleData;
	}
	
	@Override
	public Collection<PlayerSimpleData> getPlayerSimpleData(Collection<Integer> collection_userId)
	{
		Collection<PlayerSimpleData> collection_players = new ArrayList<PlayerSimpleData>();
		
		if(collection_userId != null && collection_userId.size() > 0)
		{
			for (Integer userId : collection_userId)
			{
				PlayerSimpleData playerSimpleData = null;
				try
				{
					playerSimpleData = getPlayerSimpleData(userId);
				}
				catch (Exception e)
				{
					Tool.print_error("获取一组简单数据时错误",e);
					e.printStackTrace();
				}
				collection_players.add(playerSimpleData);
			}
		}

		return collection_players;
	}

	@Override
	public boolean isOnline(int userId)
	{
		return UserCache.isOnline(userId);
	}

	@Override
	public void updatePlayerNickname(int userId, String nickName)
	{
		Player player = getPlayer(userId);
		player.gameData.nickName = nickName;
		try
		{
			PlayerSimpleData data = getPlayerSimpleData(userId);
			data.setNickName(nickName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<PlayerNicknameVO> getPlayersNickname(ArrayList<Integer> list_userIds)
	{
		ArrayList<PlayerNicknameVO> list_vo = new ArrayList<PlayerNicknameVO>();

		if (list_userIds != null && list_userIds.size() > 0)
		{
			for (int id : list_userIds)
			{
				PlayerSimpleData data = null;
				try
				{
					data = getPlayerSimpleData(id);
				}
				catch (Exception e)
				{

				}
				if (data != null)
				{
					PlayerNicknameVO vo = new PlayerNicknameVO(data.userId, data.nickName);
					list_vo.add(vo);
				}
			}
		}

		return list_vo;
	}

	private int saveNum;

	@Override
	public void fiveMinuteTimeUp()
	{
		long start = MyTimeUtil.getCurrTimeMM();
		savePlayer();
		long use = MyTimeUtil.getCurrTimeMM() - start;
		Tool.print_debug_level0("保存玩家完成，耗时:" + use + ",保存人数:" + saveNum);
	}

	private void savePlayer()
	{
		list_save.clear();
		for (Player player : map_players.values())
		{
			if (player.isModify())
			{
				list_save.add(player);
				player.clearModify();
			}
		}
		saveNum = list_save.size();
		Tool.print_debug_level0("需要保存的玩家数:"+list_save.size());
		if (list_save.size() <= 0)
		{
			return;
		}
		Object[][] params = new Object[list_save.size()][10];// //coins,exp,level,victoryNum,failureNum,crystals,modifyNickName,robPos,addTime
		for (int i = 0 ; i < list_save.size() ; i++)
		{
			Player player = list_save.get(i);
			int index = 0;
			params[i][index++] = player.getCoins();
			params[i][index++] = player.getExp();
			params[i][index++] = player.getLevel();
			params[i][index++] = player.getVictoryNum();
			params[i][index++] = player.getFailureNum();
			params[i][index++] = player.getCrytstal();
			params[i][index++] = player.getModifyNickNameNum();
			params[i][index++] = player.getRobPosNum();
			params[i][index++] = player.getAddTimeNum();
			params[i][index++] = player.getUserId();
		}

		PlayerDaoImpl.updatePlayer(params);
	}

	@Override
	public void shutDown()
	{
		Tool.print_debug_level0("服务器关机..................");
		savePlayer();
	}

	@Override
	public void sendMsg(Response response, int userId)
	{
		Player player = getPlayer(userId);
		if (player != null && player.isOnline())
		{
			player.sendResponse(response);
		}
	}

	@Override
	public ConcurrentHashMap<Integer, Player> getMapPlayers()
	{
		return map_players;
	}

	@Override
	public Collection<Player> getAllPlayer()
	{
		return map_players.values();
	}

	@Override
	public Player getRobotBankerPlayer()
	{
		int select = MyUtil.getRandom(0, list_robotPlayer.size());
		return list_robotPlayer.get(select);
	}

	@Override
	public void playerInGame(Integer userId)
	{
		System.out.println("PlayerInGame=============");

		onLineFriends(getPlayer(userId));

		safeBoxService.onPlayerOnLine(userId);

		//moneyTreeService.calculate(getPlayer(userId));

		signService.initPlaySign(userId);

		friendsService.initPlayerFriendChatInfo(userId);
	}

	@Override
	public void addPlayerExp(int userId, int exp)
	{
		Player player = getPlayer(userId);
		if (player != null)
		{
			if (player.getLevel() >= 55)
			{
				return;
			}

			if (isExpDebug)
			{
				Tool.print_debug_level0("玩家获得经验值:" + exp + ",nickName:" + player.getNickName() + ",userId:" + userId);
			}

			int nowLevel = player.getLevel();
			int playerExp = player.addAndGetExp(exp);

			playerExp = calcExp(player, playerExp);// 计算当前给定的经验值是否可以获得新等级，并且返回剩余经验值，如果未升级则返回当前经验值
			player.setExp(playerExp);

			if (nowLevel != player.getLevel() && player.isOnline())// 等级有变化
			{
				Push_LevelUP response = new Push_LevelUP(playerExp, player.getLevel());
				player.sendResponse(response);
			}
		}
	}

	public int calcExp(Player player, int playerExp)
	{
		if (player == null)
		{
			Tool.print_error("calcExp====计算经验时玩家为空，不该发生得调用方式");
			return playerExp;
		}
		if (player.getLevel() >= 55)
		{
			return playerExp;
		}

		int costExp = getCostExp(player.getLevel());
		// Tool.print_debug_level0("calcExp=====玩家[" +
		// player.getNickName() + "]计算等级.userid:" + player.getUserId() +
		// ",经验值:" + player.getExp() + ",当前总经验值:" + playerExp + ",当前等级:"
		// + player.getLevel() + ",升级所需:" + costExp);
		while (playerExp >= costExp)
		{
			playerExp -= costExp;
			player.levelUP();
			costExp = getCostExp(player.getLevel());
			if (player.getLevel() >= 55)
			{
				break;
			}
			Tool.print_debug_level0("玩家[" + player.getNickName() + "]升级，当前等级:" + player.getLevel() + ",升级后剩余经验值:" + playerExp + ",下一集所需经验值 :" + costExp);
		}
		return playerExp;
	}

	public int getCostExp(int level)
	{
		if (level < 1)
		{
			Tool.print_error("获取经验时等级太低:" + level);
			return 0;
		}
		int a = (int) Math.pow(level - 1, 3);

		int exp = (a + 35) / 3 * ((level - 1) * 3 + 35) + 35;
		return exp;
	}
	
	/**
	 * 重置当月摇钱树增加的等级数
	 * @param player
	 */
	public void resCurMonthAddTreeLv(Player player)
	{
		if (player==null) {
			return;
		}
		player.gameData.resCurMonthAddTreeLv();
		UserDaoImpl.updateUserResAddTreeLv(player.getUserId());
	}

	@Override
	public void sendToAll(Response response)
	{
		for (Player player : getAllPlayer())
		{
			if(player != null)
			{
				player.sendResponse(response);
			}
		}
	}

	@Override
	public void bannid(int userId)
	{
		Player player = getPlayer(userId);
		if(player != null)
		{
			player.close();
		}
	}
}