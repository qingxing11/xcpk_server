package com.wt.naval.server;

import org.springframework.stereotype.Service;

@Service
public class GameServerImpl implements GameServerService
{
	public static transient GameServerImpl instance;

	public GameServerImpl()
	{}

	public void init()
	{
		
	}

	public long lastUpdateTime;

	public int maxPlayerNum;
	public long maxPlayerTime;

}
