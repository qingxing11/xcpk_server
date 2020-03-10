package com.wt.xcpk.manypepol;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.xcpk.classic.FoldResponse;
import com.wt.cmd.xcpk.manypepol.EnterManyPepolRoomResponse;
import com.wt.cmd.xcpk.manypepol.LeaveManyPepolRoomResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolFollowBetResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomAllInResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomCheckPokerResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomComparerPokerResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomFoldResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomPayBetResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomRaiseBetResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomSitdownResponse;
import com.wt.cmd.xcpk.manypepol.ManyPepolRoomStandUpResponse;
import com.wt.cmd.xcpk.manypepol.push.ManyPepolPush_otherPlayerResignBanker;
import com.wt.event.server.GameServerStartup;
import com.wt.event.server.ServerEvent;
import com.wt.factory.MyBeanFactory;
import com.wt.naval.dao.impl.ManyPopelRoomDaoImpl;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.Tool;
import com.wt.util.timetask.SimpleTaskUtil;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.classic.ClassicGame;
import com.wt.xcpk.classicgame.ConfigClassicGame;
import com.wt.xcpk.vo.JackpotData;
import com.wt.xcpk.vo.poker.PokerGroup;
import com.wt.xcpk.zjh.logic.PokerLogicService;

import io.netty.channel.Channel;
import model.GameWanren;
/**
 * 
 * @author WangTuo
 *
 */
@Service
public class ManyPepolImpl implements ManyPepolService,GameServerStartup,Runnable,PlayerListener
{
	private static final boolean isDebug = false;
	
	private static final boolean isAllInDebug = false;
	
	private ClassicGame game;
	
	@Autowired
	private PokerLogicService pokerLogicService;
	
	private GameWanren configGameWanren;
	
	private ConfigClassicGame config;
	
	@Override
	public ArrayList<PlayerSimpleData> getPlayers()
	{
		return game.getAllPlayerSimpleData();
	}
	
	public ConfigClassicGame getConfig()
	{
		return config;
	}
	
	@Override
	public int getState()
	{
		return game.getState();
	}
	
	@Override
	public void gameServerStartup()
	{
		config = new ConfigClassicGame();
		game = MyBeanFactory.getBean(ClassicGame.class);
		Tool.print_debug_level0("万人场启动成功，game："+game);
		game.initGame(5, ClassicGame.LEVEL_万人场, config,true);
		game.setJackPot(configGameWanren.getStartJackPot());
		try
		{
			SimpleTaskUtil.startTask("ManyPepolRoom", 500, 500, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try
		{
			game.runClassicGame();
		}
		catch (Exception e)
		{
			Tool.print_error("万人场运行错误:",e);
		}
	}

	@Override
	public void sitdown(Player player)
	{
//		return;
		
		ManyPepolRoomSitdownResponse response = null;
		if(game.isSitdown(player))
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_申请上桌,"ERROR_已经坐下,player:"+player.getSimpleData());
			response = new ManyPepolRoomSitdownResponse(ManyPepolRoomSitdownResponse.ERROR_已经坐下);
			player.sendResponse(response);
			return;
		}
		game.manyPepolRoomSitdown(player);
	}

	@Override
	public void checkPoker(Player player)
	{
		PokerGroup group = pokerLogicService.getPokerType(player.getPokers());
		ManyPepolRoomCheckPokerResponse response = new ManyPepolRoomCheckPokerResponse(ManyPepolRoomCheckPokerResponse.SUCCESS, player.getPokers(),group.groupType);
		player.sendResponse(response);
		
		game.checkPoker(player);
	}

	@Override
	public void playerFollowBet(Player player)
	{
		if(player.isWaitPlay())
			return ;
		
		ManyPepolFollowBetResponse response = null;
		ClassicGame classicGame = player.getManyPepolGame();
		
		if(classicGame.isAllIn())
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家跟注,"ERROR_全压不能跟注,player:"+player.getSimpleData());
			
