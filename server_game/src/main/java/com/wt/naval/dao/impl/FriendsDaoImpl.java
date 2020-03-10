package com.wt.naval.dao.impl;

import java.util.List;

import com.wt.archive.AddFriendsVO;
import com.wt.db.SqlSimpleUtil;
import com.wt.util.Tool;

import model.UserFriendseventModel;
import model.UserFriendsinfoModel;

public class FriendsDaoImpl
{
		private static final String UPDATE_FriendChatInfo= "UPDATE `user_friendsinfo` SET `friendChatList`=? WHERE `userId` = ?";
		/**更新好友私人聊天*/
		public static void updatatUserFriendsChatInfo(int userId, byte[] b) 
		{
			int num=SqlSimpleUtil.update(UPDATE_FriendChatInfo,b,userId);
			Tool.print_debug_level0("更新私人聊天:num="+num);
		}
		
		private static final String GET_USER_FRIENDSINFO_BY_USERID= "SELECT * FROM `user_friendsinfo` WHERE `userId`=?";
		/**获取所有好友信息和私人聊天消息*/
		public static UserFriendsinfoModel selectFriendsData(int userId)
		{
			return SqlSimpleUtil.selectBean(GET_USER_FRIENDSINFO_BY_USERID, UserFriendsinfoModel.class, userId);
		}
	
		private static final String UPDATE_USER_FRIENDSINFO= "UPDATE `user_friendsinfo` SET `friendsList`=? WHERE `userId` = ?";
		/**更新好友列表*/
		public static void updatatUserFriendsList(int userId, byte[] b) 
		{
			Tool.print_debug_level0("更新好友列表,b长度:"+b.length+"userId:"+userId);
			int num=SqlSimpleUtil.update(UPDATE_USER_FRIENDSINFO,b,userId);
			Tool.print_debug_level0("更新好友列表:num="+num);
			
			if(num == 0)
			{
				insetUserFriendsList(userId, b);
			}
		}
		
		private static final String Inset_插入好友列表 = "INSERT INTO `user_friendsinfo`(`userId`,`friendsList`) VALUES (?,?)";
		/**插入好友列表*/
		public static void insetUserFriendsList(int userId, byte[] b) 
		{
			int num=SqlSimpleUtil.update(Inset_插入好友列表,userId,b);
			Tool.print_debug_level0("插入好友列表:num="+num);
		}
		
		
		private static final String GET_添加好友事件_select= "SELECT * FROM `user_friendevent` WHERE `userId`=?";
		/**获取所有添加好友事件*/
		public static  List<UserFriendseventModel> selectFriendsEventData(int userId)
		{
			return SqlSimpleUtil.selectBeanList(GET_添加好友事件_select, UserFriendseventModel.class, userId);
		}
		
		private static final String GET_添加好友事件= "SELECT * FROM `user_friendevent` WHERE `userId`=? AND `otherID`=?";
		/**获取所有添加好友事件*/
		public static  UserFriendseventModel selectFriendsEventData(int userId,int otherID)
		{
			return SqlSimpleUtil.selectBean(GET_添加好友事件, UserFriendseventModel.class, userId,otherID);
		}
		
//		private static final String UPDATE_添加好友事件_1= "UPDATE `user_friendevent` SET `otherID`=?,`nikeName`=?,`lv`=?,`icon`=?,`state`=?,`info`=? WHERE `userId` = ?";
//		/**更新好友列表*/
//		public static void updatatUserFriendsEventData(int userId, int otherID,String nikeName,int lv,String icon,int state,String info) 
//		{
//			SqlSimpleUtil.update(UPDATE_添加好友事件_1,otherID,nikeName,lv,icon,state,info,userId);
//		}
		
		private static final String UPDATE_添加好友事件_2= "UPDATE `user_friendevent` SET `otherID`=?,`nikeName`=?,`lv`=?,`icon`=?,`state`=?,`resultReadState`=? WHERE `userId` = ?";
		/**更新好友事件列表*/
		public static void updatatUserFriendsEventData(int userId, int otherID,String nikeName,int lv,String icon,int state,boolean resultReadState) 
		{
			Tool.print_debug_level0("更新好友事件列表:userId="+userId+",otherID="+otherID+",nikeName="+nikeName+",lv="+lv+",icon="+icon+",state="+state+",resultReadState="+resultReadState);
			int num=SqlSimpleUtil.update(UPDATE_添加好友事件_2,otherID,nikeName,lv,icon,state,resultReadState,userId);
			Tool.print_debug_level0("更新好友事件列表:num="+num);
		}
		/**更新好友事件列表*/
		public static void updatatUserFriendsEventData(int userId,  AddFriendsVO vo) 
		{
			Tool.print_debug_level0("更新好友事件列表:userId="+userId+",vo="+vo);
			int num=SqlSimpleUtil.update(UPDATE_添加好友事件_2,vo.id,vo.nickName,vo.lv,vo.icon,vo.state,vo.readState,userId);
			Tool.print_debug_level0("更新好友事件列表:num="+num);
		}
		
