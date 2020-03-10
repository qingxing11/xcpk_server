package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the game_tongsha database table.
 * 发牌要求：系统庄大牌率	人庄大牌率 东南西北大牌 	豹子AAA 	豹子 	顺金
 */
public class GameTongsha implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private Date create_time;

	private Date createDate;

	private BigDecimal direction_big_chance;

	private BigDecimal leopard_a_chance;

	private BigDecimal leopard_chance;

	private BigDecimal person_banker_big_chance;

	public BigDecimal getPerson_banker_big_chance()
	{
		return person_banker_big_chance;
	}

	public void setPerson_banker_big_chance(BigDecimal person_banker_big_chance)
	{
		this.person_banker_big_chance = person_banker_big_chance;
	}



	private BigDecimal shun_jin;

	private byte status;

	private BigDecimal sys_banker_big_chance;

	private Date updateDate;

	public GameTongsha() {
	}

	
	public int getId()
	{
		return id;
	}



	public void setId(int id)
	{
		this.id = id;
	}



	public Date getCreate_time()
	{
		return create_time;
	}



	public void setCreate_time(Date create_time)
	{
		this.create_time = create_time;
	}



	public Date getCreateDate()
	{
		return createDate;
	}



	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}



	public BigDecimal getDirection_big_chance()
	{
		return direction_big_chance;
	}



	public void setDirection_big_chance(BigDecimal direction_big_chance)
	{
		this.direction_big_chance = direction_big_chance;
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



	public BigDecimal getSys_banker_big_chance()
	{
		return sys_banker_big_chance;
	}



	public void setSys_banker_big_chance(BigDecimal sys_banker_big_chance)
	{
		this.sys_banker_big_chance = sys_banker_big_chance;
	}



	public Date getUpdateDate()
	{
		return updateDate;
	}



	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}



	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}



	@Override
	public String toString()
	{
		return "GameTongsha [id=" + id + ", create_time=" + create_time + ", createDate=" + createDate + ", direction_big_chance=" + direction_big_chance + ", leopard_a_chance=" + leopard_a_chance + ", leopard_chance=" + leopard_chance + ", person_banker_big_chance=" + person_banker_big_chance + ", shun_jin=" + shun_jin + ", status=" + status + ", sys_banker_big_chance=" + sys_banker_big_chance + ", updateDate=" + updateDate + "]";
	}

}