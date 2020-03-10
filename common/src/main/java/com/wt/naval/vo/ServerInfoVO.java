package com.wt.naval.vo;

public class ServerInfoVO
{
	public String host;
	public int port;
	public String name;
	
	public ServerInfoVO()
	{
		
	}

	@Override
	public String toString()
	{
		return "ServerInfoVO [host=" + host + ", port=" + port + ", name=" + name + "]";
	}
}
