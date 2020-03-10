package model;

import java.io.Serializable;


/**
 * The persistent class for the config_fruitmechinewining database table.
 * 
 */
public class ConfigFruitmechinewining implements Serializable {
	private static final long serialVersionUID = 1L;

	private String fruitName;

	private int fruitType;

	private int grossValue;

	private float interval;

	private int max;

	private int min;

	private int rewardType;

	public ConfigFruitmechinewining() {
	}

	public String getFruitName() {
		return this.fruitName;
	}

	public void setFruitName(String fruitName) {
		this.fruitName = fruitName;
	}

	public int getFruitType() {
		return this.fruitType;
	}

	public void setFruitType(int fruitType) {
		this.fruitType = fruitType;
	}

	public int getGrossValue() {
		return this.grossValue;
	}

	public void setGrossValue(int grossValue) {
		this.grossValue = grossValue;
	}

	public float getInterval() {
		return this.interval;
	}

	public void setInterval(float interval) {
		this.interval = interval;
	}

	public int getMax() {
		return this.max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return this.min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getRewardType() {
		return this.rewardType;
	}

	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}

}