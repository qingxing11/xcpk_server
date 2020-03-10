package com.wt.event.server;

import java.util.ArrayList;

public class ServerEvent
{
	private static ArrayList<ServerEventInterface> list_eventInterfaces = new ArrayList<ServerEventInterface>();
	public static void addEvent(ServerEventInterface serverEventInterface)
	{
		list_eventInterfaces.add(serverEventInterface);
	}
	
	public static void gameServerStartup()
	{
		for (ServerEventInterface serverEventInterface : list_eventInterfaces)
		{
			if(serverEventInterface instanceof GameServerStartup)
			{
				((GameServerStartup)serverEventInterface).gameServerStartup();
			}
		}
	}
}