			response = new ManyPepolFollowBetResponse(ManyPepolFollowBetResponse.ERROR_全压不能跟注);
			player.sendResponse(response);
			return;
		}
		
		if(player.getManyGamePos() != classicGame.getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家跟注,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new ManyPepolFollowBetResponse(ManyPepolFollowBetResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		if(game.getRoundNum() >= 20)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家跟注,"ERROR_超过20回,player:"+player.getSimpleData());
			
			response = new ManyPepolFollowBetResponse(ManyPepolFollowBetResponse.ERROR_超过20回);
			player.sendResponse(response);
			return;
		}
		
		int cost = classicGame.getNowBet();
		if(player.isCheckPoker())
		{
			cost *= 2;
		}
		
		if(player.getManyPepolRoomCalcCoins() < cost)
		{
			response = new ManyPepolFollowBetResponse(ManyPepolFollowBetResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}
 	
// 		player.subCoinse(cost);
		player.addRoundBet(cost);
		response = new ManyPepolFollowBetResponse(ManyPepolFollowBetResponse.SUCCESS,cost);
		player.sendResponse(response);
	 
		game.followBet(player, cost);
	}

	@Override
	public void raiseBet(Player player, int betNum)
	{
		if(betNum<=0)
			return;
		
		if(player.isWaitPlay())
			return ;
		
		ManyPepolRoomRaiseBetResponse response = null;
		
		if(player.getManyGamePos() != game.getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家加注,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomRaiseBetResponse(ManyPepolRoomRaiseBetResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		if(game.getRoundNum() >= 20)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家加注,"ERROR_超过20回,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomRaiseBetResponse(ManyPepolRoomRaiseBetResponse.ERROR_超过20回);
			player.sendResponse(response);
			return;
		}
		
		if(betNum <  game.getNowBet())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家加注,"ERROR_加注不能低于底注,player:"+player.getSimpleData()+",betNum:"+betNum);
			response = new ManyPepolRoomRaiseBetResponse(ManyPepolRoomRaiseBetResponse.ERROR_加注不能低于底注);
			player.sendResponse(response);
			return;
		}
		
		int baseRate = 1;
		if(player.isCheckPoker())
		{
			baseRate = 2;
		}
		if(player.getManyPepolRoomCalcCoins() < betNum * baseRate)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家加注,"ERROR_金币不足,player:"+player.getSimpleData());
			response = new ManyPepolRoomRaiseBetResponse(ManyPepolRoomRaiseBetResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}
		
//		player.subCoinse(betNum * baseRate);
		player.addRoundBet(betNum * baseRate);
		response = new ManyPepolRoomRaiseBetResponse(ManyPepolRoomRaiseBetResponse.SUCCESS,betNum * baseRate);
		player.sendResponse(response);
		
		game.raiseBet(player,betNum,baseRate);
	}

	@Override
	public void payBet(Player player,int pos,int betNum)
	{
		if(betNum<=0)
			return;
		
		if(player.isWaitPlay())
			return ;
		
		ManyPepolRoomPayBetResponse response = null;
		Player[] players = game.getTablePlayers();
		if(pos < 0 || pos >= players.length || players[pos] == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_闲家下注,"下注位置错误,pos:"+pos+",player:"+player.getSimpleData());
			response = new ManyPepolRoomPayBetResponse(ManyPepolRoomPayBetResponse.ERROR_下注位置错误);
			player.sendResponse(response);
			return;
		}
		
		if(!game.isWaitStart())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_闲家下注,"ERROR_不在下注时间,player:"+player.getSimpleData());
			response = new ManyPepolRoomPayBetResponse(ManyPepolRoomPayBetResponse.ERROR_不在下注时间);
			player.sendResponse(response);
			return;
		}
		
		if(player.getCoins() < betNum)
		{
			response = new ManyPepolRoomPayBetResponse(ManyPepolRoomPayBetResponse.ERROR_金币不足);
			player.sendResponse(response);
			return;
		}
		
// 		player.subCoinse(betNum);
		player.addRoundBet(betNum);
		response = new ManyPepolRoomPayBetResponse(ManyPepolRoomPayBetResponse.SUCCESS,pos,betNum);
		player.sendResponse(response);
