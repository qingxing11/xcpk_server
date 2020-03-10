package com.wt.xcpk.classic;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.xcpk.classic.AllInResponse;
import com.wt.cmd.xcpk.classic.ChangeTableResponse;
import com.wt.cmd.xcpk.classic.CheckPokerResponse;
import com.wt.cmd.xcpk.classic.ComparerPokerResponse;
import com.wt.cmd.xcpk.classic.EnterBeginnerResponse;
import com.wt.cmd.xcpk.classic.FoldResponse;
import com.wt.cmd.xcpk.classic.FollowBetResponse;
import com.wt.cmd.xcpk.classic.PlayerReadyResponse;
import com.wt.cmd.xcpk.classic.RaiseBetResponse;
import com.wt.factory.MyBeanFactory;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.timetask.SimpleTaskUtil;
import com.wt.xcpk.classicgame.ConfigClassicGame;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.zjh.logic.PokerLogicService;

import io.netty.channel.Channel;

@Service
public class ClassicGameImpl implements ClassicGameService,Runnable,PlayerListener
{
	private static final boolean isDebug = true;
	private static final boolean isAllInDebug = true;
	
	private ArrayList<ClassicGame> list_beginnerRoom = new ArrayList<ClassicGame>();
	private ArrayList<ClassicGame> list_normalRoom = new ArrayList<ClassicGame>();
	private ArrayList<ClassicGame> list_hardRoom = new ArrayList<ClassicGame>();
	private ArrayList<ClassicGame> list_regalRoom = new ArrayList<ClassicGame>();
	
	private ConfigClassicGame configClassicGame;
	
	@Autowired
	private PokerLogicService pokerLogicService;
	
	private int beginnerRoomNum;
	private int normalRoomNum;
	private int hardRoomNum;
	private int regalRoomNum;
	
	@Override
	public void addBeginnerList(Player player,int type)
	{
		switch (type)
		{
			case ClassicGame.LEVEL_初级场:
				enterBeginnerRoom(list_beginnerRoom,player,type);
				break;
				
			case ClassicGame.LEVEL_中级场:
				enterBeginnerRoom(list_normalRoom,player,type);
				break;
				
			case ClassicGame.LEVEL_高级场:
				enterBeginnerRoom(list_hardRoom,player,type);
				break;
				
			case ClassicGame.LEVEL_土豪场:
				enterBeginnerRoom(list_regalRoom,player,type);
				break;

			default:
				break;
		}
	}
	
	private void enterBeginnerRoom(ArrayList<ClassicGame> list,Player player,int type)
	{
		synchronized (list)
		{
			ClassicGame classicGame = getNextFreeTableRoom(list);//获得当前阶段，还有空位的房间
			if(classicGame == null)//没有空位，创建空房间
			{
				classicGame = MyBeanFactory.getBean(ClassicGame.class);
				classicGame.initGame(5,type,configClassicGame,false);
				classicGame.setClassicConfig(configClassicGame);
				list.add(classicGame);
				
				Tool.print_debug_level0(MsgTypeEnum.classic_进入新手场,"没有空位，创建空房间,当前房间数:"+beginnerRoomNum+"，player:"+player.getSimpleData());
			
				switch (type)
				{
					case ClassicGame.LEVEL_初级场:
						beginnerRoomNum++;
						classicGame.setId(beginnerRoomNum);
						break;

					case ClassicGame.LEVEL_中级场:
						normalRoomNum++;
						classicGame.setId(normalRoomNum);
						break;
						
					case ClassicGame.LEVEL_高级场:
						hardRoomNum++;
						classicGame.setId(hardRoomNum);
						break;
						
					case ClassicGame.LEVEL_土豪场:
						regalRoomNum++;
						classicGame.setId(regalRoomNum);
						break;
						
					default:
						break;
				}
			}
			
			int pos = classicGame.classicGameSitDown(player,type);
			if(pos < 0)
			{
				EnterBeginnerResponse response = new EnterBeginnerResponse(EnterBeginnerResponse.ERROR_进入房间错误);
				player.sendResponse(response);
				return;
			}
		}
	}

