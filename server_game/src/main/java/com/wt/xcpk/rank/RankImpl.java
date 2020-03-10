package com.wt.xcpk.rank;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.user.push.Push_coinsChange;
import com.wt.event.server.GameServerStartup;
import com.wt.event.server.ServerEvent;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.impl.RankDaoImpl;
import com.wt.naval.event.timer.FiveMinuteEventListener;
import com.wt.naval.event.timer.NewDayEventListener;
import com.wt.naval.event.timer.TimerEvent;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.sort.MySortUtil;
import com.wt.xcpk.vo.RankVO;
import com.wt.xcpk.zjh.killroom.KillRoomService;

@Service public class RankImpl implements RankService, NewDayEventListener,GameServerStartup
{
	private static final boolean isBigWinDebug = true;
	
	private static final int PAY_RANK_MAX = 15;
	private static final int BIG_WIN_RANK_MAX = 15;

	private ArrayList<RankVO> list_coinsRank = new ArrayList<RankVO>();
	private ArrayList<RankVO> list_payRank = new ArrayList<RankVO>();
	private ArrayList<RankVO> list_lastDayPayRank = new ArrayList<RankVO>();

	private ArrayList<RankVO> list_bigWinRank = new ArrayList<RankVO>();
	private ArrayList<RankVO> list_lastDayWinRank = new ArrayList<RankVO>();

	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private KillRoomService killRoomService;

	@Override
	public void addPayNum(int userId, int payNum, String nickName, int level)
	{
		synchronized (list_payRank)
		{
//			if (list_payRank.size() < PAY_RANK_MAX)
//			{
				RankVO rankVO = getPayRank(userId);
				if (rankVO == null)
				{
					rankVO = new RankVO(payNum, userId, nickName, 0, level);
					list_payRank.add(rankVO);
				}
				else
				{
					rankVO.addScore(payNum);
				}

				list_payRank.sort(MySortUtil.highToLowByIndexLong());
				setRankVORank(list_payRank);
//			}
//			else
//			{
//				if (payNum > list_payRank.get(list_payRank.size() - 1).score)
//				{
//					RankVO rankVO = getPayRank(userId);
//					if (rankVO == null)
//					{
//						rankVO = new RankVO(payNum, userId, nickName, 0, level);
//						list_payRank.remove(list_payRank.size() - 1);
//						list_payRank.add(rankVO);
//					}
//					else
//					{
//						rankVO.addScore(payNum);
//					}
//					
//					list_payRank.sort(MySortUtil.highToLowByIndexLong());
//					setRankVORank(list_payRank);
//				}
//			}
		}
	}

	@Override
	public void addWinConisNum(int userId, long winNum, String nickName, int level)
	{
		synchronized (list_bigWinRank)
		{
//			if(isBigWinDebug)Tool.print_debug_level0("addWinConisNum--->userId:"+userId+",winNum:"+winNum+",nickName:"+nickName+",level:"+level);
			
			RankVO rankVO = getRank(userId);
			if (rankVO == null)
			{
				rankVO = new RankVO(winNum, userId, nickName, 0, level);
				list_bigWinRank.add(rankVO);
			}
			else
			{
				rankVO.addScore(winNum);
			}

			list_bigWinRank.sort(MySortUtil.highToLowByIndexLong());
			setRankVORank(list_bigWinRank);
//			if(isBigWinDebug)Tool.print_debug_level0("addWinConisNum--->userId:"+userId+",sumNum:"+rankVO.getScore());
			
//			if (list_bigWinRank.size() > BIG_WIN_RANK_MAX)
//			{
//				list_bigWinRank.remove(list_bigWinRank.size() - 1);//超员时移除原最后一名
//			}
			
			if(isBigWinDebug)Tool.print_debug_level0("addWinConisNum--->userId:"+userId+",sumNum:"+rankVO.getScore());
			
//			if (list_bigWinRank.size() < BIG_WIN_RANK_MAX)
//			{
//				RankVO rankVO = getRank(userId);
//				if (rankVO == null)
//				{
//					rankVO = new RankVO(winNum, userId, nickName, 0, level);
//					list_bigWinRank.add(rankVO);
//				}
//				else
//				{
//					rankVO.addScore(winNum);
//				}
//
//				list_bigWinRank.sort(MySortUtil.highToLowByIndexLong());
//				setRankVORank(list_bigWinRank);
//				if(isBigWinDebug)Tool.print_debug_level0("addWinConisNum--->userId:"+userId+",sumNum:"+rankVO.getScore());
//			}
//			else
//			{
//				if (winNum > list_bigWinRank.get(list_bigWinRank.size() - 1).score)//排行榜满了，超过最后一名
//				{
//					RankVO rankVO = getRank(userId);
//					if (rankVO == null)
//					{
//						rankVO = new RankVO(winNum, userId, nickName, 0, level);
//						list_bigWinRank.add(rankVO);
//						list_bigWinRank.remove(list_bigWinRank.size() - 1);//如果自己不在榜，加进榜时移除原最后一名
//					}
//					else
//					{
//						rankVO.addScore(winNum);
//					}
//					
//					list_bigWinRank.sort(MySortUtil.highToLowByIndexLong());
//					setRankVORank(list_bigWinRank);
//					if(isBigWinDebug)Tool.print_debug_level0("addWinConisNum--->userId:"+userId+",sumNum:"+rankVO.getScore());
//				}
//			}
		}
	}

