package com.brc.naval.updateinfo;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.vo.user.Player;

@Service public class UpdateInfoImpl implements UpdateInfoService
{
	@PostConstruct // 初始化方法 服务器启动的时候调用
	private void init()
	{

	}

	@Override
	public boolean updateNickname(Player player, String nickName)
	{
		int count = PlayerDaoImpl.updateNicknameByChangeNameCard(player.getUserId(), nickName);
		if (count > 0)
		{
			player.gameData.setNickName(nickName);
			return true;
		}
		return false;
	}

	public boolean updateGarder(Player player, int gender)
	{
		int count = PlayerDaoImpl.updateGender(player.getUserId(), gender);
		if (count > 0)
		{
			player.gameData.setRoleId(gender);
			return true;
		}
		return false;
	}

	public boolean updateSign(Player player, String sign)
	{
		int count = PlayerDaoImpl.updateSign(player.getUserId(), sign);
		if (count > 0)
		{
			player.gameData.sign = sign;
			return true;
		}
		return false;
	}
}
