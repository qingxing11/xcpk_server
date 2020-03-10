package com.brc.naval.lucky;

import java.util.ArrayList;

import com.brc.cmd.mail.Attach;
import com.wt.naval.vo.user.Player;

public interface LuckyService
{
	/**
	 * 幸运大转盘抽奖
	 */
	public int getLucky(Player player);

	/**
	 * 宝箱
	 */
	public ArrayList<Attach> getLuckyBox(Player player);

	/**获取当月转盘倍数*/
	public float lucky(int userId);
	
	void resetLuckyRate(int userId);
}
