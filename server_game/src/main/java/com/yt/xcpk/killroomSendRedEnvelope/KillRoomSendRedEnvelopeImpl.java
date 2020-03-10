package com.yt.xcpk.killroomSendRedEnvelope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.PlayerListener;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.quartz.SimpleTriggerQuartz;
import com.wt.xcpk.Room;
import com.wt.xcpk.killroom.KillRoomGardRedEnvelopeResponse;
import com.wt.xcpk.killroom.KillRoomSendRedEnvelopeOverResponse;
import com.wt.xcpk.killroom.KillRoomSendRedEnvelopeResponse;
import com.wt.xcpk.killroom.RedEnvelopeInfo;
import com.wt.xcpk.zjh.killroom.KillRoomService;
import com.wt.xcpk.zjh.killroom.TableMission;

import io.netty.channel.Channel;

@Service public class KillRoomSendRedEnvelopeImpl implements KillRoomSendRedEnvelopeService, PlayerListener
{
	public static final long send_redEnveTime = 5* MyTimeUtil.TIME_M;
	
	@Autowired
	private KillRoomService killRoom;
	@Autowired
	private PlayerService playerService;
	public static KillRoomSendRedEnvelopeImpl inst;

	private int redState = 1;
	private static final int SendRedEnveloped = 1; // 发红包结束
	private static final int SendRedEnveloping = 2; // 发红包中
	private ArrayList<RedEnvelopeInfo> redInfo; // 30个红包的详情
	private ArrayList<RedEnvelopeInfo> redIndex;// 被抢的红包索引编号

