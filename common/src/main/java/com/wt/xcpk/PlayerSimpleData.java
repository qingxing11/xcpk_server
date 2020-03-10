package com.wt.xcpk;

import java.io.Serializable;

public class PlayerSimpleData implements Serializable
{
	private static final long serialVersionUID = 1510875567313581199L;

	public int userId;
	public String nickName;
	public String headIconUrl;
	public long coins;
	//经典场位置
	private int classicGamepos = -1;
	//万人场位置
	private int manyGamepos = -1;
	//水果机位置
	private int fruitGamepos = -1;
	//通杀场位置
	private int killRoomPos = -1;
	
	public int roleId;
	public int vipLv;
	public int lv;
	// 钻石
	public int crystals;

	// 签名
	public String sign;

	// 胜场

	public int victoryNum;

	// 负场
	public int failureNum;
	
	/**
	 * 在当前游戏中的下注数
	 */
	public long betNum;
	
	/**是否开牌*/
	public boolean isCheckPoker;
	
	/**0,待机   1:普通  2:失败*/
	public int gameState;;
	
	public PlayerSimpleData(int userId, String nickName, String headIconUrl, long coins, int classicGamepos,int manyGamepos,int fruitGamepos, int roleId, int vipLv, int lv, int crystals, String sign, int victoryNum, int failureNum,long betNum,int killRoomPos)
	{
		this.userId = userId;
		this.nickName = nickName;
		this.headIconUrl = headIconUrl;
		this.coins = coins;
		this.classicGamepos = classicGamepos;
		this.manyGamepos = manyGamepos;
		this.fruitGamepos = fruitGamepos;
		this.roleId = roleId;
		this.vipLv = vipLv;
		this.lv = lv;
		this.crystals = crystals;
		this.sign = sign;
		this.victoryNum = victoryNum;
		this.failureNum = failureNum;
		this.betNum = betNum;
		this.killRoomPos = killRoomPos;
	}


	public int getLv()
	{
		return lv;
	}
	
	public long getCoins()
	{
		return coins;
	}

	public void setCoins(long coins)
	{
		this.coins = coins;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public String getHeadIconUrl()
	{
		return headIconUrl;
	}

	public void setHeadIconUrl(String headIconUrl)
	{
		this.headIconUrl = headIconUrl;
	}

	@Override
	public String toString()
	{
		return "PlayerSimpleData [userId=" + userId + ", nickName=" + nickName + ", headIconUrl=" + headIconUrl + ", coins=" + coins + ", classicGamepos=" + classicGamepos + ", manyGamepos=" + manyGamepos + ", fruitGamepos=" + fruitGamepos + ", roleId=" + roleId + ", vipLv=" + vipLv + ", lv=" + lv + ", crystals=" + crystals + ", sign=" + sign + ", victoryNum=" + victoryNum + ", failureNum=" + failureNum + ", betNum=" + betNum + ", isCheckPoker=" + isCheckPoker + ", gameState=" + gameState + ", killRoomPos=" + killRoomPos + "]";
	}

//	public long getPos()
//	{
//		return pos;
//	}


	public void setData(String nickName, String headImageUrl, long coins, int classicGamepos,int manyGamepos,int fruitGamepos, int roleId, int vipLv, int lv, int crystals, String sign, int victoryNum, int failureNum,long betNum,boolean isCheckPoker,int gameState,int killRoomPos)
	{
		this.nickName = nickName;
		this.headIconUrl = headImageUrl;
		this.coins = coins;
		this.classicGamepos = classicGamepos;
		this.manyGamepos = manyGamepos;
		this.fruitGamepos = fruitGamepos;
		this.roleId = roleId;
		this.vipLv = vipLv;
		this.lv = lv;
		this.crystals = crystals;
		this.sign = sign;
		this.victoryNum = victoryNum;
		this.failureNum = failureNum;
		this.betNum = betNum;
		this.isCheckPoker = isCheckPoker;
		this.gameState = gameState;
		this.killRoomPos = killRoomPos;
	}

	public int getRoleId()
	{
		return roleId;
	}

	public void setRoleId(int roleId)
	{
		this.roleId = roleId;
	}
	
}
