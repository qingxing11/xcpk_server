package model;

import java.io.Serializable;


/**
 * The persistent class for the config_killroom database table.
 * 
 */
public class ConfigKillroom implements Serializable {
	private static final long serialVersionUID = 1L;

	private int key;

	private int applicationBankerCoins;

	private int bankerLimitCoins;

	private float jackpotWinRate;

	private float playerWinRate;

	private float systemWinRate;

	private int bankerRoundNum;
	
	private int jackpot3aRate;
	private int jackpotOtherLeopardRate;
	private int jackpotFlushColorRate;
	
	private int broadcastCost;

	private long startJackPot=100000000;
	
	public int getBroadcastCost()
	{
		return broadcastCost;
	}

	public void setBroadcastCost(int broadcastCost)
	{
		this.broadcastCost = broadcastCost;
	}

	public int getJackpot3aRate()
	{
		return jackpot3aRate;
	}
	
	public int getBankerRoundNum()
	{
		return bankerRoundNum;
	}

	public void setBankerRoundNum(int bankerRoundNum)
	{
		this.bankerRoundNum = bankerRoundNum;
	}

	public void setJackpot3aRate(int jackpot3aRate)
	{
		this.jackpot3aRate = jackpot3aRate;
	}

	public int getJackpotOtherLeopardRate()
	{
		return jackpotOtherLeopardRate;
	}

	public void setJackpotOtherLeopardRate(int jackpotOtherLeopardRate)
	{
		this.jackpotOtherLeopardRate = jackpotOtherLeopardRate;
	}

	public int getJackpotFlushColorRate()
	{
		return jackpotFlushColorRate;
	}

	public void setJackpotFlushColorRate(int jackpotFlushColorRate)
	{
		this.jackpotFlushColorRate = jackpotFlushColorRate;
	}

	public ConfigKillroom() {
	}

	public int getKey() {
		return this.key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getApplicationBankerCoins() {
		return this.applicationBankerCoins;
	}

	public void setApplicationBankerCoins(int applicationBankerCoins) {
		this.applicationBankerCoins = applicationBankerCoins;
	}

	public int getBankerLimitCoins() {
		return this.bankerLimitCoins;
	}

	public void setBankerLimitCoins(int bankerLimitCoins) {
		this.bankerLimitCoins = bankerLimitCoins;
	}

	public float getJackpotWinRate() {
		return this.jackpotWinRate;
	}

	public void setJackpotWinRate(float jackpotWinRate) {
		this.jackpotWinRate = jackpotWinRate;
	}

	public float getPlayerWinRate() {
		return this.playerWinRate;
	}

	public void setPlayerWinRate(float playerWinRate) {
		this.playerWinRate = playerWinRate;
	}

	public float getSystemWinRate() {
		return this.systemWinRate;
	}

	public void setSystemWinRate(float systemWinRate) {
		this.systemWinRate = systemWinRate;
	}
	
	public long getStartJackPot()
	{
		return startJackPot;
	}
}