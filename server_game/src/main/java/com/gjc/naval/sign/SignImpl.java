package com.gjc.naval.sign;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.sign.PushSignDataResponse;
import com.gjc.cmd.sign.PushSignResponse;
import com.wt.naval.dao.impl.SignDaoImpl;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.event.timer.FiveMinuteEventListener;
import com.wt.naval.event.timer.NewDayEventListener;
import com.wt.naval.event.timer.TimerEvent;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.xcpk.vo.RankVO;

import data.data.Configs;
import model.UserSign;

@Service
public class SignImpl implements SignService,NewDayEventListener
{
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private Configs config;
	
	@PostConstruct
	private void ini()
	{
		TimerEvent.addEventListener(this);
	}
	
	@Override
	public void registerPlayerInitSign(int userId, long startTime, boolean one, boolean two, boolean three,
			boolean four, boolean five, boolean six, boolean seven) {
		SignDaoImpl.insetUserSign(userId, one, two, three, four, five, six, seven, startTime);
	}

	@Override
	public void initPlaySign(int userId) 
	{
		Player player = playerService.getPlayer(userId);
		ArrayList<Boolean> list=player.getSignList();
		ArrayList<Boolean> sendList =list;
		boolean todayIsSign=false;
		
		if (list== null||list.size()==0) 
		{
			UserSign sign = SignDaoImpl.selectSign(userId);
			if (sign == null)
			{
				//Tool.print_error("签到错误！");
				return;
			}
			list = new ArrayList<Boolean>();
			list.add(sign.isOne());
			list.add(sign.isTwo());
			list.add(sign.isThree());
			list.add(sign.isFour());
			list.add(sign.isFive());
			list.add(sign.isSix());
			list.add(sign.isSeven());
			sendList =list;
			player.setSignTime(sign.getStartTime());
			player.setSignList(list);
			//Tool.print_debug_level0("玩家上次签到时间：" + sign.getStartTime());
		}
		if (sendList==null)
		{
			Tool.print_error("错误，玩家登录时，推送数据，签到数据没有！");
			return;
		}

		long oldTime = player.getSignTime();
		long curTime = MyTimeUtil.getCurrTimeMM();
 		boolean isToday = MyTimeUtil.isSameDay(oldTime, curTime);
 		Tool.print_debug_level0("initPlaySign---->isToday:"+isToday);
//		isToday = true;
		
		if (isToday&&list.get(0)) 
		{
			todayIsSign=false;
		}
		else
		{
			todayIsSign=true;
		}
		boolean first=false;
		
		if (!isToday) 
		{
			int day = MyTimeUtil.subDay(oldTime, curTime);
			if (day > 1 &&list.get(0)) 
			{
				first=true;
			}
		}
		if (!first) 
		{
			int i=0;
			for (Boolean boolean1 : list) 
			{
				if (!boolean1) 
				{
					break;
				}
				i++;
			}
			if (i==7) 
			{
				first=true;
			}
		}
		
		if (first) 
		{
			sendList=new ArrayList<Boolean>();
			for (int i = 0; i < 7; i++) {
				sendList.add(false);
			}
		}
		
		PushSignDataResponse response=new PushSignDataResponse(sendList,todayIsSign);
		playerService.sendMsg(response, userId);
		
	}

