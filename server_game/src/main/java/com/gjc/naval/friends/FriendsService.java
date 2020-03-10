package com.gjc.naval.friends;

import java.util.ArrayList;

import com.gjc.naval.vo.chat.FriendsDataVO;
import com.gjc.naval.vo.friendChat.FriendChatVO;
import com.wt.archive.AddFriendsVO;
import com.wt.naval.vo.user.Player;

public interface FriendsService
{
	
	/**
	 * 向好友推送通知
	 * @param player
	 * @param onLine
	 */
	void friendIsOnLine(Player player,boolean onLine);
	
	/**
	 * 更新添加好友事件列表数据
	 * 
	 * @param userId 对方
	 * @param vo     请求方数据
	 * @return
	 */
	void insetUserFriendsEventInfo(int userId, AddFriendsVO vo);
	
	/**
	 * 更新好友列表数据
	 * 
	 * @param userId
	 * @return
	 */
	boolean updataUserFriendsInfo(Player player);
	
	/**
	 * 登录时初始化
	 * 
	 * @param userId
	 * @return
	 */
	ArrayList<FriendsDataVO> initFriendsData(int userId);
	
	/**
	 * 获取好友列表信息
	 * @param list_friends
	 * @return
	 */
	public ArrayList<FriendsDataVO> getFriendDataVO(ArrayList<Integer> list_friends);
	
	/**给玩家好友推送状态*/
	void playState(int userId,boolean state);
	
	/**保存玩家私聊消息状态*/
	void saveFriendChatVOState(int userId,int friendId);
	
	/**保存玩家私聊消息*/
	void saveFriendChatVO(Player player);
	
	/**初始化并推送好友私聊信息*/
	 void initPlayerFriendChatInfo(int userId) ;
	
	/** 添加好友私聊消息*/
	void updateFriendChatVO(int userId,int friendId,FriendChatVO vo);
	
	/**删除好友*/
	void removeUserFriendsList(int userId,int otherId);
	
	/**好友数量限制*/
	boolean playerIsCanAddFriend(int userId);
	
	/**数据库已经插入*/
	 boolean haveFriendsEvent(int userId,int otherId);
	
	/**更新添加信息状态*/
	void setAddInfoState(Player player,int userId,boolean state);
	
	/**更新消息状态*/
	void setInfoState(Player player,int userId,int state);
	
	/** 更新好友列表数据*/
	 void updataUserFriendsList(int userId,FriendsDataVO vo);
	
	/**插入好友事件信息、状态*/
	void insetUserFriendsEventData(int userId,int otherId, String info,int infoState);
	
	/**添加一条消息*/
	void addFriendsInfo(Player player,int userId,String info,int state);
	
	/**设置添加好友状态*/
	void setAddFriendsEventState(Player player,int userId,int state);
	
	/**好友列表添加好友*/
	void addFriendVO(Player player,FriendsDataVO vo);
	
	/**获取请求好友简略信息*/
	AddFriendsVO getAddFriendsVO(Player player,int userId);
	
	/**是否已经添加此好友*/
	 boolean haveFriends(Player player,int otherId);
	 
	 /**玩家上线初始化添加好友事件列表，初始化信息列表*/
	 void addFriendsEvent(Player player);
	 
	 /**玩家上线处理添加好友事件*/
	 void CancelFriendsEvent(Player player);
	 
	 /**玩家上线推送消息通知*/
	 void CancelFriendsInfo(Player player);
}
