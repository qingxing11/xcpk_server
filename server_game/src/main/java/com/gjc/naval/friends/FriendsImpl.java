package com.gjc.naval.friends;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmd.pushFriendsOnLine.PushFriendsOnLine;
import com.gjc.cmd.friendChat.PushFriendChatInfoResponse;
import com.gjc.cmd.friends.PushAddFriendsEventResponse;
import com.gjc.cmd.friends.PushFriendsInfoResponse;
import com.gjc.cmd.friends.PushPlayerOnLineResponse;
import com.gjc.naval.vo.chat.FriendsDataVO;
import com.gjc.naval.vo.friendChat.FriendChatVO;
import com.wt.archive.AddFriendsVO;
import com.wt.archive.FriendInfoVO;
import com.wt.archive.FriendsData;
import com.wt.naval.cache.UserCache;
import com.wt.naval.dao.impl.FriendsDaoImpl;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;
import com.wt.util.io.MySerializerUtil;
import com.wt.xcpk.PlayerSimpleData;

import data.define.FriendState;
import data.define.ReadInfoState;
import model.UserFriendseventModel;
import model.UserFriendsinfoModel;

@Service
public class FriendsImpl implements FriendsService {
	private int vipLvPre = 5;// 每一个vip等级（增加）好友的数量
	private int lvPre = 2;// 每五个等级（增加）好友的数量
	private int lvDenominator = 5;// 五个等级为一个提升点
	@Autowired
	private PlayerService playerService;

	@PostConstruct
	private void ini() {

	}

	public void InitFriendEvent(int userId) {

	}

	/** 好友数量限制 */
	@Override
	public boolean playerIsCanAddFriend(int userId) {
		boolean canAdd = false;
		// 玩家是否还可以添加好友
		Player player = playerService.getPlayer(userId);
		if (player == null) {
			Tool.print_error("FriendsImpl 玩家数据为空：userId=" + userId);
			return canAdd;
		}
		int friendsCount = player.list_friendsData.size();
		int lv = player.gameData.playerLevel;
		int vipLv = player.getVipLv();
		int curFriendsMaxNum = ((lv - 1) / lvDenominator + 1) * lvPre + vipLv * vipLvPre;

		int cancel = 0;// TODO 待修改
		if (player.getList_addFriendsData() != null) {
			for (AddFriendsVO vo : player.getList_addFriendsData()) {
				if (vo.state == FriendState.未处理) {
					cancel++;
				}
			}
		}

		if (curFriendsMaxNum - friendsCount - cancel >= 1) {
			canAdd = true;
		}
		return canAdd;
	}

	@Override
	public boolean haveFriendsEvent(int userId, int otherId) {
		return FriendsDaoImpl.selectFriendsEventData(userId, otherId) != null;
	}

	@Override
	public void setAddInfoState(Player player, int userId, boolean state) {
		if (player == null) {
			return;
		}
		player.setAddInfoState(userId, state);
		FriendsDaoImpl.updatatAddInfoUserFriendsEventData(player.getUserId(), userId, state);
	}

	@Override
	public void setInfoState(Player player, int userId, int state) {
		if (player == null) {
			return;
		}
		player.setInfoState(userId, state);
		FriendsDaoImpl.updatatUserFriendsEventData(player.getUserId(), userId, state);
	}

	@Override
	public void updataUserFriendsList(int userId, FriendsDataVO vo)
	{
		UserFriendsinfoModel userFriendsinfo = FriendsDaoImpl.selectFriendsData(userId);
		if (userFriendsinfo == null || userFriendsinfo.getFriendsList() == null)
		{
			ArrayList<Integer> list_friendsId = new ArrayList<Integer>();
			list_friendsId.add(vo.friendsData.id);
			byte[] b = MySerializerUtil.serializer_Java(list_friendsId);
			FriendsDaoImpl.updatatUserFriendsList(userId, b);

			return;
		}

		ArrayList<Integer> list_friendsId = (ArrayList<Integer>) MySerializerUtil.deserializer_Java(userFriendsinfo.getFriendsList());
		if (list_friendsId == null)
		{
			list_friendsId = new ArrayList<Integer>();
		}
		boolean isHave = false;
		for (int friendId : list_friendsId)
		{
			if (friendId == vo.friendsData.id)
			{
				isHave = true;
				break;
			}
		}
		if (isHave)
		{
			Tool.print_debug_level0("玩家：" + userId + "\n已经有好友：" + vo);
			return;
		}
		list_friendsId.add(vo.friendsData.id);
		byte[] b = MySerializerUtil.serializer_Java(list_friendsId);
		FriendsDaoImpl.updatatUserFriendsList(userId, b);
	}