	private void changeTableBeginner(ClassicGame game, Player player,ArrayList<ClassicGame> list_room,int type)
	{
		leaveRoom(player);
		ChangeTableResponse response = null;
		synchronized (list_room)
		{
			ClassicGame room = getNextFreeTableRoomAndSkip(list_room,game);//获得当前阶段，还有空位的房间
			if(room == null)//没有空位，创建空房间
			{
				room = MyBeanFactory.getBean(ClassicGame.class);
				room.initGame(5,type,configClassicGame,false);
				room.setClassicConfig(configClassicGame);
				room.setId(beginnerRoomNum);
				list_room.add(room);
				beginnerRoomNum++;
				if(isDebug)Tool.print_debug_level0(MsgTypeEnum.classic_换桌,"没有空位，创建空房间，player:"+player.getSimpleData());
			}
			
			int pos = room.classicGameChangeTableSitDown(player,type);
			if(pos < 0)
			{
				if(isDebug)Tool.print_debug_level0(MsgTypeEnum.classic_换桌,"ERROR_换桌错误,pos:"+pos);
				response = new ChangeTableResponse(ChangeTableResponse.ERROR_换桌错误);
				player.sendResponse(response);
				return;
			}
		}
	}
	
	@Override
	public void changeTable(Player player)
	{
		ClassicGame game = player.getClassicGame();
		System.out.println("changeTable----->game.getLevel():"+game.getLevel());
		switch (game.getLevel())
		{
			case ClassicGame.LEVEL_初级场:
				changeTableBeginner(game,player,list_beginnerRoom,game.getLevel());
				break;
				
			case ClassicGame.LEVEL_中级场:
				changeTableBeginner(game,player,list_normalRoom,game.getLevel());
				break;
				
			case ClassicGame.LEVEL_高级场:
				changeTableBeginner(game,player,list_hardRoom,game.getLevel());
				break;
				
			case ClassicGame.LEVEL_土豪场:
				changeTableBeginner(game,player,list_regalRoom,game.getLevel());
				break;

			default:
				break;
		}
	}

	@Override
	public void run()
	{
		runBeginner();
		runNormalRoom();
		runHardRoom();
		runRegalRoom();
	}
	
	private void runRegalRoom()
	{
		for (int i = list_regalRoom.size() - 1 ; i >= 0 ; i--)
		{
			ClassicGame classicGame = list_regalRoom.get(i);
			classicGame.runClassicGame();
			
			if(classicGame.isRemove())
			{
				list_regalRoom.remove(i);
				Tool.print_debug_level0("土豪房间id["+classicGame.getId()+"]空，移除，剩余房间数 ："+list_regalRoom.size());
			}
		}
	}

	private void runHardRoom()
	{
		for (int i = list_hardRoom.size() - 1 ; i >= 0 ; i--)
		{
			ClassicGame classicGame = list_hardRoom.get(i);
			classicGame.runClassicGame();
			
			if(classicGame.isRemove())
			{
				list_hardRoom.remove(i);
				Tool.print_debug_level0("高级房间id["+classicGame.getId()+"]空，移除，剩余房间数 ："+list_hardRoom.size());
			}
		}
	}

	private void runNormalRoom()
	{
		for (int i = list_normalRoom.size() - 1 ; i >= 0 ; i--)
		{
			ClassicGame classicGame = list_normalRoom.get(i);
			classicGame.runClassicGame();
			
			if(classicGame.isRemove())
			{
				list_normalRoom.remove(i);
				Tool.print_debug_level0("中级房间id["+classicGame.getId()+"]空，移除，剩余房间数 ："+list_normalRoom.size());
			}
		}
	}

	private void runBeginner()
	{
		for (int i = list_beginnerRoom.size() - 1 ; i >= 0 ; i--)
		{
			ClassicGame classicGame = list_beginnerRoom.get(i);
			classicGame.runClassicGame();
			
			if(classicGame.isRemove())
			{
				list_beginnerRoom.remove(i);
				Tool.print_debug_level0("初级房间id["+classicGame.getId()+"]空，移除，剩余房间数 ："+list_beginnerRoom.size());
			}
		}
	}

	/**
	 * 获取指定阶段（新手场，土豪场）里的空闲桌子
	 * @param list
	 * @return
	 */
	private ClassicGame getNextFreeTableRoom(ArrayList<ClassicGame> list)
	{
		if(list.size() == 0)
		{
			return null;
		}
		
		for (ClassicGame room : list)
		{
			if(room.getFreeTableNum() > 0)
			{
				return room;
			}
		}
		return null;//当前阶段没有空闲房间
	}
	
