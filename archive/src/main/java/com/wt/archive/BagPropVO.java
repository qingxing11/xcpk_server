package com.wt.archive;

import java.io.Serializable;

/**
 * 背包道具信息，可用于传输
 */
public class BagPropVO implements Serializable
{
	private static final long serialVersionUID = -2555968731270901358L;

	private int userId;
	private int id;
	private int srcId;// 道具
	private int num;// 数量
	private int level;// 等级
	private int ascriptionState;// 归属状态，标识归属武将还是归属背包
	private int heroId;
	private boolean bund;// 是否为绑定状态 对应PropBinding枚举值
	private int bagPos;// 在背包中所占位置 从0开始
	// public int heroPos;// 在武将中所占的位置

	// public long begainTime;// 获取时的时间，有的道具过一段时间之后会消失

	public BagPropVO()
	{}

	public BagPropVO(int userId, int srcId, int bagPos, int num, int level)
	{
		setUserId(userId);
		this.srcId = srcId;
		this.bagPos = bagPos;
		this.num = num;
		this.level = level;
	}

	public BagPropVO(int userId, int id, int srcId, int num, int level, int ascriptionState)
	{
		setUserId(userId); 
		this.id = id;
		this.srcId = srcId;
		this.num = num;
		this.level = level;
		this.ascriptionState = ascriptionState;
	}
	
	public int getUserId()
	{
		return userId;
	}
	public void setUserId(int userId)
	{
		this.userId = userId;
	}
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getSrcId()
	{
		return srcId;
	}
	public void setSrcId(int srcId)
	{
		this.srcId = srcId;
	}

	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}

	public int getLevel()
	{
		return level;
	}
	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getAscriptionState()
	{
		return ascriptionState;
	}
	public void setAscriptionState(int ascriptionState)
	{
		this.ascriptionState = ascriptionState;
	}

	public int getHeroId()
	{
		return heroId;
	}
	public void setHeroId(int heroId)
	{
		this.heroId = heroId;
	}

	public boolean isBund()
	{
		return bund;
	}
	public void setBund(boolean bund)
	{
		this.bund = bund;
	}

	public int getBagPos()
	{
		return bagPos;
	}
	public void setBagPos(int bagPos)
	{
		this.bagPos = bagPos;
	}
}
