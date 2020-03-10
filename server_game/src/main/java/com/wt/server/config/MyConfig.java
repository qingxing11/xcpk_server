package com.wt.server.config;

import com.wt.config.BaseConfig;

public final class MyConfig
{
	public static void main(String[] args)
	{
		instance();
	}

	private static transient MyConfig instance = null;
	/**
	 * 是否开启数据加解密
	 */
	public boolean isEncryption;// 是否开启数据加解密

	/** 日志的打印等级，任何大于等于指定等级的日志都被记录 */
	public int print_debug_leven;

	// 游戏配置
	public String game_config_path;

	public String authIP;
	public int authPort;
	
	/**
	 * 是否打印当前行数
	 */
	public boolean print_line;
	
	public static MyConfig instance()
	{
		if (instance == null)
		{
			instance = new MyConfig();
			BaseConfig.init(instance, "config/config.properties");
		}
		return instance;
	}

}
