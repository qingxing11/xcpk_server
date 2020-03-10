package model;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * The persistent class for the user_vip database table.
 * 
 */
public class UserVip implements Serializable {
	private static final long serialVersionUID = 1L;

	/**vip等级*/
	private int id;

	/**银行功能*/
	private boolean bank;

	/**银行保存金额*/
	private long bankSaveNum;

	/**每日登录奖励金币*/
	private int loginAwardMoney;

	/**房间聊天功能*/
	private boolean roomChat;
	
	/**充值金币（vip等级解锁条件）*/
	private int topUp;

	/**转账功能*/
	private boolean transferAccounts;

	/**转账手续费率*/
	private float transferAccountsPer;

	/**经典场宝箱功能*/
	private boolean treasureBox;

	/**大喇叭消耗金币*/
	private int typhonChatMoney;

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isBank() {
		return bank;
	}

	public void setBank(boolean bank) {
		this.bank = bank;
	}

	public long getBankSaveNum() {
		return bankSaveNum;
	}

	public void setBankSaveNum(long bankSaveNum) {
		this.bankSaveNum = bankSaveNum;
	}

	public int getLoginAwardMoney() {
		return loginAwardMoney;
	}

	public void setLoginAwardMoney(int loginAwardMoney) {
		this.loginAwardMoney = loginAwardMoney;
	}

	public boolean isRoomChat() {
		return roomChat;
	}

	public void setRoomChat(boolean roomChat) {
		this.roomChat = roomChat;
	}

	public int getTopUp() {
		return topUp;
	}

	public void setTopUp(int topUp) {
		this.topUp = topUp;
	}

	public boolean isTransferAccounts() {
		return transferAccounts;
	}

	public void setTransferAccounts(boolean transferAccounts) {
		this.transferAccounts = transferAccounts;
	}

	public float getTransferAccountsPer() {
		return transferAccountsPer;
	}

	public void setTransferAccountsPer(float transferAccountsPer) {
		this.transferAccountsPer = transferAccountsPer;
	}

	public boolean isTreasureBox() {
		return treasureBox;
	}

	public void setTreasureBox(boolean treasureBox) {
		this.treasureBox = treasureBox;
	}

	public int getTyphonChatMoney() {
		return typhonChatMoney;
	}

	public void setTyphonChatMoney(int typhonChatMoney) {
		this.typhonChatMoney = typhonChatMoney;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserVip() {
	}

	@Override
	public String toString() {
		return "UserVip [id=" + id + ", bank=" + bank + ", bankSaveNum=" + bankSaveNum + ", loginAwardMoney="
				+ loginAwardMoney + ", roomChat=" + roomChat + ", topUp=" + topUp + ", transferAccounts="
				+ transferAccounts + ", transferAccountsPer=" + transferAccountsPer + ", treasureBox=" + treasureBox
				+ ", typhonChatMoney=" + typhonChatMoney + "]";
	}

}