	private ClassicGame getNextFreeTableRoomAndSkip(ArrayList<ClassicGame> list,ClassicGame skipRoom)
	{
		if(list.size() == 0)
		{
			return null;
		}
	
		boolean isPing = MyUtil.getRandomBoolean();
		if(isPing)
		{
			for (ClassicGame room : list)
			{
				if(room.getFreeTableNum() > 0 && !room.equals(skipRoom))
				{
					return room;
				}
			}
		}
		else
		{
			for (int i = list.size() - 1 ; i >= 0 ; i--)
			{
				ClassicGame game = list.get(i);
				if(game.getFreeTableNum() > 0 && !game.equals(skipRoom))
				{
					return game;
				}
			}
		}
		
		return null;//当前阶段没有空闲房间
	}
	
	@PostConstruct
	private void init()
	{
		WorldMapUnitEvent.addEventListener(this);
		initConfig();
		
		try
		{
			SimpleTaskUtil.startTask("classicGameRun",500,500, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initConfig()
	{
//		configClassicGame = ClassicGameDaoImpl.getConfigClassicGame();
		configClassicGame = new ConfigClassicGame();
	}

	@Override
	public void checkPoker(Player player)
	{
		CheckPokerResponse response = null;
		ClassicGame classicGame = player.getClassicGame();
//		if(player.getPos() != classicGame.getPlayPos())
//		{
//			Tool.print_debug_level0(MsgTypeEnum.classic_玩家看牌,"ERROR_没轮到行动,player:"+player.getSimpleData());
//			response = new CheckPokerResponse(CheckPokerResponse.ERROR_没轮到行动);
//			player.sendResponse(response);
//			return;
//		}
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家看牌, "player:"+player.getSimpleData()+"poker:"+player.getPokers().toString());
		PokerGroup group = pokerLogicService.getPokerType(player.getPokers());
		response = new CheckPokerResponse(CheckPokerResponse.SUCCESS, player.getPokers(),group.groupType);
		player.sendResponse(response);
		classicGame.checkPoker(player);
	}

	@Override
	public void playerFollowBet(Player player)
	{
		FollowBetResponse response = null;
		ClassicGame classicGame = player.getClassicGame();
		
		if(classicGame.isAllIn())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家跟注,"ERROR_全压不能跟注,player:"+player.getSimpleData());
			response = new FollowBetResponse(FollowBetResponse.ERROR_全压不能跟注);
			player.sendResponse(response);
			return;
		}
		
		if(player.getClassicGamePos() != classicGame.getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家跟注,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new FollowBetResponse(FollowBetResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		if(classicGame.getRoundNum() >= 20)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家跟注,"ERROR_超过20回,player:"+player.getSimpleData());
			
			response = new FollowBetResponse(FollowBetResponse.ERROR_超过20回);
			player.sendResponse(response);
			return;
		}
		
		int cost = classicGame.getNowBet();
		if(player.isCheckPoker())
		{
			cost *= 2;
		}
		if(player.getCoins() < cost)
		{
			response = new FollowBetResponse(FollowBetResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}
		Tool.print_subCoins(player.getNickName(),cost,"经典场跟注",player.getCoins());
		player.subCoinse(cost);
		player.addRoundBet(cost);
		response = new FollowBetResponse(FollowBetResponse.SUCCESS,cost);
		player.sendResponse(response);
		classicGame.followBet(player,cost);
	}

	@Override
	public void raiseBet(Player player,int betNum)
	{
		RaiseBetResponse response = null;
		
		ClassicGame classicGame = player.getClassicGame();
		
		if(player.getClassicGamePos() != classicGame.getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家加注,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new RaiseBetResponse(RaiseBetResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		if(classicGame.getRoundNum() >= 20)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家加注,"ERROR_超过20回,player:"+player.getSimpleData());
			
			response = new RaiseBetResponse(RaiseBetResponse.ERROR_超过20回);
			player.sendResponse(response);
			return;
		}
		
		if(betNum <  classicGame.getNowBet())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家加注,"ERROR_加注不能低于底注,player:"+player.getSimpleData()+",betNum:"+betNum);
			response = new RaiseBetResponse(RaiseBetResponse.ERROR_加注不能低于底注);
			player.sendResponse(response);
			return;
		}
		
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家加注,"玩家是否已经看牌:"+player.isCheckPoker());
		
		int baseRate = 1; 
		if(player.isCheckPoker())
		{
			baseRate = 2;
		}
		if(player.getCoins() < betNum * baseRate)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家加注,"ERROR_金币不足,player:"+player.getSimpleData());
			response = new RaiseBetResponse(RaiseBetResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}
		Tool.print_subCoins(player.getNickName(),betNum * baseRate,"经典场加注",player.getCoins());
		player.subCoinse(betNum * baseRate);
		player.addRoundBet(betNum * baseRate);
		response = new RaiseBetResponse(RaiseBetResponse.SUCCESS,betNum * baseRate);
		player.sendResponse(response);
		
		classicGame.raiseBet(player,betNum,baseRate);
	}

	@Override
	public void leaveRoom(Player player)
	{
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame != null)
		{
			Tool.print_debug_level0("玩家离开游戏房间，player："+player.getSimpleData());
			classicGame.classicGameLeave(player);
		}
		else
		{
			Tool.print_debug_level0("玩家离开时不在游戏房间，player："+player.getSimpleData());
		}
	}

	@Override
	public void fold(Player player)
	{
		FoldResponse response = null;
		ClassicGame classicGame = player.getClassicGame();
		
		response = new FoldResponse(FoldResponse.SUCCESS);
		player.sendResponse(response);
		
		if(player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"ERROR_没轮到行动,isDie,player:"+player.getSimpleData());
			response = new FoldResponse(FoldResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		classicGame.fold(player);
	}
	
	@Override
	public void comparerPoker(Player player, int pos)
	{
		Tool.print_debug_level0(MsgTypeEnum.classic_玩家比牌,"player.pos:"+player.getClassicGamePos()+",pos:"+pos);
		ComparerPokerResponse response = null;
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null)
		{
			Tool.print_error(MsgTypeEnum.classic_玩家比牌,"玩家比牌时所处房间空:"+player.getUserId()+",nickName:"+player.getNickName());
			return;
		}
		Player[] players =  classicGame.getTablePlayers();
		Player tragetPlayer = null;
		if(player.getClassicGamePos() == pos || pos < 0 || pos >= players.length)
		{
			response = new ComparerPokerResponse(ComparerPokerResponse.ERROR_比牌位置错误);
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家比牌,"ERROR_比牌位置错误,player:"+player.getSimpleData()+",tragetPos:"+pos);
			player.sendResponse(response);
			return;
		}
		tragetPlayer = classicGame.getTablePlayers()[pos];
		if(tragetPlayer == null || !tragetPlayer.isNormal())
		{
			for (Player player2 : classicGame.getTablePlayers())
			{
				if(player2 != null)
				System.out.println("classicGame.getTablePlayers():"+player2.getSimpleData());
			}
			
			response = new ComparerPokerResponse(ComparerPokerResponse.ERROR_比牌对象空);
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家比牌,"ERROR_比牌对象空,player:"+player.getSimpleData()+",tragetPos:"+pos);
			player.sendResponse(response);
			return;
		}
		
		int cost = classicGame.getNowBet();
		if(classicGame.isAllIn())
		{
			cost = 0;
		}
		if(player.isCheckPoker())
		{
			cost *= 2;
		}
		if(player.getCoins() < cost)
		{
			cost = (int) player.getCoins();
		}
		Tool.print_subCoins(player.getNickName(),cost,"经典场比牌",player.getCoins());
		player.subCoinse(cost);
		player.addRoundBet(cost);
		response = new ComparerPokerResponse(ComparerPokerResponse.SUCCESS);
		player.sendResponse(response);
		
		player.setComparerPoker();
		tragetPlayer.setComparerPoker();
		int comparer = pokerLogicService.pokerComparer(player.getPokers(), tragetPlayer.getPokers());
		if(comparer <= 0)//输了
		{
			classicGame.pokerComparerResult(player.getClassicGamePos(), pos, player.getClassicGamePos(),false,cost);
		}
		else//赢了
		{
			classicGame.pokerComparerResult(player.getClassicGamePos(), pos, tragetPlayer.getClassicGamePos(),true,cost);
		}
	}

	@Override
	public void online(Player player, Channel channel)
	{
		player.clearOfflineRound();
	}

	@Override
	public void offlineOnGame(Player player)
	{
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame != null && classicGame.isClassicGame())
		{
			if(isDebug)Tool.print_debug_level0("经典场玩家离线，player:"+player.getSimpleData());
			leaveRoom(player);
		}
	}

	@Override
	public void offlineOnServer(Player player)
	{
		
	}

	@Override
	public void allIn(Player player)
	{
		AllInResponse response = null;
		ClassicGame room = player.getClassicGame();
		
		if(player.getClassicGamePos() != room.getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new AllInResponse(AllInResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		if(room.getRoundNum() >= 20)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"ERROR_超过20回,player:"+player.getSimpleData());
			
			response = new AllInResponse(AllInResponse.ERROR_超过20回);
			player.sendResponse(response);
			return;
		}
		
		int alivePlayerNum = room.getAlivePlayerNum();
		if(alivePlayerNum != 2)
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"ERROR_不在可全压人数,当前人数:"+alivePlayerNum+",player:"+player.getSimpleData());
			response = new AllInResponse(AllInResponse.ERROR_不在可全压人数);
			player.sendResponse(response);
			return;
		}
		
		int cost = room.getAllInBetNum();//当前房间全压数量
		Player tragetPlayer = room.allInOtherPlayer(player.getClassicGamePos());//对家
		
		long allInPay = 0;
		if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"是否已经allIn:"+room.isAllIn()+",当前行动玩家:"+player.getNickName());
		if(!room.isAllIn())//发起人金币是否大于万人场全压金币
		{
			if(player.getCoins() < cost)//全压发起人金币不够，使用他的金币作为cost
			{
				if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"发起人全压,发起人自己金币小于全压金币:"+player.getCoins());
				cost = (int) player.getCoins();
			}
			allInPay = cost;
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"发起人全压,根据全压发起人携带金币修正后的全压金额:"+allInPay+",当前行动玩家:"+player.getNickName());
			 
			if(tragetPlayer.getCoins() < allInPay)//对家金币小于全压上限(此时全压金币已经和发起人金币对比过)
			{
				allInPay = tragetPlayer.getCoins();
			 
				if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"发起人全压,对家金币小于全压上限(此时全压金币已经和发起人金币对比过)取对家金币:"+ tragetPlayer.getCoins()+",allInPay:"+allInPay+",对面玩家:"+tragetPlayer.getSimpleData());
			}
			 
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"发起人全压,当前房间全压数量:"+room.getAllInBetNum()+",扣掉已经下注的,剩余需下:"+cost+",自己总金币："+player.getCoins()+",最终下注:"+allInPay);
		}
		else
		{
			allInPay = room.getNowBet();
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"全压跟注,最终跟注金币:"+allInPay+",跟注玩家:"+player.getSimpleData());
		}
		
		response = new AllInResponse(AllInResponse.SUCCESS,allInPay);
		player.sendResponse(response);
		Tool.print_subCoins(player.getNickName(),allInPay,"经典场全压",player.getCoins());
 		player.subCoinse(allInPay);
		player.addRoundBet(allInPay);
		
		room.allIn(player,allInPay);
		if(room.isAllIn())//已经有一家全压，开始比牌
		{
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.classic_玩家全压,"已经有一家全压，开始比牌");
			
			comparerPoker(tragetPlayer,player.getClassicGamePos());
		}
		room.setAllIn();
	}

	@Override
	public void ready(Player player)
	{
		ClassicGame room = player.getClassicGame();
		PlayerReadyResponse response = null;
		 
		if(!room.isWaitStart())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家准备,"ERROR_不在可准备状态,当前状态:"+room.getState()+"player:"+player.getSimpleData());
			response = new PlayerReadyResponse(PlayerReadyResponse.ERROR_不在可准备状态);
			player.sendResponse(response);
			return;
		}
		
		if(player.isReday())
		{
			Tool.print_debug_level0(MsgTypeEnum.classic_玩家准备,"ERROR_已经准备,当前状态:"+room.getState()+"player:"+player.getSimpleData());
			response = new PlayerReadyResponse(PlayerReadyResponse.ERROR_已经准备);
			player.sendResponse(response);
			return;
		}
		
		room.playerReady(player);
	}

	@Override
	public boolean reconnect(Player player)
	{
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame != null)
		{
			classicGame.reconnect(player);
			return true;
		}
		return false;
	}

	@Override
	public boolean setGetBoxUserId(Player player)
	{
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null)
		{
			Tool.print_error("领取宝箱是错误,不在比赛中,player:"+player.getSimpleData());
			return false;
		}
		classicGame.setGetBoxUserId(player.getUserId());
		return true;
	}

	@Override
	public boolean isGetBoxIdExist(Player player)
	{
		ClassicGame classicGame = player.getClassicGame();
		if(classicGame == null)
		{
			Tool.print_error("检查本局是否领取过宝箱时错误,player:"+player.getSimpleData());
			return false;
		}
		return classicGame.isGetBoxIdExist(player.getUserId());
	}

	@Override
	public int getCost(int type)
	{
		int cost = 0;
		switch (type)
		{
			case 0:
				cost = 3000;
				break;

			case 1:
				cost = 5000;
				break;
				
			case 2:
				cost = 100000;
				break;
				
			case 3:
				cost = 2000000;
				break;
				
			default:
				break;
		}
		return cost;
	}
}