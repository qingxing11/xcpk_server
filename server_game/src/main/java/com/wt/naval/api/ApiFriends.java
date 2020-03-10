package com.wt.naval.api;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.friends.AddFriendAgreeRequest;
import com.gjc.cmd.friends.AddFriendAgreeResponse;
import com.gjc.cmd.friends.AddFriendsRequest;
import com.gjc.cmd.friends.AddFriendsResponse;
import com.gjc.cmd.friends.AskFriendsRequest;
import com.gjc.cmd.friends.AskFriendsResponse;
import com.gjc.cmd.friends.DeleteFriendRequest;
import com.gjc.cmd.friends.DeleteFriendResponse;
import com.gjc.cmd.friends.DeleteInfoRequest;
import com.gjc.cmd.friends.DeleteInfoResponse;
import com.gjc.cmd.friends.LookFriendsRequest;
import com.gjc.cmd.friends.LookFriendsResponse;
import com.gjc.cmd.friends.ReadAddInfoRequest;
import com.gjc.cmd.friends.ReadInfoRequest;
import com.gjc.naval.friends.FriendsService;
import com.gjc.naval.vo.chat.FriendsDataVO;
import com.wt.annotation.Validation.Validation.ValidationSession;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.archive.AddFriendsVO;
import com.wt.archive.FriendInfoVO;
import com.wt.archive.FriendsData;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.xcpk.classic.ChangeTableRequest;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.PlayerSimpleData;

import data.define.FriendState;
import data.define.ReadInfoState;
import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.gjc.cmd.friends") 
@Service 
public class ApiFriends
{

	public static boolean isDebug = true;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private FriendsService friendsService;

	
	@Protocol(msgType = MsgTypeEnum.Delete_删除好友)
	/**删除好友*/
	public void deleteFriend(ChannelHandlerContext ctx, DeleteFriendRequest obj, MySession session)
	{
		Tool.print_debug_level0("**Delete_删除好友*"+obj.toString());
		
		int userId=obj.userId;
		int otherId=session.getUserId();
		friendsService.removeUserFriendsList(userId, otherId);
		friendsService.removeUserFriendsList(otherId,userId);
		
		DeleteFriendResponse response=new DeleteFriendResponse(DeleteFriendResponse.SUCCESS,userId,otherId);
		playerService.sendMsg(response, otherId);
		
		DeleteFriendResponse response2=new DeleteFriendResponse(DeleteFriendResponse.SUCCESS,otherId,otherId);
		playerService.sendMsg(response2, userId);
	}
	
	@Protocol(msgType = MsgTypeEnum.Delete_删除消息)
	/**删除信息*/
	public void deleteInfo(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		DeleteInfoRequest request = (DeleteInfoRequest) obj;
		
		Tool.print_debug_level0("**删除信息*"+request.toString());
		
		Player player = playerService.getPlayer(session);
		int targetUserId=request.userId;
		friendsService.setInfoState(player,targetUserId,  ReadInfoState.删除);
		DeleteInfoResponse response=new DeleteInfoResponse(targetUserId);
		playerService.sendMsg(response, session.getUserId());
	}
	
	@Protocol(msgType = MsgTypeEnum.Add_消息已读)
	/**阅读添加信息*/
	public void readAddInfo(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		Tool.print_debug_level0("**阅读添加信息*");
		ReadAddInfoRequest request = (ReadAddInfoRequest) obj;
		Player player = playerService.getPlayer(session);
		int targetUserId=request.userId;
		friendsService.setAddInfoState(player,targetUserId, true);
	}
	
	@Protocol(msgType = MsgTypeEnum.Info_消息已读)
	/**阅读信息*/
	public void readInfo(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		Tool.print_debug_level0("**阅读信息*");
		ReadInfoRequest request = (ReadInfoRequest) obj;
		Player player = playerService.getPlayer(session);
		int targetUserId=request.userId;
		friendsService.setInfoState(player,targetUserId, ReadInfoState.已读);
	}
	