		private static final String Inset_添加好友事件= "INSERT INTO `user_friendevent`(`userId`,`otherID`,`nikeName`,`lv`,`icon`,`state`,`resultReadState`,`roleId`,`vipLv`) VALUES (?,?,?,?,?,?,?,?,?)";
		/**插入好友事件列表*/
		public static void insetUserFriendsEventData(int userId, AddFriendsVO vo) 
		{
			Tool.print_debug_level0("插入好友事件列表:userId="+userId+",AddFriendsVO="+vo);
			int num=SqlSimpleUtil.update(Inset_添加好友事件,userId,vo.id,vo.nickName,vo.lv,vo.icon,vo.state,vo.readState,vo.roleId,vo.vipLv);
			Tool.print_debug_level0("插入好友事件列表:num="+num);
		}
		
		private static final String Delete_删除好友事件= "DELETE FROM`user_friendevent`  WHERE `userId` = ? AND `otherID`=?";
		/**删除好友事件列表*/
		public static void deleteUserFriendsEventData(int userId,int otherId) 
		{
			//Tool.print_debug_level0("插入好友事件列表:userId="+userId+",AddFriendsVO="+vo);
			int num=SqlSimpleUtil.update(Delete_删除好友事件,userId,otherId);
			Tool.print_debug_level0("删除好友事件列表:num="+num);
		}
		
		private static final String UPDATE_更新添加好友状态_state= "UPDATE `user_friendevent` SET `state`=? WHERE `userId` = ? AND `otherID`=?";
		/**更新添加好友状态*/
		public static void updatatUserFriendsEvent_state(int userId,int otherId, int state) 
		{
			Tool.print_debug_level0("更新添加好友状态:state="+state+",userId="+userId+",otherId="+otherId);
			int num=SqlSimpleUtil.update(UPDATE_更新添加好友状态_state,state,userId,otherId);      
			Tool.print_debug_level0("更新添加好友状态:num="+num);
		}
		
		
		private static final String UPDATE_添加好友事件_4= "UPDATE `user_friendevent` SET `infoState`=? WHERE `userId` = ? AND `otherID`=?";
		/**更新好友事件消息状态*/
		public static void updatatUserFriendsEventData(int userId,int otherId, int infoState) 
		{
			Tool.print_debug_level0("更新好友事件消息状态:infoState="+infoState+",userId="+userId+",otherId="+otherId);
			int num=SqlSimpleUtil.update(UPDATE_添加好友事件_4,infoState,userId,otherId);      
			Tool.print_debug_level0("更新好友事件消息状态:num="+num);
		}
		private static final String UPDATE_添加好友事件_5= "UPDATE `user_friendevent` SET `resultReadState`=? WHERE `userId` = ? AND `otherID`=?";
		
		/**更新好友事件添加好友信息状态*/
		public static void updatatAddInfoUserFriendsEventData(int userId,int otherId, boolean resultReadState) 
		{
			Tool.print_debug_level0("更新好友事件添加好友信息状态:userId="+userId+",otherId="+otherId+",resultReadState="+resultReadState);
			int num=SqlSimpleUtil.update(UPDATE_添加好友事件_5,resultReadState,userId,otherId);      
			Tool.print_debug_level0("更新好友事件添加好友信息状态:num="+num);
		}
		
		private static final String UPDATE_添加好友事件_3= "UPDATE `user_friendevent` SET `info`=?,`infoState`=?  WHERE `userId` = ? AND `otherID`=?";
		/**更新好友事件信息*/
		public static void updatatUserFriendsEventData(int userId,int otherId, String info,int infoState) 
		{
			Tool.print_debug_level0("更新好友事件信息:userId="+userId+",otherId="+otherId+",info="+info+",infoState="+infoState);
			int num=SqlSimpleUtil.update(UPDATE_添加好友事件_3,info,infoState,userId,otherId);
			Tool.print_debug_level0("更新好友事件信息:num="+num);
		}
		
		private static final String Inset_插入消息= "INSERT INTO `user_friendevent`(`userId`,`otherID`,`info`,`infoState`) VALUES (?,?,?,?)";
		/**插入好友事件信息*/
		public static void insetUserFriendsEventData(int userId,int otherId, String info,int infoState) 
		{
			Tool.print_debug_level0("插入好友事件信息:userId="+userId+",otherId="+otherId+",info="+info+",infoState="+infoState);
			int num=SqlSimpleUtil.update(Inset_插入消息,userId,otherId,info,infoState);
			Tool.print_debug_level0("插入好友事件信息:num="+num);
		}
}
