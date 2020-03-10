package com.wt.xcpk.zjh.killroom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.naval.moneytree.MoneyTreeService;
import com.gjc.naval.vip.VipService;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.event.server.GameServerStartup;
import com.wt.event.server.ServerEvent;
import com.wt.naval.dao.impl.KillRoomDaoImpl;
import com.wt.naval.dao.model.Config_killroomPoker;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.quartz.SimpleTriggerQuartz;
import com.wt.util.sort.MySortUtil;
import com.wt.util.timetask.SimpleTaskUtil;
import com.wt.xcpk.KillRoomDirectionPlayer;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;
import com.wt.xcpk.killroom.EnterKillRoomResponse;
import com.wt.xcpk.killroom.KillRoomLog;
import com.wt.xcpk.killroom.KillRoomSitDownResponse;
import com.wt.xcpk.killroom.KillRoomTablePlayerRoundScore;
import com.wt.xcpk.killroom.push.Push_killRoomBankerCoins;
import com.wt.xcpk.killroom.push.Push_killRoomBigWin;
import com.wt.xcpk.killroom.push.Push_killRoomChangeBanker;
import com.wt.xcpk.killroom.push.Push_killRoomChangeToBetResponse;
import com.wt.xcpk.killroom.push.Push_killRoomChangeToIdleResponse;
import com.wt.xcpk.killroom.push.Push_killRoomChangeToLotteryResponse;
import com.wt.xcpk.killroom.push.Push_killRoomPayBetResponse;
import com.wt.xcpk.killroom.push.Push_killRoomResignBanker;
import com.wt.xcpk.killroom.push.Push_killRoomSitDown;
import com.wt.xcpk.killroom.push.Push_killRoomStandUp;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.JackpotData;
import com.wt.xcpk.vo.JackpotRoundSort;
import com.wt.xcpk.vo.KillRoomBigWinVO;
import com.wt.xcpk.vo.KillRoomNoticVO;
import com.wt.xcpk.vo.RankVO;
import com.wt.xcpk.vo.poker.CardTypeEnum;
import com.wt.xcpk.vo.poker.FaceTypeEnum;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.vo.poker.PokerVO;
import com.wt.xcpk.zjh.logic.PokerLogicService;
import com.yt.xcpk.killroomSendRedEnvelope.KillRoomSendRedEnvelopeService;
import com.yt.xcpk.task.TaskService;
import io.netty.channel.Channel;
import model.ConfigKillroom;
import model.GameTongsha;

/**
闲家视角

+150  -100   
 东    南     西      北
+50   +100   -50     -50   1.赢，50 如果赢，暂时记录，等全部玩家输赢统计结束后，结算庄家

+50   -100	 -50     +50   2.输，-50 如果输，算出自身总可输金币作为庄家可赢金币，等全部玩家输赢统计结束后，结算庄家

计算庄家总输赢（每个玩家的输赢金币数总和）

庄家赢：
庄家直接加上可赢金币
 
庄家输：
庄家是否够赔，在不够赔的情况下按比例赔（玩家已经知道自己赢多少）

1.统计闲家在每个方位输赢，汇总该玩家输赢情况，
    	一.如果赢，暂时记录，等全部玩家输赢统计结束后，结算庄家
    	二.如果输，算出自身总可输金币作为庄家可赢金币，等全部玩家输赢统计结束后，结算庄家
    	
2.玩家总输赢即为庄家输赢情况
	一.庄家赢：庄家直接加上可赢金币(此时可赢金币已计算过玩家可输金币)
	二.庄家输：庄家是否够赔，在不够赔的情况下按比例赔（庄家已知赢多少，算出倍数比例）
 
 * @author WangTuo
 */
@Service
public class KillRoomImpl implements KillRoomService,Runnable,PlayerListener,GameServerStartup
{
	private static final boolean isDebug = false;
	private static final boolean lotteryDebug = true;
	private static final boolean isSendMsgDebug = false;
	private static final boolean isJackpotDebug = true;

	public static final int JACKPOT_3A = 3;
	public static final int JACKPOT_OTHER_LEOPARD = 2;
	public static final int JACKPOT_FLUSH_COLOR = 1;
	
	public static final float PLAYER_WIN_RATE = 0.94f;
	public static final float SYSTEM_WIN_RATE = 0.04f;
	public static final float JACKPOT_WIN_RATE = 0.02f;
	
	/**下注时间*/
	public static final long BET_TIME = 18 * MyTimeUtil.TIME_S;
	
	/**开奖时间*/
	public static final long LOTTERY_TIME = 15 * MyTimeUtil.TIME_S;
	
	/**休息时间*/
	public static final long IDLE_TIME = 5 * MyTimeUtil.TIME_S;
	
	/**单玩家最大下注*/
	public static final int BET_MAX = 20000000;
	
	/**下注状态*/
	public static final int STATE_BET_TIME = 0;
	/**开奖状态*/
	public static final int STATE_LOTTERY = 1;
	/**休息状态*/
	public static final int STATE_IDLE = 2;
	
	
	@Autowired
	private PokerLogicService pokerLogicService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private RankService rankService;
	
	@Autowired
	private TaskService taskService;
	
//	@Autowired
//	private VipService vipService;
	
//	@Autowired
//	private MoneyTreeService moneyTreeService;
	
	@Autowired
	private KillRoomSendRedEnvelopeService killRoomSendRedEnvelope;
	
	private int state;
	private long stateTime;
	
	/**东南西北虚拟玩家，主要用于存筹码，存手牌*/
	private ArrayList<KillRoomDirectionPlayer> list_directionPlayer;
	
	/**庄家位置手牌*/
	private ArrayList<PokerVO> list_bankerPoker;
	
	private int bankerJockpotType;
	
	/**
	 * 游戏房间 */
	private Room room;
	
	private ConfigKillroom configKillroom;
	
	private Config_killroomPoker configKillroomPoker;
	
	private ArrayList<JackpotSort> list_jackpotSorts;
	
	private long jackPotWin;
	
	private ArrayList<KillRoomLog> list_killRoomLog;
	
	private HashMap<Integer,Integer> map_otherSubCoins;
	
	/**
	 * 赢金榜前4名，每天更新
	 */
	private ArrayList<KillRoomBigWinVO> list_bigWin = new ArrayList<KillRoomBigWinVO>();	
	
	private ArrayList<KillRoomBigWinVO> list_jackpotPlayers = new ArrayList<KillRoomBigWinVO>();
	private JackpotData lastJackpotData = new JackpotData();
	public static KillRoomImpl inst;
	
	private ArrayList<JackpotRoundSort > list_jackpotWinSort = new ArrayList<JackpotRoundSort>();
	
	private GameTongsha gameTongsha;
	
	private ArrayList<KillRoomNoticVO> list_noticWin = new ArrayList<KillRoomNoticVO>();
	
	private int roundIndex;
	
	/**
	 * 每个方向倍数
	 */
	private int[] directionRate = new int[4];//每个方向倍数
	
	/**每个方向获赔*/
	private long[] directionScore = new long[4];// 
	
	private long[] bankerWinDirectionScore = new long[4];
	public ConfigKillroom getConfigKillroom()
	{
		return configKillroom;
	}

