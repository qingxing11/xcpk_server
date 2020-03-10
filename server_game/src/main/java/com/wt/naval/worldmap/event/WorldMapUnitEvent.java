package com.wt.naval.worldmap.event;

import java.util.ArrayList;

import com.wt.naval.vo.user.Player;

import io.netty.channel.Channel;

public class WorldMapUnitEvent
{
	private static ArrayList<MapUnitEventListener> list_mapUnitEvent = new ArrayList<>();

	public static void addEventListener(MapUnitEventListener listener)
	{
		list_mapUnitEvent.add(listener);
	}

	/**
	 * 单位完成移动
	 * 
	 * @param worldMapUnit
	 */


	public static void playerOnline(Player player, Channel channel)
	{
		for (MapUnitEventListener listener : list_mapUnitEvent)
		{
			if (listener instanceof PlayerListener)
			{
				((PlayerListener) listener).online(player, channel);
			}
		}
	}

	public static void playerOfflineOnGame(Player player)
	{
		for (MapUnitEventListener listener : list_mapUnitEvent)
		{
			if (listener instanceof PlayerListener)
			{
				((PlayerListener) listener).offlineOnGame(player);
			}
		}
	}

	public static void playerOfflineOnServer(Player player)
	{
		for (MapUnitEventListener listener : list_mapUnitEvent)
		{
			if (listener instanceof PlayerListener)
			{
				((PlayerListener) listener).offlineOnServer(player);
			}
		}
	}

}