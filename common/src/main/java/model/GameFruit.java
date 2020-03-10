package model;

import java.math.BigDecimal;
import java.util.Date;


/**
 * The persistent class for the game_fruit database table.
 * 
 */
public class GameFruit   {

	private int id;

	private BigDecimal apple_chance;

	private BigDecimal bar_chance;

	private Date createDate;

	private BigDecimal double_chance;

	private BigDecimal double_seven_chance;

	private BigDecimal lingdang_chance;

	private BigDecimal special_chance;

	private byte status;

	private Date updateDate;

	public GameFruit() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString()
	{
		return "GameFruit [id=" + id + ", apple_chance=" + apple_chance + ", bar_chance=" + bar_chance + ", createDate=" + createDate + ", double_chance=" + double_chance + ", double_seven_chance=" + double_seven_chance + ", lingdang_chance=" + lingdang_chance + ", special_chance=" + special_chance + ", status=" + status + ", updateDate=" + updateDate + "]";
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getApple_chance()
	{
		return apple_chance;
	}

	public void setApple_chance(BigDecimal apple_chance)
	{
		this.apple_chance = apple_chance;
	}

	public BigDecimal getBar_chance()
	{
		return bar_chance;
	}

	public void setBar_chance(BigDecimal bar_chance)
	{
		this.bar_chance = bar_chance;
	}

	public BigDecimal getDouble_chance()
	{
		return double_chance;
	}

	public void setDouble_chance(BigDecimal double_chance)
	{
		this.double_chance = double_chance;
	}

	public BigDecimal getDouble_seven_chance()
	{
		return double_seven_chance;
	}

	public void setDouble_seven_chance(BigDecimal double_seven_chance)
	{
		this.double_seven_chance = double_seven_chance;
	}

	public BigDecimal getLingdang_chance()
	{
		return lingdang_chance;
	}

	public void setLingdang_chance(BigDecimal lingdang_chance)
	{
		this.lingdang_chance = lingdang_chance;
	}

	public BigDecimal getSpecial_chance()
	{
		return special_chance;
	}

	public void setSpecial_chance(BigDecimal special_chance)
	{
		this.special_chance = special_chance;
	}
}