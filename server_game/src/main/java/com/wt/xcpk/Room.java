package com.wt.xcpk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.cmd.chat.push.SendMsg_ChargesEmoji;
import com.wt.naval.dao.model.Config_killroomPoker;
import com.wt.naval.main.MyGlobalService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.sort.MySortUtil;
import com.wt.xcpk.killroom.ApplicationKillRoomBankerResponse;
import com.wt.xcpk.killroom.KillRoomRoundRankVO;
import com.wt.xcpk.killroom.KillRoomTablePlayerRoundScore;
import com.wt.xcpk.killroom.push.Push_killRoomOtherPlayerApplicationBanker;
import com.wt.xcpk.vo.poker.FaceTypeEnum;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.vo.poker.PokerVO;

public class Room
{
	private static final boolean isDebug = true;
	
	public static final int MODEL_KILLROOM = 0;
	public static final int MODEL_CLASSIC = 1;
	
	
	/**最大庄家列表*/
	private int bankerListMax = 10;
	
	public static final  int bankerRoundNumMax = 6;
	
	public static final int RANK_MAX = 5;
	
	/**
	 * 庄家申请列表
	 */
	private ArrayList<Player> list_bankerRequest = new ArrayList<Player>();
//	private ArrayList<PlayerSimpleData> list_bankerRequestSimple = new ArrayList<PlayerSimpleData>();
	
	/**
	 * 当前庄家
	 */
	private Player banker;
	
	/**庄家本局计分*/
	private long bankerWinSocre;
	
	/**当前庄家坐庄局数*/
	private int bankerRoundNum = 1;
	
	/**
	 * 当前房间所有玩家
	 */
	private ConcurrentHashMap<Integer,Player> map_allPlayers;
	private HashMap<Integer,Long> map_roundCalcScore;
	private HashMap<Integer,Integer> map_roundExp;
	private HashMap<Integer,Long> map_roundJackpotWin;
	
	/**
	 * 当前申请离开房间的玩家，由各场次维护改列表
	 * 通杀场当前为每个回合结束时检查该列表并且从房间中移除相应玩家
	 */
	private ConcurrentHashMap<Integer,Integer> map_leaveRoomPlayers;
	
	/**
	 * 当前上桌玩家，用于显示头像和聊天气泡，输赢时有特殊效果
	 */
	private Player[] table_players;
	private int tablePlayerNum;
	
	private ArrayList<KillRoomRoundRankVO> list_roundRank;//一局赢分榜
	
	/***
	 * 当前上座玩家的输赢分数，每回合刷新
	 */
	private HashMap<Integer,KillRoomTablePlayerRoundScore> map_tablePlayerScore;
	
	private ArrayList<PokerVO> list_roomPoker;
	
	/**
	 * 房间当前奖池
	 */
	private long jackpot = 0;

	/**
	 * 是否申请下庄
	 */
	private boolean isResignBanker;
	
	private int costCoins;
	
	private Config_killroomPoker confiKillroomPoker;
	/**
	 * 座位数
	 * @param playerNum
	 */
	public Room(int playerNum)
	{
		table_players = new Player[playerNum];
		list_roundRank = new ArrayList<KillRoomRoundRankVO>();
		map_allPlayers = new ConcurrentHashMap<Integer,Player>();
		map_leaveRoomPlayers = new ConcurrentHashMap<Integer, Integer>();
		list_roomPoker = new ArrayList<PokerVO>();
		map_tablePlayerScore = new HashMap<Integer, KillRoomTablePlayerRoundScore>();
		map_roundCalcScore = new HashMap<Integer, Long>();
		map_roundExp = new HashMap<Integer,Integer>();
		map_roundJackpotWin = new HashMap<Integer, Long>();
	}
	
	public void putKillRoomJackpotWin(int userId,long winScore)
	{
		map_roundJackpotWin.put(userId, winScore);
	}
	
	public void addKillRoomJackpotWin(int userId,long winScore)
	{
		map_roundJackpotWin.put(userId, getKillRoomJackpotWin(userId) + winScore);
	}
	
