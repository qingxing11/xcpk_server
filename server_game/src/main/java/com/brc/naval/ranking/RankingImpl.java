package com.brc.naval.ranking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.impl.RankDaoImpl;
import com.wt.naval.event.timer.NewDayEventListener;
import com.wt.naval.event.timer.TimerEvent;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;
import com.wt.xcpk.rank.RankService;
import com.wt.xcpk.vo.RankLogVO;
import com.wt.xcpk.vo.RankVO;

@Service public class RankingImpl implements RankingService, NewDayEventListener
{
	@Autowired
	private RankService rankService;

	/**
	 * 赢金榜领取
	 */
	public ConcurrentHashMap<Integer, Integer> map_getingBigWin = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 充值榜
	 */
	public ConcurrentHashMap<Integer, Integer> map_getingPay = new ConcurrentHashMap<Integer, Integer>();

	/**
	 * 低保
	 */
	public ConcurrentHashMap<Integer, Integer> map_low = new ConcurrentHashMap<Integer, Integer>();

	@PostConstruct
	private void init()
	{
		TimerEvent.addEventListener(this);
	}

	@Override
	public boolean isFirst(int userId, int type)
	{
		if (type == 0)
		{
			ArrayList<RankVO> bigWinArrayList = rankService.getBigWinRank();
			if (bigWinArrayList == null)
				return false;
			for (int i = 0 ; i < 15 && i < bigWinArrayList.size() ; i++) // 赢金榜前15名
			{
				if (bigWinArrayList.get(i).userId == userId)
					return true;
			}
		}
		else if (type == 1)
		{
			ArrayList<RankVO> payArrayList = rankService.getPayRank();
			if (payArrayList == null)
				return false;
			for (int i = 0 ; i < 3 && i < payArrayList.size() ; i++) // 充值榜前3名
			{
				if (payArrayList.get(i).userId == userId)
					return true;
			}
		}
		return false;
	}

	// 判断是否领取奖励
	@Override
	public boolean isGeting(int userId, int type)
	{
		if (type == 0)
		{
			if(map_getingBigWin.containsKey(userId))
			{
				return true;
			}else
			{
				List<RankLogVO> list = RankDaoImpl.getBigWinRankRewardLog(userId,type);
				if(list==null)
				{
					return false;
				}

				if(list.size()>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		else if (type == 1)
		{
			if(map_getingPay.containsKey(userId))
			{
				return true;
			}else
			{
				List<RankLogVO> list = RankDaoImpl.getBigWinRankRewardLog(userId,type);
				if(list==null)
				{
					return false;
				}
	
				if(list.size()>0)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}

	// 领取奖励
	public long getReward(Player player, int type)
	{
		if (type == 0) // 赢金榜
		{
			ArrayList<RankVO> bigWinArrayList = rankService.getBigWinRank();
			for (int i = 0 ; i < 15 && i < bigWinArrayList.size() ; i++) // 赢金榜前15名
			{
				RankVO rankVO = bigWinArrayList.get(i);
				if (rankVO.userId == player.getUserId())
				{
//					player.gameData.coins += rankVO.score / 50;// 需要保存到数据库
//					PlayerDaoImpl.addPlayerCoins(rankVO.score / 50, player.getUserId());
					Tool.print_debug_level0("玩家赢金榜领取奖励,userId:"+player.getUserId()+",金币:"+(rankVO.score / 50));
					Tool.print_coins(player.getNickName(),rankVO.score / 50,"赢金榜领取奖励",player.getCoins());
					player.addCoins(rankVO.score / 50);
					
					map_getingBigWin.put(player.getUserId(), player.getUserId());
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
					String timer = df.format(new Date()).toString(); // 邮件的时间	
					
					RankDaoImpl.insertBigWinLog(player.getUserId(),rankVO.score / 50,timer,0);
					return rankVO.score / 50;
				}
			}

		}
		else if (type == 1) // 充值榜
		{
			ArrayList<RankVO> payArrayList = rankService.getPayRank();

			int index = -1;
			RankVO rankVO = null;
			for (int i = 0 ; i < 3 && i < payArrayList.size() ; i++) // 充值榜前3名
			{
				rankVO = payArrayList.get(i);
				if (rankVO.userId == player.getUserId())
				{
					index = i;
					break;
				}
			}
			if (index != -1)
			{
				long coins = 0;
				switch (index)
				{
					case 0:
						coins = rankVO.score*10000;
						break;
					case 1:
						coins = rankVO.score*10000 / 2;
						break;
					case 2:
						coins = rankVO.score*10000 / 5;
						break;
				}
				
				player.addCoins(coins);
				Tool.print_coins(player.getNickName(),coins,"充值榜领取金币",player.getCoins());
				map_getingPay.put(player.getUserId(), player.getUserId());
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
				String timer = df.format(new Date()).toString(); // 邮件的时间	
				
				RankDaoImpl.insertBigWinLog(player.getUserId(),coins,timer,1);
				return coins;
			}
		}
		return 0;
	}

	@Override
	public boolean canLow(int userId)
	{
		if (map_low.containsKey(userId) && map_low.get(userId) >= 3)
		{
			return false;
		}
		return true;
	}

	@Override
	public int getLow(Player player)
	{
		Tool.print_coins(player.getNickName(),5000,"低保",player.getCoins());
		player.addCoins(5000);
		
		if (map_low.containsKey(player.getUserId()))
		{
			map_low.put(player.getUserId(), map_low.get(player.getUserId()) + 1);
		}
		else
		{
			map_low.put(player.getUserId(), 1);
		}
		return map_low.get(player.getUserId());
	}

	@Override
	public void newDayEvent()
	{
		Tool.print_debug_level0("newDayEvent  RankingImp start");
		map_getingBigWin.clear();
		map_getingPay.clear();
		map_low.clear();
		Tool.print_debug_level0("newDayEvent  RankingImp end");
	}
}