	private void initRoom()
	{
		map_otherSubCoins = new HashMap<Integer, Integer>();
		
		room = new Room(8);
		room.setConfigKillRoom(configKillroomPoker);
		room.setBankerCostCoins(configKillroom.getApplicationBankerCoins());
		Player player = playerService.getRobotBankerPlayer();//没有人申请坐庄了，系统坐庄
		room.changeBanker(player);
		list_jackpotSorts = new ArrayList<JackpotSort>();
		jackPotWin = 0;
		
		room.setJackpot(configKillroom.getStartJackPot());
		list_directionPlayer = new ArrayList<KillRoomDirectionPlayer>();
		for (int i = 0 ; i < 4 ; i++)
		{
			KillRoomDirectionPlayer direction_east = new KillRoomDirectionPlayer();
			direction_east.setPos(i);
			list_directionPlayer.add(direction_east);
		}
		
		list_bankerPoker = new ArrayList<PokerVO>();
		
		list_killRoomLog = new ArrayList<KillRoomLog>();
		
		setState(STATE_IDLE);
		bankerJockpotType = 0;
	}
	
	public void addRoundIndex()
	{
		roundIndex++;
	}
	
	public int getRoundIndex()
	{
		return roundIndex;
	}

	@Override
	public void run()
	{
		try
		{
			runKillRoom();
		}
		catch (Exception e)
		{
			Tool.print_error("通杀场运行错误",e);
		}
	}

	private void runKillRoom()
	{
		switch (state)
		{
			case STATE_BET_TIME:
				runBetTimeState();
				break;

			case STATE_IDLE:
				runIdleState();
				break;
				
			case STATE_LOTTERY:
				runLotteryState();
				break;
				
			default:
				break;
		}
	}

	private void runLotteryState()
	{
		if(getState() == STATE_IDLE)
			return;
		
		long off_time = MyTimeUtil.getCurrTimeMM() - stateTime;
		if(off_time >= LOTTERY_TIME)
		{
			setState(STATE_IDLE);
			checkBankerRound();
			onIdle();
		}
	}

	/**
	 * 闲家视角

		+150  -100   
		 东    南     西      北
		+50   +100   -50     -50   1.赢，50 如果赢，暂时记录，等全部玩家输赢统计结束后，结算庄家
		
		+50   -100	   -50    +50   2.输，-50 如果输，算出自身总可输金币作为庄家可赢金币，考虑jackpot情况，等全部玩家输赢统计结束后，结算庄家
		
		
		
		计算庄家总输赢（每个玩家的输赢金币数总和）
		
		庄家赢：
		庄家直接加上可赢金币
		 
		庄家输：
		庄家是否够赔，在不够赔的情况下按比例赔（玩家已经知道自己赢多少）
	 */
	private HashMap<Integer,Integer> map_playerCalcChip = new HashMap<Integer, Integer>();
	private void onLottery()
	{
		initLottery();
		if(room.getAllPlayer().size() == 0)
		{
			return;
		}
		
		long allScore = 0;//>0庄家赢
		long bankerWinScore = 0;
		bankerJockpotType = 0;
		
//		if(lotteryDebug)
//		{
//			for (Player item : room.getAllPlayer())
//			{
//				Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"参与玩家:"+item.getSimpleData());
//			}
//		}
		
		PokerGroup bankerPokerGroup = onLotteryDealAndCheck();
		 onLotteryCalcJackpot();//奖池情况
		 
		/*
		 * 1.统计闲家在每个方位输赢，汇总该玩家输赢情况，
    			一.如果赢，暂时记录，等全部玩家输赢统计结束后，结算庄家
    			二.如果输，算出自身总可输金币作为庄家可赢金币,并且直接扣除自己输金币，等全部玩家输赢统计结束后，结算庄家
    	
		   2.玩家总输赢即为庄家输赢情况
			一.庄家赢：庄家直接加上可赢金币(此时可赢金币已计算过玩家可输金币)
			二.庄家输：庄家是否够赔，在不够赔的情况下按比例赔（庄家已知赢多少，算出倍数比例）
		 * */
		
		Player banker = room.getBankerPlayer();
		if (!banker.isRobot())
		{
			bankerWinScore += room.getKillRoomJackpotWin(banker.getUserId());
		}
		
		Collection<Player> list_allPlayer = room.getAllPlayer();
		for (Player player : list_allPlayer)
		{
			if(player == null) continue;
			bankerWinScore += onLotteryCalcPlayerDirectionScore(player);
			//赢，暂时记录应该赢得筹码，待庄家得出总输赢时结算，此时不用再处理
			
			//输
			long playerJockpotScore = room.getKillRoomJackpotWin(player.getUserId());
			long playerAllCalcScore = player.getKillRoomRoundCalcScore() + playerJockpotScore;
			if(playerAllCalcScore < 0)//闲家总结算输，看身上金币是否足够
			{
				long giveBackScore = 0;//统计玩家赢得方位下注分数
				long allLostChips = Math.abs(playerAllCalcScore);
				player.setKillRoomGiveBack(false);
				if(allLostChips <= player.getCoins() + player.getKillRoomPayBet())//够陪，还下注金币
				{
					long winScore = player.getKillRoomRoundCalcWinScore();//赢得部分
					winScore *= (1 - configKillroom.getPlayerWinRate());
					giveBackScore = player.getKillRoomPayBet() - winScore;
					player.setKillRoomGiveBack(true);
					Tool.print_debug_level0("闲家总结算输，够赔时计算返回金币:"+giveBackScore);
				}
				
				long calcBet =  0;
				if(allLostChips > (player.getCoins() + player.getKillRoomPayBet()))//有翻倍，输的数量超过身上携带和下注数
				{
					calcBet = player.getCoins() + player.getKillRoomPayBet();
					player.setKillRoomRoundCalcScore(-player.getCoins());//记录本局该玩家应该输的分数，最多输到0分,此时需要额外输掉身上剩余的金币
					
					Tool.print_coins(player.getNickName(),-player.getCoins() + giveBackScore,"通杀场",player.getCoins());
					player.addCoins(-player.getCoins() + giveBackScore);
//					rankService.addWinConisNum(player.getUserId(),-player.getCoins() + giveBackScore,player.getNickName(),player.getLevel());
					Tool.print_debug_level0("闲家总结算输，不够赔:"+(-player.getCoins() + giveBackScore));
				}
				else
				{
					calcBet = allLostChips;
					player.setKillRoomRoundCalcScore(-calcBet);//记录本局该玩家应该输的分数，最多输到0分
					
					Tool.print_coins(player.getNickName(),-calcBet + giveBackScore,"通杀场",player.getCoins());
					player.addCoins(-calcBet + giveBackScore);
					
//					rankService.addWinConisNum(player.getUserId(), -calcBet + giveBackScore,player.getNickName(),player.getLevel());
				}
//				long calcBet =  allLostChips >= (player.getCoins() + player.getKillRoomPayBet()) ? (player.getCoins() + + player.getKillRoomPayBet()) : allLostChips;
				
				allScore += calcBet;
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"闲家["+player.getNickName()+"]玩家总结算输,分数:"+calcBet+",产生输赢总分:"+player.getKillRoomRoundAllScore()+",其中输分:"+player.getKillRoomRoundCalcScore()+",giveBackScore:"+giveBackScore);
			}
			else if(player.getKillRoomRoundCalcScore() == 0)
			{
				long backBet = player.getKillRoomPayBet() - (long) (player.getKillRoomRoundAllScore()/2 * 1.0f * (1 - configKillroom.getPlayerWinRate()));
				
				Tool.print_coins(player.getNickName(),backBet,"通杀场",player.getCoins());
				player.addCoins(backBet);
				
			}
			else
			{
				//闲家赢
				allScore -= player.getKillRoomRoundCalcScore();
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"闲家["+player.getNickName()+"]玩家总结算赢,分数:"+player.getKillRoomRoundCalcScore()+",暂时记录");
			}
		}
		
		long bankerAllScore = getBankerPlayer().getCoins() + room.getKillRoomJackpotWin(getBankerPlayer().getUserId());
		long bankerAllLostScore = Math.abs(allScore);
		float winRate = 1;
		long bankerTax = (long) (bankerWinScore * (1 - configKillroom.getPlayerWinRate()));
		/*结算庄家*/
		if(allScore > 0)//庄家赢，庄家直接加上可赢金币(此时可赢金币已计算过玩家可输金币)
		{
			if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"最终庄家赢,allScore:"+allScore);
