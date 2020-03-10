package model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The persistent class for the user_baginfo database table.
 * 
 */
public class UserBaginfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int id;

	private int ascriptionState;

	private int bagPos;

	private Timestamp begainTime;

	private boolean bund;

	private int heroId;

	private int level;

	private int num;

	private int srcId;

	private int userId;

	public UserBaginfo()
	{}

	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getAscriptionState()
	{
		return this.ascriptionState;
	}

	public void setAscriptionState(int ascriptionState)
	{
		this.ascriptionState = ascriptionState;
	}

	public int getBagPos()
	{
		return this.bagPos;
	}

	public void setBagPos(int bagPos)
	{
		this.bagPos = bagPos;
	}

	public Timestamp getBegainTime()
	{
		return this.begainTime;
	}

	public void setBegainTime(Timestamp begainTime)
	{
		this.begainTime = begainTime;
	}

	public boolean getBund()
	{
		return this.bund;
	}

	public void setBund(boolean bund)
	{
		this.bund = bund;
	}

	public int getHeroId()
	{
		return this.heroId;
	}

	public void setHeroId(int heroId)
	{
		this.heroId = heroId;
	}

	public int getLevel()
	{
		return this.level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public int getNum()
	{
		return this.num;
	}

	public void setNum(int num)
	{
		this.num = num;
	}

	public int getSrcId()
	{
		return this.srcId;
	}

	public void setSrcId(int srcId)
	{
		this.srcId = srcId;
	}

	public int getUserId()
	{
		return this.userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "UserBaginfo [id=" + id + ", ascriptionState=" + ascriptionState + ", bagPos=" + bagPos + ", begainTime=" + begainTime + ", bund=" + bund + ", heroId=" + heroId + ", level=" + level + ", num=" + num + ", srcId=" + srcId + ", userId=" + userId + "]";
	}

}