	@Protocol(msgType = MsgTypeEnum.Agree_同意添加好友)
	/**添加好友结果*/
	public void addFriendAgree(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		if (isDebug)
			Tool.print_debug_level0("************添加好友结果********************");
		AddFriendAgreeRequest request = (AddFriendAgreeRequest) obj;

		Player player = playerService.getPlayer(session);
		int targetUserId = request.id;
		boolean stateOnLine = playerService.isOnline(targetUserId);
		AddFriendsVO addVO = friendsService.getAddFriendsVO(player, targetUserId);
		if (addVO == null)
		{
			Tool.print_error("找不到添加好友的简略信息：targetUserId=" + targetUserId + ",player=" + player);
			return;
		}

		if (request.result)
		{
			if (isDebug)
				Tool.print_debug_level0("************同意添加玩家********************");

			FriendsDataVO vo = new FriendsDataVO(addVO, stateOnLine);
			friendsService.addFriendVO(player, vo);
			friendsService.setAddFriendsEventState(player, targetUserId, FriendState.同意);
			String info = "您已成功添加" + addVO.nickName + "为好友！";
			int state = ReadInfoState.删除;
			friendsService.addFriendsInfo(player, targetUserId, info, state);

			FriendInfoVO infoVO = new FriendInfoVO(targetUserId, info, state);
			AddFriendAgreeResponse response = new AddFriendAgreeResponse(AddFriendAgreeResponse.SUCCESS, vo, infoVO);
			player.sendResponse(response);

			int id = player.getUserId();
			int lv = player.gameData.playerLevel;
			String icon = player.gameData.headIconUrl;
			String nikeName = player.getNickName();
			int roleId = player.getRoleId();
			int vipLv = player.getVipLv();
			FriendsDataVO other = new FriendsDataVO(id, lv, icon, nikeName, stateOnLine, roleId, vipLv, player.getHeadIconUrl());

			Player targetPlayer = playerService.getPlayer(targetUserId);

			String infoTar = addVO.nickName + "同意添加您为好友！";
			int stateTar = ReadInfoState.删除;
			if (stateOnLine || targetPlayer != null) // 对方玩家在线
			{
				if (isDebug)
					Tool.print_debug_level0("************对方玩家在线********************");

				friendsService.addFriendVO(targetPlayer, other);
				friendsService.addFriendsInfo(targetPlayer, player.getUserId(), infoTar, stateTar);
				FriendInfoVO infoVO2 = new FriendInfoVO(player.getUserId(), infoTar, state);
				// 对方应该产生一条消息
				AddFriendAgreeResponse responseTar = new AddFriendAgreeResponse(AddFriendAgreeResponse.SUCCESS, other, infoVO2);
				targetPlayer.sendResponse(responseTar);
			}
			else// 玩家不在线
			{
				if (isDebug)
					Tool.print_debug_level0("************对方玩家不在线********************");
				// 插入到数据库
				friendsService.insetUserFriendsEventData(targetUserId, player.getUserId(), infoTar, stateTar);
				friendsService.updataUserFriendsList(targetUserId, other);
			}
		}
		else
		{
			if (isDebug)
				Tool.print_debug_level0("************拒绝添加玩家********************");

			friendsService.setAddFriendsEventState(player, targetUserId, FriendState.拒绝);
			String info = "您拒绝添加" + addVO.nickName + "为好友！";
			int state = ReadInfoState.删除;
			friendsService.addFriendsInfo(player, targetUserId, info, state);
			FriendInfoVO infoVO = new FriendInfoVO(targetUserId, info, state);
			AddFriendAgreeResponse response = new AddFriendAgreeResponse(AddFriendAgreeResponse.SUCCESS, null, infoVO);
			player.sendResponse(response);

			Player targetPlayer = playerService.getPlayer(targetUserId);

			String infoTar = addVO.nickName + "同意添加您为好友！";
			int stateTar = ReadInfoState.删除;
			if (stateOnLine) // 对方玩家在线
			{
				if (isDebug)
					Tool.print_debug_level0("************对方玩家在线********************");

				friendsService.addFriendsInfo(targetPlayer, targetUserId, infoTar, stateTar);
				FriendInfoVO infoVO2 = new FriendInfoVO(player.getUserId(), infoTar, state);
				AddFriendAgreeResponse responseTar = new AddFriendAgreeResponse(AddFriendAgreeResponse.SUCCESS, null, infoVO2);
				targetPlayer.sendResponse(responseTar);
			}
			else// 玩家不在线
			{
				if (isDebug)
					Tool.print_debug_level0("************对方玩家不在线********************");

				friendsService.insetUserFriendsEventData(targetUserId, player.getUserId(), infoTar, stateTar);
			}
		}
	}
	