//		game.payBet(player,pos,betNum);
		
	}
	
	@Override
	public ArrayList<PlayerSimpleData> getTableList()
	{
		return game. getTableList();
	}

	@Override
	public void enter(Player player)
	{
		EnterManyPepolRoomResponse response = null;
		response = new EnterManyPepolRoomResponse(EnterManyPepolRoomResponse.SUCCESS);
		response.setData(getState(), getPlayers(), 0,getAllBet(),getJackpotNum(),getRoundNum(),game.getListAllBet());
		ConfigClassicGame configClassicGame = getConfig();
		response.initConfig(configClassicGame.getWaitPayBetTime(),configClassicGame.getWaitPlayerThink(),configClassicGame.getWaitPayBetTime());
		player.sendResponse(response);
		
		player.setManyPepolGame(game);
		game.addPlayer(player);
	}
	
	@PostConstruct
	private void init()
	{
		ServerEvent.addEvent(this);
		WorldMapUnitEvent.addEventListener(this);
		
		//上庄单牌率	上庄对子率	上庄大牌率	豹子AAA率	豹子率	顺金率
		configGameWanren = ManyPopelRoomDaoImpl.getGameWanren();
		Tool.print_debug_level0("初始化万人场概率,单牌:"+configGameWanren.getUp_banker_single_chance().floatValue() +",对子:"+configGameWanren.getUp_banker_double_chance().floatValue()
				+",大牌:"+configGameWanren.getUp_banker_big_chance().floatValue()+",AAA:"+configGameWanren.getLeopard_a_chance().floatValue()
				+",豹子:"+configGameWanren.getLeopard_chance().floatValue()+",顺金:"+configGameWanren.getShun_jin().floatValue());
	}
	
	public synchronized void  refreshKillroomConfig()
	{
		configGameWanren = ManyPopelRoomDaoImpl.getGameWanren();
		Tool.print_debug_level0("初始化万人场概率,单牌:"+configGameWanren.getUp_banker_single_chance().floatValue() +",对子:"+configGameWanren.getUp_banker_double_chance().floatValue()
				+",大牌:"+configGameWanren.getUp_banker_big_chance().floatValue()+",AAA:"+configGameWanren.getLeopard_a_chance().floatValue()
				+",豹子:"+configGameWanren.getLeopard_chance().floatValue()+",顺金:"+configGameWanren.getShun_jin().floatValue());
	}

	@Override
	public void comparerPoker(Player player, int pos,boolean isAllIn)
	{
		if(player.isWaitPlay())
			return ;
		
		ManyPepolRoomComparerPokerResponse response = null;
		ClassicGame classicGame = player.getManyPepolGame();
		Player[] players =  classicGame.getTablePlayers();
		Player tragetPlayer = null;
		
		if(player.getManyGamePos() == pos || pos < 0 || pos >= players.length || (tragetPlayer = classicGame.getTablePlayers()[pos]) == null || !tragetPlayer.isNormal())
		{
			response = new ManyPepolRoomComparerPokerResponse(ManyPepolRoomComparerPokerResponse.ERROR_比牌错误);
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家比牌,"ERROR_比牌错误,player:"+player.getSimpleData()+",tragetPos:"+pos);
			player.sendResponse(response);
			return;
		}
		
		int cost = classicGame.getNowBet();
		if(player.isCheckPoker())
		{
			cost *= 2;
		}
		
		if(player.getManyPepolRoomCalcCoins() < cost)
		{
			cost = (int) player.getManyPepolRoomCalcCoins();
		}
		if(isAllIn)
		{
			cost = 0;
		} 
		player.addRoundBet(cost);
		
		response = new ManyPepolRoomComparerPokerResponse(ManyPepolRoomComparerPokerResponse.SUCCESS);
		player.sendResponse(response);
		player.setComparerPoker();
		tragetPlayer.setComparerPoker();
		int comparer = pokerLogicService.pokerComparer(player.getPokers(), tragetPlayer.getPokers());
		if(comparer <= 0)//输了
		{
			classicGame.pokerComparerResult(player.getManyGamePos(), pos, player.getManyGamePos(),false,cost);
		}
		else
		{
			classicGame.pokerComparerResult(player.getManyGamePos(), pos, tragetPlayer.getManyGamePos(),true,cost);
		}
	}

	@Override
	public void fold(Player player)
	{
		if(player.isWaitPlay())
			return ;
		
		ManyPepolRoomFoldResponse response = null;
		ClassicGame classicGame = player.getManyPepolGame();
		if(!classicGame.runing())
		{
			response = new ManyPepolRoomFoldResponse(FoldResponse.ERROR_不在比赛中);
			player.sendResponse(response);
			return;
		}
		response = new ManyPepolRoomFoldResponse(ManyPepolRoomFoldResponse.SUCCESS);
		player.sendResponse(response);
		classicGame.fold(player);
	}

	/**
	 * 赢得人金币加了两次
	 */
	@Override
	public void allIn(Player player)
	{
		if(player.isWaitPlay())
			return ;
		
		ManyPepolRoomAllInResponse response = null;
		ClassicGame room = player.getManyPepolGame();
		
		if(player.getManyGamePos() != room.getPlayPos() || player.isDie())
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"ERROR_没轮到行动,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomAllInResponse(ManyPepolRoomAllInResponse.ERROR_没轮到行动);
			player.sendResponse(response);
			return;
		}
		
		if(room.getRoundNum() >= 20)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"ERROR_超过20回,player:"+player.getSimpleData());
			
			response = new ManyPepolRoomAllInResponse(ManyPepolRoomAllInResponse.ERROR_超过20回);
			player.sendResponse(response);
			return;
		}
		
		int alivePlayerNum = room.getAlivePlayerNum();
		if(alivePlayerNum != 2)
		{
			Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"ERROR_不在可全压人数,当前人数:"+alivePlayerNum+",player:"+player.getSimpleData());
			response = new ManyPepolRoomAllInResponse(ManyPepolRoomAllInResponse.ERROR_不在可全压人数);
			player.sendResponse(response);
			return;
		}
		
		int cost = room.getAllInBetNum();//当前房间全压数量
