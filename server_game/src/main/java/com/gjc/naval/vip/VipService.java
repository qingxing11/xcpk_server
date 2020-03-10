package com.gjc.naval.vip;

import java.util.HashMap;

import model.UserVip;

public interface VipService 
{
	/**VIP配置表数据*/
	HashMap<Integer, UserVip> getMapVIP() ;
	
	/**玩家充值处理*/
	void TopUp(int userId,int topUpmoney);
	
	/**获取对应等级的VIP数据*/
	UserVip getVipData(int lv);
	
	/**获得玩家等级称谓*/
	String GetLvName(int lv);
	
	/**是否有转账功能*/
	boolean haveTransferAccounts(int vipLv);
	
	/**转账手续费*/
	float transferAccountsPer(int vipLv);
	
	/**大喇叭聊天所需金币*/
	 int typhonChatMoney(int vipLv);
	 
	 /**有无房间聊天功能*/
	 boolean haveRoonChat(int vipLv);
	 
	 /**每日登录奖励金币*/
	 int loginAwardMoney(int vipLv);
	 
	 /**是否有宝箱功能*/
	 boolean haveTreasureBox(int vipLv);
}
