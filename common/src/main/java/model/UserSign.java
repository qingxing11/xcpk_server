package model;

import java.io.Serializable;


/**
 * The persistent class for the user_sign database table.
 * 
 */
public class UserSign implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;

	private boolean five;

	private boolean four;

	private boolean one;

	private boolean seven;

	private boolean six;

	private long startTime;

	private boolean three;

	private boolean two;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public boolean isFive() {
		return five;
	}

	public void setFive(boolean five) {
		this.five = five;
	}

	public boolean isFour() {
		return four;
	}

	public void setFour(boolean four) {
		this.four = four;
	}

	public boolean isOne() {
		return one;
	}

	public void setOne(boolean one) {
		this.one = one;
	}

	public boolean isSeven() {
		return seven;
	}

	public void setSeven(boolean seven) {
		this.seven = seven;
	}

	public boolean isSix() {
		return six;
	}

	public void setSix(boolean six) {
		this.six = six;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public boolean isThree() {
		return three;
	}

	public void setThree(boolean three) {
		this.three = three;
	}

	public boolean isTwo() {
		return two;
	}

	public void setTwo(boolean two) {
		this.two = two;
	}

	public UserSign() 
	{
		
	}
}