	@Protocol(msgType = MsgTypeEnum.Look_查找好友)
	/**查找好友*/
	public void lookFriends(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		if (isDebug)Tool.print_debug_level0("************查找好友********************");
		
		int userId = session.getUserId();
		LookFriendsRequest request = (LookFriendsRequest) obj;
		int askUserId=request.userId;
		if (askUserId<= 0)
		{
			if (isDebug)Tool.print_debug_level0("ERROR_查无此人*");
			
			LookFriendsResponse response = new LookFriendsResponse(LookFriendsResponse.ERROR_查无此人);
			playerService.sendMsg(response, userId);
			return;
		}
		Player player = playerService.getPlayer(askUserId);
		if (player==null) //玩家不在线
		{
			if (isDebug)Tool.print_debug_level0("玩家不在线");
			
			PlayerSimpleData playerSimple=null;
			try {
				 playerSimple=playerService.getPlayerSimpleData(askUserId);//报错
			} catch (Exception e) 
			{
				Tool.print_error("Exception e="+e+"获取玩家简单数据出错");
			}
			if (playerSimple==null) 
			{
				if (isDebug)Tool.print_debug_level0("ERROR_查无此人");
				
				LookFriendsResponse response = new LookFriendsResponse(LookFriendsResponse.ERROR_查无此人);
				playerService.sendMsg(response, userId);
				return;
			}
			
			int id=playerSimple.userId;
			String nickName=playerSimple.nickName;
			String icon=playerSimple.headIconUrl;
			int roleId=playerSimple.roleId;
			int vipLv=playerSimple.vipLv;
			int lv=playerSimple.lv;
			
			FriendsData fd=new FriendsData( id,  nickName,  icon,  lv,  roleId,  vipLv,playerSimple.getHeadIconUrl());
			FriendsDataVO vo=new FriendsDataVO(fd,false);
			
			
			LookFriendsResponse response = new LookFriendsResponse(LookFriendsResponse.SUCCESS,vo);
			playerService.sendMsg(response, userId);
			
			if (isDebug)Tool.print_debug_level0("1,aSUCCESS"+response);
			
		}
		else 
		{
			int id=player.gameData.userId;
			String nickName=player.gameData.nickName;
			String icon=player.gameData.headIconUrl;
			int roleId=player.getRoleId();
			int vipLv=player.getVipLv();
			int lv=player.gameData.playerLevel;
			
			FriendsData fd=new FriendsData( id,  nickName,  icon,  lv,  roleId,  vipLv,player.getHeadIconUrl());
			FriendsDataVO vo=new FriendsDataVO(fd,true);
			
			LookFriendsResponse response = new LookFriendsResponse(LookFriendsResponse.SUCCESS,vo);
			playerService.sendMsg(response, userId);
			
			if (isDebug)Tool.print_debug_level0("2,SUCCESS"+response);
			
		}
	}
	 
