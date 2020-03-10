package model;

import java.io.Serializable;

import java.sql.Timestamp;
import java.math.BigInteger;




public class Safebox implements Serializable {
	private static final long serialVersionUID = 1L;


	private int key;

	private long money;

	private int otherId;

	private Timestamp time;

	private int type;

	private int userId;

	public Safebox() {
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public long getMoney() {
		return this.money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public int getOtherId() {
		return this.otherId;
	}

	public void setOtherId(int otherId) {
		this.otherId = otherId;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "Safebox [key=" + key + ", money=" + money + ", otherId=" + otherId + ", time=" + time + ", type=" + type + ", userId=" + userId + "]";
	}
}