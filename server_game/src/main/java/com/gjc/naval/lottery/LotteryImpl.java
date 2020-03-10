package com.gjc.naval.lottery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.lottery.AskAutoLotteryResponse;
import com.gjc.cmd.lottery.AskLotteryResponse;
import com.gjc.cmd.lottery.AskLotteryTimeResponse;
import com.gjc.cmd.lottery.PushBottomPourTimeResponse;
import com.gjc.cmd.lottery.PushRunALotteryTimeResponse;
import com.gjc.cmd.lottery.PushTitleMoneyResponse;
import com.gjc.cmd.lottery.PushWinMoneyTxtResponse;
import com.gjc.naval.vo.lottery.LotteryDataVO;
import com.gjc.naval.vo.lottery.TxtVO;
import com.gjc.naval.vo.lottery.WinVO;
import com.wt.cmd.Response;
import com.wt.event.server.GameServerStartup;
import com.wt.event.server.ServerEvent;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.util.timetask.SimpleTaskUtil;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.poker.PokerVO;

import io.netty.channel.Channel;

@Service
public class LotteryImpl  implements LotteryService,Runnable,PlayerListener,GameServerStartup
{
	//TODO 推送总钱数，每局一清buylist
	
	ConcurrentHashMap<Integer,LotteryDataVO> buyList=new ConcurrentHashMap<Integer, LotteryDataVO>();
	ConcurrentHashMap<Integer,WinVO> winList=new ConcurrentHashMap<Integer, WinVO>();
	HashSet<Integer> userIdList=new HashSet<Integer>();
	private long titleMoney=0;
	private boolean isCanBuy=true;
	private int intTime=1000;//间隔5秒
	private int buyTime=120*1000;//毫秒
	private int winTime=130*1000;//毫秒
	private boolean isWinTime=false;
	
	private int buyCost=20000;
	private int typeMin=0;
	private int typeMax=5;
	private int defaultButNum=10;
	private float serviceChargePro =0.05f;
	private long curBuyTime=0;//当前购买时间
	private long curWaiteWinRime=0;//当前等待开奖时间
	private int indexTime=0;
	
	@Autowired
	private PlayerService playerService;
	
	/**************************计时器部分***************************/
//	private int cardNum=3;
//	
//	private ArrayList<PokerVO> list_roomPoker=new ArrayList<PokerVO>();
//	private int saveHistoryMax=20;//开奖记录数量
//	private ArrayList<Integer> recordWinType=new ArrayList<Integer>();
//	
//	@Autowired
//	private LotteryService lotterySerivice;
//	
//	@Autowired
//	private PokerLogicService pokerLogicService;
	/**************************计时器部分***************************/
	
	
	
	@PostConstruct
	private void ini()
	{
		WorldMapUnitEvent.addEventListener(this);
		ServerEvent.addEvent(this);
	}
	
	@Override
	public KeySetView<Integer, LotteryDataVO> getXZPlayer()
	{
		return buyList.keySet();
		
	}
	
	@Override
	public HashMap<Integer,WinVO> getWinnerAndMoney(int type,ArrayList<PokerVO> list)
	{
		if (buyList==null||buyList.size()==0) 
		{
			return null;
		}
		
		HashMap<Integer,WinVO> winner=new HashMap<Integer,WinVO>();
		int titleNum=0;
		
		for (LotteryDataVO vo : buyList.values()) 
		{
			if (vo.type==type) 
			{
				WinVO win=new WinVO();
				win.userId=vo.userId;
				win.type=type;
				win.card1=list.get(0);
				win.card2=list.get(1);
				win.card3=list.get(2);
				titleNum+=vo.typeNum;
				
				winner.put(win.userId, win);
			}
		}
		if (winner==null||winner.size()==0) 
		{
			return null;
		}
		for (WinVO vo : winner.values()) 
		{
			float buyNum=buyList.get(vo.userId).typeNum*1f;
			vo.money=(long)(titleMoney*(1-serviceChargePro)*buyNum/titleNum);
		}
		return winner;
	}
	
