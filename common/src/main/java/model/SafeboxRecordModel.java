package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class SafeboxRecordModel implements Serializable {
	private static final long serialVersionUID = 1L;


	private int key;

	private long money;

	private int otherId;

	private Timestamp time;

	private int type;

	private int userId;
	
	private boolean sign;
	
	private float pre;
	

	@Override
	public String toString() {
		return "SafeboxRecordModel [key=" + key + ", money=" + money + ", otherId=" + otherId + ", time=" + time
				+ ", type=" + type + ", userId=" + userId + ", sign=" + sign + ", pre=" + pre + "]";
	}

	public float getPre() {
		return pre;
	}

	public void setPre(float pre) {
		this.pre = pre;
	}

	public boolean isSign() {
		return sign;
	}

	public void setSign(boolean sign) {
		this.sign = sign;
	}

	public SafeboxRecordModel() {
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

}