package com.wt.naval.cache;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.archive.GMMailData;
import com.wt.config.BaseConfig;
import com.wt.factory.MyBeanFactory;
import com.wt.naval.dao.impl.ServerDaoImpl;
import com.wt.server.config.MyConfig;
import com.wt.util.WordFilter;

import data.data.Configs;

/**
 * 服务器全局数据,重启后无需恢复
 */
@Service
public class ServerCache
{
	public static final int cpu_number = Runtime.getRuntime().availableProcessors() * 2;

	public static boolean isServerShutDown = false;


	/** 全服邮件 **/
	public static ArrayList<GMMailData> arrayList_serverMail = new ArrayList<>();
	public static int index_serverMail;

 	@Deprecated
 	@Autowired
 	public static Configs configs;

	public static void init()
	{
		initServerConfig();
		initGameDef();
		// initServerMails();
		initFilterWords();
//		UserBiz.updateCountryCityDatas();
		
		initMap();
	}

	private static void initMap()
	{
		
	}

	private static void initFilterWords()
	{
		WordFilter.InitFilter(BaseConfig.getConfigPath() + MyConfig.instance().game_config_path + "filterword.txt");
	}

	public static void initServerMails()
	{
		arrayList_serverMail = ServerDaoImpl.getServerMails();
	}

	public static void initGameDef()
	{
  		configs = MyBeanFactory.getBean(Configs.class);
	}

	public static void addServerMail(GMMailData mailData)
	{
		mailData.mailId = index_serverMail++;
		arrayList_serverMail.add(mailData);
	}

	public static void initServerConfig()
	{
		// ServerDef_serverList.instance =
		// ServerDaoImpl.getServerList();
		// ServerDef_game.instance =
		// GameServerDaoImpl.getConfig_servergame();
		//
		// ServerDef_settings.instance =
		// ServerDaoImpl.getConfig_serversettings();
	}
	
}