	@Override
	public void askLotteryTime(int userId)
	{
		int  residueBuyTime=-1;//剩余押注时间
		int residueWinTime=-1;//剩余开奖时间
		if (isCanBuy) 
		{
			int test=buyTime-(int)(MyTimeUtil.getCurrTimeMM()-curBuyTime);
			residueBuyTime=test>buyTime?buyTime:test;
		}
		if (isWinTime) 
		{
			int realTime=winTime-buyTime;
			int test=realTime-(int)(MyTimeUtil.getCurrTimeMM()-curWaiteWinRime);
			residueWinTime=test>realTime?realTime:test;
		}
		AskLotteryTimeResponse response=new AskLotteryTimeResponse(AskLotteryTimeResponse.SUCCESS,residueBuyTime,residueWinTime,LotteryTime.inst.getRecordWinType());
		LotteryDataVO lastVO=getBuyList(userId);
		if(lastVO != null)
		{
			response.curType = lastVO.type;
			response.curNum = lastVO.typeNum;
		}
		
		playerService.sendMsg(response, userId);
		
		pushTitleMoney(userId);
		
		addUserList(userId);
	}
	
	private void addUserList(int userId)
	{
		synchronized (userIdList)
		{
			userIdList.add(userId);
		}
	}
	
	@Override
	public HashSet<Integer> getUserList()
	{
		return userIdList;
	}
	
	@Override
	public HashSet<Integer> getResidueUserList()
	{
		HashSet<Integer> list=new HashSet<Integer>();
		for (Integer userId : userIdList) 
		{
			if (!buyList.containsKey(userId)) 
			{
				list.add(userId);
			}
		}
		return list;
	}
	
	@Override
	public void removeUserList(Integer userId)
	{
		synchronized (userIdList)
		{
			userIdList.remove(userId);
		}
	}
	
	@Override
	public void askLottery(int userId,int type,int typeNum)
	{
		if(typeNum<=0)
			return;
		
		Player player=playerService.getPlayer(userId);
		if (player==null) 
		{
			Tool.print_debug_level0("下注：：：：：玩家不存在："+userId);
			return;
		}
		if (isCanBuy) 
		{
			Tool.print_debug_level0("askLottery type="+type+",typeNum="+typeNum);
			long money=player.gameData.coins;
			long number=buyCost*typeNum;
			
			LotteryDataVO lastVO=getBuyList(userId);
			long lastMoney=0;
			if (lastVO!=null) 
			{
				lastMoney=buyCost*lastVO.typeNum;
			}
			long realMoney=0;
			if (number>=lastMoney) 
			{
				realMoney=number-lastMoney;
				
				if(money<realMoney)
				{
					Tool.print_debug_level0("玩家钱不够 money="+money+",realMoney="+realMoney);
					
					Response response=new AskLotteryResponse(AskLotteryResponse.Error_钱不够);
					player.sendResponse(response);
					return;
				}
				
				Tool.print_subCoins(player.getNickName(),realMoney,"彩票投注",player.getCoins());
				player.subCoinse(realMoney);	//玩家扣钱
				Tool.print_debug_level0("玩家扣钱 ="+realMoney+",number="+number+",lastMoney="+lastMoney);
			}
			else
			{
				Response response=new AskLotteryResponse(AskLotteryResponse.Error_不能押注);
				player.sendResponse(response);
				
				return ;
//				realMoney=number-lastMoney;
//				
//				Tool.print_coins(player.getNickName(),realMoney,"彩票投注",player.getCoins());
//				player.addCoins(Math.abs(realMoney));	//玩家加钱
				//Tool.print_debug_level0("玩家加钱 ="+realMoney+",number="+number+",lastMoney="+lastMoney);
			}
			
			titleMoney+=realMoney;
			
			Tool.print_debug_level0("玩家剩余金币："+player.getCoins()+",玩家下注数量："+typeNum);
			
			LotteryDataVO vo=new LotteryDataVO(userId,type,typeNum);
			buyList.put(userId, vo);
			
			Response response=new AskLotteryResponse(AskLotteryResponse.SUCCESS,titleMoney,realMoney,typeNum);
			player.sendResponse(response);
			
			pushTitleMoney();
		}
		else
		{
//			Tool.print_debug_level0("下注：：：：：玩家不能下注");
			
			Response response=new AskLotteryResponse(AskLotteryResponse.Error_不能押注);
			player.sendResponse(response);
		}
	}
	