	private RankVO getPayRank(int userId)
	{
		RankVO rankVO = null;
		for (RankVO item : list_payRank)
		{
			if (item.getUserId() == userId)
			{
				rankVO = item;
				break;
			}
		}
		return rankVO;
	}
	
	private RankVO getRank(int userId)
	{
		RankVO rankVO = null;
		for (RankVO item : list_bigWinRank)
		{
			if (item.getUserId() == userId)
			{
				rankVO = item;
				break;
			}
		}
		return rankVO;
	}

	@Override
	public ArrayList<RankVO> getPayRank()
	{
		return list_lastDayPayRank;
	}

	@Override
	public ArrayList<RankVO> getCoinsRank()
	{
		if (list_coinsRank.size() == 0)
		{
			synchronized (list_coinsRank)
			{
				if (list_coinsRank.size() == 0)
				{
					ArrayList<RankVO> list = RankDaoImpl.getCoinsRank();
					list_coinsRank.addAll(list);
				}
			}
		}
		return list_coinsRank;
	}

	@Override
	public ArrayList<RankVO> getBigWinRank()
	{
		return list_lastDayWinRank;
	}

	@Override
	public void newDayEvent()
	{
		Tool.print_debug_level0("RankImpl---->newDayEvent start");
		payRankCalc();
		bigWinCalc();

		list_coinsRank.clear();
		Tool.print_debug_level0("RankImpl---->newDayEvent end");
	}
	
	private void bigWinCalc()
	{
		ArrayList<RankVO> list_bigWinRank_temp = new ArrayList<RankVO>();
		synchronized (list_bigWinRank)
		{
			for(int i=0;i<Math.min(BIG_WIN_RANK_MAX, list_bigWinRank.size());i++)
			{
				list_bigWinRank_temp.add(list_bigWinRank.get(i));
			}
			if(isBigWinDebug)Tool.print_debug_level0("bigWinCalc---->list_bigWinRank_temp:"+list_bigWinRank_temp);
//			if(list_bigWinRank.size()>BIG_WIN_RANK_MAX)
//			{
//				List<RankVO> list = list_bigWinRank.subList(BIG_WIN_RANK_MAX, list_bigWinRank.size());
//				list_bigWinRank.removeAll(list);
//			}
		}
		int size = list_bigWinRank_temp.size();
		list_bigWinRank.clear();
		list_lastDayWinRank.clear();
		Object[][] par = new Object[size][];
		
		for (int i = 0 ; i < size ; i++)
		{
			RankVO rankVO = list_bigWinRank_temp.get(i);
			par[i] = new Object[5];
			par[i][0] = rankVO.userId;
			par[i][1] = rankVO.nickName;
			par[i][2] = rankVO.score;
			par[i][3] = rankVO.rank;
			par[i][4] = rankVO.level;
		}

		if(list_bigWinRank_temp.size() > 0)
		{
			RankDaoImpl.updateBigWin(par);
		}
		
		list_lastDayWinRank.addAll(list_bigWinRank_temp);
//		if(isBigWinDebug)Tool.print_debug_level0("bigWinCalc---->list_lastDayWinRank:"+list_lastDayWinRank);
		onHandlerKillroomBigWin();
	
	}

