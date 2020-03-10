package com.yt.xcpk.fruitMachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.dao.impl.FruitMachineDaoImpl;
import com.wt.naval.dao.impl.YTDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.timetask.SimpleTaskUtil;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.KillRoomNoticVO;
import com.yt.cmd.fruitMachine.Push_BankerWiningResponse;
import com.yt.cmd.fruitMachine.Push_ChangeBankerListInfoResponse;
import com.yt.cmd.fruitMachine.Push_FruitMachineStartXiaZhuResponse;
import com.yt.cmd.fruitMachine.Push_FruitSettlementRewardResponse;
import com.yt.cmd.fruitMachine.push.FruitPush_XiaZhu;
import com.yt.cmd.fruitMachine.push.FruitPush_bigWin;
import com.yt.cmd.fruitMachine.push.FruitPush_reconnect;

import data.data.Configs;
import data.data.DataFruitMachine;
import data.define.GrossValue;
import data.define.NormalReward;
import data.define.SpecialReward;
import data.define.UpBankerCoin;
import io.netty.channel.Channel;
import model.ConfigFruitmechinewining;
import model.GameFruit;

@Service public class FruitMachineImpl implements FruitMachineService, Runnable, PlayerListener
{

	private static boolean isDebug = true;
	
	private static final boolean isLotteryDebug = true;

	/** 下注时间 */
	public static final long XiaZhu_TIME = 30 * MyTimeUtil.TIME_S;

	/** 开奖时间 */
	public static final long KaiJiang_TIME = 20 * MyTimeUtil.TIME_S;

	/** 休息时间 */
	public static final int IDLE_TIME = 5;

	/** 单玩家最大下注 */
	public static final int BET_MAX = 20000000 * 8;

	/** 下注状态 */
	public static final int STATE_XiaZhu_TIME = 0;
	/** 开奖状态 */
	public static final int STATE_KaiReward = 1;
	/** 休息状态 */
	public static final int STATE_Free = 2;
	private int state;
	private long stateTime;

	@Autowired
	private Configs config;

	@Autowired
	private PlayerService playerService;
	@Autowired
	private RankService rankService;

	private ArrayList<FruitMachineWiningInterval> list_fruitMachineInterval = new ArrayList<>();

	private ConcurrentHashMap<Integer, FruitMachineVO> list_allPlayerInFruitRoom = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Integer, Integer> list_allXiaZhu = new ConcurrentHashMap<>();
	private long intervalTime = 500;
	public static FruitMachineImpl inst;
	private ArrayList<String> list_history = new ArrayList<String>();
	
	/**特殊率	苹果率	铃铛芒果桔子率	双7双星西瓜率	BAR率	乘2倍小的率
	 * */
	private GameFruit gameFruit;
	
	private int roundIndex;
	