	@Override
	public  void pushTitleMoney()
	{
		synchronized (userIdList)
		{
			Iterator<Integer> iterator = userIdList.iterator();
			while (iterator.hasNext())
			{
				int userId = iterator.next();
				pushTitleMoney(userId);
			}
		}
//		 for (int userId : userIdList) 
//		 {
//			 pushTitleMoney(userId);
//		}
	}
	
	@Override
	public  void pushTxtVo(ArrayList<TxtVO> list)
	{
		if (list==null||list.size()==0) 
		{
			return;
		}
		 for (int userId : userIdList) 
		 {
			PushWinMoneyTxtResponse response=new PushWinMoneyTxtResponse(list);
			playerService.sendMsg(response, userId);
		}
	}
	
	
	private void pushTitleMoney(int userId)
	{
		PushTitleMoneyResponse response=new PushTitleMoneyResponse(titleMoney);
		playerService.sendMsg(response, userId);
	}
	
	
	@Override
	public void askAutoLottery(int userId,int num)
	{
		if(num<=0)
			return;
		
		Player player=playerService.getPlayer(userId);
		if (player==null) 
		{
			Tool.print_error("玩家不存在："+userId);
			return;
		}
		int type=MyUtil.getRandom(typeMin, typeMax+1);
		int typeNum=num;
		
		if (isCanBuy) 
		{
			long money=player.gameData.coins;
			long number=buyCost*typeNum;
			
			LotteryDataVO lastVO=getBuyList(userId);
			long lastMoney=0;
			if (lastVO!=null) 
			{
				if(typeNum <= lastVO.typeNum)
				{
					Tool.print_debug_level0("彩票自动下注，已经下注:"+lastVO);
					Response response=new AskAutoLotteryResponse(AskAutoLotteryResponse.Error_不能押注);
					player.sendResponse(response);
					return;
				}
				lastMoney=buyCost*lastVO.typeNum;
			}
			
			Tool.print_debug_level0("玩家剩余金币："+player.getCoins()+",玩家下注数量："+typeNum+",需要金币:"+number+",lastMoney:"+lastMoney);
			long realMoney=0;
			if (number>=lastMoney) 
			{
				realMoney=number-lastMoney;
				
				if(money<realMoney)
				{
					Response response=new AskAutoLotteryResponse(AskAutoLotteryResponse.Error_钱不够);
					player.sendResponse(response);
					return;
				}
				Tool.print_subCoins(player.getNickName(),realMoney,"彩票自动投注",player.getCoins());
				player.subCoinse(realMoney);	//玩家扣钱
			}
			else
			{
				realMoney=number-lastMoney;
				Tool.print_coins(player.getNickName(),realMoney,"彩票自动投注",player.getCoins());
				player.addCoins(Math.abs(realMoney));	//玩家扣钱
			}
			
			titleMoney+=realMoney;
			
			if(lastVO != null)
			{
				lastVO.typeNum = num;
				type = lastVO.type;
			}
			else
			{
				LotteryDataVO vo=new LotteryDataVO(userId,type,typeNum);
				buyList.put(userId, vo);
			}
			
			Response response=new AskAutoLotteryResponse(AskAutoLotteryResponse.SUCCESS,titleMoney,type,realMoney,typeNum);
			player.sendResponse(response);
		}
		else
		{
			Response response=new AskAutoLotteryResponse(AskAutoLotteryResponse.Error_不能押注);
			player.sendResponse(response);
		}
	}
	
