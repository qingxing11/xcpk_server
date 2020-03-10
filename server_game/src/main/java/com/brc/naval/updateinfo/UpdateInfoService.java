package com.brc.naval.updateinfo;

import com.wt.naval.vo.user.Player;

public interface UpdateInfoService
{
	/**
	 * 修改昵称,并且消耗一张房卡
	 */
	public boolean updateNickname(Player player,String nickName);
	
	/**
	 *修改性别
	 */
	public boolean updateGarder(Player player,int gender);
	
	/**
	 * 修改签名
	 */
	public boolean updateSign(Player player,String sign);
}