//		cost -= player.getBetNum();//扣掉已经下注的
		
		Player tragetPlayer = room.allInOtherPlayer(player.getManyGamePos());
		
		long allInPay = 0;
		if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"是否已经allIn:"+room.isAllIn()+",当前行动玩家:"+player.getNickName());
		if(!room.isAllIn())//发起人金币是否大于万人场全压金币
		{
			if(player.getManyPepolRoomCalcCoins() < cost)//全压发起人金币不够，使用他的金币作为cost
			{
				if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"发起人全压,发起人自己金币小于全压金币:"+player.getManyPepolRoomCalcCoins());
				cost = (int) player.getManyPepolRoomCalcCoins();
			}
			allInPay = cost;
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"发起人全压,根据全压发起人携带金币修正后的全压金额:"+allInPay+",当前行动玩家:"+player.getNickName());
			 
			if(tragetPlayer.getManyPepolRoomCalcCoins() < allInPay)//对家金币小于全压上限(此时全压金币已经和发起人金币对比过)
			{
				allInPay = tragetPlayer.getManyPepolRoomCalcCoins();
			 
				if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"发起人全压,对家金币小于全压上限(此时全压金币已经和发起人金币对比过)取对家金币:"+ tragetPlayer.getManyPepolRoomCalcCoins()+",allInPay:"+allInPay+",对面玩家:"+tragetPlayer.getSimpleData());
			}
			 
//			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"发起人全压,当前房间全压数量:"+room.getAllInBetNum()+",扣掉已经下注的,剩余需下:"+cost+",自己总金币："+player.getManyPepolRoomCalcCoins()+",最终下注:"+allInPay);
		}
		else
		{
			allInPay = room.getNowBet();
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"全压跟注,最终跟注金币:"+allInPay+",跟注玩家:"+player.getSimpleData());
		}
		
		response = new ManyPepolRoomAllInResponse(ManyPepolRoomAllInResponse.SUCCESS,allInPay);
		player.sendResponse(response);