//			allScore += //加上所有下的注
			int winScore = (int) (allScore * configKillroom.getPlayerWinRate());
			int jackpotSub = (int) (allScore * configKillroom.getJackpotWinRate());
			if(!getBankerPlayer().isRobot())
			{
				room.addJackpot(jackpotSub);
				Tool.print_coins(getBankerPlayer().getNickName(),winScore,"通杀场庄家赢分",getBankerPlayer().getCoins());
				getBankerPlayer().addCoins(winScore);
//				rankService.addWinConisNum(getBankerPlayer().getUserId(), winScore,getBankerPlayer().getNickName(),getBankerPlayer().getLevel());
				if (winScore >= configKillroom.getBroadcastCost())
				{
					KillRoomNoticVO killRoomNoticVO = new KillRoomNoticVO(getBankerPlayer().getUserId(), getBankerPlayer().getNickName(), winScore);
					list_noticWin.add(killRoomNoticVO);
				}
			}
			getBankerPlayer().setKillRoomRoundCalcScore(winScore);
//			if(!getBankerPlayer().isRobot())
//			{
//				rankService.addWinConisNum(getBankerPlayer().getUserId(), winScore,getBankerPlayer().getNickName(),getBankerPlayer().getLevel());
//			}
		}
		else if(allScore == 0)
		{
			if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"最终平局，需要扣税,bankerTax:"+bankerTax);
			if(!getBankerPlayer().isRobot())
			{
				Tool.print_subCoins(getBankerPlayer().getNickName(),bankerTax<= bankerAllScore ? bankerTax : getBankerPlayer().getCoins(),"通杀",getBankerPlayer().getCoins());
				getBankerPlayer().subCoinse(bankerTax<= bankerAllScore ? bankerTax : getBankerPlayer().getCoins());
			}
			getBankerPlayer().setKillRoomRoundCalcScore(-bankerTax);
		}
		else//最终闲家赢
		{
			bankerAllLostScore += bankerTax;
			if(bankerAllScore <= bankerAllLostScore)//不够赔
			{
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"最终庄家输,不够陪,bankerAllLostScore:"+bankerAllLostScore+",bankerAllScore:"+ bankerAllScore +",其中税:"+bankerTax);
				winRate = bankerAllScore*1.0f / bankerAllLostScore;
				getBankerPlayer().setKillRoomRoundCalcScore(bankerAllScore);
				if(!getBankerPlayer().isRobot())
				{
					getBankerPlayer().setCoins(0);
				}
			}
			else
			{
				getBankerPlayer().addKillRoomRoundCalcScore(-bankerAllLostScore);
				if(!getBankerPlayer().isRobot())
				{
					Tool.print_subCoins(getBankerPlayer().getNickName(),bankerAllLostScore,"通杀",getBankerPlayer().getCoins());
					getBankerPlayer().subCoinse(bankerAllLostScore);
				}
				
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"最终庄家输,够陪,bankerAllLostScore:"+bankerAllLostScore+",bankerAllScore:"+bankerAllScore+"[输bankerAllScore]");
			}
		}

		if (!banker.isRobot())
		{
			long jackPotWinScore = room.getKillRoomJackpotWin(banker.getUserId());
			banker.addCoins(jackPotWinScore);
			Tool.print_coins(banker.getNickName(),jackPotWinScore,"通杀场庄家奖池赢分",banker.getCoins());
			
//			rankService.addWinConisNum(banker.getUserId(),allScore,banker.getNickName(),banker.getLevel());
		}
		
		/*结算庄家*/
		HashMap<Integer,Push_killRoomChangeToLotteryResponse> map_playerChangeToLotteryResponse = new HashMap<Integer, Push_killRoomChangeToLotteryResponse>();
		for (Player player : list_allPlayer)//发消息给所有玩家，包括庄家
		{
			if(player == null) continue;
			Push_killRoomChangeToLotteryResponse response = new Push_killRoomChangeToLotteryResponse();
			response.list_directionPlayers.addAll(list_directionPlayer);
			response.list_bankerPoker.addAll(list_bankerPoker);
//			response.init();
			response.bankerType = bankerPokerGroup.groupType;
			response.killRoomLog = list_killRoomLog.get(list_killRoomLog.size() - 1);
			
			response.jackpotWin = room.getKillRoomJackpotWin(player.getUserId());
			if(player.getUserId() == getBankerPlayer().getUserId() && !getBankerPlayer().isRobot())//单独处理庄家情况
			{
				//if(lotteryDebug)Tool.print_debug_level0("开奖时庄家金币:"+getBankerPlayer().getCoins()+",nickName:"+getBankerPlayer().getNickName());
				response.calcScore = allScore;
				response.bankerScore = getBankerPlayer().getCoins();
				player.clearKillRoomRoundCalcScore();
				int exp = (int) (player.getKillRoomRoundCalcScore()/20000);
				response.nowCoins = player.getCoins();
				playerService.addPlayerExp(player.getUserId(), exp);
				response.jackpot = room.getJackpot();
				
				map_playerChangeToLotteryResponse.put(player.getUserId(),response);
				
//				onLotterySendToBanker(response, player);
				continue;
			}
			
 			int payBet = player.getKillRoomPayBet();//统计玩家赢得方位下注分数
			long payTax = (long) (player.getKillRoomRoundCalcWinScore() * (1-configKillroom.getPlayerWinRate()));//总交税
			if(player.getKillRoomRoundCalcScore() > 0)//玩家最后赢
			{
				int exp = 0;
				long bankerLossOnPlayerWinScore = 0;
				//所有玩家计算完毕，可以得出庄家最后总输赢
				if(allScore > 0)//庄家赢，玩家如果赢，则获全陪（此处庄家赢不代表玩家输，庄家输赢只做赔付情况判断）
				{
					long winScore = player.getKillRoomRoundCalcScore();
					int jackpotSub = (int) (player.getKillRoomRoundCalcScore() * configKillroom.getJackpotWinRate());
					room.addJackpot(jackpotSub);
					
					bankerLossOnPlayerWinScore = winScore;
				}
				else//庄家输
				{
					//庄家输：庄家是否够赔，在不够赔的情况下按比例赔（庄家已知赢多少，算出倍数比例）
					if(bankerAllScore <= bankerAllLostScore)//不够赔
					{
						long playerCanWin = (long) (player.getKillRoomRoundCalcScore() * winRate);
						bankerLossOnPlayerWinScore = playerCanWin;
					}
					else
					{
						bankerLossOnPlayerWinScore = player.getKillRoomRoundCalcScore();
					}
				}
				
				long winScore = (long) (bankerLossOnPlayerWinScore  + player.getKillRoomPayBet() - payTax) ;	//赢得分数 + 返还下注 - 赢得部分交税
				int jackpotSub = (int) (bankerLossOnPlayerWinScore * configKillroom.getJackpotWinRate());
				room.addJackpot(jackpotSub);
				exp = (int) (winScore/20000);
				Tool.print_coins(player.getNickName(),winScore,"通杀场下注赢分",player.getCoins());
				player.addCoins(winScore);

				playerService.addPlayerExp(player.getUserId(), exp);
				response.calcScore = winScore;
				response.exp = exp;
				response.subCoins = payBet;
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"玩家最后赢，winScore:"+winScore+",交税前分数 :"+bankerLossOnPlayerWinScore+",还底注:"+player.getKillRoomPayBet()+",交税:"+payTax);
				
				if (response.calcScore - response.subCoins - response.otherSubCoins >= configKillroom.getBroadcastCost())
				{
//					Tool.print_debug_level0("赢家公告,response.calcScore:"+response.calcScore+",response.subCoins:"+response.subCoins+",response.otherSubCoins:"+response.otherSubCoins);
					long noticScore = response.calcScore - response.subCoins - response.otherSubCoins;
					KillRoomNoticVO killRoomNoticVO = new KillRoomNoticVO(player.getUserId(), player.getNickName(), noticScore);
					list_noticWin.add(killRoomNoticVO);
				}
				
//				rankService.addWinConisNum(player.getUserId(), response.calcScore - response.subCoins - response.otherSubCoins,player.getNickName(),player.getLevel());
			}
			else if(player.getKillRoomRoundCalcScore() == 0)//平局
			{
				response.calcScore = -payTax;
			}
			else//玩家最后输
			{
//				+300		-100		-100		-400
//				+300         -600
//				-300 - 18
				long giveBackScore = 0;
				
				if(player.getKillRoomGiveBack())
				{
					giveBackScore = player.getKillRoomPayBet() - payTax;//返还金币：所有下注 - 赢得部分税
				}
				
				response.calcScore = player.getKillRoomRoundCalcScore() + giveBackScore;//总输分数+返还分数 
				response.subCoins = payBet;
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"玩家最后输:"+response.calcScore+"，赢得部分:"+player.getKillRoomRoundCalcWinScore()+"返还分数 :"+giveBackScore +",交税:"+payTax);
				Tool.print_coins(player.getNickName(),player.getKillRoomRoundCalcWinScore(),"通杀场下注赢分",player.getCoins());
