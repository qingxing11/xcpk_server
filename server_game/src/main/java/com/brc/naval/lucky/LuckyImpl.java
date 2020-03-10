package com.brc.naval.lucky;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.mail.Attach;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.impl.YTDaoImpl;
import com.wt.naval.module.player.PlayerImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.MyUtil;
import com.wt.util.Tool;

@Service 
public class LuckyImpl implements LuckyService
{
	@Autowired
	private PlayerService playerService;
	// 幸运大转盘抽奖
	public int getLucky(Player player)
	{
		int index = MyUtil.getRandom(6);
		long luckyTime = MyTimeUtil.getCurrTimeMM();
		player.gameData.luckyNum = index;
		player.gameData.isLucky = true;
		PlayerDaoImpl.updateLucky(index, luckyTime, player.getUserId());
		return index;
	}

	// 宝箱
	@Override
	public ArrayList<Attach> getLuckyBox(Player player)
	{
		int vipLv = player.gameData.vipLv;
		Random random = new Random();
		ArrayList<Attach> attachs = new ArrayList<Attach>();
		if (isCoins())
		{
			int num = 0;
			switch (vipLv)
			{
				case 1:
					num = random.nextInt(200) + 100;
					break;
				case 2:
					num = random.nextInt(200) + 300;
					break;
				case 3:
					num = random.nextInt(300) + 500;
					break;
				case 4:
					num = random.nextInt(400) + 800;
					break;
				case 5:
					num = random.nextInt(800) + 1200;
					break;
				default:
					break;
			}
			Attach attach = new Attach(0, num);
			attachs.add(attach);
			Tool.print_debug_level0("AAAAAAAAAAAAAAA获取到的是金币："+num+"                vip: "+vipLv  );
		}
		else
		{
			Tool.print_debug_level0("AAAAAAAAAAAAAAAA获取到的是道具");
			switch (vipLv)
			{
				case 1:
				case 2:
				case 3:
					int num = random.nextInt(3) + 1;
					Attach attach = new Attach(num, 1);
					attachs.add(attach);
					break;
				case 4:
					for (int i = 0 ; i < 2 ; i++)
					{
						int nums = random.nextInt(3) + 1;
						Attach atta = new Attach(nums, 1);
						attachs.add(atta);
					}
					break;
				case 5:
					for (int i = 0 ; i < 3 ; i++)
					{
						int nums = random.nextInt(3) + 1;
						Attach atta = new Attach(nums, 1);
						attachs.add(atta);
					}
					break;
				default:
					break;
			}
		}

		for (Attach attach : attachs)
		{
			switch (attach.id)
			{
				case 0:
					Tool.print_coins(player.getNickName(),attach.num,"开宝箱",player.getCoins());
					player.addCoins(attach.num);
					
					break;
				case 1:
					player.addTimeNum(1);
					break;
				case 2:
					player.addRobPosNum(1);
					break;
				case 3:
					player.addModifyNickName(1);
					break;
				default:
					break;
			}
		}

		for (Attach attach : attachs)
		{
			Tool.print_debug_level0("宝箱AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"+attach.toString());
		}
		return attachs;
	}

	/**获取当月转盘倍数*/
	public float lucky(int userId)
	{
		Player player=playerService.getPlayer(userId);
		if(player.gameData.isLucky)
		{
			switch (player.gameData.luckyNum)
			{
				case 0:
					return 6;
				case 1:
					return 3.6f;
				case 2:
					return 5;
				case 3:
					return 3.3f;
				case 4:
					return 4.0f;
				case 5:
					return 6.0f;
				default:
					return 1;
			}
		}
		return 1;
	}
	
	/**
	 * 判断随机的是不是金币
	 */
	public boolean isCoins()
	{
		Random random = new Random();
		if (random.nextInt(5) < 4)
			return true;
		else
		{
			return false;
		}
	}

	@Override
	public void resetLuckyRate(int userId)
	{
		Player player=playerService.getPlayer(userId);
		player.gameData.luckyNum = -1;
		PlayerDaoImpl.updatePlayerLuckyNum(-1,userId);
	}
}
