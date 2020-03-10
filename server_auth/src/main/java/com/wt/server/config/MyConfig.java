package com.wt.server.config;

import com.wt.config.BaseConfig;

public final class MyConfig
{
	private static transient MyConfig instance = null;
	/**
	 * 是否开启数据加解密
	 */
	public boolean isEncryption;// 是否开启数据加解密

	/** 日志的打印等级，任何大于等于指定等级的日志都被记录 */
	public int print_debug_leven;

	public boolean responseLog;
	
	public String centerEureka_host;
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