	private void onHandlerKillroomBigWin()
	{
		ArrayList<RankVO> list_updateBigwin = new ArrayList<RankVO>();
		int rangeNum = list_lastDayWinRank.size() > 3 ? 4 : list_lastDayWinRank.size();
		for (int i = 0 ; i < rangeNum ; i++)
		{
			RankVO rankVO = list_lastDayWinRank.get(i);
			list_updateBigwin.add(rankVO);
		}
//		if(isBigWinDebug)Tool.print_debug_level0("onHandlerKillroomBigWin---->list_lastDayWinRank:"+list_lastDayWinRank);
		killRoomService.updateBigWinPlayer(list_updateBigwin);
	}

	private void payRankCalc()
	{
		ArrayList<RankVO> list_payRank_temp = new ArrayList<RankVO>();
		synchronized (list_payRank)
		{
	//		if(isBigWinDebug)Tool.print_debug_level0("payRankCalc---->list_payRank:"+list_payRank);
			for(int i=0;i<Math.min(PAY_RANK_MAX, list_payRank.size());i++)
			{
				list_payRank_temp.add(list_payRank.get(i));
			}
			
//			if(list_payRank.size()>PAY_RANK_MAX)
//			{
//				List<RankVO> list = list_payRank.subList(PAY_RANK_MAX, list_payRank.size());
//				list_payRank.removeAll(list);
//			}
		}
		
		int size = list_payRank_temp.size();
		list_payRank.clear();
		list_lastDayPayRank.clear();
		Object[][] par = new Object[size][];
		
		// 1充值榜显示前20名-每日充值榜前三次名，分别获取充值所得钻石100%50%30%金币奖励，次日直接到账（奖励按1元=1钻石=1万金币结算）
		for (int i = 0 ; i < size ; i++)
		{
			RankVO rankVO = list_payRank_temp.get(i);
//			Player player = playerService.getPlayer(rankVO.userId);

			par[i] = new Object[5];
			par[i][0] = rankVO.userId;
			par[i][1] = rankVO.nickName;
			par[i][2] = rankVO.score;
			par[i][3] = rankVO.rank;
			par[i][4] = rankVO.level;
			
//			long getCoinsGift = 0;
//			switch (i)
//			{
//				case 0:
//					getCoinsGift = rankVO.score * 10000;
//					break;
//
//				case 1:
//					getCoinsGift = (long) (rankVO.score * 10000 * 0.5f);
//					break;
//
//				case 2:
//					getCoinsGift = (long) (rankVO.score * 10000 * 0.3f);
//					break;
//
//				default:
//					break;
//			}
//
//			if (player != null && player.isOnline())
//			{
//				Tool.print_coins(player.getNickName(),getCoinsGift,"充值榜赢金",player.getCoins());
//				player.addCoins(getCoinsGift);
//				Push_coinsChange push_coinsChange = new Push_coinsChange(getCoinsGift);
//				player.sendResponse(push_coinsChange);
//			}
//			else
//			{
//				PlayerDaoImpl.addPlayerCoins(getCoinsGift, rankVO.userId);
//			}
		}
		
		if(list_payRank_temp.size() > 0)
		{
			RankDaoImpl.updatePayNum(par);
		}
		
//		list_lastDayPayRank.clear();
		list_lastDayPayRank.addAll(list_payRank_temp);
	}

	private void setRankVORank(ArrayList<RankVO> list_rankVOs)
	{
		for (int i = 0 ; i < list_rankVOs.size() ; i++)
		{
			list_rankVOs.get(i).setRank(i);
		}
	}

	@PostConstruct
	private void init()
	{
		TimerEvent.addEventListener(this);
		ServerEvent.addEvent(this);
	}

	private void initBigwinRank()
	{
		list_lastDayWinRank = (ArrayList<RankVO>) RankDaoImpl.getBigWinList();
		list_lastDayWinRank.sort(MySortUtil.highToLowByIndexLong());
		setRankVORank(list_lastDayWinRank);
		
		onHandlerKillroomBigWin();
	}

	private void initPayNumRank()
	{
		list_lastDayPayRank = (ArrayList<RankVO>) RankDaoImpl.getPayNumList();
		list_lastDayPayRank.sort(MySortUtil.highToLowByIndexLong());
		setRankVORank(list_lastDayPayRank);
	}
	
	@Override
	public void gameServerStartup()
	{
		initBigwinRank();
		initPayNumRank();
	}

}