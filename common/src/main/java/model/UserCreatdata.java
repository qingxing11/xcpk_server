package model;

import java.io.Serializable;


/**
 * The persistent class for the user_creatdata database table.
 * 
 */
public class UserCreatdata  {
	private static final long serialVersionUID = 1L;

	private int userId;

	private byte[] armyExtendedData;

	private byte[] homeBuildData;

	private byte[] trainArmyData;

	public UserCreatdata() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public byte[] getArmyExtendedData() {
		return this.armyExtendedData;
	}

	public void setArmyExtendedData(byte[] armyExtendedData) {
		this.armyExtendedData = armyExtendedData;
	}

	public byte[] getHomeBuildData() {
		return this.homeBuildData;
	}

	public void setHomeBuildData(byte[] homeBuildData) {
		this.homeBuildData = homeBuildData;
	}

	public byte[] getTrainArmyData() {
		return this.trainArmyData;
	}

	public void setTrainArmyData(byte[] trainArmyData) {
		this.trainArmyData = trainArmyData;
	}

}