	@PostConstruct // 初始化方法 服务器启动的时候调用
	private void init()
	{
		gameFruit = FruitMachineDaoImpl.getRewardOdds();
		
		inst = this;
		initRoom();
		WorldMapUnitEvent.addEventListener(this);
		try
		{
			SimpleTaskUtil.startTask("fruitMachine", 0, intervalTime, this);
			stateTime = MyTimeUtil.getCurrTimeMM();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void setState(int state)
	{
		this.state = state;
		stateTime = MyTimeUtil.getCurrTimeMM();
	}

	@Override
	public void run()
	{
		try
		{
			runRoomState();
		}
		catch (Exception e)
		{
			Tool.print_error("水果机运行错误", e);
		}
	}

	private void runRoomState()
	{
		switch (state)
		{
			case STATE_KaiReward:
				roundIndex++;
				runXiaZhuTimeState();
				break;
			case STATE_XiaZhu_TIME:
				runKaiJiangTimeState();
				break;
		}
	}

	private Room room;

	private void initRoom()
	{
		room = new Room(2);
	}

	private void addHistory(String historyPath)
	{
		if (list_history.size() >= 20)
		{
			list_history.remove(list_history.get(list_history.size() - 1));
		}
//		if (isDebug)
//			Tool.print_debug_level0("图片路径：" + historyPath);
		list_history.add(historyPath);
	}

	private void setHistoryInfo(int fruitType, int specialReward, int isSpecialReward)
	{
		if (isSpecialReward == 1)
		{
			switch (specialReward)
			{
				case SpecialReward.SinglePointSmple:
				case SpecialReward.SinglePointSpecial:
					String pathSinglePoint = SpecialReward.getTitle(SpecialReward.SinglePointSmple);
					addHistory(pathSinglePoint);
					break;
				case SpecialReward.OnTrain:
					String pathOnTrain = SpecialReward.getTitle(SpecialReward.OnTrain);
					addHistory(pathOnTrain);
					break;
				case SpecialReward.BigFourXi:
					String pathBigFourXi = SpecialReward.getTitle(SpecialReward.BigFourXi);
					addHistory(pathBigFourXi);
					break;
				case SpecialReward.BigThreeYuan:
					String pathBigThreeYuan = SpecialReward.getTitle(SpecialReward.BigThreeYuan);
					addHistory(pathBigThreeYuan);
					break;
				case SpecialReward.SmallThreeYuan:
					String pathSmallThreeYuan = SpecialReward.getTitle(SpecialReward.SmallThreeYuan);
					addHistory(pathSmallThreeYuan);
					break;
				case SpecialReward.TheNineTreasureLamp:
					String pathNineLight = SpecialReward.getTitle(SpecialReward.TheNineTreasureLamp);
					addHistory(pathNineLight);
					break;
			}
		}
		else
		{
			int fruitId = SetRewardIndex(specialReward);
			DataFruitMachine item = config.getDataFruitMachine().get(fruitId);
			addHistory(item.icon);
		}
	}

	/// <summary>
	/// 返回中奖所在索引
	/// </summary>
	/// <param name="rewardType"></param>
	/// <returns></returns>
	private int SetRewardIndex(int rewardType)
	{
		ArrayList<DataFruitMachine> list_fruit = new ArrayList<DataFruitMachine>();
		for (DataFruitMachine item : config.getDataFruitMachine().values())
		{
			if (item.normalReward == rewardType)
			{
				list_fruit.add(item);
			}
		}
		int random = MyUtil.getRandom(0, list_fruit.size());
		int fruitId = list_fruit.get(random).Id;// 配表id比缓存位置索引大1
		return fruitId;
	}

	private void runKaiJiangTimeState()
	{
		if (MyTimeUtil.getCurrTimeMM() - stateTime > XiaZhu_TIME)
		{
			setState(STATE_KaiReward);
			if (isDebug)
				Tool.print_debug_level0("下注时间结束 开奖：" + state);

			pushSettlementRewardResult();
		}
	}

	/**
	 * 推送开奖结果
	 */
	private void pushSettlementRewardResult()
	{
		list_KillRoomNoticVOs.clear();

		ArrayList<Integer> winingIndex = fruitMachineRequest();
		setHistoryInfo(winingIndex.get(0), winingIndex.get(1), winingIndex.get(2));
		if (isDebug)
			Tool.print_debug_level0("水果机推送开奖结果  ：" + winingIndex);
		
		Player player = room.getBankerPlayer();
		if (player == null)
		{
			pushBankerIsNullSelltement(winingIndex);
		}
		else
		{
			pushBankerIsnotNullSelltement(winingIndex, player);
		}
		broadcastFruitMachine();
	}

	private void pushBankerIsnotNullSelltement(ArrayList<Integer> winingIndex, Player player)
	{
		// 当前庄家有人时
		long bankerWiningValue = 0;
		// int jiangPool = 0;
		sbResponseUserId.delete(0, sbResponseUserId.length());
		for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
		{
			ArrayList<Integer> winingValue = fruitPlayerWinOrFail(winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), vo.getMap(), winingIndex.get(3));
			Tool.print_debug_level0("玩家结算结果 winingValue :" + winingValue+",userId:"+vo.getPlayer().getUserId());
			Push_FruitSettlementRewardResponse response = null;
			if (winingValue.size() != 0)
			{
				if (!vo.getPlayer().equals(player))
				{
					if (winingValue.get(0) > 0)
					{
						// jiangPool +=
						// winingValue.get(0) * 0.02f;
						checkAddNotic(vo, (int) (winingValue.get(0) - (winingValue.get(0) * 0.06f)) - vo.getPlayer().getFruitRoomBet());
						Tool.print_coins(vo.getPlayer().getNickName(), (int) (winingValue.get(0) - (winingValue.get(0) * 0.06f)), "水果机",player.getCoins());
						vo.getPlayer().addCoins((int) (winingValue.get(0) - (winingValue.get(0) * 0.06f)));
						response = new Push_FruitSettlementRewardResponse(Push_FruitSettlementRewardResponse.SUCCESS, winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), winingIndex.get(3), (int) (winingValue.get(0) - (winingValue.get(0) * 0.06f)), winingValue.get(1));
					}
					else
					{
						checkAddNotic(vo, winingValue.get(0) - vo.getPlayer().getFruitRoomBet());
						Tool.print_coins(vo.getPlayer().getNickName(),winingValue.get(0), "水果机",player.getCoins());
						vo.getPlayer().addCoins(winingValue.get(0));
						response = new Push_FruitSettlementRewardResponse(Push_FruitSettlementRewardResponse.SUCCESS, winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), winingIndex.get(3), winingValue.get(0), winingValue.get(1));
					}
					bankerWiningValue += winingValue.get(1).intValue();
				}
			}
			else
			{
				response = new Push_FruitSettlementRewardResponse(Push_FruitSettlementRewardResponse.SUCCESS, winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), winingIndex.get(3), 0, 0);
			}
			// room.addBankerWinScore(bankerWiningValue);
			sbResponseUserId.append(vo.getPlayer().getUserId()+",");
			response.roundIndex = roundIndex;
			vo.getPlayer().sendResponse(response);
			vo.getPlayer().clearFruitRoomBet();
		}
		Tool.print_debug_level0("水果机有庄家推送结果,userId:"+sbResponseUserId.toString());
		// 庄家结算结果
		if (bankerWiningValue < 0)
		{
			if (player.getCoins() >= Math.abs(bankerWiningValue))
			{
				Tool.print_subCoins(player.getNickName(),bankerWiningValue,"水果机",player.getCoins());
				player.subCoinse(bankerWiningValue);
			}
			else
			{
				bankerWiningValue = player.getCoins();
				Tool.print_subCoins(player.getNickName(),bankerWiningValue,"水果机",player.getCoins());
				player.subCoinse(bankerWiningValue);
			}
			int bankerRoundNum = room.getBankerRoundNum();
			bankerRoundNum++;
			room.setBankerRoundNum(bankerRoundNum);

			Push_BankerWiningResponse response = new Push_BankerWiningResponse(Push_BankerWiningResponse.SUCCESS, (int) (bankerWiningValue), player.getSimpleData());
			player.sendResponse(response);

		}
		else
		{
			Tool.print_coins(player.getNickName(),bankerWiningValue, "水果机",player.getCoins());
			player.addCoins(bankerWiningValue);
			int bankerRoundNum = room.getBankerRoundNum();
			bankerRoundNum++;
			room.setBankerRoundNum(bankerRoundNum);

			Push_BankerWiningResponse response = new Push_BankerWiningResponse(Push_BankerWiningResponse.SUCCESS, (int) (bankerWiningValue - bankerWiningValue * 0.06f), player.getSimpleData());
			player.sendResponse(response);

		}

		// long roomPoolValue = room.getJackpot();
		//// if (bankerWiningValue > 0)
		//// {
		//// jiangPool += bankerWiningValue * 0.02f;
		//// roomPoolValue += jiangPool;
		//// room.setJackpot((int)roomPoolValue);
		//// }
		//// else
		//// {
		//// roomPoolValue += jiangPool;
		//// room.setJackpot((int)roomPoolValue);
		//// }
		// for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
		// {
		// if (!vo.getPlayer().isOnline())
		// {
		// continue;
		// }
		// JiangPoolResponse responsePool = new
		// JiangPoolResponse(JiangPoolResponse.SUCCESS, roomPoolValue);
		// vo.getPlayer().sendResponse(responsePool);
		// }
	}

	private ArrayList<KillRoomNoticVO> list_KillRoomNoticVOs = new ArrayList<>();

	private StringBuilder sbResponseUserId = new StringBuilder();
	
	/**
	 * 庄家为空时推送开奖信息
	 * 
	 * @param winingIndex
	 */
	private void pushBankerIsNullSelltement(ArrayList<Integer> winingIndex)
	{
		if (list_allPlayerInFruitRoom.size() != 0)
		{
//			int bankerWiningValue = 0;
//			float jiangPool = 0;
			sbResponseUserId.delete(0, sbResponseUserId.length());
			if (isDebug)
				Tool.print_debug_level0("当前房间内的玩家有  :" + list_allPlayerInFruitRoom);
			for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
			{
				ArrayList<Integer> winingValue = fruitPlayerWinOrFail(winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), vo.getMap(), winingIndex.get(3));
				// if (isDebug)
//				Tool.print_debug_level0("玩家结算结果 winingValue :" + winingValue);
				Push_FruitSettlementRewardResponse response = null;
				if (winingValue.size() != 0)
				{
//					bankerWiningValue += winingValue.get(1).intValue();
					if (winingValue.get(0) > 0)
					{
						// jiangPool +=
						// winingValue.get(0) * 0.02f;

						int winCoins = (int) (winingValue.get(0) - (winingValue.get(0) * 0.06f));
						checkAddNotic(vo, winCoins - vo.getPlayer().getFruitRoomBet());
						Tool.print_coins(vo.getPlayer().getNickName(),winCoins, "水果机",vo.getPlayer().getCoins());
						vo.getPlayer().addCoins(winCoins);
						response = new Push_FruitSettlementRewardResponse(Push_FruitSettlementRewardResponse.SUCCESS, winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), winingIndex.get(3), (int) (winingValue.get(0) - (winingValue.get(0) * 0.06f)), winingValue.get(1));
					}
					else
					{
						checkAddNotic(vo, winingValue.get(0) - vo.getPlayer().getFruitRoomBet());
						Tool.print_coins(vo.getPlayer().getNickName(),winingValue.get(0), "水果机",vo.getPlayer().getCoins());
						vo.getPlayer().addCoins(winingValue.get(0));
						response = new Push_FruitSettlementRewardResponse(Push_FruitSettlementRewardResponse.SUCCESS, winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), winingIndex.get(3), winingValue.get(0), winingValue.get(1));
					}
					// 结算 中奖结算

					// TODO 系统玩家 输赢

					if (isDebug)
						Tool.print_debug_level0("玩家金币为 :" + vo.getPlayer().getCoins());
				}
				else
				{
					response = new Push_FruitSettlementRewardResponse(Push_FruitSettlementRewardResponse.SUCCESS, winingIndex.get(0), winingIndex.get(1), winingIndex.get(2), winingIndex.get(3), 0, 0);
				}
				// if (isDebug)
				sbResponseUserId.append(vo.getPlayer().getUserId()+",");
				response.roundIndex = roundIndex;
				vo.getPlayer().sendResponse(response);
				vo.getPlayer().clearFruitRoomBet();
			}
			Tool.print_debug_level0("水果机没庄家推送结果,userId:"+sbResponseUserId.toString());
			// TODO 庄家为空时
			// bankerWiningValue=bankerWiningValue
			// -bankerWiningValue * 0.02f;
			// room.addBankerSocre(bankerWiningValue);
			// long roomPoolValue = room.getJackpot();
			// if (bankerWiningValue > 0)
			// {
			// //jiangPool += bankerWiningValue * 0.02f;
			// roomPoolValue += jiangPool;
			// room.setJackpot((int)roomPoolValue);
			// }
			// else
			// {
			// roomPoolValue += jiangPool;
			// room.setJackpot((int)roomPoolValue);
			// }
			// for (FruitMachineVO vo :
			// list_allPlayerInFruitRoom.values())
			// {
			// if (!vo.getPlayer().isOnline())
			// {
			// continue;
			// }
			// JiangPoolResponse response = new
			// JiangPoolResponse(JiangPoolResponse.SUCCESS,
			// roomPoolValue);
			// vo.getPlayer().sendResponse(response);
			// }
		}
	}

	private void checkAddNotic(FruitMachineVO vo, int winCoins)
	{
		Player player = vo.getPlayer();
		
		if(winCoins>0)
			rankService.addWinConisNum(player.getUserId(), winCoins,player.getNickName(),player.getLevel());
		
		if (winCoins > 1000000)
		{
			KillRoomNoticVO killRoomNoticVO = new KillRoomNoticVO(vo.getPlayer().getUserId(), vo.getPlayer().getNickName(), winCoins);
			list_KillRoomNoticVOs.add(killRoomNoticVO);
		}
	}

	private void broadcastFruitMachine()
	{
		if (list_KillRoomNoticVOs.size() > 0)
		{
			FruitPush_bigWin response = new FruitPush_bigWin();
			response.list_KillRoomNoticVO = list_KillRoomNoticVOs;
			playerService.sendToAll(response);
		}
	}

	/**
	 * 设置庄家信息
	 * 
	 * @param player
	 * @param bankerWiningValue
	 */
	private void setBankerInfo()
	{
		ArrayList<PlayerSimpleData> listBankers = room.getBankerList();

		if (listBankers != null && listBankers.size() >= 0)
		{
			room.sortBankerList();
		}

		Player player = room.getBankerPlayer();
		if (isDebug)
			Tool.print_debug_level0("设置庄家信息 当前庄家信息为 ：" + player);
		if (player == null)
		{

			PlayerSimpleData banker = room.changeBankerByList();
			if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家不在线本局之后切换庄家,切换的庄家为 ：" + banker);

			if (banker != null)
			{
				if (banker.coins < UpBankerCoin.Up)
				{
					room.changeBanker(null);
					banker = null;
				}
				else
				{
					room.changeBanker(playerService.getPlayer(banker.getUserId()));
				}
			}
			else
			{
				room.changeBanker(null);
			}
			if (list_allPlayerInFruitRoom != null && list_allPlayerInFruitRoom.size() != 0)
			{
				for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
				{
					if (isDebug)
						Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家不在线本局之后切换庄家,给所有房间内的玩家发送改变庄家的消息 ：" + vo + ",切换的庄家为 ：" + banker);
					push_changeBankerListInfo(vo.getPlayer(), room.getBankerList(), banker);
				}
			}
			return;
		}
		if (!player.isOnline())
		{
			PlayerSimpleData banker = room.changeBankerByList();
			if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家不在线本局之后切换庄家,切换的庄家为 ：" + banker);

			if (banker != null)
			{
				if (banker.coins < UpBankerCoin.Up)
				{
					room.changeBanker(null);
					banker = null;
				}
				else
				{
					room.changeBanker(playerService.getPlayer(banker.getUserId()));
				}
			}
			else
			{
				room.changeBanker(null);
			}
			if (list_allPlayerInFruitRoom != null && list_allPlayerInFruitRoom.size() != 0)
			{
				for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
				{
					if (isDebug)
						Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家不在线本局之后切换庄家,给所有房间内的玩家发送改变庄家的消息 ：" + vo + ",切换的庄家为 ：" + banker);
					push_changeBankerListInfo(vo.getPlayer(), room.getBankerList(), banker);
				}
			}
			return;
		}
		else
		{

			Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "当前庄家申请下庄,切换的庄家为 ：" + room.resignBanker());
			// 当前庄家申请下庄
			if (room.resignBanker() == true)
			{

				PlayerSimpleData banker = room.changeBankerByList();
				// if (isDebug)
				Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "当前庄家申请下庄,切换的庄家为 ：" + banker);

				if (banker != null)
				{
					if (banker.coins < UpBankerCoin.Up)
					{
						room.changeBanker(null);
						banker = null;
					}
					else
					{
						room.changeBanker(playerService.getPlayer(banker.getUserId()));
					}
				}
				else
				{
					room.changeBanker(null);
				}
				if (list_allPlayerInFruitRoom != null && list_allPlayerInFruitRoom.size() != 0)
				{
					for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
					{
						if (isDebug)
							Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "当前庄家申请下庄,给所有房间内的玩家发送改变庄家的消息 ：" + vo + ",切换的庄家为 ：" + banker);
						push_changeBankerListInfo(vo.getPlayer(), room.getBankerList(), banker);
					}
				}
				return;
			}

			// 庄家的钱是否小于2000万 小于->下庄
			if (room.getBankerPlayer().getCoins() < UpBankerCoin.Down)
			{
				PlayerSimpleData banker = room.changeBankerByList();
				if (isDebug)
					Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家金币不够下限2000万时,切换的庄家为 ：" + banker);

				if (banker != null)
				{
					if (banker.coins < UpBankerCoin.Up)
					{
						room.changeBanker(null);
						banker = null;
					}
					else
					{
						room.changeBanker(playerService.getPlayer(banker.getUserId()));
					}
				}
				else
				{
					room.changeBanker(null);
				}
				if (list_allPlayerInFruitRoom != null && list_allPlayerInFruitRoom.size() != 0)
				{
					for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
					{
						if (isDebug)
							Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家金币不够下限2000万时,给所有房间内的玩家发送改变庄家的消息 ：" + vo + ",切换的庄家为 ：" + banker);
						push_changeBankerListInfo(vo.getPlayer(), room.getBankerList(), banker);
					}
				}
				return;
			}

			// 坐庄数是否等于10次 等于->下庄
			if (room.getBankerRoundNum() >= room.bankerRoundNumMax * 2)
			{
				PlayerSimpleData banker = room.changeBankerByList();
				if (isDebug)
					Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家坐庄数等于10次,切换的庄家为 ：" + banker);

				if (banker != null)
				{
					if (banker.coins < UpBankerCoin.Up)
					{
						room.changeBanker(null);
						banker = null;
					}
					else
					{
						room.changeBanker(playerService.getPlayer(banker.getUserId()));
					}
				}
				else
				{
					room.changeBanker(null);
				}
				if (list_allPlayerInFruitRoom != null && list_allPlayerInFruitRoom.size() != 0)
				{
					for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
					{
						if (isDebug)
							Tool.print_debug_level0(MsgTypeEnum.Fruit_bankerListChange, "玩家坐庄数等于10次,给所有房间内的玩家发送改变庄家的消息 ：" + vo + ",切换的庄家为 ：" + banker);
						push_changeBankerListInfo(vo.getPlayer(), room.getBankerList(), banker);
					}
				}
				return;
			}
		}
	}

	private void runXiaZhuTimeState()
	{
		if (MyTimeUtil.getCurrTimeMM() - stateTime > KaiJiang_TIME)
		{
			setState(STATE_XiaZhu_TIME);

			// Tool.print_debug_level0("切换到下注状态！");
			setBankerInfo();
			Push_FruitMachineStartXiaZhuResponse response = new Push_FruitMachineStartXiaZhuResponse();
			
 
			if (list_allPlayerInFruitRoom.size() != 0)
			{
				sbResponseUserId.delete(0, sbResponseUserId.length());
				for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
				{
					sbResponseUserId.append(vo.getPlayer().getUserId()+",");
					vo.clearMapContent();
//					Tool.print_debug_level0("runXiaZhuTimeState.userId:"+vo.getPlayer().getUserId());
					vo.getPlayer().sendResponse(response);
				}
//				Tool.print_debug_level0("水果机发送下注消息给:"+sbResponseUserId.toString());
			}
			
			clearXiazhu();
		}
	}

	private void clearXiazhu()
	{
 
		list_allXiaZhu.clear();
	}

	private void addXiaZhuInfo(int key, int value)
	{
		Tool.print_debug_level0("位置["+key+"]下注["+value+"],当前总注数:"+list_allXiaZhu.get(key)+",list_allXiaZhu.contains(key):"+list_allXiaZhu.containsKey(key));
		if (list_allXiaZhu.containsKey(key))
		{
			Tool.print_debug_level0("list_allXiaZhu.get(key):"+list_allXiaZhu.get(key)+",value:"+value);
			list_allXiaZhu.put(key, list_allXiaZhu.get(key) + value);
		}
		else
		{
			list_allXiaZhu.put(key, value);
		}
	}

	@Override
	public Collection<Integer> getCurrentAllXiaZhuKey()
	{
		if (list_allXiaZhu.size() == 0)
		{
			return null;
		}
		return list_allXiaZhu.keySet();
	}

	@Override
	public Collection<Integer> getCurrentAllXiaZhuValue()
	{
		if (list_allXiaZhu.size() == 0)
		{
			return null;
		}
//		ArrayList<Integer> list_value = new ArrayList<>();
//		for (Integer value : list_allXiaZhu.values())
//		{
//			list_value.add(value);
//		}
		return list_allXiaZhu.values();
	}

	@Override
	public ArrayList<String> getCurrentHistory()
	{
		return list_history;
	}

	/**
	 * 下注
	 */
	@Override
	public boolean payXiaZhuValue(Player player, int type, int value)
	{
		if(value<=0)
		{
			return false;
		}
		
		if (!isXiaZhu())
		{
			return false;
		}
		
//		if(player.getManyPepolGame()!=null)
//		{
//			Tool.print_debug_level0("万人场中压水果机,玩家:"+player.getUserId());
//			return false;
//		}
		
		if (list_allPlayerInFruitRoom.size() == 0)
		{
			if (isDebug)
				Tool.print_debug_level0("当前房间玩家为空 ：" + list_allPlayerInFruitRoom);
			return false;
		}
		if (list_allPlayerInFruitRoom.containsKey(player.getUserId()))
		{
			FruitMachineVO fruitMachineVo = list_allPlayerInFruitRoom.get(player.getUserId());
			fruitMachineVo.putTypeAndValue(type, value);
			Tool.print_subCoins(fruitMachineVo.getPlayer().getNickName(),value,"水果机注",player.getCoins());
			fruitMachineVo.getPlayer().addCoins(-value);
			if (isDebug)
				Tool.print_debug_level0("当前房间下注玩家为  ：" + fruitMachineVo);
			if (isDebug)
				Tool.print_debug_level0("当前房间下注玩家为  金币剩余 ：" + fruitMachineVo.getPlayer().getCoins());
		}
		addXiaZhuInfo(type, value);
		for (FruitMachineVO vo : list_allPlayerInFruitRoom.values())
		{
			FruitPush_XiaZhu response = new FruitPush_XiaZhu(player.getUserId(), type, value);
			vo.getPlayer().sendResponse(response);
		}
		player.addFruitRoomBet(value);
		return true;
	}

	private boolean isXiaZhu()
	{
		return state == STATE_XiaZhu_TIME;
	}

	@Override
	public ArrayList<Integer> fruitMachineRequest()
	{

		ArrayList<Integer> list_typeAndIsNotSpecialReward = new ArrayList<>();
		int sum = GrossValue.grossvalue;
//		if (isDebug)
//			Tool.print_debug_level0("概率基准为 ：sum ：" + sum);

		// 第一轮随机 取出同类概率的中奖类型
		int randomNormalReward = MyUtil.getRandom(1, sum + 1);
		ArrayList<FruitMachineWiningInterval> list_fruits = new ArrayList<>();
		if (list_fruitMachineInterval.size() <= 0)
		{
			setFruitMachineInterval();
		}
		/************概率和奖励**************/
//		for (FruitMachineWiningInterval fInterval : list_fruitMachineInterval)
//		{
//			if (randomNormalReward >= fInterval.min && randomNormalReward <= fInterval.max)
//			{
//				list_fruits.add(fInterval);
//				if (isDebug)
//					Tool.print_debug_level0("当抽中概率区间为 ：randomNormalReward ：" + randomNormalReward);
//			}
//		}
		/************概率和奖励**************/
		/**特殊率	苹果率	铃铛芒果桔子率	双7双星西瓜率	BAR率	乘2倍小的率
		 * */
		int rate = MyUtil.getRandom(1000);
		FruitMachineWiningInterval interval = null;
		if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+",当前中奖gameFruit:"+gameFruit);
		int specialRate = (int) (gameFruit.getSpecial_chance().floatValue() * 10);
		int barRate = (int) (gameFruit.getBar_chance().floatValue() * 10 + specialRate);
		int double77Rate = (int) (gameFruit.getDouble_seven_chance().floatValue() * 10 + barRate);
		int lingdang = (int) (gameFruit.getLingdang_chance().floatValue() * 10 + double77Rate);
		int doubleSmall = (int) (gameFruit.getDouble_chance().floatValue() * 10 + lingdang);
		if(isLotteryDebug)Tool.print_debug_level0("水果机开奖,特殊率:"+specialRate+",barRate:"+barRate+",double77Rate:"+double77Rate+",lingdang:"+lingdang+",doubleSmall:"+doubleSmall);
		if(rate < specialRate)//特殊
		{
			if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+"-1");
			//大三元2，小三元4，0bar14，0bar13，大4喜1，开火车5，通赔6
			interval = handlerSpecial();
		}
		else if(rate < barRate)//通杀
		{
			if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+"-2");
			interval = new FruitMachineWiningInterval(17, false, 0, 0);
		}
		else if(rate < double77Rate)
		{
			if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+"-3");
			interval = handlerSeven();
		}
		else if(rate < lingdang)
		{
			if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+"-4");
			interval = handlerLingdang();
		}
		else if(rate < doubleSmall)
		{
			if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+"-5");
			interval = handlerDouble();
		}
		else 
		{
			if(isLotteryDebug)Tool.print_debug_level0("选中rate:"+rate+"-6");
			interval = new FruitMachineWiningInterval(1, false, 0, 0);
		}
		
		list_fruits.add(interval);
		if (list_fruits.size() == 0)
		{
			if(isLotteryDebug)Tool.print_dbError("list_fruits.size()为0:" + list_fruits.size());
		}
		if(isLotteryDebug)Tool.print_debug_level0("当前中奖interval:"+interval);
		// 第二轮随机 取出中奖的具体类型
		int randomSpecificReward = MyUtil.getRandom(0, list_fruits.size());
		if (isLotteryDebug)
			Tool.print_debug_level0("随机到同等概率的最终水果类型的索引 ：" + randomSpecificReward + ",缓存数量" + list_fruits.size());
		FruitMachineWiningInterval fInterval = list_fruits.get(randomSpecificReward);
		
