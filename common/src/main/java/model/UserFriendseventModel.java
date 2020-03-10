package model;

import java.io.Serializable;
import java.util.Arrays;
/**
 * The persistent class for the user_friendevent database table.
 * 
 */
public class UserFriendseventModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;//玩家Id

	private String icon;//other玩家头像

	private String info;//other请求添加好友产生的消息

	private int lv;//other玩家等级

	private String nikeName;//other玩家昵称

	private int otherID;//other玩家ID
	
	private int state;//状态：确定/拒绝/未处理

	private int infoState;//消息状态：已读/未读/删除
	
	private boolean resultReadState;//添加好友结果信息状态/已读未读
	
	private byte[] toAddFriends;//TODO 待删除
	
	private int roleId;//角色
	
	private int vipLv;//vipLv等级
	
	
	
	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getVipLv() {
		return vipLv;
	}

	public void setVipLv(int vipLv) {
		this.vipLv = vipLv;
	}

	@Override
	public String toString() {
		return "UserFriendseventModel [userId=" + userId + ", icon=" + icon + ", info=" + info + ", lv=" + lv
				+ ", nikeName=" + nikeName + ", otherID=" + otherID + ", state=" + state + ", infoState=" + infoState
				+ ", resultReadState=" + resultReadState + ", toAddFriends=" + Arrays.toString(toAddFriends)
				+ ", roleId=" + roleId + ", vipLv=" + vipLv + "]";
	}

	public UserFriendseventModel()
	{
	}
	
	public boolean getResultReadState()
	{
		return resultReadState;
	}


	public void setResultReadState(boolean resultReadState) {
		this.resultReadState = resultReadState;
	}

	
	public int getInfoState() {
		return infoState;
	}

	public void setInfoState(int infoState) {
		this.infoState = infoState;
	}
	
	
	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getLv() {
		return this.lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public String getNikeName() {
		return this.nikeName;
	}

	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}

	public int getOtherID() {
		return this.otherID;
	}

	public void setOtherID(int otherID) {
		this.otherID = otherID;
	}

	public byte[] getToAddFriends() {
		return this.toAddFriends;
	}

	public void setToAddFriends(byte[] toAddFriends) {
		this.toAddFriends = toAddFriends;
	}
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}