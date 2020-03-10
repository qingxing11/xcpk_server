package com.wt.naval.vo;

import java.io.Serializable;

public class OtherPlayer implements Serializable
{
	private static final long serialVersionUID = -4207381563714129737L;
 	
	public int gender;
	public String nickName;
 	public int userId;
	public int pos;
	public String headImgUrl;
	public boolean isReady;
	public boolean isOnline;
	public int handPokerNum;
	public boolean sitDown;
	public int coins;
	public OtherPlayer(int gender,String nickName,int userId,int pos,String headImgUrl,boolean isReady,boolean isOnline,int handPokerNum,boolean sitdown,int coins)
	{
		this.gender = gender;
		this.nickName = nickName;
 		this.userId = userId;
		this.pos = pos;
		this.headImgUrl = headImgUrl;
		this.isReady = isReady;
		this.isOnline = isOnline;
		this.handPokerNum = handPokerNum;
		this.sitDown = sitdown;
		this.coins = coins;
	}
	
	public OtherPlayer(String nickName,int pos,boolean isReady,boolean isOnline,boolean sitDown,int coins,int handPokerNum)
	{
		this.nickName = nickName;
		this.pos = pos;
		this.isReady = isReady;
		this.isOnline = isOnline;
		this.sitDown = sitDown;
		this.coins = coins;
		this.handPokerNum = handPokerNum;
	}
}