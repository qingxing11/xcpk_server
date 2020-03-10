package com.wt.xcpk.classic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.cmd.xcpk.classic.ChangeTableResponse;
import com.wt.cmd.xcpk.classic.EnterBeginnerResponse;
import com.wt.cmd.xcpk.classic.LeaveClassicGameResponse;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_allin;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_bigWin;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_checkPoker;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_comparerPoker;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_dealPoker;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_otherPlayerEnter;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerAction;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerDie;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerFollow;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerLeave;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerRaiseBet;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerReady;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_playerWin;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_reconnect;
import com.wt.cmd.xcpk.classic.push.ClassicGamePush_waiStart;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomSitdownResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomStandUpResponse;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_allin;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_bigWin;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_checkPoker;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_comparerPoker;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_dealPoker;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_otherPlayerApplicationBanker;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_otherPlayerResignBanker;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerAction;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerDie;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerFollow;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerRaiseBet;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerSitdown;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerStandUp;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_playerWin;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_reconnect;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_waiPayBet;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_waiStart;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.server.config.ConfigService;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.sort.MySortUtil;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;
import com.wt.xcpk.classicgame.ConfigClassicGame;
import com.wt.xcpk.manypepol.ManyPepolService;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.GameRoundDataVO;
import com.wt.xcpk.vo.JackpotData;
import com.wt.xcpk.vo.KillRoomNoticVO;
import com.wt.xcpk.vo.poker.CardTypeEnum;
import com.wt.xcpk.vo.poker.FaceTypeEnum;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.vo.poker.PokerVO;
import com.wt.xcpk.zjh.killroom.JackpotSort;
import com.wt.xcpk.zjh.logic.PokerLogicService;

import model.ConfigManypepolroompoker;
import model.GameWanren;

/*
 * 开始比赛逻辑
投入锅底       发牌之前大家先付出的游戏分。
发牌             一副牌（52张无大小王），从庄家开始发牌，第一次开局的话，随机选择一个用户为庄家先发牌。每人发三张牌，牌面向下，为暗牌。
游戏             游戏人数为2-6人。庄家逆时针的下一家先开始下注，其他玩家依次逆时针操作。轮到玩家操作时，玩家根据条件和判断形势可以进行加、跟、看牌、放弃、比牌等操作。
看牌		
	a. 没有选择“放弃”的玩家，轮到自己动作时，可以选择看自己的牌，看过的牌为明牌。可以在看牌后再选择是否跟、加，或比牌，这时你的牌就是明牌了，跟、加也是按照明牌的规矩跟、加；当然也可以暗牌操作。
 	b. 游戏中放弃的玩家，在结束游戏前无权查看自己的牌和其他玩家的牌。
	c. 游戏结束后，将公开全部玩家的牌！
比牌
	a. 从第二轮开始，玩家在投注前可以选择“比牌”。
	b. 比牌双方进行比牌，所有玩家看不到两人的牌面。
	c. 牌型牌点相同时被比牌方胜。
跟注、加注
	a. 每次跟注、加注，单注均不可超过个人单局约定的“跟注”“加注”上限--即游戏房间内最大筹码，也就是顶注。
	b. 当选择“加注”时，加注框顶端会显示这一手加注数值。
抽水           同时每盘游戏结束后，系统会收取胜利玩家赢利游戏币的5%作为系统分！
胜负判定
	a. 豹子>顺金>金花>顺子>对子>单张
	b. 当豹子存在时，“花色不同235”>“豹子”
	c. AKQ >KQJ>...234>A23。单牌大小：A>K>Q…..>2。
	d. 对子的情况，先比对，再比单。
	e. 全部为单张时，由最大的单张开始依次分别比较
	f. 比牌牌型等大，先开为负
*/
/*
 * 流程：
 * 有玩家进入房间后，重置并开始倒计时准备
 * 倒计时结束后玩家人数超过2人，房间开始比赛
 * 
 * 随机选择庄家
 *开始比赛逻辑
 *玩家比赛中动作:看牌√，跟注×，加注×，比牌×
 */
@Service
@Scope("prototype")
public class ClassicGame
{
	private static final boolean isClassicDebug = true;
	
	public static final int JACKPOT_3A = 3;
	public static final int JACKPOT_OTHER_LEOPARD = 2;
	public static final int JACKPOT_FLUSH_COLOR = 1;
	
	private static final boolean isDebug = true;
	private static final boolean isCalcDebug = true;
	private static final boolean isAddBetDebug = true;
	private static final boolean isDebug_万人场上庄列表 = false;
	
	private static final int bankerListMax = 100;
	
	private static final boolean isDebugDeal = true;
	@Autowired
	private PokerLogicService pokerLogicService;
	
	@Autowired
	private RankService rankService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private ConfigService configService;
	
	@Autowired
	private ManyPepolService manyPepolService;
	
	public static final int LEVEL_初级场 = 0;
	public static final int LEVEL_中级场 = 1;
	public static final int LEVEL_高级场 = 2;
	public static final int LEVEL_土豪场 = 3;
	public static final int LEVEL_万人场 = 4;
	private int level;
	
	/**
	 * 待机
	 */
	public static final int STATE_IDLE = 0;
	
	public static final int STATE_WAITSTART = 1;
	
	/**
	 * 发牌
	 */
	public static final int STATE_DEAL = 2;//发牌
	/**
	 * 等待玩家思考
	 */
	public static final int STATE_PLAYER_THINK = 3;
	
	/**
	 * 结算状态，播放金币动画，等待下一局开始
	 */
	public static final int STATE_CALC_WIN = 4;
	
	/**等待下注*/
	public static final int STATE_WAIT_PAY_BET = 5;
	
	/**等待比牌*/
	public static final int STATE_WAIT_COMPARERPOKER = 6;
	
	private int state;
	
	private Room room;
	
	private long stateStartTime;
	
	private ConfigClassicGame configClassicGame; 
	
	private int playerNum;
	
	private int playPos;
	
	private int round;
	
	private Player bankerPlayer;
	private int bankerPos = -1;
	
	private int allBet;
	private int nowBet;
	
 	private ArrayList<Player> list_sitdown;
	
	/**
	 * 最后一次比牌时，发起方是否胜利
	 */
	private boolean lastComparerPokerWin;
//	private HashMap<Integer,KillRoomDirectionPlayer> map_directionPlayer;
	
	private boolean isAllIn;
	
	private boolean isRemove;
	
	private int readyNum;
	
	private ArrayList<Integer> list_allBet = new ArrayList<Integer>();
	
	private int id;
	
	private HashSet<Integer> set_getBox = new HashSet<Integer>();
	
	private boolean isFirstPlayer;
	
//	private int rewardPool;
	
	private ArrayList<JackpotSort> list_jackpotSorts = new ArrayList<JackpotSort>();
//	public int getRewardPool()
//	{
//		return rewardPool;
//	}

	private JackpotData lastJackpotData = new JackpotData();
	
	/**
	 * 最后一个行动玩家的allInState状态，断线重连用
	 */
	private int lastPlayAllInState;
	
	private boolean isFirstWaitThink;
	
	private boolean isManyPepolRoom;
	public int getId()
	{
		return id;
	}


	public boolean isAllIn()
	{
		return isAllIn;
	}

	public void setAllIn()
	{
		this.isAllIn = true;
	}
	
	public void clearAllIn()
	{
		isAllIn = false;
	}

	public ArrayList<PlayerSimpleData> getTableList()
	{
		ArrayList<PlayerSimpleData> playerSimpleData = new ArrayList<PlayerSimpleData>();
		for (Player player : list_sitdown)
		{
			playerSimpleData.add(player.getSimpleData());
		}
		
		return playerSimpleData;
	}
	
	/**
	 * 当前跟注需要筹码基数
	 * @return
	 */
	public int getNowBet()
	{
		return nowBet;
	}
	
	public void setNowBet(int num)
	{
		this.nowBet = num;
	}
	
	public void addRound()
	{
		if(isDebug)Tool.print_debug_level0("回合数增加,当前:"+round);
		round++;
	}