	@Override
	public void removeUserFriendsList(int userId, int otherId) {
		Player player = playerService.getPlayer(userId);
		ArrayList<Integer> list_friends = null;
		if (player != null) {
			// Tool.print_debug_level0("移除方在线:"+userId);
			player.removeFriend(otherId);
			player.removeAddFriendsEvent(otherId);
			list_friends = new ArrayList<Integer>();
			for (FriendsDataVO vo : player.list_friendsData) {
				list_friends.add(vo.friendsData.id);
			}

		} else {
			// Tool.print_debug_level0("移除方不在线:"+userId);
			UserFriendsinfoModel userFriendsinfo = FriendsDaoImpl.selectFriendsData(userId);
			if (userFriendsinfo == null || userFriendsinfo.getFriendsList() == null) {
				return;
			}
			list_friends = (ArrayList<Integer>) MySerializerUtil.deserializer_Java(userFriendsinfo.getFriendsList());
			if (list_friends == null) {
				return;
			}
			list_friends.remove((Integer) otherId);
		}
		Player other = playerService.getPlayer(otherId);
		if (other != null) {
			other.removeAddFriendsEvent(userId);
		}

		// Tool.print_debug_level0("序列化好友列表");
		byte[] b = MySerializerUtil.serializer_Java(list_friends);
		FriendsDaoImpl.updatatUserFriendsList(userId, b);
		FriendsDaoImpl.deleteUserFriendsEventData(userId, otherId);
	}

	@Override
	public void insetUserFriendsEventData(int userId, int otherId, String info, int infoState) {
		FriendsDaoImpl.insetUserFriendsEventData(userId, otherId, info, infoState);
	}

	@Override
	public void addFriendVO(Player player, FriendsDataVO vo)
	{
		if (player != null && vo != null)
		{
			if (player.list_friendsData == null || player.list_friendsData.size() == 0)
			{
				ArrayList<FriendsDataVO> list_friends = new ArrayList<FriendsDataVO>();
				list_friends.add(vo);
				player.list_friendsData = list_friends;

				ArrayList<Integer> list_friendId = new ArrayList<Integer>();
				list_friendId.add(vo.friendsData.id);
				byte[] b = MySerializerUtil.serializer_Java(list_friendId);
				FriendsDaoImpl.updatatUserFriendsList(player.getUserId(), b);
				return;
			}
			boolean isHave = false;
			for (FriendsDataVO fdVO : player.list_friendsData)
			{
				if (fdVO.friendsData.id == vo.friendsData.id)
				{
					isHave = true;
					break;
				}
			}
			if (isHave)
			{
				Tool.print_debug_level0("玩家：" + player + "\n已经有好友：" + vo);
				return;
			}
			player.list_friendsData.add(vo);

			ArrayList<Integer> list_friendId = new ArrayList<Integer>();
			for (FriendsDataVO fdVO : player.list_friendsData)
			{
				list_friendId.add(fdVO.friendsData.id);
			}
			byte[] b = MySerializerUtil.serializer_Java(list_friendId);
			FriendsDaoImpl.updatatUserFriendsList(player.getUserId(), b);
		}
	}

	@Override
	public void setAddFriendsEventState(Player player, int userId, int state) {
		if (player == null) {
			return;
		}
		ArrayList<AddFriendsVO> list = player.getList_addFriendsData();
		if (list == null || list.size() == 0) {
			return;
		}
		for (AddFriendsVO addFriendsVO : list) {
			if (addFriendsVO.id == userId) {
				addFriendsVO.state = state;
				FriendsDaoImpl.updatatUserFriendsEvent_state(player.getUserId(), userId, state);
			}
		}
	}

