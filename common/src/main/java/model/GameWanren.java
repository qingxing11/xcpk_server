package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the game_wanren database table.
 * 
 */
public class GameWanren{

	private int id;

	private Date createDate;

	private BigDecimal leopard_a_chance;

	private BigDecimal leopard_chance;

	private BigDecimal shun_jin;

	private byte status;

	private BigDecimal up_banker_big_chance;

	private BigDecimal up_banker_double_chance;

	private BigDecimal up_banker_single_chance;

	private Date updateDate;
	
	private int apply_coins = 5000000;//上桌要求

	private long startJackPot=36000000;
	
	public GameWanren() {
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Date getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}

	public BigDecimal getLeopard_a_chance()
	{
		return leopard_a_chance;
	}

	public void setLeopard_a_chance(BigDecimal leopard_a_chance)
	{
		this.leopard_a_chance = leopard_a_chance;
	}

	public BigDecimal getLeopard_chance()
	{
		return leopard_chance;
	}

	public void setLeopard_chance(BigDecimal leopard_chance)
	{
		this.leopard_chance = leopard_chance;
	}

	public BigDecimal getShun_jin()
	{
		return shun_jin;
	}

	public void setShun_jin(BigDecimal shun_jin)
	{
		this.shun_jin = shun_jin;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public BigDecimal getUp_banker_big_chance()
	{
		return up_banker_big_chance;
	}

	public void setUp_banker_big_chance(BigDecimal up_banker_big_chance)
	{
		this.up_banker_big_chance = up_banker_big_chance;
	}

	public BigDecimal getUp_banker_double_chance()
	{
		return up_banker_double_chance;
	}

	public void setUp_banker_double_chance(BigDecimal up_banker_double_chance)
	{
		this.up_banker_double_chance = up_banker_double_chance;
	}

	public BigDecimal getUp_banker_single_chance()
	{
		return up_banker_single_chance;
	}

	public void setUp_banker_single_chance(BigDecimal up_banker_single_chance)
	{
		this.up_banker_single_chance = up_banker_single_chance;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

	public int getApplyCoins()
	{
		return apply_coins;
	}
	
	public long getStartJackPot()
	{
		return startJackPot;
	}
	@Override
	public String toString()
	{
		return "GameWanren [id=" + id + ", createDate=" + createDate + ", leopard_a_chance=" + leopard_a_chance + ", leopard_chance=" + leopard_chance + ", shun_jin=" + shun_jin + ", status=" + status + ", up_banker_big_chance=" + up_banker_big_chance + ", up_banker_double_chance=" + up_banker_double_chance + ", up_banker_single_chance=" + up_banker_single_chance + ", updateDate=" + updateDate + "]";
	}
}