package model;

import java.io.Serializable;
public class UserTodaydataModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;

	private int armyBuy;

	private int armyHold;

	private int buildingConstruction;

	private int buildingConstructionHold;

	private int citizensBuy;

	private int citizensHold;

	private int materialProfits;

	private int productionCost;

	private int resBuyCost;

	private int revenue;

	private int tradingProfits;

	public UserTodaydataModel() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArmyBuy() {
		return this.armyBuy;
	}

	public void setArmyBuy(int armyBuy) {
		this.armyBuy = armyBuy;
	}

	public int getArmyHold() {
		return this.armyHold;
	}

	public void setArmyHold(int armyHold) {
		this.armyHold = armyHold;
	}

	public int getBuildingConstruction() {
		return this.buildingConstruction;
	}

	public void setBuildingConstruction(int buildingConstruction) {
		this.buildingConstruction = buildingConstruction;
	}

	public int getBuildingConstructionHold() {
		return this.buildingConstructionHold;
	}

	public void setBuildingConstructionHold(int buildingConstructionHold) {
		this.buildingConstructionHold = buildingConstructionHold;
	}

	public int getCitizensBuy() {
		return this.citizensBuy;
	}

	public void setCitizensBuy(int citizensBuy) {
		this.citizensBuy = citizensBuy;
	}

	public int getCitizensHold() {
		return this.citizensHold;
	}

	public void setCitizensHold(int citizensHold) {
		this.citizensHold = citizensHold;
	}

	public int getMaterialProfits() {
		return this.materialProfits;
	}

	public void setMaterialProfits(int materialProfits) {
		this.materialProfits = materialProfits;
	}

	public int getProductionCost() {
		return this.productionCost;
	}

	public void setProductionCost(int productionCost) {
		this.productionCost = productionCost;
	}

	public int getResBuyCost() {
		return this.resBuyCost;
	}

	public void setResBuyCost(int resBuyCost) {
		this.resBuyCost = resBuyCost;
	}

	public int getRevenue() {
		return this.revenue;
	}

	public void setRevenue(int revenue) {
		this.revenue = revenue;
	}

	public int getTradingProfits() {
		return this.tradingProfits;
	}

	public void setTradingProfits(int tradingProfits) {
		this.tradingProfits = tradingProfits;
	}

}