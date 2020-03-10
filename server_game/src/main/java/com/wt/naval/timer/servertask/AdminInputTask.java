package com.wt.naval.timer.servertask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.notice.NoticePush_serverNotice;
import com.wt.cmd.notice.NoticePush_shutDownResponse;
import com.wt.naval.cache.ServerCache;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.server.GameServerImpl;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;

@Service
public class AdminInputTask implements Runnable
{
	@Autowired
	private AdminRunCmdTask adminRunCmdTask;
	
	public static final int CMD_关机 = 0;
	public static final int CMD_显示玩家 = 1;
	public static final int CMD_公告 = 2;
	public static final int CMD_加载服务器端配置数据 = 3;
	public static final int CMD_清除所有房间 = 4;
	
	public static final String[][] ADMIN_CMD = {
		{"关机","关闭","shut down"},//CMD_关机
		{"显示玩家数量","show player","玩家数量","show"},//显示玩家数
		{"say:","公告:"},//管理员发言
		{"reload"},//重载服务器端数据文件
		{"clearRoom"}//清除服务器中所有房间
	};
	
	public static final int OPTION_时间 = 0;
	public static final int OPTION_在线 = 1;
	public static final int OPTION_最高 = 2;
	public static final String[] cmd_option = {
		"-s ",// 时间
		"-ol",//在线的
		"-h",
	};
	

	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	public AdminInputTask() {
	}

