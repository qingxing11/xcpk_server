package com.gjc.naval.lottery;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.lottery.PushLotteryResultResponse;
import com.gjc.naval.vo.lottery.LotteryDataVO;
import com.gjc.naval.vo.lottery.TxtVO;
import com.gjc.naval.vo.lottery.WinVO;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyUtil;
import com.wt.util.Tool;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.poker.PokerVO;
import com.wt.xcpk.zjh.logic.PokerLogicService;

@Service
public class LotteryTime
{
	public static LotteryTime inst;
	private int cardNum=3;
	
	private ArrayList<PokerVO> list_roomPoker=new ArrayList<PokerVO>();
	private int saveHistoryMax=20;//开奖记录数量
	private ArrayList<Integer> recordWinType=new ArrayList<Integer>();
	
	@Autowired
	private LotteryService lotterySerivice;
	
	@Autowired
	private PokerLogicService pokerLogicService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private RankService rankService;
	@PostConstruct
	private void ini()
	{
		inst=this;
		initRoomPoker(pokerLogicService.getInitPokersWithOutJoker());
	}
	
	private void addRecordWinType(int type)
	{
		if (recordWinType.size()==saveHistoryMax) {
			recordWinType.remove(0);
		}
		recordWinType.add(type);
	}
	
	public ArrayList<Integer> getRecordWinType()
	{
		return recordWinType;
	}
	
	public void initRoomPoker(ArrayList<PokerVO> pokers)
	{
		list_roomPoker.addAll(pokers);
	}
	
	public ArrayList<PokerVO> getRandomPoker(int num)
	{
		ArrayList<PokerVO> list = new ArrayList<PokerVO>();
		for (int i = 0 ; i < num ; i++)
		{
			int random = MyUtil.getRandom(list_roomPoker.size());
			PokerVO pokerVO = list_roomPoker.get(random);
			list.add(pokerVO);
		}
		return list;
	}
	
	
	public void run() {
		
		try {
			RunLottery();
		} 
		catch (Exception e) {
			Tool.print_error("彩票开奖报错：",e);
		}
	}
	
	private String getTypeDesc(int type)
	{
		switch(type)
		{
		case 0:return "豹子";
		case 1:return "顺金";
		case 2:return "金花";
		case 3:return "顺子";
		case 4:return "对子";
		case 5:return "单牌";
		}
		
		return "";
	}
	public void RunLottery() 
	{
		if (lotterySerivice.getWinTime()) 
		{
			//随机类型以及牌型
			 int type=MyUtil.getRandom(6);
			 ArrayList<PokerVO> list=pokerLogicService.getPokerType(type);//正式版本
			 if (list==null||list.size()==0) 
			 {
				Tool.print_error("获取牌型为空：随机类型以及牌型--彩票");
			}
			 addRecordWinType(type);//记录开奖牌型
			 Tool.print_debug_level0("彩票开奖","牌型："+type+",扑克列表:"+list);
			// ArrayList<PokerVO> list=getRandomPoker(cardNum);//TODO 测试用
			
			//查找可以赢的玩家，以及金额
			HashMap<Integer,WinVO> winner=lotterySerivice.getWinnerAndMoney(type, list);
			
			if (lotterySerivice.getXZPlayer()==null|| lotterySerivice.getXZPlayer().size()==0) 
			{
				//Tool.print_debug_level0("没有玩家押彩票");
				for (int userId :  lotterySerivice.getUserList()) 
				{
					PushLotteryResultResponse response=new PushLotteryResultResponse(PushLotteryResultResponse.Error_没有下注,false,type,list.get(0),list.get(1),list.get(2));
					playerService.sendMsg(response, userId);
				}
				return;
			}
			ArrayList<TxtVO> winnerAbout=new ArrayList<TxtVO>();//奖金大于100万者
			
			
			 for (int userId : lotterySerivice.getXZPlayer()) 
			 {
				 Player player = playerService.getPlayer(userId);
				 if(player != null)
				 {
					 player.clear();
				 }
			 }
			if (winner==null||winner.size()==0) 
			{
				//奖金累加(不处理)
				//发送信息
				 for (int userId : lotterySerivice.getXZPlayer()) 
				 {
					PushLotteryResultResponse response=new PushLotteryResultResponse(PushLotteryResultResponse.SUCCESS,false,type,list.get(0),list.get(1),list.get(2));
					playerService.sendMsg(response, userId);
				}
			}
			else
			{
				//奖金清零
				lotterySerivice.resTitleMoney();
				HashMap<Integer, Long> saveMoney=new HashMap<Integer, Long>();
				//发送信息
				 for (int userId : lotterySerivice.getXZPlayer()) 
				 {
						LotteryDataVO dataVo = lotterySerivice.getBuyList(userId);
						
						if(dataVo==null)
							continue;
						
					 //记录玩家压注
					 Tool.print_debug_level0("彩票压注","玩家："+userId+",注数:"+dataVo.typeNum*lotterySerivice.getBuyCost()+",压注位置:"+getTypeDesc(dataVo.type));
					 
					 boolean win=false;
					 if (winner.containsKey(userId)) 
					 {
						WinVO vo=winner.get(userId);
						win=true;
						int num=100;//一百万
						if (vo.money>10000*num) 
						{
							PlayerSimpleData playerSimple=null;
							try {
								 playerSimple=playerService.getPlayerSimpleData(userId);
							} catch (Exception e) 
							{
								Tool.print_error(e);
							}
							
							if (playerSimple!=null)
							{
								TxtVO txtVO=new TxtVO(vo,playerSimple.getNickName());
								winnerAbout.add(txtVO);
							}
						}
						
						Player player=playerService.getPlayer(userId);
						if (player!=null) 
						{
							Tool.print_coins(player.getNickName(),vo.money,"彩票中奖",player.getCoins());
							player.addCoins(vo.money);
						}
						else
						{
							saveMoney.put(userId, vo.money);
							//UserDaoImpl.updateUserCoins(userId, vo.money);
						}
						
						PushLotteryResultResponse response=new PushLotteryResultResponse(PushLotteryResultResponse.SUCCESS,win,vo.money,type,vo.card1,vo.card2,vo.card3);
						playerService.sendMsg(response, userId);
						
						 if (saveMoney!=null&&saveMoney.size()>0) 
						 {
							 	Object[][] bathSave=new Object[saveMoney.size()][2];
							 	int index=0;
								for (int id : saveMoney.keySet()) 
								{
									bathSave[index][1]=id;
									bathSave[index][0]=saveMoney.get(id);
									index++;
								}
								UserDaoImpl.updateUserCoinsBatch(bathSave);
						}
						 
						if(vo.money>0)
						{
							rankService.addWinConisNum(player.getUserId(), vo.money-dataVo.typeNum*lotterySerivice.getBuyCost(),player.getNickName(),player.getLevel());
						}
					}
					 else
					 {
						 WinVO vo=winner.get(userId);
						PushLotteryResultResponse response=new PushLotteryResultResponse(PushLotteryResultResponse.SUCCESS,win,type,list.get(0),list.get(1),list.get(2));
						playerService.sendMsg(response, userId);
					 }
				}
			}
			
			for (Integer userId : lotterySerivice.getResidueUserList()) 
			{
				PushLotteryResultResponse response=new PushLotteryResultResponse(PushLotteryResultResponse.Error_没有下注,false,type,list.get(0),list.get(1),list.get(2));
				playerService.sendMsg(response, userId);
			}
			

			lotterySerivice.pushTxtVo(winnerAbout);
			
			lotterySerivice.pushTitleMoney();
		}
		else
		{
			Tool.print_error("开奖计时器出错");
		}
	}

}