	@Override
	public AddFriendsVO getAddFriendsVO(Player player, int userId) {
		if (player == null) {
			return null;
		}
		ArrayList<AddFriendsVO> list = player.getList_addFriendsData();
		if (list == null || list.size() == 0) {
			return null;
		}

		for (AddFriendsVO addFriendsVO : list) {
			if (addFriendsVO.id == userId) {
				return addFriendsVO;
			}
		}
		return null;
	}

	@Override
	public boolean haveFriends(Player player, int otherId) {
		if (player.list_friendsData == null) {
			return false;
		}
		for (FriendsDataVO vo : player.list_friendsData) {
			if (vo.friendsData.id == otherId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addFriendsInfo(Player player, int userId, String info, int state)
	{
		if (player == null)
		{
			return;
		}
		FriendInfoVO vo = new FriendInfoVO(userId, info, state);
		if (player.haveFriendInfoId(userId))
		{
			FriendsDaoImpl.updatatUserFriendsEventData(player.getUserId(), userId, info, state);
		}
		else
		{
			Tool.print_debug_level0("对方没有添加好友事件，插入事件");
			FriendsDaoImpl.insetUserFriendsEventData(player.getUserId(), userId, info, state);
		}

		player.putFriendsInfo(userId, vo);
	}

	@Override
	public void addFriendsEvent(Player player) {
		if (player == null) {
			return;
		}

		ArrayList<AddFriendsVO> list_addFriendsData = new ArrayList<AddFriendsVO>();
		HashMap<Integer, FriendInfoVO> infoList = new HashMap<Integer, FriendInfoVO>();
		List<UserFriendseventModel> modelList = FriendsDaoImpl.selectFriendsEventData(player.getUserId());
		if (modelList == null) {
			return;
		}
		for (UserFriendseventModel userFriendseventModel : modelList) {
			AddFriendsVO vo = new AddFriendsVO(userFriendseventModel);
			list_addFriendsData.add(vo);
			FriendInfoVO fvo = new FriendInfoVO(userFriendseventModel.getOtherID(), userFriendseventModel.getInfo(),
					userFriendseventModel.getInfoState());
			infoList.put(userFriendseventModel.getOtherID(), fvo);
		}
		player.setList_addFriendsData(list_addFriendsData);
		player.setInfoList(infoList);
	}

	@Override
	public void CancelFriendsEvent(Player player) {
		if (player == null) {
			return;
		}

		ArrayList<AddFriendsVO> list_addFriendsData = player.getList_addFriendsData();
		if (list_addFriendsData == null || list_addFriendsData.size() == 0) {
			return;
		}

		ArrayList<AddFriendsVO> list = new ArrayList<AddFriendsVO>();
		for (AddFriendsVO addFriendsVO : list_addFriendsData) {
			if (addFriendsVO.state == FriendState.未处理) {
				list.add(addFriendsVO);
			}
		}
		if (list != null && list.size() > 0) {
			PushAddFriendsEventResponse response = new PushAddFriendsEventResponse(PushAddFriendsEventResponse.SUCCESS,
					list);
			player.sendResponse(response);
		}
	}

	@Override
	public void CancelFriendsInfo(Player player) {
		if (player == null) {
			return;
		}

		HashMap<Integer, FriendInfoVO> infoList = player.getInfoList();
		if (infoList == null || infoList.size() == 0) {
			return;
		}

		ArrayList<FriendInfoVO> list = new ArrayList<FriendInfoVO>();

		for (FriendInfoVO friendInfoVO : infoList.values()) {
			if (friendInfoVO.read != ReadInfoState.删除 && friendInfoVO.getInfo() != null
					&& (!friendInfoVO.getInfo().isEmpty())) {
				list.add(friendInfoVO);
			}
		}
		// list.addAll(infoList.values());

		if (list != null && list.size() > 0) {
			PushFriendsInfoResponse response = new PushFriendsInfoResponse(PushFriendsInfoResponse.SUCCESS, list);
			player.sendResponse(response);
		}
	}

	/**
	 * 初始化并推送好友私聊信息
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public void initPlayerFriendChatInfo(int userId) {
		Player player = playerService.getPlayer(userId);
		if (player == null) {
			Tool.print_error("玩家竟然不在线！！！+userId" + userId);
			return;
		}
		UserFriendsinfoModel userFriendsinfo = FriendsDaoImpl.selectFriendsData(userId);
		if (userFriendsinfo == null || userFriendsinfo.getFriendChatList() == null) {
			return;
		}
		ArrayList<FriendChatVO> list = (ArrayList<FriendChatVO>) MySerializerUtil
				.deserializer_Java(userFriendsinfo.getFriendChatList());

		if (list != null) {
			player.setList_friendChat(list);
			PushFriendChatInfoResponse response = new PushFriendChatInfoResponse(list);
			player.sendResponse(response);
		}

	}

	/**
	 * 添加好友私聊消息
	 * 
	 * @param userId
	 * @param vo
	 */
	@Override
	public void updateFriendChatVO(int userId, int friendId, FriendChatVO vo) {
		/** 玩家刚离线但没有真正离线 */
//		Tool.print_debug_level0("传入消息："+vo);
		Player player = playerService.getPlayer(userId);
		ArrayList<FriendChatVO> list;
		boolean online = true;
		if (player == null) {
//			Tool.print_debug_level0("玩家不在线："+userId);
			online = false;
			UserFriendsinfoModel userFriendsinfo = FriendsDaoImpl.selectFriendsData(userId);
			list = (ArrayList<FriendChatVO>) MySerializerUtil.deserializer_Java(userFriendsinfo.getFriendChatList());

//			 for (FriendChatVO friendChatVO : list) {
//					Tool.print_debug_level0("玩家不在线 当前消息有："+friendChatVO);
//				}
		} else {
			list = player.getList_friendChat();
//			 for (FriendChatVO friendChatVO : list) {
//					Tool.print_debug_level0("当前消息有："+friendChatVO);
//				}
		}

		if (list == null) {
			list = new ArrayList<FriendChatVO>();
			if (online) {
				player.setList_friendChat(list);
			}
		}

		getInfoNumAndRemove(list, friendId);
		list.add(vo);

//		for (FriendChatVO friendChatVO : list) {
//			Tool.print_debug_level0("玩家不在线 当前消息有："+friendChatVO);
//		}
		byte[] b = MySerializerUtil.serializer_Java(list);
		FriendsDaoImpl.updatatUserFriendsChatInfo(userId, b);
	}

	private int chatMax = 10;

	/**
	 * 返回当前聊天消息数量
	 * 
	 * @param list
	 * @param friendId
	 */
	private void getInfoNumAndRemove(ArrayList<FriendChatVO> list, int friendId) {
		int num = 0;
		ArrayList<Integer> removeIndex = new ArrayList<Integer>();
		int index = 0;
		for (FriendChatVO friendChatVO : list) {
			if (friendChatVO.userId == friendId || friendChatVO.receiveUserId == friendId) {
				removeIndex.add(index);
				num++;
			}
			index++;
		}
		if (num <= chatMax) {
			return;
		}
		for (int i = 0; i < num - chatMax; i++) {
			list.remove(removeIndex.get(i));
		}
	}

	/**
	 * 保存玩家私聊消息
	 * 
	 * @param userId
	 */
	@Override
	public void saveFriendChatVO(Player player) {
		if (player == null) {
			return;
		}
		ArrayList<FriendChatVO> list = player.getList_friendChat();
		if (list == null) {
			return;
		}
		byte[] b = MySerializerUtil.serializer_Java(list);
		FriendsDaoImpl.updatatUserFriendsChatInfo(player.getUserId(), b);
	}

	/**
	 * 保存玩家私聊消息状态
	 * 
	 * @param userId
	 */
	@Override
	public void saveFriendChatVOState(int userId, int friendId) {
		Player player = playerService.getPlayer(userId);
		if (player == null) {
			return;
		}
		ArrayList<FriendChatVO> list = player.getList_friendChat();
		if (list == null) {
			return;
		}
		for (FriendChatVO friendChatVO : list) {
			if (friendChatVO.userId == friendId || friendChatVO.receiveUserId == friendId) {
				if (!friendChatVO.read) {
					friendChatVO.read = true;
				}
			}
		}
	}

	/** 给玩家好友推送状态 */
	@Override
	public void playState(int userId, boolean state) {
		Player player = playerService.getPlayer(userId);
		if (player == null) {
			return;
		}
		ArrayList<FriendsDataVO> list = player.list_friendsData;

		if (list != null) {
			PushPlayerOnLineResponse response = new PushPlayerOnLineResponse(userId, state);

			for (FriendsDataVO friendsDataVO : list) {
				int id = friendsDataVO.friendsData.id;
				Player player2 = playerService.getPlayer(id);
				if (player2 != null) {
					player2.sendResponse(response);
				}
			}
		}
	}

	/**
	 * 登录时初始化
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked") 
	@Override
	public ArrayList<FriendsDataVO> initFriendsData(int userId) {
		UserFriendsinfoModel userFriendsinfo = FriendsDaoImpl.selectFriendsData(userId);
		if (userFriendsinfo == null) {
			Tool.print_debug_level0("玩家：" + userId + "的数据库读取UserFriendsinfo错误！");

			return null;
		}
		if (userFriendsinfo.getFriendsList() == null) {
			Tool.print_debug_level0("玩家没有好友！");
			return null;
		}
		ArrayList<Integer> list_friends = (ArrayList<Integer>) MySerializerUtil.deserializer_Java(userFriendsinfo.getFriendsList());
		if (list_friends == null) {
			return null;
		}
		return getFriendDataVO(list_friends);
	}

	@Override
	public ArrayList<FriendsDataVO> getFriendDataVO(ArrayList<Integer> list_friends)
	{
		if (list_friends == null || list_friends.size() == 0)
		{
			return null;
		}
		ArrayList<FriendsDataVO> friends = new ArrayList<FriendsDataVO>();

		try
		{
			Collection<PlayerSimpleData> list_Simple = playerService.getPlayerSimpleData(list_friends);
			for (PlayerSimpleData simple : list_Simple)
			{
				int userId = simple.userId;
				boolean stateOnLine = playerService.isOnline(userId);
				FriendsDataVO vo = new FriendsDataVO(userId, simple.lv, simple.headIconUrl, simple.nickName, stateOnLine, simple.roleId, simple.vipLv,simple.headIconUrl);
				friends.add(vo);
				Tool.print_debug_level0("请求后的玩家好友简单信息：" + vo);
			}
		}
		catch (Exception e)
		{
			Tool.print_error(e);
		}
		return friends;
	}

	/**
	 * 更新好友列表数据
	 * 
	 * @param userId
	 * @return
	 */
	@Override
	public boolean updataUserFriendsInfo(Player player) {
		if (player == null || player.list_friendsData == null) {
			return false;
		}
		ArrayList<Integer> list_friendsId = new ArrayList<Integer>();
		for (FriendsDataVO vo : player.list_friendsData) {
			list_friendsId.add(vo.friendsData.id);
		}

		byte[] b = MySerializerUtil.serializer_Java(list_friendsId);
		FriendsDaoImpl.updatatUserFriendsList(player.getUserId(), b);
		return true;
	}

	/**
	 * 更新添加好友事件列表数据
	 * 
	 * @param userId 对方
	 * @param vo     请求方数据
	 * @return
	 */
	@Override
	public void insetUserFriendsEventInfo(int userId, AddFriendsVO vo) {
		FriendsDaoImpl.insetUserFriendsEventData(userId, vo);
	}

	/**
	 * 获取添加好友事件列表数据
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserFriendseventModel> getUserFriendsInfo(int userId) {
		List<UserFriendseventModel> modelList = FriendsDaoImpl.selectFriendsEventData(userId);
		return modelList;
	}

	@Override
	public void friendIsOnLine(Player player, boolean onLine) {
		// Tool.print_debug_level0("玩家上线！！！");
		// TODO 待修改：玩家上线一段时间推送一次
		if (player.list_friendsData == null) {
			player.list_friendsData = initFriendsData(player.getUserId());
		}
		if (player.list_friendsData == null || player.list_friendsData.size() <= 0) {
			return;
		}

		for (FriendsDataVO friend : player.list_friendsData) {
			if (UserCache.isOnline(friend.friendsData.id)) {

				Tool.print_debug_level0(
						"向玩家：" + friend.friendsData.id + "，--推送玩家：" + player.getUserId() + ",是否上线:" + onLine);

				PushFriendsOnLine push = new PushFriendsOnLine(player.getUserId(), onLine);
				GameServerHelper.sendResponse(UserCache.getChannel(friend.friendsData.id), push);
			}
		}
	}
}