	@Override
	public LotteryDataVO getBuyList(int userId)
	{
		LotteryDataVO lastVO=null;
		if (buyList.containsKey(userId)) 
		{
			lastVO=buyList.get(userId);
		}
		return lastVO;
	}
	
	
	@Override
	public void run() 
	{
		try {
			indexTime++;
			//Tool.print_debug_level0("indexTime="+indexTime);
			if (indexTime==buyTime/intTime) 
			{
				RunLottery();
				LotteryTime.inst.run();
				buyList.clear();
//				Tool.print_debug_level0("彩票清理下注记录....");
			}
			else if(indexTime==winTime/intTime)
			{
				indexTime=0;
				setBuy();
				resWinTime();
				setCurBuyTime();
				
				for (int userId : getUserList()) 
				{
					//推送押注时间
					PushBottomPourTimeResponse pushBottomPourTimeResponse=new PushBottomPourTimeResponse(getBuyTime());
					playerService.sendMsg(pushBottomPourTimeResponse, userId);
				}
//				for (Player player : playerService.getAllPlayer()) 
//				{
//					//推送押注时间
//					PushBottomPourTimeResponse pushBottomPourTimeResponse=new PushBottomPourTimeResponse(getBuyTime());
//					playerService.sendMsg(pushBottomPourTimeResponse, player.getUserId());
//				}
			}
			
		} 
		catch (Exception e) {
			Tool.print_error("彩票报错：",e);
		}
		
	}

	/**开始彩票时间计时器*/
	public void startLotteryTimeTask()
	{
		try 
		{
			curBuyTime=MyTimeUtil.getCurrTimeMM();
			curWaiteWinRime=MyTimeUtil.getCurrTimeMM();
			SimpleTaskUtil.startTask("LotteryImpl", 0, intTime, this);
		} 
		catch (Exception e) 
		{
			Tool.print_error("彩票时间任务出错："+e);
		}
	}
	
	
	/**时间任务*/
	public void RunLottery()
	{
		if (!isWinTime)
		{
//			Tool.print_debug_level0("111111玩家不能下注！");
			
			resBuy();
			setWinTime();
			//推送等待时间
			curWaiteWinRime=MyTimeUtil.getCurrTimeMM();
			
//			for (Player player : playerService.getAllPlayer()) 
//			{
//				//推送等待时间
//				PushRunALotteryTimeResponse response=new PushRunALotteryTimeResponse(winTime-buyTime);
//				playerService.sendMsg(response, player.getUserId());
//			}
			
			 for (int userId : userIdList) 
			 {
				//推送等待时间
				PushRunALotteryTimeResponse response=new PushRunALotteryTimeResponse(winTime-buyTime);
				playerService.sendMsg(response, userId);
			}
		}
		else
		{
			Tool.print_error("押注计时器出错");
		}
	}
	public void setWinTime()
	{
		isWinTime=true;
	}
	@Override
	public void resWinTime()
	{
		isWinTime=false;
	}
	@Override
	public boolean getWinTime()
	{
		return this.isWinTime;
	}
	@Override
	public boolean getBuy()
	{
		return this.isCanBuy;
	}
	@Override
	public void setBuy()
	{
		isCanBuy=true;
	}
	
	public void resBuy()
	{
//		Tool.print_debug_level0("玩家不能下注！");
		isCanBuy=false;
	}
	
	public enum WinType
	{
	    豹子,顺金,金花,顺子,对子,单牌
	}
	@Override
	public int getBuyTime()
	{
		return this.buyTime;
	}
	
	@Override
	public int getBuyCost()
	{
		return this.buyCost;
	}
	
	@Override
	public void resTitleMoney()
	{
		titleMoney=0;
		
	}
	
	@Override
	public void setCurBuyTime()
	{
		this.curBuyTime=MyTimeUtil.getCurrTimeMM();
	}
	 
	@Override
	public void online(Player player, Channel channel)
	{
		
	}

	@Override
	public void offlineOnGame(Player player)
	{
		
	}
	
	private void test()
	{
		 ArrayList<TxtVO> list_TxtVOs = new ArrayList<TxtVO>();
		 
			for (int i = 0 ; i < 3 ; i++)
			{
				WinVO winVO = new WinVO();
				winVO.userId = i;
				winVO.money = i;

				TxtVO vo = new TxtVO(winVO, "name:" + i);

				list_TxtVOs.add(vo);
			}
			
			 for (Player userId : playerService.getAllPlayer()) 
			 {
				PushWinMoneyTxtResponse response=new PushWinMoneyTxtResponse(list_TxtVOs);
				userId.sendResponse(response);
			}
	}

	@Override
	public void offlineOnServer(Player player)
	{
		
	}

	@Override
	public void gameServerStartup()
	{
		startLotteryTimeTask();
	}
}