	@Override
	public boolean sign(int userId,int daysIndex) 
	{
		Player player = playerService.getPlayer(userId);
		if (player == null || player.getSignList() == null)
		{
			Tool.print_error("签到错误！玩家不存在：" + userId + "player.getSignList()=" + player.getSignList());
			return false;
		}
		long oldTime = player.getSignTime();
		long curTime = MyTimeUtil.getCurrTimeMM();
		boolean isToday = MyTimeUtil.isSameDay(oldTime, curTime);
		int oneDay = 0;
		boolean one = player.getSignList().get(oneDay);
		if (isToday && one)
		{
			Tool.print_debug_level0("同一天并且不是第一天（排除初始登录）");
			return false;
		}
		int day = MyTimeUtil.subDay(oldTime, curTime);
		if (day > 1)
		{
			if (oneDay != daysIndex - 1)
			{
				Tool.print_debug_level0("错误 ：签到天数不一致" + oneDay + ",signDay" + daysIndex);
				return false;
			}
			resOneDay(userId, player, curTime, oneDay);
			Tool.print_debug_level0("清零，第一天");
			return true;
		}
		else if (day >= 0)
		{
			boolean suss = true;// 签到是否成功

			int dayIndex = 0;
			for (Boolean days : player.getSignList())
			{
				if (!days)
				{
					break;
				}
				dayIndex++;
			}
			boolean signDay = true;
			int dayMax = 7;
			if (dayIndex == dayMax) // 满七天。重置
			{
				if (oneDay != daysIndex - 1)
				{
					Tool.print_debug_level0("错误 ：签到天数不一致" + oneDay + ",signDay" + daysIndex);
					return false;
				}
				// resOneDay(userId, player, curTime, oneDay);
				suss = sameDay(userId, player, curTime, dayIndex - 1, signDay);
			}
			else
			{
				if (dayIndex != daysIndex - 1)
				{
					System.out.println("错误 ：签到天数不一致" + dayIndex + ",signDay" + daysIndex);
					return false;
				}
				suss = sameDay(userId, player, curTime, dayIndex, signDay);
			}
			Tool.print_debug_level0("第" + dayIndex + "+1天");
			return suss;
		}
		else
		{
			Tool.print_debug_level0("出错");
			return false;
		}
	}

	private boolean sameDay(int userId, Player player, long curTime, int dayIndex, boolean signDay)
	{
		player.getSignList().set(dayIndex, signDay);
		player.setSignTime(curTime);

		dayIndex++;
		
		int vipLv=player.getVipLv();
		long vipMoney=0;
		if (config.getDataVipSign().containsKey(vipLv)) 
		{
			vipMoney=config.getDataVipSign().get(vipLv).AddMoney;
		}
		long money=config.getDataSign().get(dayIndex).AddMoney+Math.abs(vipMoney);
		Tool.print_coins(player.getNickName(),money,"每日签到",player.getCoins());
		player.addCoins(money);
//		UserDaoImpl.updateUserCoins(player.getCoins(), userId);
		
		switch (dayIndex) 
		{
		case 1:
			SignDaoImpl.updataSign(userId, signDay,curTime);
			return true;
		case 2:
			SignDaoImpl.updataSign2(userId, signDay,curTime);
			return true;
		case 3:
			SignDaoImpl.updataSign3(userId, signDay,curTime);
			return true;
		case 4:
			SignDaoImpl.updataSign4(userId, signDay,curTime);
			return true;
		case 5:
			SignDaoImpl.updataSign5(userId, signDay,curTime);
			return true;
		case 6:
			SignDaoImpl.updataSign6(userId, signDay,curTime);
			return true;
		case 7:
			SignDaoImpl.updataSign7(userId, !signDay,curTime);//第七天保持没签到状态
			return true;

		default:
			System.out.println("出错");
			return false;
		}
	}

	private void resOneDay(int userId, Player player, long curTime, int oneDay) 
	{
		player.setSignTime(curTime);
		ArrayList<Boolean> list = player.getSignList();
		boolean resSign = false;
		for (int i = 0; i < list.size(); i++) 
		{
			list.set(i, resSign);
		}
		list.set(oneDay, !resSign);
		SignDaoImpl.updataUserSign(userId, !resSign, resSign, resSign, resSign, resSign, resSign, resSign, curTime);
		
		Tool.print_coins(player.getNickName(),config.getDataSign().get(oneDay+1).AddMoney,"每日签到",player.getCoins());
		player.addCoins(config.getDataSign().get(oneDay+1).AddMoney);
//		UserDaoImpl.updateUserCoins(player.getCoins(), userId);
	}

	@Override
	public void newDayEvent() 
	{
		Tool.print_debug_level0("newDayEvent Signlmp start");
		//更新签到标签
		PushSignResponse response=new PushSignResponse(true);
		for (Player player : playerService.getAllPlayer()) 
		{
			if (player!=null) 
			{
				player.sendResponse(response);
			}
		}
		
		Tool.print_debug_level0("newDayEvent Signlmp end");
	}
}