	@Protocol(msgType = MsgTypeEnum.ADDFRIENDS)
	@ValidationSession
	/**添加好友*/
	public  void addFriends(ChannelHandlerContext ctx, Request obj)
	{
		if (isDebug)Tool.print_debug_level0("************添加好友********************");
		
		AddFriendsRequest request = (AddFriendsRequest) obj;

		if (isDebug)
			Tool.print_debug_level0(request.toString());

		int userId=Tool.getSession(ctx).getUserId();
		Player my = UserCache.map_onlines.get(userId);// 玩家
		
		if (userId==request.id) 
		{
			AddFriendsResponse response =new AddFriendsResponse(AddFriendsResponse.ERROR_UNKNOWN);
			my.sendResponse(response);
			return;
		}
		
		boolean canAdd=friendsService.playerIsCanAddFriend(userId);
		if (!canAdd) 
		{
			AddFriendsResponse response =new AddFriendsResponse(AddFriendsResponse.Error_超过好友上限);
			my.sendResponse(response);
			return;
		}
		
		boolean have=friendsService.haveFriends(my,request.id);
		if (have) 
		{
			Tool.print_debug_level0("已经重复添加");
			AddFriendsResponse response =new AddFriendsResponse(AddFriendsResponse.Error_已经添加好友);
			my.sendResponse(response);
			return;
		}
		
		int id=my.getUserId();
		String nickName = my.getNickName();
		String icon=my.gameData.headIconUrl;
		int lv=my.gameData.playerLevel;
		int state=FriendState.未处理;//未处理
		boolean readState=false;
		int roleId=my.getRoleId();
		int vipLv=my.getVipLv();
		
		AddFriendsVO vo =new AddFriendsVO(id,nickName,icon,lv,state,readState,roleId,vipLv);
		Player player = UserCache.map_onlines.get(request.id);// 对方玩家
		if (player==null) 
		{
			if (isDebug)Tool.print_debug_level0("玩家不在线");
			if (!friendsService.haveFriendsEvent(request.id, id)) //数据库是否存在 
			{
				friendsService.insetUserFriendsEventInfo(request.id, vo);
				AddFriendsResponse response =new AddFriendsResponse(AddFriendsResponse.SUCCESS,vo);
				my.sendResponse(response);
			}
			else 
			{
				Tool.print_debug_level0("玩家："+my+"\n重复请求添加："+vo);
				AddFriendsResponse response =new AddFriendsResponse(AddFriendsResponse.Error_重复请求添加);
				my.sendResponse(response);
			}
		}
		else 
		{
			if (isDebug)Tool.print_debug_level0("玩家在线");
			if (!player.haveFriendInfoId(userId)) 
			{
				player.addFriendsData(vo);
				friendsService.insetUserFriendsEventInfo(request.id, vo);
				
				AddFriendsResponse response =new AddFriendsResponse(AddFriendsResponse.SUCCESS,vo);
				player.sendResponse(response);
				my.sendResponse(response);
			}
			else
			{
				
			}
		}
	}

	@Protocol(msgType = MsgTypeEnum.ASKFRIENDS)
	/**请求好友*/
	public  void askFriends(ChannelHandlerContext ctx,AskFriendsRequest request, MySession session)
	{
		Player my = playerService.getPlayerAndCheck(session);
		if (my == null)
		{
			return;
		}

		// 取得缓存好友列表
		if (my.list_friendsData == null)
		{
			my.list_friendsData = friendsService.initFriendsData(my.getUserId());// 初始化好友列表
		}
		else
		{
			ArrayList<Integer> list_friendId = new ArrayList<Integer>();
			for (FriendsDataVO vo : my.list_friendsData)
			{
				list_friendId.add(vo.friendsData.id);
			}
			my.list_friendsData = friendsService.getFriendDataVO(list_friendId);// 同步信息
		}

		if (my.list_friendsData == null || my.list_friendsData.size() <= 0)
		{
			my.list_friendsData = new ArrayList<FriendsDataVO>();// 没有好友

			if (isDebug)
				Tool.print_debug_level0("玩家：" + my.getUserId() + my.getNickName() + "，没有好友");

			// 请求好友列表响应（推送到客户端）
			AskFriendsResponse response = new AskFriendsResponse(AskFriendsResponse.ERROR_NUll);
			GameServerHelper.sendResponse(ctx, response);
		}
		else
		{
			for (FriendsDataVO friendVO : my.list_friendsData)
			{
				Tool.print_debug_level0("玩家：" + my + "好友有：" + friendVO);
				if ((UserCache.isOnline(friendVO.friendsData.id)))
				{
					friendVO.stateOnLine = true;
				}
				else
				{
					friendVO.stateOnLine = false;
				}
			}

			if (isDebug)
				Tool.print_debug_level0("玩家：" + my.getUserId() + my.getNickName() + "，好友有：" + my.list_friendsData);

			// 请求好友列表响应（推送到客户端）
			AskFriendsResponse response = new AskFriendsResponse(AskFriendsResponse.SUCCESS, my.list_friendsData);
			GameServerHelper.sendResponse(ctx, response);
		}
	}

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
}