	public Long getKillRoomJackpotWin(int userId)
	{
		Long winScore = map_roundJackpotWin.get(userId);
		return winScore == null ? 0 : winScore;
	}

	
	public void setBankerCostCoins(int costCoins)
	{
		this.costCoins = costCoins;
	}

	public boolean resignBanker()
	{
		return isResignBanker;
	}
	
	/**
	 * 获取当前房间空闲桌子数
	 * @return
	 */
	public int getFreeTableNum()
	{
		synchronized (table_players)
		{
			return table_players.length - tablePlayerNum;
		}
	}
	
	public int getTablePlayerNum()
	{
//		synchronized (table_players)
//		{
//			return tablePlayerNum;
//		}
		
		int num = 0;
		if(table_players != null && table_players.length > 0)
		{
			for (Player item : table_players)
			{
				if(item != null)
				{
					num++;
				}
			}
		}
		return num;
	}
	
	/**
	 * 当前庄家申请下庄
	 */
	public void setResignBanker()
	{
		isResignBanker = true;
	}
	
//	public void setPlayerRoundCalcScore(int userId,int score)
//	{
//		map_roundCalcScore.put(userId, score);
//	}
	
	/**
	 *记录玩家回合获得得经验值，开奖时告知玩家
	 * @param userId
	 * @param exp
	 */
	public void addPlayerRoundExp(int userId,int exp)
	{
		int num = getPlayerRoundExp(userId);
		
		map_roundExp.put(userId, num + exp);
	}
	
	public int getPlayerRoundExp(int userId)
	{
		Integer integer = map_roundExp.get(userId);
		return integer == null ? 0 : integer;
	}
	
	public void addPlayerRoundCalcScore(int userId,long score)
	{
		long num = getPlayerRoundCalcScore(userId);
		
		map_roundCalcScore.put(userId, num + score);
	}
	
	public long getPlayerRoundCalcScore(int userId)
	{
		Long integer = map_roundCalcScore.get(userId);
		return integer == null ? 0 : integer;
	}
	
	/**
	 * 记录赢分人和分数
	 * @param userId
	 * @param num
	 */
	public void addRoundRank(int userId,long num)
	{
		KillRoomRoundRankVO killRoomRoundRankVO = new KillRoomRoundRankVO(userId,num);
		list_roundRank.add(killRoomRoundRankVO);
	}
	
	public void sortRoundRank()
	{
		if(list_roundRank == null || list_roundRank.size() <= 0)
		{
			return;
		}
		list_roundRank.sort(MySortUtil.highToLowByIndexLong());
	}
	
	public ArrayList<KillRoomRoundRankVO> getRoundRank()
	{
		ArrayList<KillRoomRoundRankVO> list = new ArrayList<KillRoomRoundRankVO>();
		for (int i = 0 ; i < RANK_MAX ; i++)
		{
			if(i >= list_roomPoker.size())
			{
				break;
			}
			
			KillRoomRoundRankVO killRoomRoundRankVO = list_roundRank.get(i);
			list.add(killRoomRoundRankVO);
		}
		return list;
	}
	
