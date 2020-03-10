package com.wt.naval.timer.servertask;

import java.util.Timer;
import java.util.TimerTask;

import com.wt.naval.biz.ServerBiz;
import com.wt.naval.server.GameServerImpl;
import com.wt.util.Tool;

public class SaveServerInfoTask
{
	public long interval =  60 * 1000L;

	private Timer timer;

	private TimerTask timerTask;

	public SaveServerInfoTask() {
	}

	public void init() {
		timer = new Timer();
		timerTask = new TimerTask() {
			public void run() {
				GameServerImpl bean = GameServerImpl.instance;
				
				if(bean!=null && bean.lastUpdateTime+5*60*1000<Tool.getCurrTimeMM()){
					ServerBiz.saveServerData(bean);
				}
			}
		};
	}

	public void start() {
		if (timer == null) {
			init();
		}
		timer.schedule(timerTask, 0, interval);
		System.out.println("服务器全局数据存储任务启动，当前频率:"+5*interval/1000+"秒。");
	}
}
