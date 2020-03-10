package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.friendChat.FriendChatInfoRequest;
import com.gjc.cmd.friendChat.FriendChatInfoResponse;
import com.gjc.cmd.friendChat.FriendChatRequest;
import com.gjc.cmd.friendChat.FriendChatResponse;
import com.gjc.naval.friends.FriendsService;
import com.gjc.naval.vo.friendChat.FriendChatVO;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.gjc.cmd.friendChat") @Service
public class ApiFriendChat 
{
	@PostConstruct
	private void init()
	{
		try
		{
			ClassTool.registerApi(this);
		}
		catch (Exception e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private FriendsService friendsService;
	
	
	@Protocol(msgType = MsgTypeEnum.ChatInfo_好友聊天)
	/**好友聊天*/
	public void friendChat(ChannelHandlerContext ctx, FriendChatRequest request, MySession session)
	{
		int userId=request.userId;
		int friendId=request.friendId;
		String info=request.info;
		boolean sendRead=true;
		boolean resRead=false;
		FriendChatVO vo=new FriendChatVO(userId,info,friendId,sendRead);
		
		Player friendPlayer=playerService.getPlayer(friendId);
		FriendChatVO friendVO=new FriendChatVO(userId,info,friendId,resRead);
		friendsService.updateFriendChatVO(userId, friendId, vo);
		friendsService.updateFriendChatVO(friendId, userId, friendVO);
		
		if (friendPlayer!=null) 
		{
			FriendChatResponse response=new FriendChatResponse(FriendChatResponse.SUCCESS,friendVO);
			friendPlayer.sendResponse(response);
		}
		FriendChatResponse response=new FriendChatResponse(FriendChatResponse.SUCCESS,vo);
		playerService.sendMsg(response, userId);
	}
	
	@Protocol(msgType = MsgTypeEnum.ChatInfo_好友聊天状态)
	/**更新好友聊天消息*/
	public void friendChatInfo(ChannelHandlerContext ctx, FriendChatInfoRequest request, MySession session)
	{
		int userId=session.getUserId();
		int friendId=request.friendId;
		friendsService.saveFriendChatVOState(userId, friendId);
		FriendChatInfoResponse response=new FriendChatInfoResponse(friendId);
		playerService.sendMsg(response, userId);
	}
}