//				player.addCoins(player.getKillRoomRoundCalcWinScore());
			}
			
			long jackPotWinScore = room.getKillRoomJackpotWin(player.getUserId());
			player.addCoins(jackPotWinScore);
			Tool.print_coins(player.getNickName(),jackPotWinScore,"通杀场奖池赢分",player.getCoins());
			
			response.nowCoins = player.getCoins();
			if(isSendMsgDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"玩家["+player.getNickName()+"]"+",当前金币数:"+player.getCoins()+",userId:"+player.getUserId());
//			response.subCoins = response.calcScore >= 0 ? payBet : 0;
			
			response.bankerScore = getBankerPlayer().getCoins();
			response.jackpot = room.getJackpot();

//			player.sendResponse(response);
			map_playerChangeToLotteryResponse.put(player.getUserId(),response);
			player.clearKillRoomRoundCalcScore();
			player.clearKillRoomPayBet();
		}
		
		for (Player player : list_allPlayer)//发消息给所有玩家，包括庄家
		{
			if(player == null) continue;
			
			Push_killRoomChangeToLotteryResponse response = map_playerChangeToLotteryResponse.get(player.getUserId());
			
			if(response==null) continue;

			response.list_tablePlayerScore.addAll(room.getTablePlayerScore());
			if(response.list_tablePlayerScore != null && response.list_tablePlayerScore.size() > 0)
			{
				for (KillRoomTablePlayerRoundScore item : response.list_tablePlayerScore)
				{
					Player playerData = room.getPlayer(item.userId);
//					PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(item.userId);
					item.score = playerData.getCoins();
					//if(lotteryDebug)Tool.print_debug_level0("开奖时坐下玩家金币:"+item.score+",userid:"+item.userId);
				}
			}
//			Tool.print_debug_level0("2222,score="+response.calcScore+",userid="+player.getUserId());
			
			if(response.calcScore>0)
			{
				rankService.addWinConisNum(player.getUserId(), response.calcScore-player.getKillRoomPayBet(),player.getNickName(),player.getLevel());
			}
			player.sendResponse(response);
		}
		calcJackpotBigWin();
 		
		checkLeavePlayer();
		
		for (int i = 0 ; i < list_directionPlayer.size() ; i++)
		{
			KillRoomDirectionPlayer directionPlayer = list_directionPlayer.get(i);
			directionPlayer.clearDirection();
		}
		
		if (list_noticWin.size() > 0)
		{
			for (Player player : playerService.getAllPlayer())
			{
				Push_killRoomBigWin bigWin = new Push_killRoomBigWin(list_noticWin);
				player.sendResponse(bigWin);
			}
		}
	}

	private int getWinDirectionBetScore(Player player)
	{
		int winDirectionBetScore = 0;
		for (KillRoomDirectionPlayer directionPlayerItem : list_directionPlayer)
		{
			if(directionPlayerItem.isWin())
			{
				winDirectionBetScore += directionPlayerItem.getPlayerChipNum(player.getUserId());
			}
		}
		return winDirectionBetScore;
	}

	/**
	 * 计算玩家在通杀场每个方位的输赢
	 * @param player
	 */
	private long onLotteryCalcPlayerDirectionScore(Player player)
	{
		long bankerWinScore  = 0;
		for (KillRoomDirectionPlayer killRoomDirectionPlayer : list_directionPlayer)
		{
			if(killRoomDirectionPlayer.getAllChips() == 0)
			{
				continue;
			}
			
			long chips = killRoomDirectionPlayer.getPlayerChipNum(player.getUserId()) * (killRoomDirectionPlayer.getBaseRate());
			float chipsRate = killRoomDirectionPlayer.getPlayerChipNum(player.getUserId())*1.0f / killRoomDirectionPlayer.getAllChips();
			long directionJackpotNum = killRoomDirectionPlayer.getJockPotScore();
			
			int jackpotScore = (int) (directionJackpotNum * chipsRate);
			if(jackpotScore > 0)
			{
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位:"+killRoomDirectionPlayer.getPos()+"有奖池,jackpotScore:"+jackpotScore+",当前玩家奖池收益:"+chipsRate);
			
				list_jackpotWinSort.add(new JackpotRoundSort(jackpotScore, player.getUserId(),killRoomDirectionPlayer.getJockPotType(),killRoomDirectionPlayer.getPokers()));
			}
			room.addKillRoomJackpotWin(player.getUserId(), jackpotScore);
			if(killRoomDirectionPlayer.isWin())
			{
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位:"+killRoomDirectionPlayer.getPos()+",闲家["+player.getNickName()+"]赢,下注:"+killRoomDirectionPlayer.getPlayerChipNum(player.getUserId())+",倍数:"+killRoomDirectionPlayer.getBaseRate()+",奖池:"+jackpotScore+",总:"+(chips + jackpotScore));
				
				player.addKillRoomRoundCalcScore(chips);
				player.addKillRoomRoundAllScore(chips);
				player.addKillRoomRoundCalcWinScore(chips);
			}
			else
			{
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位:"+killRoomDirectionPlayer.getPos()+",闲家["+player.getNickName()+"]输,下注:"+killRoomDirectionPlayer.getPlayerChipNum(player.getUserId())+",倍数:"+killRoomDirectionPlayer.getBaseRate()+",奖池:"+jackpotScore+",总:"+(-chips + jackpotScore));
				
				player.addKillRoomRoundCalcScore(-chips);
				player.addKillRoomRoundAllScore(chips);
				bankerWinScore += chips;
			}
		}
		return bankerWinScore;
	}

	private int getLostDirectionBetScore(Player player)
	{
		int lostDirectionBetScore = 0;
		for (KillRoomDirectionPlayer directionPlayerItem : list_directionPlayer)
		{
			if(!directionPlayerItem.isWin())
			{
				lostDirectionBetScore += directionPlayerItem.getPlayerChipNum(player.getUserId());
			}
		}
		return lostDirectionBetScore;
	}

	private void onLotterySendToBanker(Push_killRoomChangeToLotteryResponse response, Player player)
	{
		response.bankerScore = getBankerPlayer().getCoins();
		player.clearKillRoomRoundCalcScore();
		int exp = (int) (player.getKillRoomRoundCalcScore()/20000);
		response.nowCoins = player.getCoins();
		playerService.addPlayerExp(player.getUserId(), exp);
		response.jackpot = room.getJackpot();
		
		response.list_tablePlayerScore.addAll(room.getTablePlayerScore());
		if(response.list_tablePlayerScore != null && response.list_tablePlayerScore.size() > 0)
		{
			for (KillRoomTablePlayerRoundScore item : response.list_tablePlayerScore)
			{
				PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(item.userId);
				item.score = playerSimpleData.getCoins();
			}
		}
		
		player.sendResponse(response);
	}

	private void onLotteryCalcJackpot()
	{
		list_jackpotSorts.sort(MySortUtil.highToLowByIndex());//排序，按大小取
		if(isJackpotDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"排序后奖池,list_jackpotSorts:"+list_jackpotSorts);
		if (list_jackpotSorts.size() > 0)// 中奖池了
		{
			for (int i = 0 ; i < list_jackpotSorts.size() ; i++)
			{
				JackpotSort jackpotSort = list_jackpotSorts.get(i);// 排序后取最大奖
				long jackpotNum = calcJackpotScore(jackpotSort);
				if (jackpotNum > room.getJackpot())
				{
					jackpotNum = room.getJackpot();
				}
				if (jackpotNum <= 0)
				{
					break;
				}
				jackPotWin += jackpotNum;
				if(isJackpotDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"中奖池了，方位:" + jackpotSort.pos + ",jackpotNum:" + jackpotNum);
				switch (jackpotSort.pos)
				{
					case 5:// 庄家赢奖池
						Player banker = room.getBankerPlayer();
						if (!banker.isRobot())
						{
							room.subJackPot(jackpotNum);
							room.putKillRoomJackpotWin(banker.getUserId(), jackpotNum);

							list_jackpotWinSort.add(new JackpotRoundSort((int) jackpotNum, banker.getUserId(), bankerJockpotType, banker.getPokers()));
						}
						break;

					default:
						KillRoomDirectionPlayer roomDirectionPlayer = list_directionPlayer.get(jackpotSort.pos);
						roomDirectionPlayer.addJackpotScore(jackpotNum);
						room.subJackPot(jackpotNum);
						break;
				}
			}
		}
	}

	/**
	 * 发派并且检查输赢情况
	 * @return
	 */
	private PokerGroup onLotteryDealAndCheck()
	{
		PokerGroup bankerPokerGroup = onLotteryBankerPoker();
		for (int i = 0 ; i < list_directionPlayer.size() ; i++)
		{
			//和庄家比牌
			KillRoomDirectionPlayer directionPlayer = list_directionPlayer.get(i);
			ArrayList<PokerVO> list_pokers = getDirectionPlayerPokers(i);
			if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位[东,南,西,北]:"+directionPlayer.getPos()+",手牌："+list_pokers);
			PokerGroup group = pokerLogicService.getPokerType(list_pokers);
			checkGroupTypeMission(directionPlayer, group.groupType);
			directionPlayer.setPokers(list_pokers,group.groupType);
			directionPlayer.setJockPotType(group.groupType);
			
			if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位[东,南,西,北]:"+directionPlayer.getPos()+",PokerGroup："+group);
			
			int pokerCompare = pokerLogicService.pokerComparer(bankerPokerGroup, group);
			
			directionPlayer.setWin(pokerCompare < 0 ? true : false);
			if(pokerCompare < 0)
			{
				setDirectionWin(i);
				int baseScore = pokerLogicService.getBaseScore(group.groupType);//倍数
				directionPlayer.setBaseRate(baseScore);
				
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位[东,南,西,北]:"+directionPlayer.getPos()+",比牌方位赢，用方位牌："+pokerCompare+",groupType:"+group.groupType+",方位倍数:"+baseScore);
			}
			else
			{
				int baseScore = pokerLogicService.getBaseScore(bankerPokerGroup.groupType);//倍数
				directionPlayer.setBaseRate(baseScore);
				
				if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"方位[东,南,西,北]:"+directionPlayer.getPos()+",比牌方位输，用庄家牌："+pokerCompare+",groupType:"+bankerPokerGroup.groupType+",方位倍数:"+baseScore);
			}
		 
			int jockpotType = getJockpotType(group);
			if(isJackpotDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"jockpotType:"+jockpotType+",directionPlayer.isExistsChip():"+directionPlayer.isExistsChip());
			if(jockpotType > 0 && directionPlayer.isExistsChip())
			{//加入奖池夺取
				JackpotSort bankJackpotSort = new JackpotSort(i,jockpotType);
				list_jackpotSorts.add(bankJackpotSort);
			}
		}
		return bankerPokerGroup;
	}

	/**庄家发派和奖池牌型
	 * */
	private PokerGroup onLotteryBankerPoker()
	{
		Player banker = room.getBankerPlayer();
		/***********发牌****************/
		list_bankerPoker.clear();
		
		ArrayList<PokerVO> list_pokers = getThreeBankerPokers(banker.isRobot());
		list_bankerPoker.addAll(list_pokers);//发庄家牌
 		if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"发牌，庄家手牌:"+list_bankerPoker);
		
		PokerGroup bankerPokerGroup = pokerLogicService.getPokerType(list_bankerPoker);
 		if(lotteryDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"发牌，庄家PokerGroup:"+bankerPokerGroup);
		/***********发牌****************/
 		
 		if(!banker.isRobot() && isDirectionExistsChip())//不是机器人，并且任意方位下过注
 		{
 			bankerJockpotType = getJockpotType(bankerPokerGroup);
 			if(bankerJockpotType > 0)
 			{
 				JackpotSort bankJackpotSort = new JackpotSort(5,bankerJockpotType);
 				list_jackpotSorts.add(bankJackpotSort);
 			}
 		}
 
		return bankerPokerGroup;
	}

	/**
	 * 发牌要求：系统庄大牌率	人庄大牌率 东南西北大牌 	豹子AAA 	豹子 	顺金
	 * @param robot
	 * @return
	 */
	private ArrayList<PokerVO> getThreeBankerPokers(boolean robot)
	{
		boolean isDebug = false;
		if(isDebug)
		{
			return room.getPokerType(2);
		}
		 
		ArrayList<PokerVO> list_jackpotPoker = getJackpotPoker();
		if(list_jackpotPoker != null)
		{
			return list_jackpotPoker;
		}
		
		 int rate = MyUtil.getRandom(1000);
		 int bankerBigPokerRate = (int) (gameTongsha.getSys_banker_big_chance().floatValue() * 10);//电脑庄
		 if(!robot)
		 {
			 bankerBigPokerRate = (int) (gameTongsha.getPerson_banker_big_chance().floatValue() * 10);//人庄
		 }
		 if(rate < bankerBigPokerRate)
		 {
			 ArrayList<PokerVO> list = room.getPokerType(MyUtil.getRandomBoolean() ? 2 : 3);
			 return list;
		 }
		 
		return room.getRandomPoker(3);
	}

	private ArrayList<PokerVO> getDirectionPlayerPokers(int i)
	{
//		if(i==0)
//		{
//			ArrayList<PokerVO> list_jackpotPoker = room.getPokerType(0);
//			 if(list_jackpotPoker != null)
//			 {
//				 return list_jackpotPoker;
//			 }
//		}
		ArrayList<PokerVO> list_jackpotPoker = getJackpotPoker();
		if(list_jackpotPoker != null)
		{
			return list_jackpotPoker;
		}

		int rate = MyUtil.getRandom(1000);
		int directionRate = (int) (gameTongsha.getDirection_big_chance().floatValue() * 10);//人庄概率
//		Tool.print_debug_level0("人庄开奖大牌概率:"+directionRate+"当前选中:"+rate);
		 if(rate < directionRate)
		 {
			 ArrayList<PokerVO> list = room.getPokerType(MyUtil.getRandomBoolean() ? 2 : 3);
			 return list;
		 }
		 return room.getRandomPoker(3);
	}
	
	private ArrayList<PokerVO> getJackpotPoker()
	{
		int rate = MyUtil.getRandom(1000);
		 int leopardARate = (int) (gameTongsha.getLeopard_a_chance().floatValue() * 10);
		 int leopardRate = (int) (gameTongsha.getLeopard_chance().floatValue() * 10);
		 int flushRate = (int) (gameTongsha.getShun_jin().floatValue() * 10);
		 if(rate < leopardARate)
		 {
			 //AAA豹子
			 ArrayList<PokerVO> list = room.getAAA();
			 if(list != null)
			 {
				 return list;
			 }
		 }
		 else if(rate < leopardRate)
		 {
			 //豹子
			 ArrayList<PokerVO> list = room.getPokerType(0);
			 return list;
		 }
		 else if(rate < flushRate)
		 {
			 //顺金
			 ArrayList<PokerVO> list = room.getPokerType(1);
			 return list;
		 }
		 return null;
	}

	private synchronized boolean isSysBankerBig()
	{
		boolean isBig = false;
		int rate = MyUtil.getRandom(1000);
		int sysBankerRate = (int) (gameTongsha.getPerson_banker_big_chance().floatValue() * 10);//人庄概率
		if(getBankerPlayer().isRobot())
		{
			sysBankerRate = (int) (gameTongsha.getSys_banker_big_chance().floatValue() * 10);//机器人庄概率
		}
		if(rate < sysBankerRate)
		{
			isBig = true;
		}
		return isBig;
	}
	
	private void calcJackpotBigWin()
	{
		if(isJackpotDebug)Tool.print_debug_level0("本次中奖池的数量:"+list_jackpotWinSort.size()+",list_jackpotWinSort:"+list_jackpotWinSort);
		if(list_jackpotWinSort.size() > 0)
		{
			list_jackpotWinSort.sort(MySortUtil.highToLowByIndex());
			JackpotRoundSort jackpotRoundSort = list_jackpotWinSort.get(0);
			PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(jackpotRoundSort.userId);
			
			lastJackpotData.setData(room.getJackpot(), jackpotRoundSort.score, playerSimpleData.getNickName(), playerSimpleData.getRoleId(), playerSimpleData.getHeadIconUrl(), jackpotRoundSort.type, MyTimeUtil.getCurrTimeMM(),jackpotRoundSort.list_handPokers);
		}else
			lastJackpotData.setAllJackpot(room.getJackpot());
		if(isJackpotDebug)Tool.print_debug_level0("更新服务器端奖池信息:"+lastJackpotData);
	}

	private boolean isDirectionExistsChip()
	{
		boolean isExistsChip = false; 
		for (KillRoomDirectionPlayer item : list_directionPlayer)
		{
			isExistsChip = item.isExistsChip();//当前方位有没有下过注
			if(isExistsChip)
			{
				return true;
			}
		}
		return isExistsChip;
	}

	private void checkLeavePlayer()
	{
		for (int userId : room.getLeavePlayers())
		{
			Player player = room.removePlayer(userId);
			player.clearRoom();
		}
		room.clearLeavePlayers();
	}


	/**
	 * 任务
	 * @param directionPlayer
	 * @param groupType
	 */
	private void checkGroupTypeMission(KillRoomDirectionPlayer directionPlayer, int groupType)
	{
		switch (groupType)
		{
			case CardTypeEnum.CARD_TYPE_豹子:
				Iterator<Entry<Integer, Integer>> iterator = directionPlayer.getAllPlayerAndChip().entrySet().iterator();
				while (iterator.hasNext())
				{
					Entry<Integer, Integer> entry = iterator.next();
					int userId = entry.getKey();
					taskService.killRoomAndInTheBaoZiTask(userId, 1);
				}
				break;

			default:
				break;
		}
	}

	private void setDirectionWin(int direction)
	{
		KillRoomLog killRoomLog = list_killRoomLog.get(list_killRoomLog.size() - 1);
		switch (direction)
		{
			case 0://东
				killRoomLog.dongWin= true;
				break;
				
			case 1://南
				killRoomLog.nanWin = true;
				break;
				
			case 2://西
				killRoomLog.xiWin = true;
				break;
				
			case 3://北
				killRoomLog.beiWin = true;
				break;
		}
	}

	
	/**
	 * 开奖时，检查奖池牌型
	 */
	private void onLotteryJackpot()
	{
//		if(bankerJockpotType > 0)
//		{
//			JackpotSort bankJackpotSort = new JackpotSort(5,bankerJockpotType);
//			list_jackpotSorts.add(bankJackpotSort);
//		}
//		//此时所有奖池牌型已经获取
//		
//		list_jackpotSorts.sort(MySortUtil.highToLowByIndex());
//		if(list_jackpotSorts.size() > 0)//中奖池了
//		{
//			for (int i = 0 ; i < list_jackpotSorts.size() ; i++)
//			{
//				JackpotSort jackpotSort = list_jackpotSorts.get(i);//排序后取最大奖
//				long jackpotNum = calcJackpotScore(jackpotSort);
//				if(jackpotNum > room.getJackpot())
//				{
//					jackpotNum = room.getJackpot();
//				}
//				if(jackpotNum <= 0)
//				{
//					break;
//				}
//				jackPotWin += jackpotNum;
//				Tool.print_debug_level0("中奖池了，方位:"+jackpotSort.pos+",jackpotNum:"+jackpotNum);
//				switch (jackpotSort.pos)
//				{
//					case 5://庄家赢奖池
//						Player banker = room.getBankerPlayer();
//						if(!banker.isRobot())
//						{
//							room.addBankerWinScore(jackpotNum);
//							room.subJackPot(jackpotNum);
//							room.putKillRoomJackpotWin(banker.getUserId(), jackpotNum);
//							list_jackpotWinSort.add(new JackpotRoundSort((int) jackpotNum, banker.getUserId(),bankerJockpotType,banker.getPokers()));
//						}
//						break;
//						
//					default:
//						KillRoomDirectionPlayer roomDirectionPlayer = list_directionPlayer.get(jackpotSort.pos);
//						roomDirectionPlayer.addJackpotScore(jackpotNum);
//						
//						room.subJackPot(jackpotNum);
//						
//						break;
//				}
//			}
//		}
	}

	private int calcJackpotScore(JackpotSort jackpotSort)
	{
		int jackpotNum = 0;
		switch (jackpotSort.type)
		{
			case JACKPOT_3A://50
				jackpotNum = (int) (room.getJackpot() *0.5f);
				break;
				
			case JACKPOT_OTHER_LEOPARD://20
				jackpotNum = (int) (room.getJackpot() *0.2f);
				break;
				
			case JACKPOT_FLUSH_COLOR://10
				jackpotNum = (int) (room.getJackpot() *0.1f);
				break;
				
			default:
				break;
		}
		return jackpotNum;
	}

	private int getJockpotType(PokerGroup group)
	{
		switch (group.groupType)
		{
			case CardTypeEnum.CARD_TYPE_豹子:
				if(group.list_poker.get(0).type == FaceTypeEnum.FACE_TYPE_A_VALUE)
				{
					return JACKPOT_3A;
				}
				return JACKPOT_OTHER_LEOPARD;

			case CardTypeEnum.CARD_TYPE_顺金:
				return JACKPOT_FLUSH_COLOR;
				
			default:
				return 0;
		}
	}

	private void checkBankerRound()
	{
		Player bankerPlayer= room.getBankerPlayer();
		if(bankerPlayer.isRobot() && room.getBankerList().size() > 0)//机器人坐庄,列表有人
		{
			Tool.print_debug_level0("检查切换庄家,当前庄家是机器人，并且上庄列表有人，尝试切换庄家");
			changeBanker();
		}
		else if(!bankerPlayer.isRobot())
		{
			Tool.print_debug_level0("检查切换庄家,当前庄家不是机器人");
			if(room.addBankerAndCheckBankerRoundNum() || !bankerPlayer.isOnline() || room.resignBanker() || bankerPlayer.getCoins() <= 4000000)//增加坐庄次数，并且判断连庄次数是否已满
			{
				Tool.print_debug_level0("检查切换庄家,当前庄家不是机器人，回合数到了或者庄家下线或者申请下庄，或者金币不够了，尝试切换庄家");
				changeBanker();
			}
		}
		//机器人在坐庄，庄家列表没人，什么都不做
	}

	private void changeBanker()
	{
		Push_killRoomChangeBanker response = null;
		Player player = null;
		PlayerSimpleData simpleData = room.changeBankerByList();
		if(simpleData != null)
		{
			Tool.print_debug_level0("检查切换庄家,从上庄列表找到玩家,simpleData:"+simpleData);
			player = playerService.getPlayer(simpleData.getUserId());
			response = new Push_killRoomChangeBanker(simpleData);
			
			taskService.everyDayUpBankerInKillRoom(player.getUserId(), 1);
			standUp(player);
		}
		else
		{
			Tool.print_debug_level0("检查切换庄家,没人申请上庄，机器人上庄");
			player = playerService.getRobotBankerPlayer();//没有人申请坐庄了，系统坐庄
			response = new Push_killRoomChangeBanker(player.getSimpleData());
		}
		
		Push_killRoomResignBanker killRoomResignBanker = new Push_killRoomResignBanker();
		room.getBankerPlayer().sendResponse(killRoomResignBanker);//通知下庄
		room.changeBanker(player);
		broadResponse(response);
	}

	private Player getBankerPlayer()
	{
		return room.getBankerPlayer();
	}

	private void initLottery()
	{
		addRoundIndex();
		list_noticWin.clear();
		list_jackpotWinSort.clear();
		list_jackpotPlayers.clear();
		map_otherSubCoins.clear();
		list_bankerPoker.clear();
		
		room.clear();
		room.initRoomPoker(pokerLogicService.getInitPokersWithOutJoker());
		room.sortBankerList();
		map_playerCalcChip.clear();
		list_jackpotSorts.clear();
		jackPotWin = 0;
		
	
		Arrays.fill(directionRate,0);
		Arrays.fill(directionScore,0);
		Arrays.fill(bankerWinDirectionScore,0);
		
		if(list_killRoomLog.size() >= 10)
		{
			list_killRoomLog.remove(0);
		}
		KillRoomLog killRoomLog = new KillRoomLog();
		list_killRoomLog.add(killRoomLog);
	}

	private void addPayerRoundCalcChip(int userId,int chip)
	{
		int num = getPlayerRoundCalcChip(userId);
		num -= chip;
		map_playerCalcChip.put(userId, num);
	}
	
	private int getPlayerRoundCalcChip(int userId)
	{
		Integer num = map_playerCalcChip.get(userId);
		return num == null ? 0 : num;
	}

	private void sendToAll(Response response)
	{
		for (Player player : room.getAllPlayer())
		{
			player.sendResponse(response);
		}
	}

	private void runIdleState()
	{
		if(getState() == STATE_BET_TIME)
			return;
		
		long off_time = MyTimeUtil.getCurrTimeMM() - stateTime;
		if(off_time >= IDLE_TIME)
		{
			setState(STATE_BET_TIME);
			onBetTime();
		}
	}

	private void onBetTime()
	{
		Push_killRoomChangeToBetResponse response = new Push_killRoomChangeToBetResponse();
		sendToAll(response);
	}

	private void onIdle()
	{
		Push_killRoomChangeToIdleResponse response = new Push_killRoomChangeToIdleResponse();
		sendToAll(response);
	}

	private void runBetTimeState()
	{
		if(getState() == STATE_LOTTERY)
			return;
		
		long off_time = MyTimeUtil.getCurrTimeMM() - stateTime;
		if(off_time >= BET_TIME)
		{
			setState(STATE_LOTTERY);
			room.sortBankerList();
//			onLotteryCalcScore();
			onLottery();
		}
	}

	private void setState(int state)
	{
		this.state = state;
		stateTime = MyTimeUtil.getCurrTimeMM();
	}

	@Override
	public boolean payChip(Player player,int pos,int chipNum)
	{
//		vipService.TopUp(player.getUserId(), 10);
//		moneyTreeService.addTreeLv(player.getUserId(), 10);
		if(chipNum<=0)
		{
			return false;
		}
		
		if(!isBetTime())
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下注,"玩家："+player.getNickName()+",userId:"+player.getUserId()+",下注时不在可下注状态，当前状态:"+state);
			return false;
		}
		
		if(player.getManyPepolGame()!=null)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下注,"玩家："+player.getNickName()+",userId:"+player.getUserId()+",万人场下注,pos:"+pos+",下注:"+chipNum);
			return false;
		}
		
		if(pos < 0 && pos >= list_directionPlayer.size())
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下注,"玩家："+player.getNickName()+",userId:"+player.getUserId()+",下注时位置错误,pos:"+pos);
			return false;
		}
		
		KillRoomDirectionPlayer killDirectionPlayer =  list_directionPlayer.get(pos);
		int playerChip = killDirectionPlayer.getPlayerChipNum(player.getUserId());
		if(playerChip + chipNum <= 20000000 || true)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下注,"玩家："+player.getNickName()+",userId:"+player.getUserId()+",下注,pos:"+pos+",单边总下注(包含本次):"+(playerChip+chipNum));
			
			killDirectionPlayer.addChip(player.getUserId(), chipNum);
			Tool.print_subCoins(player.getNickName(),chipNum,"通杀注",player.getCoins());
			player.subCoinse(chipNum);
			player.addKillRoomPayBet(chipNum);
			room.addPlayerRoundCalcScore(player.getUserId(),-chipNum);
			Push_killRoomPayBetResponse response = new Push_killRoomPayBetResponse(player.getUserId(), pos, chipNum); 
			for (Player item : room.getAllPlayer())
			{
				if(item.getUserId() != player.getUserId())
				{
					item.sendResponse(response);
				}
			}
			return true;
		}