//		//固定类型ce's
//		fInterval.fruitType=2;
//		fInterval.rewardSpecial=false;
		if (isLotteryDebug)
			Tool.print_debug_level0("随机到同等概率的最终水果类型为 ：" + fInterval);
		
		
		// 当奖励是特殊奖励时
		if (fInterval.rewardSpecial)
		{
			switch (fInterval.fruitType)
			{
				case SpecialReward.SinglePointSmple:
				case SpecialReward.SinglePointSpecial:
					list_typeAndIsNotSpecialReward = getSinglePoint(fInterval);
					break;
				case SpecialReward.OnTrain:
					// 开火车
					list_typeAndIsNotSpecialReward = getOnTrain(fInterval);
					break;
				default:
					list_typeAndIsNotSpecialReward.add(fInterval.fruitType);
					list_typeAndIsNotSpecialReward.add(fInterval.fruitType);
					list_typeAndIsNotSpecialReward.add(1);
					list_typeAndIsNotSpecialReward.add(0);
					if (isDebug)
						Tool.print_debug_level0("当抽中其他特殊奖励时 ：list_typeAndIsNotSpecialReward ：" + list_typeAndIsNotSpecialReward);
					break;
			}

			return list_typeAndIsNotSpecialReward;
		}
		else
		{
			// 当奖励是普通奖励
			ArrayList<DataFruitMachine> list_dataFruit = new ArrayList<>();
			for (DataFruitMachine fruit : config.getDataFruitMachine().values())
			{
				if (fruit.normalReward == fInterval.fruitType)
				{
					list_dataFruit.add(fruit);
					if (isDebug)
						Tool.print_debug_level0("当奖励时普通奖励时 随机出同类的索引：" + fruit);
				}
			}
			int indexNormalReward = MyUtil.getRandom(0, list_dataFruit.size());
			if (isLotteryDebug)
				Tool.print_debug_level0("当奖励时普通奖励时 随机到的水果的最终索引：" + indexNormalReward + ",list_dataFruit.size()" + list_dataFruit.size());
			DataFruitMachine dataFruit = list_dataFruit.get(indexNormalReward);
			if (isLotteryDebug)
				Tool.print_debug_level0("当奖励时普通奖励时 随机到的最终水果  ：" + dataFruit.Name);
			list_typeAndIsNotSpecialReward.add(dataFruit.Id);
			list_typeAndIsNotSpecialReward.add(fInterval.fruitType);
			list_typeAndIsNotSpecialReward.add(0);
			list_typeAndIsNotSpecialReward.add(0);
			if (isLotteryDebug)
				Tool.print_debug_level0("当抽中普通奖励时 ：list_typeAndIsNotSpecialReward ：" + list_typeAndIsNotSpecialReward);
			return list_typeAndIsNotSpecialReward;
		}
	}

	private FruitMachineWiningInterval handlerDouble()
	{
		int selectIndex = MyUtil.getRandom(7);
		FruitMachineWiningInterval interval = null;
		switch (selectIndex)	//1，7，3，15，9，11，5
		{
			case 0:
				interval = new FruitMachineWiningInterval(2, false, 0, 0);
				break;

			case 1:
				interval = new FruitMachineWiningInterval(6, false, 0, 0);
				break;
				
			case 2:
				interval = new FruitMachineWiningInterval(3, false, 0, 0);
				break;
				
			case 3:
				interval = new FruitMachineWiningInterval(8, false, 0, 0);
				break;
				
			case 4:
				interval = new FruitMachineWiningInterval(16, false, 0, 0);
				break;
				
			case 5:
				interval = new FruitMachineWiningInterval(10, false, 0, 0);
				break;
				
			case 6:
				interval = new FruitMachineWiningInterval(11, false, 0, 0);
				break;
				
			default:
				break;
		}
		return interval;
	}

	/**
	 * 10倍
	 * @return
	 */
	private FruitMachineWiningInterval handlerLingdang()
	{
		int index = MyUtil.getRandom(3);
		FruitMachineWiningInterval interval = null;
		switch (index)
		{
			case 0:// 
				interval = new FruitMachineWiningInterval(15, false, 0, 0);
				break;

			case 1:// 
				interval = new FruitMachineWiningInterval(4, false, 0, 0);
				break;
				
			case 2:// 
				interval = new FruitMachineWiningInterval(7, false, 0, 0);
				break;
				
				
			default:
				break;
		}
		return interval;
	}

	/**
	 * 双7：12，双星：10，大西瓜：6
	 * @return
	 */
	private FruitMachineWiningInterval handlerSeven()
	{
		int index = MyUtil.getRandom(3);
		FruitMachineWiningInterval interval = null;
		switch (index)
		{
			case 0:
				interval = new FruitMachineWiningInterval(12, false, 0, 0);
				break;

			case 1:
				interval = new FruitMachineWiningInterval(9, false, 0, 0);
				break;
				
			case 2:
				interval = new FruitMachineWiningInterval(5, false, 0, 0);
				break;
				
			default:
				break;
		}
		return interval;
	}

	/**
	 * //大三元2，小三元4，0bar14，0bar13，大4喜1，开火车5，通赔6
	 * @return
	 */
	private FruitMachineWiningInterval handlerBar()//50,100bar
	{
		boolean isBar100 = MyUtil.getRandomBoolean();
		if(isBar100)
		{
			FruitMachineWiningInterval interval = new FruitMachineWiningInterval(13, false, 0, 0);
			return interval;
		}
		FruitMachineWiningInterval interval = new FruitMachineWiningInterval(14, false, 0, 0);
		return interval;
	}

	/**
	 * //大三元2，小三元4，0bar14，0bar13，大4喜1，开火车5，通赔6
	 * @param rate
	 * @return
	 */
	private FruitMachineWiningInterval handlerSpecial()
	{
		//1.开特殊6个接100%比例如下        1.小三元40%   2.大四喜30%       3.大三元20%   4.开火车4% =940           5.BAR 2.5%  1.5        6.通赔2%
		
		int rate = MyUtil.getRandom(1000);
		int type = 0;
		boolean isSpecial = true;
		if(rate < 400)
		{
			type = 4;
		}
		else if(rate < 700)
		{
			type = 1;
		}
		else if(rate < 900)
		{
			type = 2;
		}
		else if(rate < 940)
		{
			type = 5;
		}
		else if(rate < 965)
		{
			type = 13;
			isSpecial = false;
		}
		else if(rate < 980)
		{
			type = 14;
			isSpecial = false;
		}
		else
		{
			type = 6;
		}
	
		FruitMachineWiningInterval interval = new FruitMachineWiningInterval(type, isSpecial, 0, 0);
		return interval;
	}

	/**
	 * 开火车时 爆灯
	 * 
	 * @param fInterval
	 * @return
	 */
	private ArrayList<Integer> getOnTrain(FruitMachineWiningInterval fInterval)
	{
		ArrayList<Integer> list_typeAndIsNotSpecialReward = new ArrayList<>();
		ArrayList<DataFruitMachine> List_fruit = new ArrayList<>();
		for (DataFruitMachine fruit : config.getDataFruitMachine().values())
		{
			if (fruit.Id != 10 || fruit.Id != 22)
			{
				List_fruit.add(fruit);
			}
		}

		int random = MyUtil.getRandom(0, List_fruit.size());
		int ranNum = MyUtil.getRandom(2, 6);
		list_typeAndIsNotSpecialReward.add(List_fruit.get(random).Id);
		list_typeAndIsNotSpecialReward.add(fInterval.fruitType);
		list_typeAndIsNotSpecialReward.add(1);
		list_typeAndIsNotSpecialReward.add(ranNum);
		if (isDebug)
			Tool.print_debug_level0("当抽中开火车时时 ：list_typeAndIsNotSpecialReward ：" + list_typeAndIsNotSpecialReward);
		return list_typeAndIsNotSpecialReward;
	}

	/**
	 * 单点时爆灯
	 * 
	 * @param fInterval
	 * @return
	 */
	private ArrayList<Integer> getSinglePoint(FruitMachineWiningInterval fInterval)
	{
		final ArrayList<DataFruitMachine> list_specialReward = new ArrayList<>();
		ArrayList<Integer> list_typeAndIsNotSpecialReward = new ArrayList<>();
		if (fInterval.fruitType == SpecialReward.SinglePointSmple || fInterval.fruitType == SpecialReward.SinglePointSpecial)
		{
			for (DataFruitMachine fruitData : config.getDataFruitMachine().values())
			{
				int normalReward = fruitData.normalReward;
				if (normalReward == NormalReward.BigBar || normalReward == NormalReward.smallBar)
				{
					list_specialReward.add(fruitData);
					if (isDebug)
						Tool.print_debug_level0("单点随机到特殊奖励时 ：" + fruitData);
				}
				else
				{
					continue;
				}
			}
		}
		if (list_specialReward.size() != 0)
		{
			int indexDataSpecial = MyUtil.getRandom(0, list_specialReward.size());
			DataFruitMachine dataFruit = list_specialReward.get(indexDataSpecial);
			list_typeAndIsNotSpecialReward.add(dataFruit.normalReward);
			list_typeAndIsNotSpecialReward.add(fInterval.fruitType);
			list_typeAndIsNotSpecialReward.add(1);
			list_typeAndIsNotSpecialReward.add(0);
			if (isDebug)
				Tool.print_debug_level0("单点随机到特殊奖励  最终 的结果 ：" + dataFruit);
		}

		if (isDebug)
			Tool.print_debug_level0("当抽中单点时 ：list_typeAndIsNotSpecialReward ：" + list_typeAndIsNotSpecialReward);
		return list_typeAndIsNotSpecialReward;
	}

	/****
	 * 返回普通奖励 和单点奖励时 当前局玩家和庄家的结算结果 ArrayList() 第一个元素 代表 玩家的结算结果
	 * 第二个元素代表庄家的结算结果
	 * 
	 * @param dataFruitMachine
	 * @param typeAndValue
	 * @return
	 */
	private ArrayList<Integer> getNormalOrSinglePointReward(DataFruitMachine dataFruitMachine, HashMap<Integer, Integer> map_typeAndValue)
	{
		ArrayList<Integer> playerAndZhuangJiaWiningValue = new ArrayList<>();
		int sumMoney = 0;
		for (Integer value : map_typeAndValue.values())
		{
			sumMoney += value;
		}
		// if (isDebug)
		Tool.print_debug_level0("单点或者普通奖励 下注的总金额：" + sumMoney);

		if (dataFruitMachine.normalReward == NormalReward.LuckNone)
		{
			playerAndZhuangJiaWiningValue.add(0);
			playerAndZhuangJiaWiningValue.add(sumMoney);
			return playerAndZhuangJiaWiningValue;
		}

		int rewardMoney = 0;
		if (map_typeAndValue.containsKey(dataFruitMachine.fruitType))
		{
			rewardMoney = dataFruitMachine.rewardMultiple * map_typeAndValue.get(dataFruitMachine.fruitType);
			Tool.print_debug_level0("玩家总赢数 ：" + rewardMoney);
		}
		int currentPlayerWiningValue = 0;
		int currentZhuangJiaWiningValue = 0;
		if (rewardMoney == 0)
		{
			currentPlayerWiningValue = 0;
			currentZhuangJiaWiningValue = sumMoney;
		}
		else
		{
			currentPlayerWiningValue = rewardMoney;
			currentZhuangJiaWiningValue = sumMoney - rewardMoney;
		}
		// if (isDebug)
		Tool.print_debug_level0("单点或者普通奖励 赢的总金额：" + rewardMoney);
		// if (isDebug)
		Tool.print_debug_level0("单点或者普通奖励 结算： 玩家赢得：" + currentPlayerWiningValue + "，庄家赢得：" + currentZhuangJiaWiningValue);
		playerAndZhuangJiaWiningValue.add(currentPlayerWiningValue);
		playerAndZhuangJiaWiningValue.add(currentZhuangJiaWiningValue);
		return playerAndZhuangJiaWiningValue;

	}

	@Override
	public ArrayList<Integer> fruitPlayerWinOrFail(int fruitType, int fruitRewardType, int specialRewardType, HashMap<Integer, Integer> map_typeAndValue, int fruitNum)
	{
		if (isDebug)
			Tool.print_debug_level0("傳遞給結算方法的數據為 ：fruitType " + fruitType + ",fruitRewardType" + fruitRewardType + ",specialRewardType" + specialRewardType + ",map_typeAndValue" + map_typeAndValue);
		ArrayList<Integer> playerAndZhuangJiaWiningValue = new ArrayList<>();
		if (map_typeAndValue.isEmpty())
		{
			if (isDebug)
				Tool.print_debug_level0("！！！！！！！！！！！！！！！！！！！玩家未下注！！！！！！！！！！！！！！！！！！！！！！！！");
			return playerAndZhuangJiaWiningValue;
		}
		if (specialRewardType == 1)// 特殊奖励
		{
			if (isDebug)
				Tool.print_debug_level0("！！！！！！！！！！！！！！！！！！！特殊獎勵結算！！！！！！！！！！！！！！！！！！！！！！！！");
			switch (fruitRewardType)
			{

				case SpecialReward.SinglePointSmple:
				case SpecialReward.SinglePointSpecial:
					if (isDebug)
						Tool.print_debug_level0("结算  单点 ");
					// 特殊奖励为单点时
					DataFruitMachine dataFruitMachine = null;
					for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
					{
						if (dataFruit.normalReward == fruitType)
						{
							dataFruitMachine = dataFruit;
							if (isDebug)
								Tool.print_debug_level0("结算  单点  选中的位置为 ：" + dataFruitMachine);
							break;
						}
					}
					if (dataFruitMachine != null)
					{
						playerAndZhuangJiaWiningValue = getNormalOrSinglePointReward(dataFruitMachine, map_typeAndValue);
						// if (isDebug)
						Tool.print_debug_level0("单点   playerAndZhuangJiaWiningValue :" + playerAndZhuangJiaWiningValue);
					}
					break;
				case SpecialReward.OnTrain:
					//if (isDebug)
						Tool.print_debug_level0("结算  开火车 ");
					ArrayList<DataFruitMachine> dataOnTrain = getSpecialRewardDataList(fruitType, fruitNum);
					if (isDebug)
						Tool.print_debug_level0("开火车  所中的元素有  :" + dataOnTrain);
					if (dataOnTrain.size() != 0)
					{
						playerAndZhuangJiaWiningValue = getSpecialRewardWiningValue(dataOnTrain, map_typeAndValue);
						// if (isDebug)
						Tool.print_debug_level0("开火车  playerAndZhuangJiaWiningValue :" + playerAndZhuangJiaWiningValue);
					}
					break;
				case SpecialReward.BigFourXi:
					ArrayList<DataFruitMachine> dataBigFourXi = getSpecialRewardDataList(fruitType, 0);
					if (dataBigFourXi.size() != 0)
					{
						playerAndZhuangJiaWiningValue = getSpecialRewardWiningValue(dataBigFourXi, map_typeAndValue);
						// if (isDebug)
						Tool.print_debug_level0("大四喜  playerAndZhuangJiaWiningValue :" + playerAndZhuangJiaWiningValue);

					}
					break;
				case SpecialReward.BigThreeYuan:
					ArrayList<DataFruitMachine> dataBigThreeYuan = getSpecialRewardDataList(fruitType, 0);
					if (dataBigThreeYuan.size() != 0)
					{
						playerAndZhuangJiaWiningValue = getSpecialRewardWiningValue(dataBigThreeYuan, map_typeAndValue);
						// if (isDebug)
						Tool.print_debug_level0("大三元  playerAndZhuangJiaWiningValue :" + playerAndZhuangJiaWiningValue);

					}
					break;
				case SpecialReward.SmallThreeYuan:
					ArrayList<DataFruitMachine> dataSmallThreeYuan = getSpecialRewardDataList(fruitType, 0);
					if (dataSmallThreeYuan.size() != 0)
					{
						playerAndZhuangJiaWiningValue = getSpecialRewardWiningValue(dataSmallThreeYuan, map_typeAndValue);
						// if (isDebug)
						Tool.print_debug_level0("小三元  playerAndZhuangJiaWiningValue :" + playerAndZhuangJiaWiningValue);
						for (DataFruitMachine dataFruitMachine2 : dataSmallThreeYuan)
						{
							Tool.print_debug_level0("小三元  dataFruitMachine2 :" + dataFruitMachine2.Id);
						}
					}
					break;
				case SpecialReward.TheNineTreasureLamp:
					ArrayList<DataFruitMachine> dataTheNineTreasureLamp = getSpecialRewardDataList(fruitType, 0);
					if (dataTheNineTreasureLamp.size() != 0)
					{
						playerAndZhuangJiaWiningValue = getSpecialRewardWiningValue(dataTheNineTreasureLamp, map_typeAndValue);

						// if (isDebug)
						Tool.print_debug_level0("九宝莲灯  playerAndZhuangJiaWiningValue :" + playerAndZhuangJiaWiningValue);
					}
					break;
			}
		}
		else
		{
			if (isDebug)
				Tool.print_debug_level0("！！！！！！！！！！！！！！！！！！！普通奖励结算！！！！！！！！！！！！！！！！！！！！！！！！");
			// 普通奖励
			DataFruitMachine fruit = null;
			for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
			{
				if (dataFruit.normalReward == fruitRewardType)
				{
					fruit = dataFruit;
					if (isDebug)
						Tool.print_debug_level0("普通奖励结算：" + fruit);
					break;
				}
			}
			if (fruit != null)
			{
				playerAndZhuangJiaWiningValue = getNormalOrSinglePointReward(fruit, map_typeAndValue);
			}
		}
		return playerAndZhuangJiaWiningValue;
	}

	/**
	 * 获得除单点外的特殊奖励爆出的所有元素
	 * 
	 * @param fruitType
	 * @return
	 */
	private ArrayList<DataFruitMachine> getSpecialRewardDataList(int fruitType, int fruitNum)
	{
		// ArrayList<DataFruitMachine> list_dataFruits =
		// (ArrayList<DataFruitMachine>)
		// config.getDataFruitMachine()
		// .values();
		ArrayList<DataFruitMachine> list_datafruits = new ArrayList<>();
		if(fruitNum>=2)
		{
			Tool.print_debug_level0("结算  开火车fruitType: "+fruitType+",fruitNum:"+fruitNum);
			if (fruitType + fruitNum > 24)
			{
				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{
					if (dataFruit.Id >= fruitType && dataFruit.Id <= 24)
					{
						list_datafruits.add(dataFruit);
					}
				}
				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{
					if (dataFruit.Id >= 1 && dataFruit.Id <= (fruitType + fruitNum - 24))
					{
						list_datafruits.add(dataFruit);
					}
				}
			}
			else
			{
				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{
					if (dataFruit.Id >= 1 && dataFruit.Id <= (fruitType + fruitNum))
					{
						list_datafruits.add(dataFruit);
					}
				}
			}
			if (isDebug)
				Tool.print_debug_level0("结算  开火车 :" + list_datafruits);
			return list_datafruits;
		}
		switch (fruitType)
		{
			case SpecialReward.OnTrain:
				//if (isDebug)				
				break;
			case SpecialReward.BigFourXi:
				if (isDebug)
					Tool.print_debug_level0("结算  大四喜 :");
				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{
					if (dataFruit.bigFourXi == SpecialReward.BigFourXi)
					{
						list_datafruits.add(dataFruit);
					}
				}
				if (isDebug)
					Tool.print_debug_level0("结算 大四喜  :" + list_datafruits);
				break;
			case SpecialReward.BigThreeYuan:
				if (isDebug)
					Tool.print_debug_level0("结算 大三元  :");

				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{
					if (dataFruit.bigThreeYuan == SpecialReward.BigThreeYuan)
					{
						list_datafruits.add(dataFruit);
					}
				}
				if (isDebug)
					Tool.print_debug_level0("结算 大三元  :" + list_datafruits);
				break;
			case SpecialReward.SmallThreeYuan:
				if (isDebug)
					Tool.print_debug_level0("结算 小三元  :");
				int ran = MyUtil.getRandom(1, 3);
				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{

					if (ran == 1)
					{
						if (dataFruit.Id < 13)
						{
							if (dataFruit.smallThreeYuan == SpecialReward.SmallThreeYuan)
							{
								list_datafruits.add(dataFruit);
							}
						}
					}
					else
					{
						if (dataFruit.Id >= 13)
						{
							if (dataFruit.smallThreeYuan ==  SpecialReward.SmallThreeYuan)
							{
								list_datafruits.add(dataFruit);
							}
						}
					}
				}
				if (isDebug)
					Tool.print_debug_level0("结算 小三元  :" + list_datafruits);
				break;
			case SpecialReward.TheNineTreasureLamp:
				if (isDebug)
					Tool.print_debug_level0("结算 九宝莲灯 :");
				for (DataFruitMachine dataFruit : config.getDataFruitMachine().values())
				{
					if (dataFruit.nineTreasureLamp == SpecialReward.TheNineTreasureLamp)
					{
						list_datafruits.add(dataFruit);
					}
				}
				if (isDebug)
					Tool.print_debug_level0("结算 九宝莲灯  :" + list_datafruits);
				break;
		}

		if (isDebug)
			Tool.print_debug_level0("特殊奖励类型 所中的元素 :" + list_datafruits);

		return list_datafruits;
	}

	/**
	 * 结算 除单点外的其他特殊奖励的战绩 第一个元素代表玩家战绩 第二个元素代表庄家战绩
	 * 
	 * @param list_dataFruit
	 * @param map_typeAndValue
	 * @return
	 */
	private ArrayList<Integer> getSpecialRewardWiningValue(ArrayList<DataFruitMachine> list_dataFruit, HashMap<Integer, Integer> map_typeAndValue)
	{
		ArrayList<Integer> playerAndZhuangJiaWiningValue = new ArrayList<>();
		int playerWining = 0;
		boolean isTongPei = false;
		for (DataFruitMachine fruit : list_dataFruit)
		{
			if (fruit.Id == 10)
			{
				isTongPei = true;
				break;
			}
		}
		if (isTongPei)
		{
			Tool.print_debug_level0("开火车中通赔！");
			list_dataFruit.clear();
			for (DataFruitMachine fruit : config.getDataFruitMachine().values())
			{
				if (fruit.nineTreasureLamp == SpecialReward.TheNineTreasureLamp)
				{
					list_dataFruit.add(fruit);
				}
			}
		}
		for (DataFruitMachine fruit : list_dataFruit)
		{
			if (map_typeAndValue.containsKey(fruit.fruitType))
			{
				playerWining += map_typeAndValue.get(fruit.fruitType) * fruit.rewardMultiple;
				if (isDebug)
					Tool.print_debug_level0("特殊獎勵 下注類型 ：" + fruit.fruitType);
			}
		}
		// if (isDebug)
		Tool.print_debug_level0("開獎結算總和 ：" + playerWining);
		int playerSumOnNote = 0; // 玩家总上注
		for (Integer value : map_typeAndValue.values())
		{
			playerSumOnNote += value;
		}
		// if (isDebug)
		Tool.print_debug_level0("玩家下注總和 ：" + playerSumOnNote);
		int currentPlayerWiningValue = 0;
		int currentZhuangJiaWiningValue = 0;
		if (playerWining == 0)
		{
			currentPlayerWiningValue = 0;
			currentZhuangJiaWiningValue = playerSumOnNote;
		}
		else
		{
			currentPlayerWiningValue = playerWining;
			currentZhuangJiaWiningValue = playerSumOnNote - playerWining;
		}

		if (isDebug)
			Tool.print_debug_level0("特殊獎勵  开火车->玩家贏得 ：" + currentPlayerWiningValue);
		if (isDebug)
			Tool.print_debug_level0("特殊獎勵  开火车->莊家贏得 ：" + currentZhuangJiaWiningValue);
		playerAndZhuangJiaWiningValue.add(currentPlayerWiningValue);
		playerAndZhuangJiaWiningValue.add(currentZhuangJiaWiningValue);
		return playerAndZhuangJiaWiningValue;
	}

	/**
	 * 初始化 当前中奖概率
	 */
	public void setFruitMachineInterval()
	{
		ArrayList<ConfigFruitmechinewining> list_configFruitmechinewiningModel = YTDaoImpl.getRewardOdds();
		if (list_configFruitmechinewiningModel == null)
		{
			if (isDebug)
				Tool.print_debug_level0("初始化 概率类 list_configFruitmechinewiningModel :" + list_configFruitmechinewiningModel);
			return;
		}
		for (ConfigFruitmechinewining configFruitmechinewining : list_configFruitmechinewiningModel)
		{
			int fruitType = configFruitmechinewining.getFruitType();
			int rewardType = configFruitmechinewining.getRewardType();
			int min = configFruitmechinewining.getMin();
			int max = configFruitmechinewining.getMax();
			FruitMachineWiningInterval fruitInterval = null;
			if (rewardType == 0)
			{
				fruitInterval = new FruitMachineWiningInterval(fruitType, false, min, max);
			}
			else
			{
				fruitInterval = new FruitMachineWiningInterval(fruitType, true, min, max);
			}
			if (isDebug)
				Tool.print_debug_level0("初始化 概率类 fruitInterval :" + fruitInterval);
			list_fruitMachineInterval.add(fruitInterval);
		}
	}

	@Override
	public void online(Player player, Channel channel)
	{

	}

	@Override
	public void offlineOnGame(Player player)
	{
		
	}

	@Override
	public void offlineOnServer(Player player)
	{
		if(player != null)
		{
			Tool.print_debug_level0("玩家离线,从水果机移除:"+player.getUserId());
			list_allPlayerInFruitRoom.remove(player.getUserId());
			room.removePlayer(player.getUserId());
		}
	}

	@Override
	public void enterRoom(Player player)
	{
//		player.setInsideRoom(room);
//		if (!list_allPlayerInFruitRoom.containsKey(player.getUserId()))
//		{
//			FruitMachineVO vo = new FruitMachineVO();
//			vo.setPlayer(player);
//			list_allPlayerInFruitRoom.put(vo.getPlayer().getUserId(), vo);
//		}
//		list_allPlayerInFruitRoom.get(player.getUserId()).setIsContinueXiaZhu(false);
//		room.addPlayer(player);
//		Tool.print_debug_level0("当前房间状态:"+state+",玩家 :" + list_allPlayerInFruitRoom);
		
		player.setInsideRoom(room);
		FruitMachineVO vo = list_allPlayerInFruitRoom.get(player.getUserId());
		if(vo == null)
		{
			vo = new FruitMachineVO();
			vo.setPlayer(player);
			list_allPlayerInFruitRoom.put(vo.getPlayer().getUserId(), vo);
		}
		else
		{
			vo.setPlayer(player);
		}
		
		vo.setIsContinueXiaZhu(false);
		room.addPlayer(player);
		Tool.print_debug_level0("当前房间状态:"+state+",玩家 :" + list_allPlayerInFruitRoom);
	}
