package com.wt.xcpk.vo;

import java.util.ArrayList;
import java.util.Arrays;

import com.wt.xcpk.vo.poker.PokerVO;

public class JackpotData
{
	/**
	 * 当前奖池
	 */
	public long allJackpot=0;
	
	/**
	 * 上次大奖
	 */
	public long lastJackpot;
	
	/**
	 * 上次中将玩家名字
	 */
	public String lastName;
	public int roleId;
	public String lastHeadIconUrl;
	
	public int jackpotType;
	public long jackpotTime;
	public ArrayList<PokerVO> list_pokers = new ArrayList<PokerVO>();
	public JackpotData() {
	}
	
	public void setData(long allJackpot, long lastJackpot, String lastName, int roleId, String lastHeadIconUrl, int jackpotType, long jackpotTime,ArrayList<PokerVO> list_pokers)
	{
		this.allJackpot = allJackpot;
		this.lastJackpot = lastJackpot;
		this.lastName = lastName;
		this.roleId = roleId;
		this.lastHeadIconUrl = lastHeadIconUrl;
		this.jackpotType = jackpotType;
		this.jackpotTime = jackpotTime;
		
		this.list_pokers.clear();
		this.list_pokers.addAll(list_pokers);
	}



	public long getAllJackpot()
	{
		return allJackpot;
	}



	public void setAllJackpot(long allJackpot)
	{
		this.allJackpot = allJackpot;
	}



	public long getLastJackpot()
	{
		return lastJackpot;
	}



	public void setLastJackpot(long lastJackpot)
	{
		this.lastJackpot = lastJackpot;
	}



	public String getLastName()
	{
		return lastName;
	}



	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}



	public int getRoleId()
	{
		return roleId;
	}



	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}



	public String getLastHeadIcon()
	{
		return lastHeadIconUrl;
	}



	public void setLastHeadIcon(String lastHeadIconUrl)
	{
		this.lastHeadIconUrl = lastHeadIconUrl;
	}



	public int getJackpotType()
	{
		return jackpotType;
	}



	public void setJackpotType(int jackpotType)
	{
		this.jackpotType = jackpotType;
	}



	public long getJackpotTime()
	{
		return jackpotTime;
	}



	public void setJackpotTime(long jackpotTime)
	{
		this.jackpotTime = jackpotTime;
	}



	@Override
	public String toString()
	{
		return "JackpotData [allJackpot=" + allJackpot + ", lastJackpot=" + lastJackpot + ", lastName=" + lastName + ", roleId=" + roleId + ", lastHeadIcon=" + ", jackpotType=" + jackpotType + ", jackpotTime=" + jackpotTime + ", list_pokers=" + list_pokers + "]";
	}
}