	@PostConstruct
	private void init() {
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				adminInput();
			}
		}).start();
	}
	
	public void adminInput()
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true)
		{
			try
			{
				String cmd = br.readLine();
				if(cmd== null)
				{
					System.err.println("未知指令:" + cmd);
					return;
				}
				boolean isRun = false;
				for (int i = 0 ; i < ADMIN_CMD.length ; i++)
				{
					for (int j = 0 ; j < ADMIN_CMD[i].length ; j++)
					{
						int indexOf = cmd.indexOf(ADMIN_CMD[i][j]);
						if(indexOf != -1 && indexOf < ADMIN_CMD[i][j].length())
						{
							ArrayList<Integer> arrayList_option = new ArrayList<Integer>();
							for (int j2 = 0 ; j2 < cmd_option.length ; j2++)
							{
								if (cmd.indexOf(cmd_option[j2]) != -1)
								{
									arrayList_option.add(j2);
								}
							}
							isRun = true;
							runCmd(i,cmd,arrayList_option);
							break;
						}
					}
				}
				if (!isRun)
					System.err.println("未知指令:" + cmd);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	protected void runCmd(int cmdIndex,String cmd,ArrayList<Integer> arrayList_option)
	{
		switch (cmdIndex)
		{
			case CMD_关机:
				startCmd_shutDown(cmd,arrayList_option);
				break;
				
			case CMD_显示玩家:
				srartCmd_showPlayersNumber(arrayList_option);
				break;

			case CMD_公告:
				startCmd_showSay(cmd);
				break;
				
			case CMD_加载服务器端配置数据:
				startCmd_reloadServerData();
				break;
				
			case CMD_清除所有房间:
				startCmd_clearAllRoom();
				break;
				
			default:
				break;
		}
	}
	
	private void startCmd_clearAllRoom()
	{
	}

	public static void startCmd_reloadServerData()
	{
		ServerCache. initServerConfig();
		ServerCache.initGameDef();
		
		System.out.println("重新加载服务器端配置成功。。。");
	}

	private void startCmd_showSay(String cmd)
	{
		int index = -1;
		for (int i = 0 ; i < ADMIN_CMD[CMD_公告].length ; i++)
		{
			index = cmd.indexOf(ADMIN_CMD[CMD_公告][i]);
			if(index != -1)
			{
				index = ADMIN_CMD[CMD_公告][i].length();
				break;
			}
		}
		
		String sayStr = cmd.substring(index);
		System.out.println("sayStr:"+sayStr+",index:"+index);
		
		NoticePush_serverNotice push = new NoticePush_serverNotice(sayStr);
		GameServerHelper.serverPushAll(push);
	}

	private void srartCmd_showPlayersNumber(ArrayList<Integer> arrayList_option)
	{
		int state = 0;
		for (int i = 0 ; i < arrayList_option.size() ; i++)
		{
			switch (arrayList_option.get(i))
			{
				case OPTION_在线:
					if(state == 0)
					{
						state = 1;
					}
					else if(state == 2)
					{
						state = 3;
					}
					
					break;
					
				case OPTION_最高:
					if(state == 0)
					{
						state = 2;
					}
					else if(state == 1)
					{
						state = 3;
					}
					break;
			}
		}
		
		switch (state)
		{
			case 0:
				Tool.print_debug_level0("当前总玩家数:"+0+",可接受-ol[在线],-h[最高]");
				break;

			case 1:
				Tool.print_debug_level0("当前在线玩家数:"+UserCache.map_onlines.size());
				Iterator<Entry<Integer, Player>> iter = UserCache.map_onlines.entrySet().iterator();
				while (iter.hasNext())
				{
					Map.Entry<Integer, Player> entry = (Map.Entry<Integer, Player>) iter.next();
					Tool.print_debug_level0("昵称:"+entry.getValue().getNickName()+",userid:"+entry.getKey());
				}
				break;
				
			case 2:
				System.out.println("最高在线数:"+GameServerImpl.instance.maxPlayerNum +",发生在:"+df.format(new Date(GameServerImpl.instance.maxPlayerTime)));
				break;
				
			case 3:
				Tool.print_debug_level0("当前在线玩家数:"+UserCache.map_onlines.size()+",最高在线数:"+GameServerImpl.instance.maxPlayerNum +",发生在:"+df.format(new Date(GameServerImpl.instance.maxPlayerTime)));
				break;
			default:
				break;
		}
	}

	public  void startCmd_shutDown(int time)//600,300,60,30,10
	{
		NoticePush_shutDownResponse push = new NoticePush_shutDownResponse(time);
		GameServerHelper.serverPushAll(push);
		
		adminRunCmdTask.addCmd_shutDown(time);
		Tool.print_debug_admin("管理员关机，延迟:"+time+"秒" +(time == 0 ? ",可接受-s[时间]" : ""));
	}
	
	private void startCmd_shutDown(String cmd,ArrayList<Integer> arrayList_option)//600,300,60,30,10
	{
		int time = 0;
		for (int i = 0 ; i < arrayList_option.size() ; i++)
		{
			switch (arrayList_option.get(i))
			{
				case OPTION_时间:
					time = runOption_delay(cmd);
					break;

				default:
					break;
			}
		}
		
		NoticePush_shutDownResponse push = new NoticePush_shutDownResponse(time);
		GameServerHelper.serverPushAll(push);
		
		adminRunCmdTask.addCmd_shutDown(time);
		Tool.print_debug_admin("管理员关机，延迟:"+time+"秒" +(time == 0 ? ",可接受-s[时间]" : ""));
	}
	
	private int runOption_delay(String cmd)
	{
		String option_str = cmd_option[OPTION_时间];
		String time_str = cmd.substring(cmd.indexOf(option_str)+option_str.length());
		int time = 0;
		try
		{
			time = Integer.parseInt(time_str);
		}
		catch (NumberFormatException e)
		{
			time = -1;
			System.err.println("字符串转int错误:"+option_str+"后面必须跟数字!");
		}
		return time;
	}

	@Override
	public void run()
	{
		System.out.println("cmd----adminInput");
		adminInput();
	}

//	public void start()
//	{
//		if (timer == null) {
//			init();
//		}
//		timer.schedule(timerTask, 0, interval);
//		Tool.print_debug_level0("服务器管理员控制台启动。。");
//	}
}