//当前房间内玩家 list_allPlayerInFruitRoom :{32386=FruitMachineVO [player=32386, isContinueXiaZhu=false, map_typeAnfValue={}], 32393=FruitMachineVO [player=32393, isContinueXiaZhu=false, map_typeAnfValue={}], 32395=FruitMachineVO [player=32395, isContinueXiaZhu=false, map_typeAnfValue={3=1000000, 4=1000000, 5=1000000, 6=1000000, 7=1000000}]}

	@Override
	public void leaveRoom(Player player)
	{
		if (list_allPlayerInFruitRoom.containsKey(player.getUserId()))
		{
			list_allPlayerInFruitRoom.remove(player.getUserId());
		}
		room.removePlayer(player.getUserId());
	}

	@Override
	public boolean requestBanker(Player player)
	{
		return room.requestBanker(player);
	}

	@Override
	public Room getRoom()
	{
		return room;
	}

	@Override
	public int getRoomState()
	{

		return this.state;
	}

	@Override
	public long getStateTime()
	{
		return this.stateTime;
	}

	/**
	 * 改变庄家时 给所有在房间内的玩家发送更新庄家列表的消息
	 * 
	 * @param player
	 * @param list_bankers
	 */
	@Override
	public void push_changeBankerListInfo(Player player, ArrayList<PlayerSimpleData> bankers, PlayerSimpleData nextBanker)
	{
		if (player.isOnline())
		{
			Push_ChangeBankerListInfoResponse response = new Push_ChangeBankerListInfoResponse(Push_ChangeBankerListInfoResponse.SUCCESS, bankers, nextBanker);
			player.sendResponse(response);
		}
	}

	@Override
	public void setPlayerContinueXiaZhu(Player player, boolean isContinueXiaZhu)
	{
		if (list_allPlayerInFruitRoom == null || list_allPlayerInFruitRoom.size() == 0)
		{
			return;
		}

		if (!list_allPlayerInFruitRoom.containsKey(player.getUserId()))
		{
			return;
		}

		list_allPlayerInFruitRoom.get(player.getUserId()).setIsContinueXiaZhu(isContinueXiaZhu);
	}

	@Override
	public boolean reconnect(Player player)
	{
		// Tool.print_debug_level0("玩家断线重连水果机数据,player:" +
		// player.getSimpleData());
		if (list_allPlayerInFruitRoom.containsKey(player.getUserId()))
		// TODO 玩家断线重连水果机数据
		{// 如果玩家还在水果机场，同步水果机数据给玩家,客户端需要判断当前是水果机电视机还是水果机场

			FruitPush_reconnect reponse = new FruitPush_reconnect();
			Tool.print_debug_level0("roomState:" + getRoomState());
			Tool.print_debug_level0("getStateTime:" + getStateTime());
			Tool.print_debug_level0("getJackpot:" + room.getJackpot());
			Collection<Integer> list_xiaZhuKey = getCurrentAllXiaZhuKey();
			Collection<Integer> list_xiaZhuValue = getCurrentAllXiaZhuValue();
			if (room.getBankerPlayer() == null)
			{
				reponse.setData(getRoomState(), getStateTime(), null, room.getJackpot(), list_xiaZhuKey, list_xiaZhuValue, list_history,roundIndex);
				player.sendResponse(reponse);
			}
			else
			{
				Tool.print_debug_level0("getSimpleData:" + room.getBankerPlayer().getSimpleData());
				reponse.setData(getRoomState(), getStateTime(), room.getBankerPlayer().getSimpleData(), room.getJackpot(), list_xiaZhuKey, list_xiaZhuValue, list_history,roundIndex);
				player.sendResponse(reponse);
			}
			return true;
		}
		return false;
	}

	@Override
	public void refreshConfig()
	{
		gameFruit = FruitMachineDaoImpl.getRewardOdds();
		Tool.print_debug_level0("刷新配置："+gameFruit);
	}

	@Override
	public int getRoundIndex()
	{
		return roundIndex;
	}
}