	@PostConstruct // 初始化方法 服务器启动的时候调用
	private void init()
	{
		inst = this;

		WorldMapUnitEvent.addEventListener(this);
		try
		{

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void online(Player player, Channel channel)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offlineOnGame(Player player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void offlineOnServer(Player player)
	{
		// TODO Auto-generated method stub

	}

	static Random random = new Random();

	@Override
	public ArrayList<RedEnvelopeInfo> getRenEnvelopeInfo(int userId, long total, int count, long max, long min)
	{
		long[] result = new long[count];
		long average = total / count;
		// long a = average - min;
		// long b = max - min;
		//
		// 这样的随机数的概率实际改变了，产生大数的可能性要比产生小数的概率要小。
		// 这样就实现了大部分红包的值在平均数附近。大红包和小红包比较少。
		// long range1 = sqr(average - min);
		// long range2 = sqr(max - average);
		for (int i = 0 ; i < result.length ; i++)
		{
			// 因为小红包的数量通常是要比大红包的数量要多的，因为这里的概率要调换过来。
			// 当随机数>平均值，则产生小红包
			// 当随机数<平均值，则产生大红包
			if (nextLong(min, max) > average)
			{
				// 在平均线上减钱
				// long temp = min + sqrt(nextLong(range1));
				long temp = min + xRandom(min, average);
				result[i] = temp;
				total -= temp;
			}
			else
			{
				// 在平均线上加钱
				// long temp = max - sqrt(nextLong(range2));
				long temp = max - xRandom(average, max);
				result[i] = temp;
				total -= temp;
			}
		}
		// 如果还有余钱，则尝试加到小红包里，如果加不进去，则尝试下一个。
		while (total > 0)
		{
			for (int i = 0 ; i < result.length ; i++)
			{
				if (total > 0 && result[i] < max)
				{
					result[i]++;
					total--;
				}
			}
		}
		// 如果钱是负数了，还得从已生成的小红包中抽取回来
		while (total < 0)
		{
			for (int i = 0 ; i < result.length ; i++)
			{
				if (total < 0 && result[i] > min)
				{
					result[i]--;
					total++;
				}
			}
		}

		if (redInfo != null)
		{
			redInfo.clear();
		}
		if (redIndex != null)
		{
			redIndex.clear();
		}
		for (int i = 0 ; i < result.length ; i++)
		{
			if (redInfo == null)
			{
				redInfo = new ArrayList<>();
			}
			RedEnvelopeInfo redEnvelope = new RedEnvelopeInfo(i, result[i],userId);
			redInfo.add(redEnvelope);
		}
		return redInfo;
	}

	/**
	 * 生产min和max之间的随机数，但是概率不是平均的，从min到max方向概率逐渐加大。
	 * 先平方，然后产生一个平方值范围内的随机数，再开方，这样就产生了一种“膨胀”再“收缩”的效果。
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	long xRandom(long min, long max)
	{
		return sqrt(nextLong(sqr(max - min)));
	}

	long sqrt(long n)
	{
		// 改进为查表？
		return (long) Math.sqrt(n);
	}

	long sqr(long n)
	{
		// 查表快，还是直接算快？
		return n * n;
	}

	long nextLong(long n)
	{
		return random.nextInt((int) n);
	}

	long nextLong(long min, long max)
	{
		return random.nextInt((int) (max - min + 1)) + min;
	}

	@Override
	public int returnSendRedState()
	{
		return redState;
	}

	@Override
	public void setSendRedStateToCanSend()
	{
		redState = SendRedEnveloped;
	}

	@Override
	public void setSendRedStateToNotCanSend()
	{
		redState = SendRedEnveloping;
		//启动一个计时器,当达到指定时间时清楚红包发送的状态
		
		SimpleTriggerQuartz.removeJob("startSendRedEnve");
		SimpleTriggerQuartz.startSimpleTask("startSendRedEnve", send_redEnveTime,0, 0, KillRoomSendJob.class);
	}

	// @Override
	// public void execute(JobExecutionContext context) throws
	// JobExecutionException {
	// if (MyTimeUtil.getCurrTimeMM() - stateTime > SendRedStayTime) {
	// setSendRedStateToCanSend();
	// Room room = killRoom.getRoom();
	// Collection<Player> list_roomPlayers = room.getAllPlayer();
	// KillRoomSendRedEnvelopeOverResponse response = new
	// KillRoomSendRedEnvelopeOverResponse();
	// if (list_roomPlayers != null) {
	// for (Player roomPlayer : list_roomPlayers) {
	// // 红包信息计算成功后发送给所有玩家
	// if (roomPlayer.isOnline()) {
	// roomPlayer.sendResponse(response);
	// }
	// }
	// }
	// }
	// }
	//
	// @Override
	// public void StartSimpleTask() {
	// SimpleTriggerQuartz.startSimpleTask("sendRedEnvelope", 0, 1000,
	// SendRedStayTime,
	// KillRoomSendRedEnvelopeImpl.class);
	// }

	@Override
	public synchronized void GrabRedEnvelope(RedEnvelopeInfo redEnvelope)
	{
		try
		{
			if (redIndex == null)
			{
				redIndex = new ArrayList<>();
			}
			Tool.print_debug_level0("添加前  redIndex.size():" + redIndex.size() + ",redIndex :" + redIndex);
			Tool.print_debug_level0("redInfo:" + redInfo.size() + ",redInfo :" + redInfo);

			if (redIndex.size() >= redInfo.size())
			{
				setSendRedStateToCanSend();
				Room room = killRoom.getRoom();
				Collection<Player> list_roomPlayers = room.getAllPlayer();
				KillRoomSendRedEnvelopeOverResponse response = new KillRoomSendRedEnvelopeOverResponse();
				if (list_roomPlayers != null)
				{
					for (Player roomPlayer : list_roomPlayers)
					{
						// 红包信息计算成功后发送给所有玩家
						if (roomPlayer.isOnline())
						{
							roomPlayer.sendResponse(response);
						}
					}
				}
				return;
			}
			redIndex.add(redEnvelope);
			Tool.print_debug_level0("添加后  redIndex.size():" + redIndex.size() + ",redIndex :" + redIndex);

		}
		catch (Exception e)
		{

		}
	}

	@Override
	public ArrayList<RedEnvelopeInfo> getCurrentShengYuRedEnvelope()
	{
		if (redInfo == null || redInfo.size() <= 0)
		{
			return null;
		}
		ArrayList<RedEnvelopeInfo> redEnvelope = new ArrayList<>();

		for (RedEnvelopeInfo red : redInfo)
		{
			redEnvelope.add(red);
		}
		if (redIndex != null && redIndex.size() > 0)
		{
			for (RedEnvelopeInfo envelope : redIndex)
			{

				for (int i = redEnvelope.size() - 1 ; i >= 0 ; i--)
				{

					if (redEnvelope.get(i).redEnvelopeIndex == envelope.redEnvelopeIndex)
					{
						redEnvelope.remove(i);
						break;
					}
				}
			}
		}
		return redEnvelope;
	}

	@Override
	public ArrayList<RedEnvelopeInfo> getCurrentBureauAllRedEnvelopeInfo()
	{

		return redInfo;
	}

	@Override
	public ArrayList<RedEnvelopeInfo> getCurrentWasRobbedRedEnvelope()
	{

		return redIndex;
	}

	@Override
	public void giveEnterRoomPlayerSendShengYuRedEnvelopeInfo(Player player)
	{
		ArrayList<RedEnvelopeInfo> list_redShengYu = getCurrentShengYuRedEnvelope();
		if (list_redShengYu == null || list_redShengYu.size() <= 0)
		{
			return;
		}
		KillRoomSendRedEnvelopeResponse response = new KillRoomSendRedEnvelopeResponse(KillRoomSendRedEnvelopeResponse.SUCCESS, list_redShengYu, redState,list_redShengYu.get(0).userId);
		player.sendResponse(response);
	}

	@Override
	public synchronized void getCurrentBeWasRobbedRenEnvelope(int userId, int redEnvelopeIndex, long redEnvelopeValue)
	{
		KillRoomGardRedEnvelopeResponse response = null;
		Player player = playerService.getPlayer(userId);
		ArrayList<RedEnvelopeInfo> list_alreadyHave = getCurrentShengYuRedEnvelope();

		Tool.print_debug_level0("剩余可抢红包为   list_alreadyHave :" + list_alreadyHave.size());
		if (list_alreadyHave == null || list_alreadyHave.size() <= 0)
		{
			response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.您的手慢了_红包已被抢完);
			player.sendResponse(response);
			return;
		}

		ArrayList<RedEnvelopeInfo> list_alreadyWasRobbed = getCurrentWasRobbedRedEnvelope();
		if (list_alreadyWasRobbed != null && list_alreadyWasRobbed.size() > 0)
		{
			Tool.print_debug_level0("已经被抢红包为   list_alreadyWasRobbed :" + list_alreadyWasRobbed.size());
			for (RedEnvelopeInfo redEnvelope : list_alreadyWasRobbed)
			{
				if (redEnvelope.redEnvelopeIndex == redEnvelopeIndex && redEnvelope.redEnvelopeValue == redEnvelopeValue)
				{
					response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.您的手慢了_当前红包被抢走了);
					player.sendResponse(response);
					return;
				}
			}
		}

		ArrayList<RedEnvelopeInfo> redInfo = getCurrentBureauAllRedEnvelopeInfo();

		RedEnvelopeInfo requsetRedEnvelope = null;
		for (RedEnvelopeInfo redEnvelope : redInfo)
		{
			if (redEnvelope.redEnvelopeIndex == redEnvelopeIndex && redEnvelope.redEnvelopeValue == redEnvelopeValue)
			{
				requsetRedEnvelope = redEnvelope;
				break;
			}
		}

		if (requsetRedEnvelope == null)
		{
			response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.请求错误_您当前抢的红包不存在);
			player.sendResponse(response);
			return;
		}

		GrabRedEnvelope(requsetRedEnvelope);
		Tool.print_coins(player.getNickName(),redEnvelopeValue, "抢红包，无调用",player.getCoins());
		player.addCoins(redEnvelopeValue);

		response = new KillRoomGardRedEnvelopeResponse(KillRoomGardRedEnvelopeResponse.SUCCESS, player.getUserId(), requsetRedEnvelope);
		Collection<Player> list_roomPlayers = killRoom.getRoom().getAllPlayer();
		if (list_roomPlayers != null)
		{
			for (Player roomPlayer : list_roomPlayers)
			{
				// 红包信息计算成功后发送给所有玩家
				if (roomPlayer.isOnline())
				{
					roomPlayer.sendResponse(response);
				}
			}
		}
		if (list_alreadyHave != null)
		{
			if (list_alreadyWasRobbed.size() == redInfo.size())
			{
				setSendRedStateToCanSend();
				KillRoomSendRedEnvelopeOverResponse sendRedEnvelopeOverResponse = new KillRoomSendRedEnvelopeOverResponse();
				if (list_roomPlayers != null)
				{
					for (Player roomPlayer : list_roomPlayers)
					{
						// 红包信息计算成功后发送给所有玩家
						if (roomPlayer.isOnline())
						{
							roomPlayer.sendResponse(sendRedEnvelopeOverResponse);
						}
					}
				}
			}
		}
	}

	public void clearRedPackState()
	{
		setSendRedStateToCanSend();
		setShengYuRedClear();
	}

	private void setShengYuRedClear()
	{
		Tool.print_debug_level0("到达时间  红包未抢完  处理消息");
		if (redInfo != null)
		{
			redInfo.clear();
		}
		if (redIndex != null)
		{
			redIndex.clear();
		}
		Collection<Player> list_roomPlayers = killRoom.getRoom().getAllPlayer();		
		KillRoomSendRedEnvelopeOverResponse sendRedEnvelopeOverResponse = new KillRoomSendRedEnvelopeOverResponse();
		if (list_roomPlayers != null)
		{
			for (Player roomPlayer : list_roomPlayers)
			{
				// 红包信息计算成功后发送给所有玩家
				if (roomPlayer.isOnline())
				{
					roomPlayer.sendResponse(sendRedEnvelopeOverResponse);
				}
			}
		}
	}
}
