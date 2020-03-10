package com.wt.naval.po.server;

import java.io.Serializable;

/** 服务器实例化存档类 */
public class ServerBean implements Serializable
{
	private static final long serialVersionUID = 8744817121726812094L;

	public static transient ServerBean instance;

	public ServerBean()
	{}

	public static void init()
	{
		ServerBean.instance = new ServerBean();// TODO 重网络获取存档
	}

	public long lastUpdateTime;

	/**
	 * 所有已生成的订单。玩家完成订单，在收到阿里post前服务器维护，在重启时读出订单记录，等待阿里重新post
	 */
	public int maxPlayerNum;
	public long maxPlayerTime;

}
