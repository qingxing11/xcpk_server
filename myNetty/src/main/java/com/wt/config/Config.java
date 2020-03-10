package com.wt.config;

public class Config
{
	/**
	  * #序列化工具选择,现支持
	<br>0：<protobuf>google源生protobuf,需要写proto
	<br>1：<protobuf-net>c#的protobuf_net项目,java端使用protostuff，不需要写proto 
	<br>2：<fastjson>阿里fastjosn，明文,支持最广泛
	 */
	public int serializerUtil;
	
	/**监听端口*/
	public int port;
 
	/**api模块类包路径*/
	public String apiClassPath;
	
	/**
	 * 是否记录返回消息
	 */
	public boolean responseLog;
	
	private static transient Config instance = null;
	public static Config instance()
	{
		if(instance == null)
		{
			System.out.println("初始化配置:netty.properties");
			instance = new Config();
			BaseConfig.init(instance,"config/netty.properties");
		}
		return instance;
	}
}