	public void addTablePlayerScore(int userId,long score)
	{
		KillRoomTablePlayerRoundScore killRoomTablePlayerRoundScore = map_tablePlayerScore.get(userId);
		if(killRoomTablePlayerRoundScore != null)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.KillRoom_通杀场开奖状态,"是上桌玩家，记录分数:"+score+",userId:"+userId);
			killRoomTablePlayerRoundScore.score += score;
		}
	}
	
	/**
	 * 增加奖池
	 * @param num
	 */
	public void addJackpot(int num)
	{
		this.jackpot += num;
	}
	
	/**
	 * 清空奖池
	 */
	public void clearJackpot()
	{
		this.jackpot = 0;
	}
	
	public long getJackpot()
	{
		return jackpot;
	}

	public void setJackpot(long jackpot)
	{
		this.jackpot = jackpot;
	}
	
	public void initRoomPoker(ArrayList<PokerVO> pokers)
	{
		list_roomPoker.clear();
		list_roomPoker.addAll(pokers);
	}
	
	public ArrayList<PokerVO> getRandomPoker(int num)
	{
		ArrayList<PokerVO> list = new ArrayList<PokerVO>();
		for (int i = 0 ; i < num ; i++)
		{
			int random = MyUtil.getRandom(list_roomPoker.size());
			PokerVO pokerVO = list_roomPoker.remove(random);
			list.add(pokerVO);
		}
		return list;
	}
	
	private ArrayList<PokerVO> list_3Poker = new ArrayList<PokerVO>();
	private ArrayList<PokerVO> list_allPoker = new ArrayList<PokerVO>();
	/**
	 * 根据牌型随机牌 0：豹子 1：顺金 2：金花 3：顺子 4：对子 5：单牌
	 * @param type
	 * @return
	 */
	public ArrayList<PokerVO> getPokerType(int type) {
		while (true)
		{
			list_3Poker.clear();
			list_allPoker.clear();
			list_allPoker.addAll(list_roomPoker);
			for (int i = 0 ; i < 3 ; i++)
			{
				int index = MyUtil.getRandom(list_allPoker.size());
				list_3Poker.add(list_allPoker.remove(index));
			}
			PokerGroup pokerGroup = MyGlobalService.getPokerLogicService().getPokerType(list_3Poker);
			if(pokerGroup.groupType == type + 1)
			{
				list_roomPoker.removeAll(list_3Poker);
				return list_3Poker;
			}
		}
	}
	
	
	public ArrayList<PokerVO> getAAA()
	{
		ArrayList<PokerVO> list = new ArrayList<PokerVO>();
		for (int i = list_roomPoker.size() - 1 ; i >= 0 ; i--)
		{
			PokerVO pokerVO = list_roomPoker.get(i);
			if(pokerVO.getType() == FaceTypeEnum.FACE_TYPE_A_VALUE)
			{
				PokerVO pokerA = list_roomPoker.remove(i);
				list.add(pokerA);
				if(list.size() == 3)
				{
					return list;
				}
			}
		}
		list_roomPoker.addAll(list);
		return null;
	}
	
	public long getBankerWinSocre()
	{
		return bankerWinSocre;
	}

	public void addBankerWinScore(long bankerSocre)
	{
		this.bankerWinSocre += bankerSocre;
	}
	
	public void clearBankerRoundScore()
	{
		bankerWinSocre = 0;
	}
	
	public void addBankerRoundNum()
	{
		bankerRoundNum++;
	}

	public int getBankerRoundNum()
	{
		return bankerRoundNum;
	}

	public void setBankerRoundNum(int bankerRoundNum)
	{
		this.bankerRoundNum = bankerRoundNum;
	}
	
	public boolean requestBanker(Player player)
	{
		synchronized (list_bankerRequest)
		{
//			if(banker == null)
//			{
//				changeBanker(player);
//				return true;
//			}
//			
			if(list_bankerRequest.size() >= bankerListMax)
			{
				return false;
			}
			
			for (Player exitsPlayer : list_bankerRequest)
			{
				if(exitsPlayer.getUserId() == player.getUserId())//已在列表
				{
					if(isDebug) Tool.print_debug_level0("申请上庄时已在列表:"+player.getNickName());
					return false;
				}
			}
			
			list_bankerRequest.add(player);
//			list_bankerRequestSimple.add(player.getSimpleData());
			return true;
		}
	}
	
	/**
		public static final int ERROR_上庄人数太多 = 1;
	public static final int ERROR_申请上庄时已是庄家 = 2;
	public static final int ERROR_申请上庄时人数太多 = 3;
	public static final int ERROR_申请上庄时已在列表 = 4;
	 * 1000:成功
	 * @param player
	 * @return
	 */
	public int requestBankerKillRoom(Player player)
	{
		synchronized (list_bankerRequest)
		{
			if(player.getUserId() == banker.getUserId())
			{
				if(isDebug) Tool.print_debug_level0("申请上庄时已是庄家");
				return ApplicationKillRoomBankerResponse.ERROR_申请上庄时已是庄家;
			}
			
			if(list_bankerRequest.size() >= bankerListMax)
			{
				if(isDebug) Tool.print_debug_level0("申请上庄时人数太多");
				return ApplicationKillRoomBankerResponse.ERROR_上庄人数太多;
			}
			
			for (Player exitsPlayer : list_bankerRequest)
			{
				if(exitsPlayer.getUserId() == player.getUserId())//已在列表
				{
					if(isDebug) Tool.print_debug_level0("申请上庄时已在列表:"+player.getNickName());
					return ApplicationKillRoomBankerResponse.ERROR_申请上庄时已在列表;
				}
			}
			
			list_bankerRequest.add(player);
//			list_bankerRequestSimple.add(player.getSimpleData());
			
			for (Player item : map_allPlayers.values())
			{
				item.sendResponse(new Push_killRoomOtherPlayerApplicationBanker(player.getSimpleData()));
			}
			return ApplicationKillRoomBankerResponse.SUCCESS;
		}
	}
	
	public void changeBanker(Player player)
	{
		banker = player;
		bankerRoundNum = 0;
		isResignBanker = false;
		
		if(player != null)
		{
			if(isDebug)Tool.print_debug_level0("changeBanker---切换庄家为:"+player.getSimpleData());
		}
	}
	
	public Player getBankerPlayer()
	{
		return banker;
	}

	public boolean addPlayer(Player player)
	{
		map_allPlayers.put(player.getUserId(), player);
		map_leaveRoomPlayers.remove(player.getUserId());
		return true;
	}
	
	public Player removePlayer(int userId)
	{
		return map_allPlayers.remove(userId);
	}

	/**
	 * 坐到位子上
	 * @param player
	 * @param pos
	 * @return code 0:ERROR_桌子有人  1:ERROR_桌子号错误  1000:success
	 */
	public int mGameSitDown(Player player,int pos)
	{
		if(pos < 0 || pos >= table_players.length)
		{
			Tool.print_debug_level0("玩家:"+player.getNickName()+",userId:"+player.getUserId()+",申请坐下的pos:"+pos+",不合法");
			return 1;
		}
		synchronized (table_players)
		{
			if(table_players[pos] == null)
			{
				player.setManyGamePos(pos);
				table_players[pos] = player;
				
				KillRoomTablePlayerRoundScore killRoomTablePlayerRoundScore = new KillRoomTablePlayerRoundScore(pos, 0,player.getUserId());
				map_tablePlayerScore.put(player.getUserId(), killRoomTablePlayerRoundScore);
				tablePlayerNum ++;
				player.clearManyPepolRoomRound();
				return 1000;
			}
			return 0;
		}
	}
	
	public int mGameSitDown(Player player)
	{
		boolean isPing = MyUtil.getRandomBoolean();
		int pos = -1;
		synchronized (table_players)
		{
			if(isPing)//随机从左或者右开始选位置
			{
				for (int i = 0 ; i < table_players.length ; i++)
				{
					if(table_players[i] == null)
					{
						pos = i;
						break;
					}
				}
			}
			else
			{
				for (int i = table_players.length - 1 ; i > 0 ; i--)
				{
					if(table_players[i] == null)
					{
						pos = i;
						break;
					}
				}
			}
			int code = mGameSitDown(player,pos);
			if(code == 1000)
			{
				return pos;
			}
			else
			{
				return -1;
			}
			
		}
	}
	
	/**
	 * 坐到位子上
	 * @param player
	 * @param pos
	 * @return code 0:ERROR_桌子有人  1:ERROR_桌子号错误  1000:success
	 */
	public int killRoomSitDown(Player player,int pos)
	{
		if(pos < 0 || pos >= table_players.length)
		{
			Tool.print_debug_level0("玩家:"+player.getNickName()+",userId:"+player.getUserId()+",申请坐下的pos:"+pos+",不合法");
			return 1;
		}
		synchronized (table_players)
		{
			if(table_players[pos] == null)
			{
				player.setKillRoomPos(pos);
				table_players[pos] = player;
				
				KillRoomTablePlayerRoundScore killRoomTablePlayerRoundScore = new KillRoomTablePlayerRoundScore(pos, 0,player.getUserId());
				map_tablePlayerScore.put(player.getUserId(), killRoomTablePlayerRoundScore);
				tablePlayerNum ++;
				return 1000;
			}
			return 0;
		}
	}

	/**
	 * 获取当前房间座位空座数
	 * @return
	 */
	public int calcTableFreeNum()
	{
		int num = 0;
		for (Player player : table_players)
		{
			if(player == null)
			{
				num++;
			}
		}
		return num;
	}
	
	/**
	 * 玩家站起，重置玩家座位下标，，移除玩家房间数据
	 * @param player
	 * @return
	 */
	public boolean classicGameStandUp(Player player)
	{
		synchronized (table_players)
		{
			if(player.getClassicGamePos() < 0)
			{
				Tool.print_debug_level0("classicGameStandUp1,UserID="+player.getSimpleData());
				for (int i = 0 ; i < table_players.length ; i++)
				{
					Player temp_player = table_players[i];
					if(temp_player != null)
					{
						if(temp_player.getUserId() == player.getUserId())
						{
							table_players[i] = null;
							map_tablePlayerScore.remove(player.getUserId());
							player.setClassicGamePos(-1);
							tablePlayerNum--;
							removePlayer(player.getUserId());
							
							player.clearRoomData(false);
							return true;
						}
					}
				}
				
				return false;
			}
			
			if(table_players[player.getClassicGamePos()] != null)
			{
				Tool.print_debug_level0("classicGameStandUp2,UserID1="+table_players[player.getClassicGamePos()].getUserId()+",UserID2="+player.getUserId());
				if(table_players[player.getClassicGamePos()].getUserId() == player.getUserId())
				{
					table_players[player.getClassicGamePos()] = null;
					map_tablePlayerScore.remove(player.getUserId());
					player.setClassicGamePos(-1);
					tablePlayerNum--;
					removePlayer(player.getUserId());
					
					player.clearRoomData(false);
					return true;
				}
			}
			return false;
		}
	}
	
	public boolean killroomStandUp(Player player)
	{
		return killRoomStandUp(player);
	}
	
	/**
	 * 回合数检查和站起检查
	 * @param player
	 * @return
	 */
	public boolean manyPepolRoomStandUp(Player player)
	{
		Tool.print_debug_level0("万人场玩家申请站起:"+player.getNickName());
		synchronized (table_players)
		{
			if(player.getManyGamePos() < 0)
			{
				Tool.print_error("万人场玩家申请站起时不在座位:"+player.getNickName());
				return false;
			}
			
			if(table_players[player.getManyGamePos()] != null)
			{
				if(table_players[player.getManyGamePos()].getUserId() == player.getUserId())
				{
					table_players[player.getManyGamePos()] = null;
					map_tablePlayerScore.remove(player.getUserId());
					player.setManyGamePos(-1);//万人场站起，只能重置下标不能移出房间或者清空自己房间信息
					tablePlayerNum--;
					return true;
				}
			}
			else
			{
				Tool.print_error("申请站起时错误，位置玩家空:"+player.getNickName()+",pos:"+player.getManyGamePos());
			}
			return false;
		}
	}
	
	public boolean killRoomStandUp(Player player)
	{
		synchronized (table_players)
		{
			if(player.getKillRoomPos() < 0)
			{
				return false;
			}
			
			if(table_players[player.getKillRoomPos()] != null)
			{
				if(table_players[player.getKillRoomPos()].getUserId() == player.getUserId())
				{
					table_players[player.getKillRoomPos()] = null;
					map_tablePlayerScore.remove(player.getUserId());
					player.setKillRoomPos(-1);
					tablePlayerNum--;
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public String toString()
	{
		return "Room [list_bankerRequest=" + list_bankerRequest + ", banker=" + banker + ", bankerSocre=" + bankerWinSocre + ", bankerRoundNum=" + bankerRoundNum + ", 玩家数=" + map_allPlayers.size() + ", table_players=" + Arrays.toString(table_players) + "]";
	}

	public Collection<Player> getAllPlayer()
	{
		return map_allPlayers.values();
	}
	
	public ArrayList<PlayerSimpleData> getTableSimplePlayers(boolean isManyPepolRoom)
	{
		ArrayList<PlayerSimpleData> list = new ArrayList<PlayerSimpleData>();
		synchronized (list)
		{
			for (int i = 0 ; i < table_players.length ; i++)
			{
				Player player = table_players[i];
				if(player != null)
				{
					list.add(player.getSimpleData(isManyPepolRoom));
				}
			}
			return list;
		}
	}
	
	public ArrayList<PlayerSimpleData> getTableSimplePlayers()
	{
		ArrayList<PlayerSimpleData> list = new ArrayList<PlayerSimpleData>();
		for (int i = 0 ; i < table_players.length ; i++)
		{
			Player player = table_players[i];
			if(player != null)
			{
				list.add(player.getSimpleData(false));
			}
		}
		return list;
	}

	public void clear()
	{
		list_roundRank.clear();
		
		clearTablePlayerScore();
		clearBankerRoundScore();
		
		map_roundCalcScore.clear();
		map_roundExp.clear();
		map_roundJackpotWin.clear();
	}

	private void clearTablePlayerScore()
	{
		for (KillRoomTablePlayerRoundScore killRoomTablePlayerRoundScore : map_tablePlayerScore.values())
		{
			killRoomTablePlayerRoundScore.score = 0;
		}
	}

	public Collection<KillRoomTablePlayerRoundScore> getTablePlayerScore()
	{
		return map_tablePlayerScore.values();
	}

	public void clearBankerRound()
	{
		bankerRoundNum = 1;
	}

	private boolean isBankerRoundMaxLimit()
	{
		return bankerRoundNum >= bankerRoundNumMax;
	}

	public boolean addBankerAndCheckBankerRoundNum()
	{
		bankerRoundNum++;
		return isBankerRoundMaxLimit();
	}

	/**
	 * 庄家列表中有玩家时，切换到该玩家，否则返回空
	 * @return
	 */
	public PlayerSimpleData changeBankerByList()
	{
		synchronized (list_bankerRequest)
		{
			if(list_bankerRequest != null && list_bankerRequest .size() <= 0)
			{
				return null;
			}
			sortBankerList();
			PlayerSimpleData banker = list_bankerRequest.remove(0).getSimpleData();
			if(banker.getCoins() < costCoins)
			{
				return null;
			}
//			for (int i = 0 ; i < list_bankerRequestSimple.size() ; i++)//切换庄家时移除上庄列表中成为庄家的玩家
//			{
//				PlayerSimpleData simpleData = list_bankerRequestSimple.get(i);
//				if(simpleData.getUserId() == banker.getUserId())
//				{
//					list_bankerRequestSimple.remove(simpleData);
//					break;
//				}
//			}
			
			clearBankerRound();
			return banker;
		}
	}

	public boolean isTableExits(int pos)
	{
		if(pos < 0 || pos >= table_players.length)
		{
			return false;
		}
		return table_players[pos] != null;
	}

	public Player getTablePlayer(int pos)
	{
		if(pos < 0 || pos >= table_players.length)
		{
			return null;
		}
		return table_players[pos];
	}

	public ArrayList<PlayerSimpleData> getBankerList()
	{
		ArrayList<PlayerSimpleData> list = new ArrayList<PlayerSimpleData>();
		for (Player player : list_bankerRequest)
		{
			PlayerSimpleData playerSimpleData = player.getSimpleData();
			list.add(playerSimpleData);
		}
		return list;
	}
	

	public void subJackPot(long socre)
	{
		jackpot -= Math.abs(socre);
	}
	
	public  int getBankerListMaxNum()
	{
		
		return bankerListMax;
	}
	
	public long getBankerCoins()
	{
		return banker.getCoins();
	}

	public Player getPlayer(int userId)
	{
		return map_allPlayers.get(userId);
	}

	public void sortBankerList()
	{
		list_bankerRequest.sort(MySortUtil.highToLowByIndexLong());
	}

	public boolean isExitsPlayer(int userId)
	{
		return map_allPlayers.containsKey(userId);
	}

	public void addLeaveRoomList(int userId)
	{
		map_leaveRoomPlayers.put(userId, userId);
	}
	
	public void removeLeavePlayer(int userId)
	{
		map_leaveRoomPlayers.remove(userId);
	}

	public Collection<Integer> getLeavePlayers()
	{
		return map_leaveRoomPlayers.values();
	}

	public void clearLeavePlayers()
	{
		map_leaveRoomPlayers.clear();
	}

	public boolean isBanker(int userId)
	{
		if(banker == null)
		{
			return false;
		}
		return userId == banker.getUserId();
	}

	public boolean removeBankerListExist(int userId)
	{
		synchronized (list_bankerRequest)
		{
			for (int i = 0 ; i < list_bankerRequest.size() ; i++)
			{
				if(list_bankerRequest.get(i).getUserId() == userId)
				{
					list_bankerRequest.remove(i);
					return true;
				}
			}
			return false;
		}
	}

	public void chargesEmoji(int userId, int emojiId, int toUserId,int roomId)
	{
		SendMsg_ChargesEmoji chargesEmoji = new SendMsg_ChargesEmoji(userId, emojiId, toUserId,roomId);
		for (Player player : map_allPlayers.values())
		{
			if(player.getUserId() == userId)//不广播给发起人
			{
				continue;
			}
			player.sendResponse(chargesEmoji);
		}
	}



	public Player[] getTablePlayers()
	{
		return table_players;
	}

	public int classicGameSitDown(Player player)
	{
		if(player.getClassicGamePos()!=-1)
		{
			//已经在房间中
			//判断是不是当前房间
			if(getPlayer(player.getUserId())==null)
			{
				//不在当前房间中
				return -1;
			}else
			{
				return player.getClassicGamePos();
			}
		}
		
		boolean isPing = MyUtil.getRandomBoolean();
		int pos = -1;
		synchronized (table_players)
		{
			if(isPing)//随机从左或者右开始选位置
			{
				for (int i = 0 ; i < table_players.length ; i++)
				{
					if(table_players[i] == null)
					{
						pos = i;
						break;
					}
				}
			}
			else
			{
				for (int i = table_players.length - 1 ; i > 0 ; i--)
				{
					if(table_players[i] == null)
					{
						pos = i;
						break;
					}
				}
			}
			if(pos != -1)//还有位置
			{
				player.setClassicGamePos(pos);
				table_players[pos] = player;
				map_allPlayers.put(player.getUserId(), player);
 				player.changeToWaitPlay();
				tablePlayerNum ++;
			}
			return pos;
		}
	}

	public int manyGameSitDown(Player player)
	{
		boolean isPing = MyUtil.getRandomBoolean();
		int pos = -1;
		synchronized (table_players)
		{
			if(isPing)//随机从左或者右开始选位置
			{
				for (int i = 0 ; i < table_players.length ; i++)
				{
					if(table_players[i] == null)
					{
						pos = i;
						break;
					}
				}
			}
			else
			{
				for (int i = table_players.length - 1 ; i > 0 ; i--)
				{
					if(table_players[i] == null)
					{
						pos = i;
						break;
					}
				}
			}
			if(pos != -1)//还有位置
			{
				player.setManyGamePos(pos);
				table_players[pos] = player;
				map_allPlayers.put(player.getUserId(), player);
 				player.changeToWaitPlay();
				tablePlayerNum ++;
			}
			return pos;
		}
	}
	
	public void setConfigKillRoom(Config_killroomPoker confiKillroomPoker)
	{
		this.confiKillroomPoker = confiKillroomPoker;
	}
	
	public void broadResponseToAll(Response response)
	{
		Collection<Player> allPlayers = getAllPlayer();
		Tool.print_debug_level0("广播消息号["+response.msgType+"]时房间中人数:"+allPlayers.size());
		if(allPlayers != null)
		{
			for (Player item : allPlayers)
			{
				item.sendResponse(response);
			}
		}
	}
}