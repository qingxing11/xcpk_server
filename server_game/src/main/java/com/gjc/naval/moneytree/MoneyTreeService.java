package com.gjc.naval.moneytree;

import com.wt.naval.vo.user.Player;

public interface MoneyTreeService 
{
	void getMoney(int userId);
	//void  calculate(Player player);
	void  askMonetTree(int userId);
	
	/**
	 * 摇钱树升级
	 * @param userId
	 * @param topUp 充值金额
	 */
	void addTreeLv(int userId,int topUp);
	
	long calculateMoney(int treelv,int vipLv,int lv);
}
