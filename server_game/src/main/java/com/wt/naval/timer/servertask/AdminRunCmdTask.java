package com.wt.naval.timer.servertask;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.iserver.MyNetty;
import com.wt.naval.cache.ServerCache;
import com.wt.naval.main.GameMain;
import com.wt.util.Tool;
import com.wt.util.timetask.SimpleTaskUtil;

@Service
public class AdminRunCmdTask implements Runnable
{
	public static ArrayList<AdminCmd> arrayList_AdminCmds = new ArrayList<>();
	
	public long interval =100L;

	private Timer timer;

	private TimerTask timerTask;

	public AdminRunCmdTask() {
	}

	@PostConstruct
	private void init() {
		try
		{
			SimpleTaskUtil.startTask("AdminCmd", 1000, interval, this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void addCmd_shutDown(int time)
	{
		synchronized (arrayList_AdminCmds)
		{
			ServerCache.isServerShutDown = true;
		}
		
		
		AdminCmd adminCmd_shutDown = new AdminCmd();
		adminCmd_shutDown.startShutDown(time);
		arrayList_AdminCmds.add(adminCmd_shutDown);
	}
	
	public  void runCmd()
	{
		if(arrayList_AdminCmds.size() > 0)
		{
			if(arrayList_AdminCmds.get(0).run())
			{
				arrayList_AdminCmds.remove(0);
			}
		}
	}

	@Override
	public void run()
	{
		runCmd();
	}
}

class AdminCmd
{
	public static final int CMD_关机 = 0;
	
	public int cmd_type;
	
	public int time;

	public long startTime;
	public boolean run()
	{
		switch (cmd_type)
		{
			case CMD_关机:
				return runShutDown(time);
				
			default:
				return false;
		}
	}

	public void startShutDown(int time)
	{
		cmd_type = AdminCmd.CMD_关机;
		this.time = time;
		startTime = Tool.getCurrTimeMM();
	}

	private boolean runShutDown(int time)
	{
		long now = Tool.getCurrTimeMM() - startTime;
		if(now > time * 1000l)
		{
			try
			{
				MyNetty.instance().shutDown();
				GameMain.inst.shutDown();
				Tool.print_debug_admin("完成关闭服务,退出");
				System.exit(0);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}