package model;

import java.io.Serializable;

public class UserCardDataModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int archerAdapt;

	private int armyId;

	private int brave;

	private int cavalryAdapt;

	private int commander;

	private int heroCardId;

	private int infantryAdapt;

	private int know;

	private int number;

	private int politics;

	private int shipAdapt;

	private int skillLv;

	private int state;

	private int userId;

	private int weaponAdapt;
	private byte[] equip;
	private String curCityId;
	private String targetCityId;
	private int duty;
	private int cityTimer;

	public UserCardDataModel()
	{}

	public String getCurCityId()
	{
		return curCityId;
	}

	public void setCurCityId(String curCityId)
	{
		this.curCityId=curCityId;
	}
	public String getTargetCityId()
	{
		return targetCityId;
	}

	public void setTargetCityId(String targetCityId)
	{
		this.targetCityId=targetCityId;
	}
	public int getDuty()
	{
		return duty;
	}

	public void setCityTimer(int cityTimer)
	{
		this.cityTimer=cityTimer;
	}
	public int getCityTimer()
	{
		return cityTimer;
	}

	public void setDuty(int duty)
	{
		this.duty=duty;
	}
	public int getArcherAdapt()
	{
		return this.archerAdapt;
	}

	public void setArcherAdapt(int archerAdapt)
	{
		this.archerAdapt = archerAdapt;
	}

	public int getArmyId()
	{
		return this.armyId;
	}

	public void setArmyId(int armyId)
	{
		this.armyId = armyId;
	}

	public int getBrave()
	{
		return this.brave;
	}

	public void setBrave(int brave)
	{
		this.brave = brave;
	}

	public int getCavalryAdapt()
	{
		return this.cavalryAdapt;
	}

	public void setCavalryAdapt(int cavalryAdapt)
	{
		this.cavalryAdapt = cavalryAdapt;
	}

	public int getCommander()
	{
		return this.commander;
	}

	public void setCommander(int commander)
	{
		this.commander = commander;
	}

	public int getHeroCardId()
	{
		return this.heroCardId;
	}

	public void setHeroCardId(int heroCardId)
	{
		this.heroCardId = heroCardId;
	}

	public int getInfantryAdapt()
	{
		return this.infantryAdapt;
	}

	public void setInfantryAdapt(int infantryAdapt)
	{
		this.infantryAdapt = infantryAdapt;
	}

	public int getKnow()
	{
		return this.know;
	}

	public void setKnow(int know)
	{
		this.know = know;
	}

	public int getNumber()
	{
		return this.number;
	}

	public void setNumber(int number)
	{
		this.number = number;
	}

	public int getPolitics()
	{
		return this.politics;
	}

	public void setPolitics(int politics)
	{
		this.politics = politics;
	}

	public int getShipAdapt()
	{
		return this.shipAdapt;
	}

	public void setShipAdapt(int shipAdapt)
	{
		this.shipAdapt = shipAdapt;
	}

	public int getSkillLv()
	{
		return this.skillLv;
	}

	public void setSkillLv(int skillLv)
	{
		this.skillLv = skillLv;
	}

	public int getState()
	{
		return this.state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public int getUserId()
	{
		return this.userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public int getWeaponAdapt()
	{
		return this.weaponAdapt;
	}

	public void setWeaponAdapt(int weaponAdapt)
	{
		this.weaponAdapt = weaponAdapt;
	}

	public byte[] getEquip()
	{
		return equip;
	}

	public void setEquip(byte[] equip)
	{
		this.equip = equip;
	}

}