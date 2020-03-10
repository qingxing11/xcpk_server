package model;

import java.io.Serializable;

/**
 * The persistent class for the user_friendsinfo database table.
 * 
 */
public class UserFriendsinfoModel implements Serializable
{
	private static final long serialVersionUID = 1L;

	private int userId;

	private byte[] friendsList;
	
	private byte[] friendChatList;
	
	

	public byte[] getFriendChatList() {
		return friendChatList;
	}

	public void setFriendChatList(byte[] friendChatList) {
		this.friendChatList = friendChatList;
	}

	public UserFriendsinfoModel()
	{}

	public int getUserId()
	{
		return this.userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public byte[] getFriendsList()
	{
		return this.friendsList;
	}

	public void setFriendsList(byte[] friendsList)
	{
		this.friendsList = friendsList;
	}

}