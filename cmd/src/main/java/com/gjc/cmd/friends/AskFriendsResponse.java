package com.gjc.cmd.friends;

import java.util.ArrayList;

import com.gjc.naval.vo.chat.FriendsDataVO;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskFriendsResponse extends Response
{
	public static final int ERROR_NUll= 0; //没有好友
	
	public ArrayList<FriendsDataVO> friends_list;
	public AskFriendsResponse()
	{
		this.msgType=MsgTypeEnum.ASKFRIENDS.getType(); 
	}
	public AskFriendsResponse(int code)
	{
		this.msgType=MsgTypeEnum.ASKFRIENDS.getType(); 
		this.code=code;
	}
	public AskFriendsResponse(int code,ArrayList<FriendsDataVO> friends_list)
	{
		this.msgType=MsgTypeEnum.ASKFRIENDS.getType(); 
		this.code=code;
		this.friends_list=friends_list;
	}
	
	@Override
	public String toString()
	{
		return "AskFriendsResponse [friends_list=" + friends_list+"]";
	}
}
