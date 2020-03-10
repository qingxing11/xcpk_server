package com.wt.naval.timer.servertask;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.MsgType;
import com.wt.naval.biz.UserBiz;
import com.wt.naval.cache.UserCache;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.naval.worldmap.event.WorldMapUnitEvent;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.util.timetask.SimpleTaskUtil;

import io.netty.channel.Channel;

@Service
public class OnlineDetectionTaskImpl implements Runnable
{
	@Autowired
	private PlayerService playerService;
	
	private boolean isDebug = false;

	public static long interval = Tool.TIME_M * 5;

	
	@PostConstruct
	private void init()
	{
		try
		{
			SimpleTaskUtil.startTask("OnlineDetectionTask", interval, interval, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("检查玩家在线状态线程启动完毕（当前检测频率： " + interval / 1000 + "s）");
		System.out.println("在线玩家自动存档线程启动完毕（当前检测频率： " + interval / 1000 + "s）");
	}
	 
	@Override
	public void run()
	{
		try
		{
			if (isDebug)
				System.out.println("检查掉线...");
			// 检查掉线
			Iterator<Entry<Integer, Channel>> iter = UserCache.map_channel.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry<Integer, Channel> entry = (Map.Entry<Integer, Channel>) iter.next();
				Integer userId = entry.getKey();
				Channel channel = entry.getValue();
				if (channel != null)
				{
					MySession session = channel.attr(MySession.attr_session).get();
					if (session != null)
					{
						if (isDebug)
							System.out.println("session.isOffLine():" + session.isOffline());
					}
					if (session != null && session.isOffline())
					{
						if (isDebug)
							System.out.println("Tool.getCurrTimeMM() - session.getOfflineStartTime():" + (Tool.getCurrTimeMM() - session.getOfflineStartTime()));

						if (session.offlineOverTime())
						{
							Player player = playerService.getPlayer(userId);
							Tool.print_debug_level0(player.getNickName(), MsgType.LOG_检测离线时间, "离线超过" + MySession.OFFLINE_TIME / 1000 + "s，移除存档");

							UserCache.offline(userId);
							UserBiz.updateLogOutTime(userId, Tool.getCurrTimeMM());

							// 从服务器离线，移除存档
							WorldMapUnitEvent.playerOfflineOnServer(player);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.err.print("在线检查任务错误:");
			e.printStackTrace();
		}
	}
}