	/***
	 * 当前总筹码
	 * @return
	 */
	public int getAllBet()
	{
		return allBet;
	}

	
	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		stateStartTime = MyTimeUtil.getCurrTimeMM();
		if(isDebug)Tool.print_debug_level0("设置状态到==========="+state);
		this.state = state;
	}

	public int getPlayPos()
	{
		return playPos;
	}
	
	public void setClassicConfig(ConfigClassicGame configClassicGame)
	{
		this.configClassicGame = configClassicGame;
	}
	
	private void changeToWaitStart()
	{
		if(getState() == STATE_WAITSTART)
			return;
		
		stateStartTime = MyTimeUtil.getCurrTimeMM();
		//切换状态
		setState(STATE_WAITSTART);
//		initClassicGameData();
		
		Collection<Player> list_all = room.getAllPlayer();
		if(list_all != null)
		{
			for (Player player : list_all)
			{
				if(player != null && !player.isOnline())
				{
					if(level != LEVEL_万人场)
					{
						if(isDebug)Tool.print_debug_level0("changeToWaitStart:检查玩家时不在线，申请站起"+player.getSimpleData());
						standUp(player);//此处会减少可开始人数
					}
				
//					playerNum++;//补偿
				}
			}
		}
		
		if(level == LEVEL_万人场)
		{
			broadcastStateToAll(new ManyPepolPush_waiStart());
		}
		else
		{
			ClassicGamePush_waiStart classicGamePush_waiStart = new ClassicGamePush_waiStart();
			classicGamePush_waiStart.waitPlayerTime = configClassicGame.getWaitStartTime();
			broadcastStateToAll(classicGamePush_waiStart);
		}
	}
	
	
	private void changeToWaitCalc()
	{
		for (Player item : room.getAllPlayer())
		{
			item.clearCheck();
		}
	}
	
	private void changeAllPlayerIdle()
	{
		for (Player item : room.getAllPlayer())
		{
			if(level == LEVEL_万人场)
			{
				if(item.getManyGamePos()>0)
					item.changeIdle();
			}else
			{
				item.changeIdle();
			}
		}
	}

	public void runClassicGame()
	{
//		System.out.println("房间当前状态 ："+state);
		try
		{
			switch (state)
			{
				case STATE_WAITSTART:
					runWaitStart();
					break;
				
				case STATE_DEAL:
					changeToWaitPlayerThink();
					break;
					
				case STATE_PLAYER_THINK:
					runWaitPlayerThink();
					break;

				case STATE_WAIT_PAY_BET:
					runWaitPayBet();
					break;
					
				case STATE_CALC_WIN:
					runCalcWin();
					break;
					
				case STATE_WAIT_COMPARERPOKER:
					runWaitComparerpoker();
					break;
					
				default:
					break;
			}
		}
		catch (Exception e)
		{
			Tool.print_error("经典场房间逻辑出错",e);
		}
	}
	
	private void runWaitComparerpoker()
	{
		if(getState()==STATE_CALC_WIN)
			return;
		
		long off_time = MyTimeUtil.getCurrTimeMM() - stateStartTime;
		if( off_time>= configClassicGame.getWaitComparerpoker() * MyTimeUtil.TIME_S )
		{
			boolean isGameOver = checkGameOver();
			if(!isGameOver)
			{
 				changeToWaitPlayerThink();
//				if(lastComparerPokerWin)
//				{
//					waitPlayerAction(room.getTablePlayers()[playPos]);
//				}
//				else
//				{
//					changeToWaitPlayerThink();
//				}
			}
		}
	}

	private void runCalcWin()
	{
		long off_time = MyTimeUtil.getCurrTimeMM() - stateStartTime;
		if(off_time >= configClassicGame.getWaitCalcWin() * MyTimeUtil.TIME_S && off_time<(configClassicGame.getWaitCalcWin()+1) * MyTimeUtil.TIME_S)
		{
			changeAllPlayerIdle();
			checkLeavePlayer();
			calcCoinsLeft();
			addManyPepolPlayerRound();
			checkManyPepolPlayerRound();
			
			if(room.getTablePlayerNum() > 1)//结束后剩余玩家满足开局条件
			{
				Tool.print_debug_level0("runCalcWin,人数大于1，继续比赛");
				changeToWaitStart();
			}
			else
			{
				Tool.print_debug_level0("runCalcWin,人数不足:"+room.getAllPlayer().size());
				changeToIdle();//人数不足，等待开始
			}
		}
	}

	private void calcCoinsLeft()
	{
		int cost = getLevelCostBaseBet();
		for (Player player : room.getTablePlayers())
		{
			if(player != null)
			{
				if(player.getCoins() < cost)
				{
					standUp(player);
				}
			}
		}
	}

	private int getLevelCostBaseBet()
	{
 		switch (level)
		{
			case LEVEL_初级场:
				return 3000;
				
			case LEVEL_中级场:
				return 50000;
				
			case LEVEL_高级场:
				return 100000;
				
			case LEVEL_土豪场:
				return 2000000;

			case LEVEL_万人场:
				return 2500000;
				
			default:
				return 100;
		}
	}

	//TODO 玩家下庄后上庄，进入上庄列表但不能上桌bug，获取座位时错误
	private synchronized void checkManyPepolPlayerRound()
	{
		if(isManyPepolRoom())
		{
			try
			{
				//达不到上桌条件的，从队列中剔除
				for (int i = 0 ; i < list_sitdown.size()  ; )
				{
					Player item = list_sitdown.get(i);
					if(item!=null)
		        	{
		        		if(item.getCoins()<manyPepolService.getWanrenConfig().getApplyCoins())
						{
							if (removeBankerListExist(item.getUserId()))
							{
								Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌, "金币不够被强制下桌，玩家:" + item.getNickName() + ",userId:" + item.getUserId());
								broadcastStateToAll(new ManyPepolRoomStandUpResponse(ManyPepolRoomStandUpResponse.SUCCESS));
							}else
								i++;
						}else
							i++;
		        	}else
						i++;
				}
			}catch (Exception e)
			{
				Tool.print_debug_level0("金币不够被强制下桌失败:");
			}
			
//	        Iterator<Player> iterator = list_sitdown.iterator();
//	        while (iterator.hasNext()) {
//	        	Player item = iterator.next();
//	        	if(item!=null)
//	        	{
//	        		if(item.getCoins()<manyPepolService.getWanrenConfig().getApplyCoins())
//					{
//						if (removeBankerListExist(item.getUserId()))
//						{
//							Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌, "金币不够被强制下桌，玩家:" + item.getNickName() + ",userId:" + item.getUserId());
//							broadcastStateToAll(new ManyPepolRoomStandUpResponse(ManyPepolRoomStandUpResponse.SUCCESS));
//						}
//					}
//	        	}
//	        }
			
			//6局下桌
			//人不满不下桌
			//没人排队不下桌
			//下桌时换人
			//排队补位
			sortBankerList();
			for (int i = 0 ; i < list_sitdown.size()  ; i++)
			{
				int tableNum = room.getTablePlayerNum();
				int tableLeftNum = room.getTablePlayers().length - tableNum;
				if(isDebug_万人场上庄列表)Tool.print_debug_level0("一局结束时，上庄列表中有:"+list_sitdown.size()+"人在排队,当前空位数:"+tableLeftNum);
				if (tableLeftNum > 0)// 有空位，有人排队
				{
					Player player_sitdown = list_sitdown.remove(i);// 上桌列表中移除，准备上桌
					int pos = room.manyGameSitDown(player_sitdown);//坐下	
					if(isDebug_万人场上庄列表)Tool.print_debug_level0("有空位，有人排队，当前座位人数:"+tableNum+","+player_sitdown.getNickName()+"，获得座位:"+pos);
					if (pos >= 0)
					{
						playerNum++;
						Tool.print_debug_level0("万人场有空位，有人排队，坐到位置,pos:"+pos+",当前玩家数,playerNum:"+playerNum);
						if(isDebug_万人场上庄列表)Tool.print_debug_level0("有空位，有人排队，坐到位置,pos:"+pos+",当前玩家数,playerNum:"+playerNum);
						broadcastStateToAll(new ManyPepolPush_playerSitdown(player_sitdown.getSimpleData()));
					}
				}
				else//人满,有人排队,踢下满6局的人
				{
					if(isDebug_万人场上庄列表)Tool.print_debug_level0("人满,有人排队,踢下满6局的人");
					Player[] players = room.getTablePlayers();
					for (int j = players.length - 1 ; j >= 0 ; j--)//所有人
					{
						Player item = players[j];
						if(isDebug_万人场上庄列表)Tool.print_debug_level0("人满,有人排队,踢下满6局的人,nickName:"+item.getNickName()+",局数:"+item.getManyPepolRoomRound());
						if (item.getManyPepolRoomRound() > 6)//找到打了6局以上的人
						{
							Tool.print_debug_level0("人满,有人排队,踢下满6局的人:"+item.getNickName()+",满6局，被踢!");
							int pos = item.getManyGamePos();
							broadcastStateToAll(new ManyPepolPush_playerStandUp(pos));//通知其他玩家，该位置玩家站起
							
							room.manyPepolRoomStandUp(item);//原玩家站起
							Player player_sitdown = list_sitdown.remove(i);//上桌列表中移除，准备上桌
							int code = room.mGameSitDown(player_sitdown,pos);//列表中玩家上桌
							if(code != 1000)
							{
								Tool.print_error("万人场排队上庄时,踢走满六局玩家:"+item.getNickName()+",替换该玩家位置时错误,pos:"+pos+",code:"+code);
							}
							broadcastStateToAll(new ManyPepolPush_playerSitdown(player_sitdown.getSimpleData()));
							break;
						}
					}
				}
			}
		}
	}

	private void addManyPepolPlayerRound()
	{
		if(isManyPepolRoom())
		{
			Player[] player = room.getTablePlayers();
			for (Player item : player)
			{
				if(item != null && !item.isWaitPlay())
				{
					item.addManyPepolRoomRound();
				}
			}
		}
	}

	private void runWaitPayBet()
	{
		long off_time = MyTimeUtil.getCurrTimeMM() - stateStartTime;
		if(off_time >= configClassicGame.getWaitPayBetTime() * MyTimeUtil.TIME_S)
		{
			changeToDeal();
		}
	}

	private void runWaitPlayerThink()
	{
		long off_time = MyTimeUtil.getCurrTimeMM() - stateStartTime;
		int waitPlayerThinkTime = configClassicGame.getWaitPlayerThink();
		waitPlayerThinkTime += isFirstWaitThink ? playerNum : 0;
		if(off_time >= waitPlayerThinkTime * MyTimeUtil.TIME_S)
		{
			//时间到，当前玩家弃牌
			thinkOver(playPos);
		}
	}

	private void thinkOver(int pos)
	{
		Player[] players = room.getTablePlayers();
		Player player = players[pos];
		
		if(player == null)
		{
			Tool.print_error("思考结束时座位玩家空，已经在别的地方移除,pos:"+pos);
			return;
		}
		if(isClassicDebug)Tool.print_debug_level0("弃牌玩家:"+player.getNickName());
		player.changeDie();
		if(isClassicDebug)Tool.print_debug_level0("思考结束，自动弃牌,players:"+players[playPos].getNickName());
		broadcastPlayerThinkOver(pos);
		
		 if(isManyPepolRoom() && !player.isOnline())
		 {
			 player.addOfflineRound();
			 Tool.print_debug_level0("万人场思考结束时离线，离线回合:"+player.getOfflineRound()+",nickname:"+player.getNickName());
			 if(player.getOfflineRound() > 2)
			 {
				 mGameLeaveRoom(player);
			 }
		 }
		
		 Tool.print_debug_level0("思考结束，自动弃牌,当前活动位置:"+playPos+",nickName:"+players[playPos].getNickName()+",弃牌玩家位置"+pos+",nickName:"+player.getNickName());
		
		 
		boolean isGameOver = checkGameOver();
		if(!isGameOver)
		{
			Tool.print_debug_level0("思考结束，自动弃牌，还有多个玩家=============,players:"+players[playPos].getSimpleData());
			 if(playPos == pos)
			 {
					Tool.print_debug_level0("思考结束，自动弃牌，还有多个玩家,行动玩家弃牌，切换到下个玩家");
					changeToWaitPlayerThink();
			 }
		}
	}
	
	public Player[] getTablePlayers()
	{
		return room.getTablePlayers();
	}

	private void broadcastPlayerThinkOver(int pos)
	{
		if(level == LEVEL_万人场)
		{
			broadcastStateToAll(new ManyPepolPush_playerDie(pos));
		}
		else
		{
			broadcastStateToClassic(new ClassicGamePush_playerDie(pos));
		}
	}

	private boolean checkGameOver()
	{
		Player winPlayer = getWinPlayer(false);
		if(winPlayer != null)//产生胜利玩家
		{
			setState(STATE_CALC_WIN);
			
			onGameOver(winPlayer);
			return true;
		}
		return false;
	}
	
	private void onGameOver(Player winPlayer)
	{
		//一局结束，赢家获取金币
		//推送相关消息
		//切换下一个回合，检查人数等
//		winPlayer.addCoins(getAllBet());
		if(level == LEVEL_万人场)
		{
			bankerPos = winPlayer.getManyGamePos();
		}else
		{
			bankerPos = winPlayer.getClassicGamePos();
		}
		bankerPlayer = winPlayer;
		Tool.print_debug_level0("比赛结束=============,winPlayer:"+winPlayer.getSimpleData());
		
		if(level == LEVEL_万人场)
		{
			ArrayList<GameRoundDataVO> list_data = calcManyPepolGameWinScore(winPlayer);
			 
			handlerJackPot(list_data);
			
			broadcastMprWiner(list_data,winPlayer.getUserId());
			ManyPepolPush_playerWin response = new ManyPepolPush_playerWin(list_data,room.getJackpot());
			broadcastStateToAll(response);
		}
		else
		{
			ArrayList<GameRoundDataVO> list_data = calcClassicGameWinScore(winPlayer);
			broadcastWiner(list_data,winPlayer.getUserId());
			ClassicGamePush_playerWin response = new ClassicGamePush_playerWin(list_data);
			broadcastStateToClassic(response);
		}
		
		Tool.print_debug_level0("比赛结束=============,切换到结算等待状态");
		changeToWaitCalc();
	}
	
	private void broadcastWiner(ArrayList<GameRoundDataVO> list_data, int winUserId)
	{
		GameRoundDataVO winDataVO = null;
		for (GameRoundDataVO gameRoundDataVO : list_data)
		{
			if(gameRoundDataVO.userId == winUserId)
			{
				winDataVO = gameRoundDataVO;
				break;
			}
		}
		
		if(winDataVO == null)
		{
			Tool.print_error("broadcastWiner时没有获取到胜利玩家，winUserId:"+winUserId+",list_data:"+list_data);
			return;
		}
		
		if(winDataVO.roundBet >= 10000000)
		{
			ClassicGamePush_bigWin classicGamePush_bigWin = new ClassicGamePush_bigWin();
			PlayerSimpleData winPlayerSimpleData = playerService.getPlayerSimpleData(winUserId);
			classicGamePush_bigWin.killRoomNoticVO = new KillRoomNoticVO(winUserId, winPlayerSimpleData.getNickName(), winDataVO.roundBet);
			playerService.sendToAll(classicGamePush_bigWin);
		}
	}

	private void broadcastMprWiner(ArrayList<GameRoundDataVO> list_data, int winUserId)
	{
		GameRoundDataVO winDataVO = null;
		for (GameRoundDataVO gameRoundDataVO : list_data)
		{
			if(gameRoundDataVO.userId == winUserId)
			{
				winDataVO = gameRoundDataVO;
				break;
			}
		}
		ManyPepolPush_bigWin manyPepolPush_bigWin = new ManyPepolPush_bigWin();
		PlayerSimpleData winPlayerSimpleData = playerService.getPlayerSimpleData(winUserId);
		manyPepolPush_bigWin.killRoomNoticVO = new KillRoomNoticVO(winUserId, winPlayerSimpleData.getNickName(), winDataVO.roundBet);
		playerService.sendToAll(manyPepolPush_bigWin);
	}

	private void handlerJackPot(ArrayList<GameRoundDataVO> list_data)
	{
		for (Player player : room.getTablePlayers())
		{
			if(player != null && !player.isWaitPlay())
			{
				PokerGroup group = pokerLogicService.getPokerType(player.getPokers());
				int jockpotType = getJockpotType(group);
				if(jockpotType > 0)
				{
					Tool.print_debug_level0("handlerJackPot万人场中奖池，jockpotType："+jockpotType+",手牌:"+player.getPokers());
					JackpotSort bankJackpotSort = new JackpotSort(player.getManyGamePos(),group.groupType,group.list_poker,group.groupValue,jockpotType);
					list_jackpotSorts.add(bankJackpotSort);
				}
			}
		}

		//此时所有奖池牌型已经获取
		if(list_jackpotSorts.size() > 0)
		{
			list_jackpotSorts.sort(MySortUtil.highToLowByIndex());
			Tool.print_debug_level0("万人场奖池算分，有奖池，排序后:"+list_jackpotSorts);
			
			JackpotSort jackpotSort = list_jackpotSorts.get(0);
			long jackpotScore = calcJackpotScore(jackpotSort);
			
			if(jackpotScore > room.getJackpot())
			{
				jackpotScore = room.getJackpot();
			}
			room.subJackPot(jackpotScore);
			
			Player jackpotPlayer = room.getTablePlayers()[jackpotSort.pos];
			Tool.print_debug_level0("万人场奖池算分，handlerJackPot----> jackpotScore:"+jackpotScore+",pos:"+jackpotSort.pos+",userId:"+jackpotPlayer.getUserId());
			for (int i = 0 ; i < list_data.size() ; i++)
			{
				GameRoundDataVO gameRoundDataVO = list_data.get(i);
				if(gameRoundDataVO.userId == jackpotPlayer.getUserId())
				{
					//中奖池的人得分增加
					gameRoundDataVO.jackpotWin = jackpotScore;
					gameRoundDataVO.roundBet += jackpotScore;
					Tool.print_coins(jackpotPlayer.getNickName(),jackpotScore,"万人场奖池赢分",jackpotPlayer.getCoins());
					jackpotPlayer.addCoins(jackpotScore);
					gameRoundDataVO.newCoins += jackpotScore;
					break;
				}
			}
			
			lastJackpotData.setData(room.getJackpot(), jackpotScore, jackpotPlayer.getNickName(), jackpotPlayer.getRoleId(), jackpotPlayer.getHeadIconUrl(), jackpotSort.type, MyTimeUtil.getCurrTimeMM(),jackpotSort.list_pokers);
		}else
			lastJackpotData.setAllJackpot(room.getJackpot());
	}
	
	private int calcJackpotScore(JackpotSort jackpotSort)
	{
		int jackpotNum = 0;
		switch (jackpotSort.jackpotType)
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
		if(group==null)
			return 0;
		
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

//	private void calcOtherPlayerScore()
//	{
//		map_directionPlayer
//	}

	ArrayList<GameRoundDataVO> list_data = new ArrayList<GameRoundDataVO>();
	private ArrayList<GameRoundDataVO> calcManyPepolGameWinScore(Player winPlayer)
	{
		int baseScore = 100000;
		list_data.clear();
		
 		ArrayList<PokerVO> list_Poker = winPlayer.getPokers();//
 		PokerGroup group = pokerLogicService.getPokerType(list_Poker);
// 		System.out.println("pokerLogicService:"+pokerLogicService+",group:"+group+",list_Poker:"+list_Poker);
 		
 		int scoreRate = 1;
 		if(group == null)
 		{
 			Tool.print_error("calcManyPepolGameWinScore时赢家手牌空:"+winPlayer.getNickName()+",list_Poker:"+list_Poker);
 			list_Poker = room.getRandomPoker(3);
 			group = pokerLogicService.getPokerType(list_Poker);
 		}
 		else
 		{
 			scoreRate = pokerLogicService.getBaseScore(group.groupType);//赢家的手牌倍数
 		}
 		
// 		if(isAllIn())
// 		{
// 			scoreRate = 1;
// 		}
 		
 		if(isCalcDebug)Tool.print_debug_level0("万人场结算,scoreRate:"+scoreRate);
 		for (Player tablePlayer : room.getTablePlayers())
		{
 			if(tablePlayer == null || tablePlayer.getManyGamePos() == winPlayer.getManyGamePos() || tablePlayer.isWaitPlay())
 			{
 				continue;
 			}
 			
 			long cost = (tablePlayer.getRoundBet()) * scoreRate;
 			long tablePlayerPay = cost + baseScore;
 			if(isCalcDebug)Tool.print_debug_level0("万人场结算,cost:"+cost+",tablePlayerPay:"+tablePlayerPay+",tablePlayer.getCoins():"+tablePlayer.getCoins());
 			if(tablePlayer.getCoins() < cost)//输家金币不足，取最大金币
 			{
 				tablePlayerPay = tablePlayer.getCoins();
 				if(isCalcDebug)Tool.print_debug_level0("万人场结算,输家金币不足，使用输家金币数:"+tablePlayerPay);
 			}
 			Tool.print_subCoins(tablePlayer.getNickName(),tablePlayerPay,"万人结算扣钱",tablePlayer.getCoins());
 			tablePlayer.subCoinse(tablePlayerPay);
 			tablePlayer.addRoundCalcBet(-tablePlayerPay);
 			
 			int rewardPool = (int) (tablePlayerPay * 0.02f);
 			if(isCalcDebug)Tool.print_debug_level0("万人场结算,奖池增加:"+rewardPool+",tablePlayerPay:"+tablePlayerPay);
 			tablePlayerPay = (long) (tablePlayerPay * 0.94f);
 			if(isCalcDebug)Tool.print_debug_level0("万人场结算,赢家赢金币,tablePlayerPay:"+tablePlayerPay);
 			
 			Tool.print_coins(winPlayer.getNickName(),tablePlayerPay,"万人场赢分",winPlayer.getCoins());
 			winPlayer.addCoins(tablePlayerPay);
 			winPlayer.addRoundCalcBet(tablePlayerPay);
 			addRewardPool(rewardPool);
 			
 			GameRoundDataVO manyPepolRoomRoundDataVO = new GameRoundDataVO();
 			manyPepolRoomRoundDataVO.pos = tablePlayer.getManyGamePos();
 			manyPepolRoomRoundDataVO.list_handPoker = tablePlayer.getPokers();
 			manyPepolRoomRoundDataVO.roundBet = tablePlayer.getRoundCalcBet();
 			PokerGroup group1 = pokerLogicService.getPokerType(tablePlayer.getPokers());
 			if(group1 != null)
 			{
 				manyPepolRoomRoundDataVO.pokerType = group1.groupType;
 			}
 			
 			manyPepolRoomRoundDataVO.newCoins = tablePlayer.getCoins();
 			manyPepolRoomRoundDataVO.userId = tablePlayer.getUserId();
 			list_data.add(manyPepolRoomRoundDataVO);

 			tablePlayer.addFailureNum();
		}
 		
// 		if(isCalcDebug)Tool.print_debug_level0("万人场结算,赢家本局自己下注金币收回::"+winPlayer.getRoundBet());
// 		winPlayer.addCoins(winPlayer.getRoundBet());
 		winPlayer.addRoundCalcBet(winPlayer.getRoundBet() + 100000);
 		
 		GameRoundDataVO manyPepolRoomRoundDataVO = new GameRoundDataVO();
		manyPepolRoomRoundDataVO.pos = winPlayer.getManyGamePos();
		manyPepolRoomRoundDataVO.list_handPoker = winPlayer.getPokers();
		manyPepolRoomRoundDataVO.roundBet = winPlayer.getRoundCalcBet();
		manyPepolRoomRoundDataVO.pokerType = group.groupType;
		manyPepolRoomRoundDataVO.newCoins = winPlayer.getCoins();
		manyPepolRoomRoundDataVO.userId = winPlayer.getUserId();
		list_data.add(manyPepolRoomRoundDataVO);
		winPlayer.addVictoryNum();
		
		rankService.addWinConisNum(winPlayer.getUserId(), winPlayer.getRoundCalcBet(),winPlayer.getNickName(),winPlayer.getLevel());
		return list_data;
	}
	
	private ArrayList<GameRoundDataVO> calcClassicGameWinScore(Player winPlayer)
	{
		list_data.clear();
		
		ArrayList<PokerVO> list_Poker = winPlayer.getPokers();
 		PokerGroup group = pokerLogicService.getPokerType(list_Poker);
		//处理有玩家逃跑时，房间中只剩一个本局有效玩家的情况
 		int normalPlayerNum = getRoundNormalPlayer();
 		Tool.print_debug_level0("胜利结算时，本局有效玩家数:"+normalPlayerNum);
		if(normalPlayerNum == 1)
		{
			GameRoundDataVO manyPepolRoomRoundDataVO = new GameRoundDataVO();
			manyPepolRoomRoundDataVO.pos = winPlayer.getClassicGamePos();
			manyPepolRoomRoundDataVO.list_handPoker = winPlayer.getPokers();
			manyPepolRoomRoundDataVO.roundBet = getAllBet();
			manyPepolRoomRoundDataVO.pokerType = group.groupType;
			manyPepolRoomRoundDataVO.userId = winPlayer.getUserId();
			list_data.add(manyPepolRoomRoundDataVO);
			
			winPlayer.addVictoryNum();
			return list_data;
		}
 		
// 		int scoreRate = pokerLogicService.getBaseScore(group.groupType);
 		
 		for (Player tablePlayer : room.getTablePlayers())
		{
 			if(tablePlayer == null || tablePlayer.getClassicGamePos() == winPlayer.getClassicGamePos() || tablePlayer.isIdle() || tablePlayer.isWaitPlay())
 			{
 				continue;
 			}
  			long tablePlayerPay = tablePlayer.getRoundBet();
  			
 			tablePlayer.addRoundCalcBet(-tablePlayerPay);
 			
 			tablePlayerPay = (long) (tablePlayerPay * 0.94f);
 			
 			Tool.print_coins(winPlayer.getNickName(),tablePlayerPay,"经典场赢分(注),level:"+level,winPlayer.getCoins());
 			winPlayer.addCoins(tablePlayerPay);
 			winPlayer.addRoundCalcBet(tablePlayerPay);
 			
 			GameRoundDataVO manyPepolRoomRoundDataVO = new GameRoundDataVO();
 			manyPepolRoomRoundDataVO.pos = tablePlayer.getClassicGamePos();
 			if(tablePlayer.isComparerPoker())
 			{
 				manyPepolRoomRoundDataVO.list_handPoker = tablePlayer.getPokers();
 			}
 			manyPepolRoomRoundDataVO.roundBet = tablePlayer.getRoundCalcBet();
 			PokerGroup group1 = pokerLogicService.getPokerType(tablePlayer.getPokers());
 			if(group1 == null)
 			{
 				Tool.print_error("calcClassicGameWinScore计算结果时错误，group1空"+(tablePlayer != null ? tablePlayer.getPokers() : "tablePlayer空"));
 			}
 			else
 			{
 				manyPepolRoomRoundDataVO.pokerType = group1.groupType;
 			}
 			
 			manyPepolRoomRoundDataVO.userId = tablePlayer.getUserId();
 			list_data.add(manyPepolRoomRoundDataVO);
 			
 			tablePlayer.addVictoryNum();
		}
 		
 		Tool.print_coins(winPlayer.getNickName(),winPlayer.getRoundBet(),"经典场赢分,level:"+level,winPlayer.getCoins());
 		winPlayer.addCoins(winPlayer.getRoundBet());
 		winPlayer.addRoundCalcBet(winPlayer.getRoundBet());

 		GameRoundDataVO manyPepolRoomRoundDataVO = new GameRoundDataVO();
		manyPepolRoomRoundDataVO.pos = winPlayer.getClassicGamePos();
		manyPepolRoomRoundDataVO.list_handPoker = winPlayer.getPokers();
		manyPepolRoomRoundDataVO.roundBet = winPlayer.getRoundCalcBet();
		manyPepolRoomRoundDataVO.pokerType = group.groupType;
		manyPepolRoomRoundDataVO.userId = winPlayer.getUserId();
		list_data.add(manyPepolRoomRoundDataVO);
		winPlayer.addVictoryNum();
		
		rankService.addWinConisNum(winPlayer.getUserId(), winPlayer.getRoundCalcBet(),winPlayer.getNickName(),winPlayer.getLevel());
		return list_data;
	}

	private void addRewardPool(int rewardPool)
	{
		room.addJackpot(rewardPool);
	}
	
	private int getRoundNormalPlayer()
	{
		int num = 0;
		Collection<Player> allPlayers = room.getAllPlayer();
		if(allPlayers.size() < 2)
		{
			Tool.print_debug_level0("getRoundNormalPlayer--->allPlayers.size():"+allPlayers.size());
			return allPlayers.size();
		}
		for (Player item : room.getAllPlayer())
		{
			if(!item.isIdle())
			{
				num++;
			}
		}
		
		return num;
	}

	/**
	 * 清理座位上等待离开的玩家，只有万人场会有此逻辑
	 */
	private void checkLeavePlayer()
	{
		for (int userId : room.getLeavePlayers())
		{
			Player player = room.getPlayer(userId);
			Tool.print_debug_level0("万人场移除准备离开人员:"+player.getNickName());
			standUp(player);
			
//			player.clearManyPepolGame();
//			room.removePlayer(userId);
// 			player.clearRoom();
//			player.offlineClearAllRoom();
		}
		room.clearLeavePlayers();
	}

	private void changeToIdle()
	{
		stateStartTime = MyTimeUtil.getCurrTimeMM();
		if(isDebug)Tool.print_debug_level0("设置状态到待机");
		this.state = STATE_IDLE;
		clearRoomData();
	}

	private Player getWinPlayer(boolean isBug)
	{
		Player winPlayer = null;
		int alivePlayerNum = 0;
		Player[] players = room.getTablePlayers();
		synchronized (players)
		{
			for (Player player : players)
			{
				if(player != null)
				{
					if(isBug)
					{
						Tool.print_debug_level0("获取胜利玩家,player:"+player);
					}
				}
				
				if(player != null)
				{
					Tool.print_debug_level0("获取胜利玩家,getWinPlayer,player:"+player.getState()+",nickName:"+player.getNickName()+",isNormal:"+player.isNormal());
				}
				
				if(player != null && player.isNormal())
				{
					alivePlayerNum++;
					winPlayer = player;
				}
			}
			
			Tool.print_debug_level0("获取胜利玩家,getWinPlayer,alivePlayerNum:"+alivePlayerNum);
			if(alivePlayerNum == 1)
			{
				return winPlayer;
			}
		}
		return null;
	}

	public int getAlivePlayerNum()
	{
		int alivePlayerNum = 0;
		Player[] players = room.getTablePlayers();
		for (Player player : players)
		{
			if(player != null && !player.isDie() && !player.isIdle() &&!player.isWaitPlay())
			{
				alivePlayerNum++;
			}
		}
		return alivePlayerNum;
	}

	/**
	 * 切换到下个玩家行动，并且等待该玩家思考
	 */
	private void changeToWaitPlayerThink()
	{
		stateStartTime = MyTimeUtil.getCurrTimeMM();
		setState(STATE_PLAYER_THINK);
		
		Player[] players = room.getTablePlayers();
		synchronized (players)
		{
			int nowPlayPos = playPos;//暂存当前行动玩家
			Player nextPlayer = null;
			do
			{
				int nextPos = nextPos();//获取下家pos
				nextPlayer = players[nextPos];
				if(nextPlayer != null)
				{
					if(isDebug)Tool.print_debug_level0("changeToWaitPlayerThink--->nowPlayPos:"+nowPlayPos+",nextPos:"+nextPos+",isDie:"+nextPlayer.isDie()+",isIdle:"+nextPlayer.isIdle()
					+",isWaitPlay:"+nextPlayer.isWaitPlay());
				}
				else
				{
					if(isDebug)Tool.print_debug_level0("changeToWaitPlayerThink--->nowPlayPos:"+nowPlayPos+",nextPos:"+nextPos+",位置空");
				}
				if(isDebug)
				{
					if(nextPlayer != null)
					{
						Tool.print_debug_level0("state:"+nextPlayer.getState());
					}
				}
				if(nextPos == bankerPos)//轮到庄家位置，回合数+1
				{
					addRound();
				}
				
				if(nowPlayPos ==  nextPos)//回到起始pos，玩家不够了
				{
					Player winPlayer = getWinPlayer(true);
					Tool.print_debug_level0("changeToWaitPlayerThink死循环 ----->changeToIdle,winPlayer:"+winPlayer);
					if(winPlayer != null)
					{
						Tool.print_error("changeToWaitPlayerThink时死循环,切换到准备开始状态,winPlayer:"+winPlayer.getSimpleData());
					}
					changeToIdle();
					return;
				}
			}
			while (nextPlayer == null || nextPlayer.isDie() || nextPlayer.isIdle() || nextPlayer.isWaitPlay());
			
			if(level == LEVEL_万人场)
			{
				broadcastPlayerThink(nextPlayer.getManyGamePos(),getNowBet(),nextPlayer.isCheckPoker());
			}else
			{
				broadcastPlayerThink(nextPlayer.getClassicGamePos(),getNowBet(),nextPlayer.isCheckPoker());
			}
		}
	}
	
	private void waitPlayerAction(Player player)
	{
		stateStartTime = MyTimeUtil.getCurrTimeMM();
		setState(STATE_PLAYER_THINK);
		
		if(level == LEVEL_万人场)
		{
			broadcastPlayerThink(player.getManyGamePos(),getNowBet(),player.isCheckPoker());
		}else
		{
			broadcastPlayerThink(player.getClassicGamePos(),getNowBet(),player.isCheckPoker());
		}
	}
	
	private void broadcastPlayerThink(int pos,int nowBet,boolean isCheckPoker)
	{//int pos,int nowBet,boolean isCheckPoker
		int alivePlayerNum = getAlivePlayerNum();

		/**0:不显示全压  1:显示全压  2:强制全压*/
		int allinState = 0;
		if(alivePlayerNum == 2 && round > 2)
		{
			if(isDebug)Tool.print_debug_level0("可以全压，此时是否全压过:"+isAllIn());
			if(isAllIn())
			{
				allinState = 2;
			}
			else
			{
				allinState = 1;
			}
		}
		if(level == LEVEL_万人场)
		{
			if(isFirstPlayer)
			{
				isFirstPlayer = false;
				allinState = 3;
			}
			
			lastPlayAllInState = allinState;
			broadcastStateToAll(new ManyPepolPush_playerAction(pos,nowBet,isCheckPoker,round,allinState));
		}
		else
		{
			broadcastStateToClassic(new ClassicGamePush_playerAction(pos,nowBet,isCheckPoker,round,allinState));
		}
	}

	private int nextPos()
	{
		playPos ++;
		if(playPos > room.getTablePlayers().length - 1)
		{
			playPos = 0;
		}
		return playPos;
	}

	private Player selectBanker()
	{
		if(bankerPos == -1)
		{
			Player[] table_players = room.getTablePlayers();
			Player banker = null;
			if(MyUtil.getRandomBoolean())
			{
				for (int i = 0 ; i < table_players.length ; i++)
				{
					banker = table_players[i];
					if(banker != null && banker.isNormal())
					{
						break;
					}
				}
			}
			else
			{
				for (int i = table_players.length - 1 ; i >= 0 ; i--)
				{
					banker = table_players[i];
					if(banker != null && banker.isNormal())
					{
						break;
					}
				}
			}
			bankerPlayer = banker;
			
			if(level == LEVEL_万人场)
			{
				bankerPos = banker.getManyGamePos();
			}else
			{
				bankerPos = banker.getClassicGamePos();
			}
		}
		return bankerPlayer;
	}
	
	private void runWaitStart()
	{
		long off_time = MyTimeUtil.getCurrTimeMM() - stateStartTime;
		if(off_time >= MyTimeUtil.TIME_S * configClassicGame.getWaitStartTime())//新进玩家后的等待时间
		{
			if(level == LEVEL_万人场)
			{
				Player[] players = room.getTablePlayers();
				int tablePlayerNum = 0;
				if(players != null)
				{
					for (Player player : players)
					{
						if(player != null)
						{
							tablePlayerNum++;
						}
					}
				}
				
				if(tablePlayerNum >= 2)
				{
					bankerPos = -1;
					changeToStartGame();
				}
			}
			else
			{
				checkStartGame();
			}
		}
		else
		{
//			Tool.print_debug_level0("切换到发牌倒计时，剩余:"+(MyTimeUtil.getCurrTimeMM() - stateStartTime)/1000);
		}
	}
	


	/**
	 * 
	 */
	private void checkStartGame()
	{
 		//Tool.print_debug_level0("room.getAllPlayer().size():"+room.getAllPlayer().size());
		if(room.getAllPlayer() != null && room.getAllPlayer().size() >= 2)
		{
			bankerPos = -1;
			changeToStartGame();
		}
 		
//		if(playerNum >= 2)
//		{
//			
//		}
	}

	private void changeToStartGame()
	{
		if(isDebug)
		{
			for (Player item : room.getTablePlayers())
			{
				if(item != null)
				{
					Tool.print_debug_level0("当前比赛玩家:"+item.getSimpleData());
				}
			}
		}
		
		if(level == LEVEL_万人场)
		{
 			changeToWaitBet();
		}
		else
		{
			changeToDeal();
		}
//		changeToDeal();
	}

	private void changeToDeal()
	{
		if(getState() == STATE_DEAL)
			return;
		
		setState(STATE_DEAL);
		
		initClassicGameData();
		
		//随机选择庄家
 		selectBanker();
		
		playPos = getFirstPlayerPos();
		if(isDebug)Tool.print_debug_level0("发牌，当前庄家位置 ："+bankerPos+",第一个动作玩家:"+playPos);
		
		//发牌
		dealPokers();
		
		//出底注
		int cost = getLevelCostBet();
		
		if(!allPayBet(cost))
		{
			boolean isGameOver = checkGameOver();
			
			if(isGameOver)
				return;
		}
		
		//广播给桌上玩家
		broadcastDeal(cost,playPos);
		
		if(level == LEVEL_万人场)
		{
			setNowBet(50000);
			addAllBet(150000);
			addAllBet(150000);
		}
		else
		{
			setNowBet(cost);
			addAllBet(cost);
			addAllBet(cost);
			Player[] players = room.getTablePlayers();
			for (int j = 0 ; j < players.length ; j++)
			{
				Player player = players[j];
				if(player != null && !player.isWaitPlay())
				{
					Tool.print_subCoins(player.getNickName(),cost,"经典场扣底注",player.getCoins());
					player.subCoinse(cost);
				}
			}
		}
	}
	private int getFirstPlayerPos()
	{
		Player[] table_players = room.getTablePlayers();
		int num = 0;
		for (int i = 0 ; i < table_players.length ; i++)
		{
			if(table_players[i] != null && !table_players[i].isWaitPlay())
			{
				num++;
			}
		}
		Tool.print_debug_level0("获取首出玩家,玩家数:"+num+",table_players.len:"+table_players.length+",bankerPos:"+bankerPos);
		
		int firstplayPos = bankerPos;
		 
		for (int i = 0 ; i < table_players.length ; i++)
		{
			firstplayPos--;
			if(firstplayPos < 0)
			{
				firstplayPos = table_players.length - 1;
			}
			if(isDebug)Tool.print_debug_level0("获取首出玩家,firstplayPos:"+firstplayPos);
			if(table_players[firstplayPos] != null)
			{
				return firstplayPos;
			}
		}
		Tool.print_error("获取首出玩家时错误，table_players："+Arrays.toString(table_players));
		return 0;
	}

	private void changeToWaitBet()
	{
		if(getState() == STATE_WAIT_PAY_BET)
			return;
		
		stateStartTime = MyTimeUtil.getCurrTimeMM();
		setState(STATE_WAIT_PAY_BET);
		broadcastStateToAll(new ManyPepolPush_waiPayBet());
	}

	private void broadcastDeal(int cost,int playPos)
	{
		if(level == LEVEL_万人场)
		{
			broadcastStateToAll(new ManyPepolPush_dealPoker(bankerPlayer.getManyGamePos(),cost,playPos));
		}
		else
		{
			broadcastStateToClassic(new ClassicGamePush_dealPoker(bankerPlayer.getClassicGamePos(),cost,playPos));
		}
	}

	private void initClassicGameData()
	{
		list_jackpotSorts.clear();
		isFirstWaitThink = false;
		
		round = 0;
		allBet = 0;
		nowBet = 0;
		initPlayerState();
		clearAllIn();
		readyNum = 0;
		list_allBet.clear();
		set_getBox.clear();
		isFirstPlayer = true;
	}
	
	public void setGetBoxUserId(int userId)
	{
		set_getBox.add(userId);
	}
	
	public boolean isGetBoxIdExist(int userId)
	{
		return set_getBox.contains(userId);
	}
	
	private void clearRoomData()
	{
		list_allBet.clear();
		readyNum = 0;
		round = 0;
		allBet = 0;
		nowBet = 0;
		
		Tool.print_debug_level0("clearRoomData ----> room.getAllPlayer.size:"+room.getAllPlayer().size());
		for (Player iterable_element : room.getTablePlayers())
		{
			if(iterable_element != null)
			{
				Tool.print_debug_level0("clearRoomData:"+iterable_element.getSimpleData());
				iterable_element.clearRoundBet();
				iterable_element.clearPoker();
				iterable_element.clearRoundBet();
			}
		}
	}

	private void initPlayerState()
	{
		for (Player iterable_element : room.getTablePlayers())
		{
//			iterable_element.clearRoundBet();
//			iterable_element.clearPoker();
			if(iterable_element != null)
			{
				iterable_element.initState();
			}
		}
	}
	
	private void dealPokers()
	{
		room.initRoomPoker(pokerLogicService.getInitPokersWithOutJoker());
		
		//上庄单牌率	上庄对子率	上庄大牌率	豹子AAA率	豹子率	顺金率
		GameWanren gameWanren = manyPepolService.getWanrenConfig();
		
		Player[] players = room.getTablePlayers();
		for (int i = 0 ; i < players.length ; i++)
		{
			Player player = players[i];
			if(player != null && player.isNormal())
			{
				ArrayList<PokerVO> list_pokers = null;
				if(isManyPepolRoom)
				{
					list_pokers = getWanrenRatePoker(i,gameWanren);
					Tool.print_debug_level0("万人发牌:玩家名字:"+player.getNickName()+",扑克:"+list_pokers);
				}
				else
				{
					list_pokers = room.getRandomPoker(3);
					Tool.print_debug_level0("经典发牌:玩家名字:"+player.getNickName()+",扑克:"+list_pokers);
				}
				
				 player.addAllPoker(list_pokers);
			}
		}
	}


	private  ArrayList<PokerVO> getWanrenRatePoker(int i,GameWanren gameWanren)
	{
		int rate = MyUtil.getRandom(1000);
		
		 int leopardARate = (int) (gameWanren.getLeopard_a_chance().floatValue() * 10);
		 int leopardRate = (int) (gameWanren.getLeopard_chance().floatValue() * 10);
		 int flushRate = (int) (gameWanren.getShun_jin().floatValue() * 10);
		 int bigPokerRate = (int) (gameWanren.getUp_banker_big_chance().floatValue() * 10);
		 int doubleRate = (int) (gameWanren.getUp_banker_double_chance().floatValue() * 10);
		 int singleRate = (int) (gameWanren.getUp_banker_single_chance().floatValue() * 10);
		 
		 if(isDebugDeal)Tool.print_debug_level0("万人场随机数:"+rate+",leopardARate:"+leopardARate+",leopardRate:"+leopardRate+",flushRate:"+flushRate+",bigPokerRate:"+bigPokerRate);
		 
		 ArrayList<PokerVO> list_poker = null;
		 
//		if(i==0)
//		{
//			 list_poker = room.getAAA();
//			 if(list_poker == null)
//			 {
//				 list_poker = room.getRandomPoker(3);
//			 }
//			 return list_poker;
//		}
			
		 if(rate < leopardARate)
		 {
			 list_poker = room.getAAA();
			 if(list_poker == null)
			 {
				 list_poker = room.getRandomPoker(3);
			 }
		 }
		 else if(rate < leopardRate)
		 {
			 list_poker  = room.getPokerType(0);
		 }
		 else if(rate < flushRate)
		 {
			 list_poker  = room.getPokerType(1);
		 }
		 else if(rate < bigPokerRate)
		 {
			 //根据牌型随机牌 0：豹子 1：顺金 2：金花 3：顺子 4：对子 5：单牌
			 boolean randomType = MyUtil.getRandomBoolean();
			 if(randomType)
			 {
				 list_poker  = room.getPokerType(2);
			 }
			 else
			 {
				 list_poker  = room.getPokerType(3);
			 }
		 }
		 else if(rate < doubleRate)
		 {
			 list_poker  = room.getPokerType(4);
		 }
		 else if(rate < singleRate)
		 {
			 list_poker  = room.getPokerType(5);
		 }
		 else
		 {
			 list_poker = room.getRandomPoker(3);
		 }
		 return list_poker;
	}

	private int getPokerType()
	{
		ConfigManypepolroompoker configManypepolroompoker = configService.getConfigManypepolroompoker();
		int rate = MyUtil.getRandom(1000);
		int pokeyType = 5;
		if (rate < configManypepolroompoker.getLeopard())//5
		{
			pokeyType = 0;
		}
		else if (rate < configManypepolroompoker.getSamecolorflush())//10
		{
			pokeyType = 1;

		}
		else if (rate < configManypepolroompoker.getSamecolorpoker())
		{
			pokeyType = 2;

		}
		else if (rate < configManypepolroompoker.getFlush())
		{
			pokeyType = 3;

		}
		else if (rate < configManypepolroompoker.getPair())
		{
			pokeyType = 4;

		}
		else if (rate < configManypepolroompoker.getOnepoker())
		{
			pokeyType = 5;
		}
		return pokeyType;
	}
	 
	private int getLevelCostBet()
	{
 		switch (level)
		{
			case LEVEL_初级场:
				return 100;
				
			case LEVEL_中级场:
				return 500;
				
			case LEVEL_高级场:
				return 1000;
				
			case LEVEL_土豪场:
				return 20000;

			case LEVEL_万人场:
				return 150000;
				
			default:
				return 100;
		}
	}

	private boolean allPayBet(int num)
	{
		boolean isAllPay = true;
		Player[] players = room.getTablePlayers();
		for (int j = 0 ; j < players.length ; j++)
		{
			Player player = players[j];
			if(player != null && !player.isWaitPlay())
			{
				if(player.getCoins() >= num)
				{
					if(level == LEVEL_万人场)
					{
						if(isAddBetDebug)Tool.print_debug_level0(MsgTypeEnum.LOG_玩家回合增注,"玩家:"+player.getNickName()+",增注:"+50000);
						player.addRoundBet(50000);
					}
					else
					{
						if(isAddBetDebug)Tool.print_debug_level0(MsgTypeEnum.LOG_玩家回合增注,"玩家:"+player.getNickName()+",增注:"+num);
						player.addRoundBet(num);
					}
					
				}
				else
				{
					standUp(player);
					isAllPay = false;
				}
			}
		}
		return isAllPay;
	}

	public int getFreeTableNum()
	{
		return room.getFreeTableNum();
	}
	
	private void broadcastStateToClassic(Response response)
	{
		Player[] players = room.getTablePlayers();
		for (Player player : players)
		{
			if(player != null)
			{
				player.sendResponse(response);
			}
		}
	}
	
	private void broadcastStateToAll(Response response)
	{
		for (Player player : room.getAllPlayer())
		{
			if(player != null)
			{
				player.sendResponse(response);
			}
		}
	}
	
	private void broadcastStateNotPosToAll(Response response,int pos)
	{
		for (Player player : room.getAllPlayer())
		{
			if(level == LEVEL_万人场)
			{
				if(player != null && player.getManyGamePos() != pos)
				{
					player.sendResponse(response);
				}
			}else
			{
				if(player != null && player.getClassicGamePos() != pos)
				{
					player.sendResponse(response);
				}
			}
		}
	}

	public void initGame(int num, int level, ConfigClassicGame configClassicGame,boolean isManyPepolRoom)
	{
		room = new Room(num);
		this.configClassicGame = configClassicGame;
		Tool.print_debug_level0("initGame ----->changeToIdle");
		changeToIdle();
		this.level = level;
		
		list_sitdown = new ArrayList<Player>();
		this.isManyPepolRoom = isManyPepolRoom;
//		if(level == LEVEL_万人场)
//		{
//			map_directionPlayer = new HashMap<Integer, KillRoomDirectionPlayer>();
//		}
	}
	
	public boolean isExistsSitDownList(int userId)
	{
		if(list_sitdown == null || list_sitdown.size() <= 0)
		{
			return false;
		}
		for (int i = 0 ; i < list_sitdown.size() ; i++)
		{
			if(list_sitdown.get(i).getUserId() == userId)
			{
				return true;
			}
		}
		return false;
	}

	public ArrayList<PlayerSimpleData> getAllPlayerSimpleData()
	{
		return getAllPlayerSimpleData(false);
	}
	
	public ArrayList<PlayerSimpleData> getAllPlayerSimpleData(boolean isManyPepolRoom)
	{
		return room.getTableSimplePlayers(isManyPepolRoom);
	}

	public int getBankerPos()
	{
		if(bankerPlayer == null)
		{
			return -1;
		}
		
		if(level == LEVEL_万人场)
		{
			return bankerPlayer.getManyGamePos();
		}else
			return bankerPlayer.getClassicGamePos();
	}

	public void checkPoker(Player player)
	{
		player.changeChecked();
		if(level == LEVEL_万人场)
		{
			broadcastStateNotPosToAll(new ManyPepolPush_checkPoker(player.getManyGamePos()),player.getManyGamePos());	
			if(playPos == player.getManyGamePos())
			{
				waitPlayerAction(player);
			}
		}
		else
		{
			broadcastStateNotPosToClassic(new ClassicGamePush_checkPoker(player.getClassicGamePos()),player.getClassicGamePos());
			if(playPos == player.getClassicGamePos())
			{
				waitPlayerAction(player);
			}
		}
	}

	public void followBet(Player player,int num)
	{
		addAllBet(num);
		Response response = null;
		if(level == LEVEL_万人场)
		{
			response = new ManyPepolPush_playerFollow(player.getManyGamePos(),num);
			broadcastStateNotPosToAll(response,player.getManyGamePos());
		}
		else
		{
			response = new ClassicGamePush_playerFollow(player.getClassicGamePos(),num);
			broadcastStateNotPosToClassic(response,player.getClassicGamePos());
		}
	
		changeToWaitPlayerThink();
	}

	private void broadcastStateNotPosToClassic(Response response, int pos)
	{
		Player[] players = room.getTablePlayers();
		for (Player player : players)
		{
			if(player != null && player.getClassicGamePos() != pos)
			{
				player.sendResponse(response);
			}
		}
	}

	public void raiseBet(Player player, int cost, int baseRate)
	{
		setNowBet(cost);
		addAllBet(cost * baseRate);
		Response response = null;
		if(level == LEVEL_万人场)
		{
			response = new ManyPepolPush_playerRaiseBet(player.getManyGamePos(),cost * baseRate);
			broadcastStateNotPosToAll(response,player.getManyGamePos());
		}
		else
		{
			response = new ClassicGamePush_playerRaiseBet(player.getClassicGamePos(),cost * baseRate);
			broadcastStateNotPosToClassic(response,player.getClassicGamePos());
		}
		
		changeToWaitPlayerThink();
	}
	
	public void allIn(Player player, long allInPay)
	{
		int num = (int) allInPay;
		setNowBet(num);
		addAllBet(num);
		Response response = null;
		if(level == LEVEL_万人场)
		{
			response = new ManyPepolPush_allin(player.getManyGamePos(),num);
			broadcastStateNotPosToAll(response,player.getManyGamePos());
		}
		else
		{
			response = new ClassicGamePush_allin(player.getClassicGamePos(),num);
			broadcastStateNotPosToClassic(response,player.getClassicGamePos());
		}
		
		if(!isAllIn())//发起全压
		{
			Tool.print_debug_level0("发起全压切换动作======,全压人player:"+player.getSimpleData());
			changeToWaitPlayerThink();
		}
		else
		{
			Tool.print_debug_level0("跟压======,player:"+player.getSimpleData());
		}
	}
	
	private void addAllBet(int num)
	{
		allBet += num;
		list_allBet.add(num);
	}

	/**
	 * 坐到位子上
	 * @param player
	 * @return -1:错误 否则座位号
	 */
	public int classicGameSitDown(Player player,int type)
	{
		if(room.getPlayer(player.getUserId()) != null)
		{
			Tool.print_debug_level0("classicGameSitDown,player:"+player.getSimpleData()+",玩家已存在");
			return -1;
		}
		
		int pos = room.classicGameSitDown(player);
		Tool.print_debug_level0("classicGameSitDown,player:"+player.getSimpleData()+",pos:"+pos);
		if(pos >= 0)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_进入新手场,"进入成功,type:"+type+"，player:"+player.getSimpleData());
			player.setClassicGame(this);
			 
			ArrayList<PlayerSimpleData> list_allPlayer = getAllPlayerSimpleData();
			EnterBeginnerResponse response = new EnterBeginnerResponse(EnterBeginnerResponse.SUCCESS);
			response.setData(getState(),list_allPlayer,getBankerPos(),pos,getRoundNum(),getListAllBet(),playPos,getTimeLeft(),type);
			player.sendResponse(response);
			
			playerNum++;
			Tool.print_debug_level0("经典场classicGameSitDown,player:"+player.getSimpleData()+",当前玩家数playerNum:"+playerNum);
			
			broadcastStateNotPosToClassic(new ClassicGamePush_otherPlayerEnter(player.getSimpleData()),pos);
			
			if( !runing() && playerNum>=2)
			{
				changeToWaitStart();
			}
		}
		return pos;
	}
	
	public int classicGameChangeTableSitDown(Player player,int type)
	{
		if(room.getPlayer(player.getUserId()) != null)
		{
			Tool.print_debug_level0("classicGameChangeTableSitDown,player:"+player.getSimpleData()+",玩家已存在");
			return -1;
		}
		
		int pos = room.classicGameSitDown(player);
		Tool.print_debug_level0("classicGameChangeTableSitDown,player:"+player.getSimpleData()+",pos:"+pos);
		if(pos >= 0)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_换桌,"进入成功，player:"+player.getSimpleData());
			player.setClassicGame(this);
			player.sendResponse(new ChangeTableResponse(ChangeTableResponse.SUCCESS));
			
			ArrayList<PlayerSimpleData> list_allPlayer = getAllPlayerSimpleData();
			EnterBeginnerResponse response = new EnterBeginnerResponse(EnterBeginnerResponse.SUCCESS);
			ArrayList<Integer> list_betAll = null;
			if(isAction())
			{
				list_betAll = list_allBet;
			}
			else
			{
				list_betAll = new ArrayList<Integer>();
			}
			response.setData(getState(),list_allPlayer,getBankerPos(),pos,getRoundNum(),list_betAll,playPos,getTimeLeft(),type);
			player.sendResponse(response);
			
			playerNum++;

			Tool.print_debug_level0("经典场classicGameChangeTableSitDown,player:"+player.getSimpleData()+",当前玩家数playerNum:"+playerNum);
			
			broadcastStateNotPosToClassic(new ClassicGamePush_otherPlayerEnter(player.getSimpleData()),pos);
			
			if( !runing() && playerNum>=2)
			{
				changeToWaitStart();
			}
		}
		return pos;
	}

	private int getTimeLeft()
	{
		return (int) ((MyTimeUtil.getCurrTimeMM() - stateStartTime)/1000);
	}

	public boolean isSitdown(Player player)
	{
//		if(isDebug) Tool.print_debug_level0("玩家是否坐下,pos:"+player.getPos()+",room.getTablePlayer(player.getUserId()):"+room.getTablePlayer(player.getPos()));
//		if(isDebug)
//		{
//			for (int i = 0 ; i < tablePlayers.length ; i++)
//			{
//				Player item = tablePlayers[i];
//				if(item != null)
//					System.out.println("i:"+i+",isSitdown----->item:"+item.getSimpleData());
//			}
//		}
		if(level == LEVEL_万人场)
		{
			if(room.getTablePlayer(player.getManyGamePos()) != null)//已经上桌
			{
				return true;
			}
		}else
		{
			if(room.getTablePlayer(player.getClassicGamePos()) != null)//已经上桌
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 万人场申请上桌
	 * pos >= 0,直接坐下， -1，列表满  -2，已经在列表  -3，进入列表 -99,未知
	 * @param player
	 * @return
	 */
	public boolean manyPepolRoomSitdown(Player player)
	{
		ManyPepolRoomSitdownResponse response = null;
		int pos = -1;
//		if(!runing())
//		{
//			
//		}
		pos = room.mGameSitDown(player);
		Tool.print_debug_level0(MsgTypeEnum.manypepol_申请上桌,"player:"+player.getNickName()+",pos:"+pos);
		if(pos >= 0)
		{
			playerNum++;
			Tool.print_debug_level0(MsgTypeEnum.manypepol_申请上桌,"player:"+player.getSimpleData()+",当前玩家数playerNum:"+playerNum+",runing:"+runing());
			broadcastStateToAll(new ManyPepolPush_playerSitdown(player.getSimpleData()));
			
			if( !runing())
			{
				changeToWaitStart();
			}
			else
			{
				player.changeToWaitPlay();
			}
			response = new ManyPepolRoomSitdownResponse(ManyPepolRoomSitdownResponse.SUCCESS);
			player.sendResponse(response);
			return true;
		}
		else//没有位置或者比赛已经开始，进入上桌列表
		{
			synchronized (list_sitdown)
			{
				if(list_sitdown.size() >= bankerListMax)
				{
					Tool.print_debug_level0(MsgTypeEnum.manypepol_申请上桌,"player:"+player.getSimpleData()+",上桌列表满");
					response = new ManyPepolRoomSitdownResponse(ManyPepolRoomSitdownResponse.ERROR_列表满);
					player.sendResponse(response);
					return false;
				}
				
				for (Player exitsPlayer : list_sitdown)
				{
					if(exitsPlayer.getUserId() == player.getUserId())//已在列表
					{
						if(isDebug) Tool.print_debug_level0(MsgTypeEnum.manypepol_申请上桌,"申请上庄时已在列表:"+player.getNickName());
						response = new ManyPepolRoomSitdownResponse(ManyPepolRoomSitdownResponse.ERROR_已在列表);
						player.sendResponse(response);
						return false;
					}
				}
				
				list_sitdown.add(player);
				sortBankerList();
				response = new ManyPepolRoomSitdownResponse(ManyPepolRoomSitdownResponse.SUCCESS);
				player.sendResponse(response);
				
				for (Player item : room.getAllPlayer())
				{
					item.sendResponse(new ManyPepolPush_otherPlayerApplicationBanker(player.getSimpleData()));
				}
				return false;
			}
		}
	}
	
//	private void addDirectionPlayer(int pos)
//	{
//		KillRoomDirectionPlayer directionPlayer = new KillRoomDirectionPlayer();
//		map_directionPlayer.put(pos, directionPlayer);
//	}

	private boolean isIdle()
	{
		return state == STATE_IDLE || state == STATE_WAITSTART || state == STATE_WAIT_PAY_BET;
	}

	public void sortBankerList()
	{
//		list_sitdown.sort(MySortUtil.lowToHighByIndexLong());
	}

//	public void payBet(Player player, int pos, int betNum)
//	{
//		KillRoomDirectionPlayer killDirectionPlayer =  map_directionPlayer.get(pos);
//		killDirectionPlayer.addChip(player.getUserId(), betNum);
//		
//		ManyPepolPush_payBet response = new ManyPepolPush_payBet(pos,betNum);
//		broadcastStateNotPosToAll(response, pos);
//	}

	public boolean isWaitStart()
	{
		return state == STATE_WAITSTART;
	}

	public void classicGameLeave(Player player)
	{
		int pos = player.getClassicGamePos();
		
		broadcastStateNotPosToClassic(new ClassicGamePush_playerLeave(pos),pos);
		room.classicGameStandUp(player);
		
		if(isAction())
		{
			broadcastPlayerThinkOver(pos);
			
			boolean isGameOver = checkGameOver();
			if(!isGameOver && playPos==pos)
			{
				changeToWaitPlayerThink();
			}
		}
		
		player.addFailureNum();
		player.clearClassicGame();
		
		if(playerNum>0)
			playerNum--;
		
		if(room.getAllPlayer().size() == 0)
		{
			setRemove();
		}
		
		Tool.print_debug_level0("经典场玩家离开游戏房间，站起后座位人数:"+playerNum+",userId:"+ player.getUserId());
	}
	
	/**
	 * 玩家离线时：超时离线，主动离线
	 * @param player
	 */
	public void manypepolRoomOfflineLeave(Player player)
	{
		if (isSitdown(player))//已经坐下
		{
			//TODO 万人场此时不离开房间，应该以轮到该玩家行动的次数为准
//			if (runing())
//			{
//				Tool.print_debug_level0("多人场玩家离线离开时已经坐下,游戏已经开始，进入待离开列表:" + player.getSimpleData());
//				mGameLeaveRoom(player);
//			}
//			else
//			{
//				Tool.print_debug_level0("多人场玩家离线离开时已经坐下,游戏未开始，直接离开:" + player.getSimpleData());
//
//				broadcastStateToAll(new ManyPepolPush_playerStandUp(player.getPos()));
//				room.manyPepolRoomStandUp(player);
//				playerNum--;
//				Tool.print_debug_level0("多人场玩家离线离开时已经坐下,游戏未开始，直接离开,已经坐下玩家，通知其他玩家有人站起:" + player.getSimpleData()+",座位上玩家数:"+playerNum);
//			 
//				player.clearManyPepolGame();
//			}
		}
		else
		{
			if(isExistsSitDownList(player.getUserId()))//上庄等待列表
			{
				Tool.print_debug_level0("多人场玩家离线离开时未坐下,在上庄列表，直接移除:"+player.getNickName());
				list_sitdown.remove(player);
			}
			else
			{
				Tool.print_debug_level0("多人场玩家离线离开时未坐下,不在上庄列表，直接移除:"+player.getNickName());
			}
			room.removePlayer(player.getUserId());
			player.clearManyPepolGame();
		}
	}
	
	public boolean runing()
	{
		switch (state)
		{
			case STATE_IDLE:
				return false;
			default:
				return true;
		}
	}
	
	public boolean playing()
	{
		switch (state)
		{
			case STATE_IDLE:
			case STATE_WAITSTART:
				return false;
			default:
				return true;
		}
	}
	
	public boolean isAction()
	{
		return state == STATE_PLAYER_THINK || state == STATE_DEAL;
	}

	public void pokerComparerResult(int pos0, int pos1, int lossPos, boolean isWin,int subNum)
	{
		if(level == LEVEL_万人场)
		{
			ManyPepolPush_comparerPoker response = new ManyPepolPush_comparerPoker(pos0, pos1, lossPos,subNum);
			broadcastStateToAll(response);
		}
		else
		{
			ClassicGamePush_comparerPoker response = new ClassicGamePush_comparerPoker(pos0,pos1,lossPos,subNum);
			broadcastStateToClassic(response);
		}
		lastComparerPokerWin = isWin;
		Player lossPlayer = room.getTablePlayers()[lossPos];
		lossPlayer.changeDie();
		
		setState(STATE_WAIT_COMPARERPOKER);
	}

	public void fold(Player player)
	{
		if(level == LEVEL_万人场)
		{
			thinkOver(player.getManyGamePos());
		}
		else
		{
			thinkOver(player.getClassicGamePos());
		}
	}

	public int getLevel()
	{
		return level;
	}

	public int getAllInBetNum()
	{
		switch (level)
		{
			case ClassicGame.LEVEL_初级场:
				return 40000;

			case ClassicGame.LEVEL_中级场:
				return 200000;

			case ClassicGame.LEVEL_高级场:
				return 400000;

			case ClassicGame.LEVEL_土豪场:
				return 8000000;
				
			case ClassicGame.LEVEL_万人场:
				return 20000000;
				
			default:
				return -1;
		}
	}

	public Player allInOtherPlayer(int pos)
	{
		if(level == LEVEL_万人场)
		{
			Player[] players = room.getTablePlayers();
			for (Player player : players)
			{
				if(player != null && player.getManyGamePos() != pos && !player.isDie() && !player.isWaitPlay())
				{
					return player;
				}
			}
		}else
		{
			Player[] players = room.getTablePlayers();
			for (Player player : players)
			{
				if(player != null && player.getClassicGamePos() != pos && !player.isDie() && !player.isWaitPlay())
				{
					return player;
				}
			}
		}
		return null;
	}

	public void playerReady(Player player)
	{
		int pos = -1;
		
		if(level == LEVEL_万人场)
		{
			pos = player.getManyGamePos();
		}
		else
		{
			pos = player.getClassicGamePos();
		}
		
		player.setReday(true);
		readyNum++;
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家准备,"玩家准备，当前已经准备人数:"+readyNum+",player:"+player.getSimpleData());
		broadcastStateToAll(new ClassicGamePush_playerReady(pos));
		if(readyNum == getAllPlayerSimpleData().size() && readyNum >= 2)//全部准备，直接发牌
		{
			bankerPos = -1;
			changeToStartGame();
		}
	}

	public void standUp(Player player)
	{
		if(level == LEVEL_万人场)
		{
			ManyPepolPush_playerStandUp response = new ManyPepolPush_playerStandUp(player.getManyGamePos());
			
			broadcastStateToAll(response);
			room.manyPepolRoomStandUp(player);
		}
		else
		{
			ClassicGamePush_playerLeave response = new ClassicGamePush_playerLeave(player.getClassicGamePos());
			broadcastStateToClassic(response);
			
			player.sendResponse(new LeaveClassicGameResponse(LeaveClassicGameResponse.SUCCESS));
			
			room.classicGameStandUp(player);
			
			if(room.getAllPlayer().size() == 0)
			{
				setRemove();
			}
		}
	
		player.clearPlayer();
 		
		if(playerNum>0)
			playerNum--;
		
		if(level == LEVEL_万人场)
			Tool.print_debug_level0("万人场玩家站起standUp，站起后座位人数:"+playerNum+",userId:"+ player.getUserId());
		else
			Tool.print_debug_level0("经典场玩家站起standUp，站起后座位人数:"+playerNum+",userId:"+ player.getUserId());
	}

	private void setRemove()
	{
		isRemove = true;
	}
	
	public boolean isRemove()
	{
		return room.getTablePlayerNum() == 0;
//		return isRemove;
	}

	public void mGameAddStandUpList(Player player)
	{
		room.addLeaveRoomList(player.getUserId());
	}

	public void addPlayer(Player player)
	{
		room.addPlayer(player);
	}

	public void mGameLeaveRoom(Player player)
	{
		room.addLeaveRoomList(player.getUserId());
	}

	public boolean isClassicGame()
	{
		switch (level)
		{
			case LEVEL_初级场:
			case LEVEL_中级场:
			case LEVEL_高级场:
			case LEVEL_土豪场:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isManyPepolRoom()
	{
		switch (level)
		{
			case LEVEL_万人场:
				return true;
			default:
				return false;
		}
	}

	public int getRoundNum()
	{
		return round;
	}

	public Room getRoom()
	{
		return room;
	}

	public void reconnect(Player player)
	{
		ArrayList<PlayerSimpleData> list_allPlayer = getAllPlayerSimpleData();
//		EnterBeginnerResponse response = new EnterBeginnerResponse(EnterBeginnerResponse.SUCCESS);
//		response.setData(getState(),list_allPlayer,getBankerPos(),player.getPos(),getRoundNum(),list_allBet,playPos,getTimeLeft());
		
		ClassicGamePush_reconnect response = new ClassicGamePush_reconnect();
		response.setData(getState(),list_allPlayer,getBankerPos(),player.getClassicGamePos(),getRoundNum(),list_allBet,playPos,getTimeLeft());
		if(player.getPokers().size() > 0)
		{
			PokerGroup pokerGroup = pokerLogicService.getPokerType(player.getPokers());
			response.setMineData(player.getPokers(),pokerGroup.groupType);
		}
		player.sendResponse(response);
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public void manyPepolReconnect(Player player,boolean isLogin)
	{
		ArrayList<PlayerSimpleData> list_allPlayer = getAllPlayerSimpleData(true);
		 
		ManyPepolPush_reconnect response = new ManyPepolPush_reconnect();
		response.setData(getState(),list_allPlayer,getBankerPos(),player.getManyGamePos(),getRoundNum(),getListAllBet(),playPos,getTimeLeft(),lastJackpotData.allJackpot);
		if(player.getPokers().size() > 0)
		{
			PokerGroup pokerGroup = pokerLogicService.getPokerType(player.getPokers());
			
			response.setMineData(player.isCheckPoker() ? player.getPokers() : null,pokerGroup.groupType,nowBet,lastPlayAllInState);
		}
		
		player.sendResponse(response);
		
		if(isWaitPlayerAction())//断线重连，只有当玩家在行动状态才返回行动消息
		{
			if(player.getManyGamePos() == playPos && isLogin)
			{
				 waitPlayerAction(player);
			}
		}
		 
	}

	public ArrayList<Integer> getListAllBet()
	{
		return isWaitPlayerAction() ? list_allBet : null;
	}

	private boolean isWaitPlayerAction()
	{
		return state == STATE_PLAYER_THINK;
	}

	public void setJackPot(long allJackpot)
	{
		lastJackpotData.setAllJackpot(allJackpot);
		room.setJackpot(allJackpot);
	}
	
	public JackpotData getLastJackpot()
	{
		return lastJackpotData;
	}

	public void removeLeavePlayer(int userId)
	{
		room.removeLeavePlayer(userId);
	}

	/**
	 * 站起时调用
	 * @param userId
	 */
	public void removePlayer(int userId)
	{
		room.removePlayer(userId);
	}

	public boolean removeBankerListExist(int userId)
	{
		synchronized (list_sitdown)
		{
			for (int i = 0 ; i < list_sitdown.size() ; i++)
			{
				if(list_sitdown.get(i).getUserId() == userId)
				{
					list_sitdown.remove(i);
					return true;
				}
			}
			
			return false;
		}
	}
}