//		else
//		{
//			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场下注,"玩家："+player.getNickName()+",userId:"+player.getUserId()+",下注,pos:"+pos+",单边总下注数超过2千万，当前:"+playerChip+"，期望:"+chipNum);
//			return false;
//		}
		return false;
	}

	private boolean isBetTime()
	{
		return state == STATE_BET_TIME;
	}

	@Override
	public int requestBanker(Player player)
	{
		return 0;
	}

	@Override
	public boolean leaveRoom(Player player)
	{
		if(!room.isExitsPlayer(player.getUserId()))
		{
			return false;
		}
		room.addLeaveRoomList(player.getUserId());
		room.removeBankerListExist(player.getUserId());
		return true;
	}

	@Override
	public boolean enterRoom(Player player)
	{
		Room room = getRoom();
		
		 
		EnterKillRoomResponse response = new EnterKillRoomResponse(EnterKillRoomResponse.SUCCESS, getState());
		Player bankerPlayer = room.getBankerPlayer();
		response.setBanker(bankerPlayer == null ? null : bankerPlayer.getSimpleData());
		response.setTablePlayer(room.getTableSimplePlayers());
		// response.setRequestBanker(room.getRequestBanker());
		response.jackpot = room.getJackpot();
		response.stateTime = getStateTime();
		response.list_killRoomLog = getKillRoomLog();
		
		response.bankerRound = getBankerRound();
		response.list_bigWin = getBigWin();
		
		for (KillRoomDirectionPlayer item : list_directionPlayer)
		{
			response.list_directionBetNum.add(item.getAllChips());
		}
		response.roundIndex = getRoundIndex();
		player.sendResponse(response);
		
		player.setInsideRoom(room);
		return room.addPlayer(player);
	}
	
	@PostConstruct
	private void init()
	{
		setState(STATE_BET_TIME);
		inst = this;
		ServerEvent.addEvent(this);
	}
	
	private void initRoomConfig()
	{
		configKillroom = KillRoomDaoImpl.getRoomConfig();
		configKillroomPoker = KillRoomDaoImpl.getKillRoomPokerConfig();
		gameTongsha = KillRoomDaoImpl.getGameTongsha();
// 		gameTongsha.setLeopard_chance(new BigDecimal(99));
		Tool.print_debug_level0("初始化通杀场概率,系统庄:"+gameTongsha.getSys_banker_big_chance().floatValue() +",人庄:"+gameTongsha.getPerson_banker_big_chance().floatValue()+",方位:"+gameTongsha.getDirection_big_chance().floatValue()+",AAA:"+gameTongsha.getLeopard_a_chance().floatValue()
				+",豹子:"+gameTongsha.getLeopard_chance().floatValue()+",顺金:"+gameTongsha.getShun_jin().floatValue());
	}
	
	public synchronized void refreshKillroomConfig()
	{
		gameTongsha = KillRoomDaoImpl.getGameTongsha();
		Tool.print_debug_level0("初始化通杀场概率,系统庄:"+gameTongsha.getSys_banker_big_chance().floatValue() +",人庄:"+gameTongsha.getPerson_banker_big_chance().floatValue()+",方位:"+gameTongsha.getDirection_big_chance().floatValue()+",AAA:"+gameTongsha.getLeopard_a_chance().floatValue()
				+",豹子:"+gameTongsha.getLeopard_chance().floatValue()+",顺金:"+gameTongsha.getShun_jin().floatValue());
	}

	@Override
	public int sitDown(Player player, int pos)
	{
		if(player == null)
		{
			Tool.print_error("坐下时玩家空，pos："+pos);
			return Response.ERROR_UNKNOWN;
		}
		
		if(room.isTableExits(player.getKillRoomPos()))
		{
			Player tablePlayer = room.getTablePlayer(player.getKillRoomPos());
			if(tablePlayer.getUserId() == player.getUserId())
			{
				return KillRoomSitDownResponse.ERROR_已经坐下;
			}
		}
		
		int code = room.killRoomSitDown(player,pos);
		if(code == Response.SUCCESS)
		{
			Push_killRoomSitDown response = new Push_killRoomSitDown(player.getSimpleData());
			broadResponse(response);
			
			startTableTime(player.getUserId());
		}
		return code;
	}	
	
	private static final String tableMissionName = "tableMission:";
	private static final long TABLE_TIME = MyTimeUtil.TIME_M * 5000;
	private void startTableTime(int userId)
	{
		SimpleTriggerQuartz.startSimpleTask(tableMissionName + userId, TABLE_TIME, 0, 0, TableMission.class);
	}
	
	private void removeTableTime(int userId)
	{
		SimpleTriggerQuartz.removeJob(tableMissionName + userId);
	}
	
	@Override
	public boolean standUp(Player player)
	{
		if(player == null)
		{
			Tool.print_error("站起时玩家空");
			return false;
		}
		int pos = player.getKillRoomPos();

		boolean isSuccess = room.killroomStandUp(player);
		if(isSuccess)
		{
			removeTableTime(player.getUserId());
			Push_killRoomStandUp response = new Push_killRoomStandUp(pos);
			broadResponse(response);
		}
		return isSuccess;
	}

	private void broadResponse(Response response)
	{
		for (Player player : room.getAllPlayer())
		{
			if(player.isOnline())
			{
				player.sendResponse(response);
			}
		}
	}

	@Override
	public void online(Player player, Channel channel)
	{
		
	}

	@Override
	public void offlineOnGame(Player player)
	{
		standUp(player);
		room.removeBankerListExist(player.getUserId());
//		room.removePlayer(player.getUserId());
	}

	@Override
	public void offlineOnServer(Player player)
	{
		room.removePlayer(player.getUserId());
	}

	@Override
	public Room getRoom()
	{
		return room;
	}

	@Override
	public int getState()
	{
		return state;
	}

	@Override
	public int applicationKillRoomBanker(Player player)
	{
		int code = room.requestBankerKillRoom(player);
//		if(isSuccess)
//		{
//			room.clearBankerRound();
//			Push_killRoomChangeBanker response = new Push_killRoomChangeBanker(room.getBankerPlayer().getSimpleData());
//			broadResponse(response);
//		}
		return code;
	}

	@Override
	public long getStateTime()
	{
		return MyTimeUtil.getCurrTimeMM() - stateTime;
	}

	@Override
	public ArrayList<KillRoomLog> getKillRoomLog()
	{
		return list_killRoomLog;
	}

	public void tableTimeUP(int userId)
	{
		Player player = playerService.getPlayer(userId);
		if(player == null)
		{
			Tool.print_error("卡座时间到时玩家为空");
			return;
		}
		standUp(player);
	}

	@Override
	public JackpotData getJackpotData()
	{
		return lastJackpotData;
	}

	@Override
	public int getBankerRound()
	{
		return room.getBankerRoundNum();
	}

	@Override
	public void updateBigWinPlayer(ArrayList<RankVO> list_bigWinRank)
	{
		 Tool.print_debug_level0("updateBigWinPlayer:"+list_bigWinRank);
		if(list_bigWinRank == null || list_bigWinRank.size() == 0)
		{
			return;
		}
		
		list_bigWin.clear();
		
		for (RankVO rankVO : list_bigWinRank)
		{
			PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(rankVO.getUserId());
			if(playerSimpleData == null)
			{
				//玩家不存在，存档被删
				continue;
			}
			KillRoomBigWinVO killRoomBigWinVO = new KillRoomBigWinVO(rankVO.getUserId(), rankVO.getNickName(),playerSimpleData.getRoleId(),playerSimpleData.getHeadIconUrl(),rankVO.getScore());
			list_bigWin.add(killRoomBigWinVO);
		}
	}

	@Override
	public ArrayList<KillRoomBigWinVO> getBigWin()
	{
		for (KillRoomBigWinVO item : list_bigWin)
		{
			PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(item.getUserId());
			item.setNickName(playerSimpleData.getNickName());
		}
		return list_bigWin;
	}

	@Override
	public void gameServerStartup()
	{
		initRoomConfig();
		initRoom();
	
		WorldMapUnitEvent.addEventListener(this);
		
		try
		{
			SimpleTaskUtil.startTask("killRoom", 5000, 500, this);
			stateTime = MyTimeUtil.getCurrTimeMM();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public Player getBanker()
	{
		return getBankerPlayer();
	}

	@Override
	public void bankerWithdrawalSafebox(Player play)
	{
		broadResponse(new Push_killRoomBankerCoins(play.getUserId(),play.getCoins()));
	}
}
