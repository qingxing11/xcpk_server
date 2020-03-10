package com.wt.naval.dao.model.user;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * The persistent class for the user_data database table.
 * 
 */
public class GameDataModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;

	private String account;

	private int addAutoId;

	private long bankCoins;

	private int cityId;

	private long coins;

	private int countryId;

	private int crystals;

	private String emailId;

	private int exp;

	private int failureNum;


	private int headIconId;

	private String headIconUrl;


	private int lastAddPeopleLoyalTimer;

	private int lastAddPeopleTimer;

	private Timestamp lastLoginTime;

	private Timestamp lastLogoutTime;

	private long lastOutPutTime;

	private long monetTree;

	private String nickName;

	private int playerLevel;

	/**
	 * 改名
	 */
	public int modifyNickName;
	
	/**
	 * 抢座
	 */
	public int robPos;
	
	/**
	 * 增时间
	 */
	public int addTime;

	private int roleId;

	private String sign;

	private long startOutPutTime;

	private int treeLv;

	
	private int victoryNum;

	private int vipLv;
	
	private int topUp;

	private int luckyNum;

	private  long luckyTime;

	private boolean accountSupplementary;

	private int curMonthAddTreeLv;
	
	private Timestamp vipTime;
	
	private int vipPayNum;
	
	
	public Timestamp getVipTime()
	{
		return vipTime;
	}


	public void setVipTime(Timestamp vipTime)
	{
		this.vipTime = vipTime;
	}


	public int getVipPayNum()
	{
		return vipPayNum;
	}


	public void setVipPayNum(int vipPayNum)
	{
		this.vipPayNum = vipPayNum;
	}


	public int getCurMonthAddTreeLv() {
		return curMonthAddTreeLv;
	}


	public void setCurMonthAddTreeLv(int curMonthAddTreeLv) {
		this.curMonthAddTreeLv = curMonthAddTreeLv;
	}

	public GameDataModel() {
	}

	public boolean isAccountSupplementary()
	{
		return accountSupplementary;
	}



	public void setAccountSupplementary(boolean accountSupplementary)
	{
		this.accountSupplementary = accountSupplementary;
	}
	
	public int getTopUp() {
		return topUp;
	}

	public void setTopUp(int topUp) {
		this.topUp = topUp;
	}
	
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getAddAutoId() {
		return this.addAutoId;
	}

	public void setAddAutoId(int addAutoId) {
		this.addAutoId = addAutoId;
	}

	public long getBankCoins() {
		return this.bankCoins;
	}

	public void setBankCoins(long bankCoins) {
		this.bankCoins = bankCoins;
	}

	public int getCityId() {
		return this.cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public long getCoins() {
		return this.coins;
	}

	public void setCoins(long coins) {
		this.coins = coins;
	}

	public int getCountryId() {
		return this.countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public int getCrystals() {
		return this.crystals;
	}

	public void setCrystals(int crystals) {
		this.crystals = crystals;
	}

	public String getEmailId() {
		return this.emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getExp() {
		return this.exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getFailureNum() {
		return this.failureNum;
	}

	public void setFailureNum(int failureNum) {
		this.failureNum = failureNum;
	}


	public int getHeadIconId() {
		return this.headIconId;
	}

	public void setHeadIconId(int headIconId) {
		this.headIconId = headIconId;
	}

	public String getHeadIconUrl() {
		return this.headIconUrl;
	}

	public void setHeadIconUrl(String headIconUrl) {
		this.headIconUrl = headIconUrl;
	}

	public int getLastAddPeopleLoyalTimer() {
		return this.lastAddPeopleLoyalTimer;
	}

	public void setLastAddPeopleLoyalTimer(int lastAddPeopleLoyalTimer) {
		this.lastAddPeopleLoyalTimer = lastAddPeopleLoyalTimer;
	}

	public int getLastAddPeopleTimer() {
		return this.lastAddPeopleTimer;
	}

	public void setLastAddPeopleTimer(int lastAddPeopleTimer) {
		this.lastAddPeopleTimer = lastAddPeopleTimer;
	}

	public Timestamp getLastLoginTime() {
		return this.lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Timestamp getLastLogoutTime() {
		return this.lastLogoutTime;
	}

	public void setLastLogoutTime(Timestamp lastLogoutTime) {
		this.lastLogoutTime = lastLogoutTime;
	}

	public long getLastOutPutTime() {
		return this.lastOutPutTime;
	}

	public void setLastOutPutTime(long lastOutPutTime) {
		this.lastOutPutTime = lastOutPutTime;
	}

	public long getMonetTree() {
		return this.monetTree;
	}

	public void setMonetTree(long monetTree) {
		this.monetTree = monetTree;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getPlayerLevel() {
		return this.playerLevel;
	}

	public void setPlayerLevel(int playerLevel) {
		this.playerLevel = playerLevel;
	}

	public int getModifyNickName() {
		return modifyNickName;
	}



	public void setModifyNickName(int modifyNickName) {
		this.modifyNickName = modifyNickName;
	}



	public int getRobPos() {
		return robPos;
	}



	public void setRobPos(int robPos) {
		this.robPos = robPos;
	}



	public int getAddTime() {
		return addTime;
	}



	public void setAddTime(int addTime) {
		this.addTime = addTime;
	}



	public int getRoleId() {
		return this.roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public long getStartOutPutTime() {
		return this.startOutPutTime;
	}

	public void setStartOutPutTime(long startOutPutTime) {
		this.startOutPutTime = startOutPutTime;
	}

	public int getTreeLv() {
		return this.treeLv;
	}

	public void setTreeLv(int treeLv) {
		this.treeLv = treeLv;
	}

	public int getVictoryNum() {
		return this.victoryNum;
	}

	public void setVictoryNum(int victoryNum) {
		this.victoryNum = victoryNum;
	}

	public int getVipLv() {
		return this.vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}
	
	public int getLuckyNum() {
		return luckyNum;
	}

	public void setLuckyNum(int luckyNum) {
		this.luckyNum = luckyNum;
	}

	public long getLuckyTime() {
		return luckyTime;
	}

	public void setLuckyTime(long luckyTime)
	{
		this.luckyTime = luckyTime;
	}
}