//		player.subCoinse(allInPay);
		player.addRoundBet(allInPay);
		
		room.allIn(player,allInPay);
		if(room.isAllIn())//已经有一家全压，开始比牌
		{
			if(isAllInDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家全压,"已经有一家全压，开始比牌");
			
			comparerPoker(tragetPlayer,player.getManyGamePos(),true);
		}
		room.setAllIn();
	}

	/**
	 * 申请站起，只有api调用
	 */
	@Override
	public void mGameStandUp(Player player)
	{
		ManyPepolRoomStandUpResponse response = null;
		//还没坐下时不可站起
		if(!game.isSitdown(player))
		{
			if (game.removeBankerListExist(player.getUserId()))
			{
				for (Player iterable_element : game.getRoom().getAllPlayer())
				{
					iterable_element.sendResponse(new ManyPepolPush_otherPlayerResignBanker(player.getUserId()));
				}
				
				Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",不是庄家,在上庄列表!");
				response = new ManyPepolRoomStandUpResponse(ManyPepolRoomStandUpResponse.SUCCESS);
			}
			else
			{
				Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌, "玩家:" + player.getNickName() + ",userId:" + player.getUserId() + ",不是庄家,不在上庄列表!");
				response = new ManyPepolRoomStandUpResponse(ManyPepolRoomStandUpResponse.ERROR_还没上庄);
				player.sendResponse(response);
			}
			return;
		}
		
		if(game.playing())//比赛开始后进入待站起列表
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌,"比赛开始后进入待站起列表,player:"+player.getSimpleData());
			response = new ManyPepolRoomStandUpResponse(ManyPepolRoomStandUpResponse.SUCCESS);
			player.sendResponse(response);
			game.mGameAddStandUpList(player);
		}
		else
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_申请下桌,"比赛还未开始，直接站起,player:"+player.getSimpleData());
			//直接站起
			game.standUp(player);
		}
	}

	@Override
	public void leaveRoom(Player player, boolean isTV)
	{
		LeaveManyPepolRoomResponse response = null;
		ClassicGame room = player.getManyPepolGame();
		
		if(room == null)
		{
			Tool.print_error(MsgTypeEnum.manypepol_玩家离开,"玩家不在房间中,nickName:"+player.getNickName());
			 response = new LeaveManyPepolRoomResponse(LeaveManyPepolRoomResponse.ERROR_不在游戏中);
			 player.sendResponse(response);
			return;
		}
		
		 if(room.isSitdown(player))
		 {
			 if(isDebug)Tool.print_debug_level0(MsgTypeEnum.manypepol_玩家离开,"ERROR_坐下时不能离开,player:"+player.getSimpleData());
			 response = new LeaveManyPepolRoomResponse(LeaveManyPepolRoomResponse.ERROR_坐下时不能离开);
			 player.sendResponse(response);
			 return;
		 }
		 
		 response = new LeaveManyPepolRoomResponse(LeaveManyPepolRoomResponse.SUCCESS);
		 player.sendResponse(response);
		 player.clearRoomData(isTV);
		 game.removePlayer(player.getUserId());
	}

	@Override
	public void online(Player player, Channel channel)
	{
		
	}

	//TODO 断线直接离开bug，上线时重连到房间，但是没有座位
	@Override
	public void offlineOnGame(Player player)
	{
		ClassicGame classicGame = player.getManyPepolGame();
		if(classicGame != null && classicGame.isManyPepolRoom())
		{
			Tool.print_debug_level0("多人场玩家下线，player："+player.getSimpleData());
			classicGame.manypepolRoomOfflineLeave(player);
		}
	}

	@Override
	public void offlineOnServer(Player player)
	{
		
	}

	@Override
	public int getAllBet()
	{
		return game.getAllBet();
	}

	@Override
	public boolean reconnect(Player player,boolean isLogin)
	{
		ClassicGame classicGame = player.getManyPepolGame();
		if(classicGame != null)
		{
			classicGame.manyPepolReconnect(player,isLogin);
			classicGame.removeLeavePlayer(player.getUserId());
			return true;
		}
		return false;
	}

	@Override
	public JackpotData getJackpotData()
	{
		return game.getLastJackpot();
	}

	@Override
	public long getJackpotNum()
	{
		return  game.getLastJackpot().getAllJackpot();
	}

	@Override
	public int getRoundNum()
	{
		return game.getRoundNum();
	}

	@Override
	public GameWanren getWanrenConfig()
	{
		return configGameWanren;
	}
}
