package com.wt.naval.vo.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.gjc.naval.friends.FriendsService;
import com.gjc.naval.vo.chat.FriendsDataVO;
import com.gjc.naval.vo.friendChat.FriendChatVO;
import com.gjc.naval.vo.safebox.SafeBoxRecordVO;
import com.wt.archive.AddFriendsVO;
import com.wt.archive.FriendInfoVO;
import com.wt.archive.GameData;
import com.wt.archive.MailData;
import com.wt.archive.MailDataPO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.naval.cache.UserCache;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.naval.main.GameServerHelper;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.util.sort.ISortLong;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.Room;
import com.wt.xcpk.classic.ClassicGame;
import com.wt.xcpk.vo.poker.PokerVO;

import data.define.ReadInfoState;

/**
 * 游戏中的玩家角色化身
 *
 */
public class Player implements ISortLong 
{
	public static final boolean isClassicRoomDebug = true;
	
	/**
	 * 准备状态
	 */
	public static final int STATE_IDLE = 0;
	public static final int STATE_NORMAL = 1;
	public static final int STATE_DIE = 2;
	
	/**
	 * 刚坐下
	 */
	public static final int STATE_WAIT_PLAY = 3;

	private int gameState;
	
	private boolean isCheckPoker;
	
	public long lastInfoTime = -1;// 上条消息的时间

	public GameData gameData;

	public ArrayList<FriendsDataVO> list_friendsData;

	/***
	 * key:对应PropertyType枚举中的私人领地选项，value:数量。取用可调用DisposeHomeBuildData中给定的方法
	 */
	public HashMap<Integer, Integer> functionalBuildTypes = new HashMap<>();

	public HashMap<Integer, MailData> hashMap_MailData = new HashMap<>();

	public int mailIndex;

	/** 登出或者自动检测到离线时间 */
	public long last_logout_time;

	public boolean isDetection;

	/**
	 * 玩家最新聊天信息
	 */
	public String player_current_chat;

	private boolean robot;
	
	private MySession session;

	/**
	 * 在世界地图上所处的索引
	 */
	private String mapIndex;

	/**
	 * 未读邮件个数
	 */
	private int mailNoReadNum;

	/**
	 * 保存玩家的所有邮件信息
	 */
	private ArrayList<MailDataPO> userEmails = new ArrayList<>();
	
	private ArrayList<AddFriendsVO> list_addFriendsData;//请求添加好友事件

	private int classicGamepos = -1;
	
	private int manyGamepos = -1;
	
	private int fruitGamepos = -1;
	
	private int killRoomPos = -1;
	
	private boolean isModify;
	
	private Room insideRoom;
	
	private PlayerSimpleData playerSimpleData;
	
	private HashMap<Integer,FriendInfoVO> infoList=new HashMap<Integer,FriendInfoVO>();//消息通知
	
	private ArrayList<SafeBoxRecordVO> safeListVO;
	
	private ArrayList<Boolean> signList;//签到列表
	
	private long signTime;//上次签到时间
	
	private long chatTime;
	
	private boolean isReday;
	
	private ArrayList<PokerVO> list_handPokers = new ArrayList<PokerVO>();
	
	private ClassicGame classicGame;
	
	private ClassicGame manyPepolGame;
	
	private long roundBet;
	
	private long roundCalcScore;
	
	private boolean isComparerPoker;
	
	private int manyPepolRoomRound;
	
	private int gglReward;
	
	private int killRoomPayBet;
	
	private long killRoomRoundCalcScore;
	
	private boolean killRoomGiveBack;
	
	private long killRoomRoundAllScore;

	private long killRoomRoundCalcWinScore;
	
	private int fruitRoomBet;
	
	private int lotteryPayNum;
	public void clearFruitRoomBet()
	{
		fruitRoomBet = 0;
	}
	
	public void addFruitRoomBet(int num)
	{
		fruitRoomBet += num;
	}
	
	public int getFruitRoomBet()
	{
		return fruitRoomBet;
	}

	public void setFruitRoomBet(int fruitRoomBet)
	{
		this.fruitRoomBet = fruitRoomBet;
	}

	public void addKillRoomRoundCalcWinScore(long score)
	{
		this.killRoomRoundCalcWinScore += score;
	}
	
	public boolean getKillRoomGiveBack()
	{
		return killRoomGiveBack;
	}

	public void setKillRoomGiveBack(boolean killRoomGiveBack)
	{
		this.killRoomGiveBack = killRoomGiveBack;
	}
	
	public long getKillRoomRoundCalcWinScore()
	{
		return killRoomRoundCalcWinScore;
	}

	public void setKillRoomRoundCalcWinScore(long killRoomRoundCalcWinScore)
	{
		this.killRoomRoundCalcWinScore = killRoomRoundCalcWinScore;
	}

	public long getKillRoomRoundAllScore()
	{
		return killRoomRoundAllScore;
	}

	public void setKillRoomRoundAllScore(long killRoomRoundAllScore)
	{
		this.killRoomRoundAllScore = killRoomRoundAllScore;
	}
	
	public void addKillRoomRoundAllScore(long killRoomRoundAllScore)
	{
		this.killRoomRoundAllScore += killRoomRoundAllScore;
	}

	public long getKillRoomRoundCalcScore()
	{
		return killRoomRoundCalcScore;
	}
	
	public void setKillRoomRoundCalcScore(long killRoomRoundCalcScore)
	{
		this.killRoomRoundCalcScore = killRoomRoundCalcScore;
	}

	/**
	 * 通杀场每一门结算分数
	 * @param killRoomRoundCalcScore
	 */
	public void addKillRoomRoundCalcScore(long killRoomRoundCalcScore)
	{
		this.killRoomRoundCalcScore += killRoomRoundCalcScore;
	}
	
	public void clearKillRoomRoundCalcScore()
	{
		this.killRoomRoundCalcScore = 0;
		this.killRoomRoundAllScore = 0;
		killRoomRoundCalcWinScore = 0;
		killRoomGiveBack = false;
		clearOfflineRound();
	}

	public void changeToWaitPlay()
	{
		if(isClassicRoomDebug)Tool.print_debug_level0(getNickName()+",changeToWaitPlay切换到waitPlay");
		gameState = STATE_WAIT_PLAY;
	}
	
	public boolean isWaitPlay()
	{
		return  gameState == STATE_WAIT_PLAY;
	}
	
	public int getKillRoomPos()
	{
		return killRoomPos;
	}

	public void setKillRoomPos(int killRoomPos)
	{
		this.killRoomPos = killRoomPos;
	}
	
	public void addKillRoomPayBet(int num)
	{
		killRoomPayBet += num;
	}
	
	public int getKillRoomPayBet()
	{
		return killRoomPayBet;
	}
	
	public void clearKillRoomPayBet()
	{
		killRoomPayBet = 0;
	}
	
	public int getGglReward()
	{
		return gglReward;
	}

	public void setGglReward(int gglReward)
	{
		this.gglReward = gglReward;
	}
	
	private ArrayList<FriendChatVO> list_friendChat;
	
	public ClassicGame getManyPepolGame()
	{
		return manyPepolGame;
	}

	public void setManyPepolGame(ClassicGame manyPepolGame)
	{
		this.manyPepolGame = manyPepolGame;
		Tool.print_debug_level0("设置万人场，玩家:"+gameData.userId);
	}
	
	/**
	 * 万人离线站起时会调用
	 */
	public void clearManyPepolGame()
	{
		this.manyPepolGame = null;
		manyGamepos = -1;
		Tool.print_debug_level0("清除万人场，玩家:"+gameData.userId);
	}
	
	public ArrayList<FriendChatVO> getList_friendChat() {
		return list_friendChat;
	}

	public void setList_friendChat(ArrayList<FriendChatVO> list_friendChat) {
		this.list_friendChat = list_friendChat;
	}

	
	private int offlineRound;
	public int getOfflineRound()
	{
		return offlineRound;
	}

	public void addOfflineRound()
	{
		this.offlineRound++;
	}
	
	public void clearOfflineRound()
	{
		this.offlineRound  = 0;
	}

	public int getManyPepolRoomRound()
	{
		return manyPepolRoomRound;
	}

	public void addManyPepolRoomRound()
	{
		this.manyPepolRoomRound ++;
	}
	
	public void clearManyPepolRoomRound()
	{
		manyPepolRoomRound = 0;
	}

	public boolean isComparerPoker()
	{
		return isComparerPoker;
	}

	public void setComparerPoker()
	{
		isComparerPoker = true;
	}
	
	public void clearComparePoker()
	{
		isComparerPoker = false;
	}

	public long getRoundCalcBet()
	{
		return roundCalcScore;
	}
	
	public void clearRoundCalcBet()
	{
		roundCalcScore = 0;
	}

	public void addRoundCalcBet(long roundCalcScore)
	{
		this.roundCalcScore += roundCalcScore;
	}
	
	public void setRoundCalcBet(long roundCalcScore)
	{
		this.roundCalcScore = roundCalcScore;
	}

	public void addRoundBet(long num)
	{
		roundBet += num;
		Tool.print_debug_level0("addRoundBet:"+num+",all:"+roundBet);
	}
	
	public long getRoundBet()
	{
		return roundBet;
	}
	
	public void clearRoundBet()
	{
		roundBet = 0;
	}
	
	public ClassicGame getClassicGame()
	{
		return classicGame;
	}

	public void setClassicGame(ClassicGame classicGame)
	{
		this.classicGame = classicGame;
		setInsideRoom(classicGame.getRoom());
		Tool.print_debug_level0("设置经典场，玩家:"+gameData.userId);
	}
	
	public void clearClassicGame()
	{
		this.classicGame = null;
		classicGamepos = -1;
		Tool.print_debug_level0("清除经典场，玩家:"+gameData.userId);
	}
	
	public void clearPos()
	{
		classicGamepos = -1;
		manyGamepos = -1;
		fruitGamepos = -1;
		Tool.print_debug_level0("清除所有位置，玩家:"+gameData.userId);
	}
	
	/**
	 * 万人站起时会调用
	 */
	public void clearGameNotPos()
	{
		this.classicGame = null;
		manyGamepos = -1;
		Tool.print_debug_level0("clearGameNotPos，玩家:"+gameData.userId);
	}

	public void addAllPoker(ArrayList<PokerVO> list_pokers)
	{
		list_handPokers.clear();
		list_handPokers.addAll(list_pokers);
		
		Tool.print_debug_level0("发牌，player:"+getNickName()+",poker:"+list_handPokers);
	}
	
	public int getState()
	{
		return gameState;
	}

	public boolean isCheckPoker()
	{
		return isCheckPoker;
	}
	
	public ArrayList<PokerVO> getPokers()
	{
		return list_handPokers;
	}
	
	public boolean isReday()
	{
		return isReday;
	}

	public void setReday(boolean isReday)
	{
		this.isReday = isReday;
	}

	public long getChatTime() {
		return chatTime;
	}

	public void setChatTime(long chatTime) {
		this.chatTime = chatTime;
	}

	public boolean isAccountsupplementary()
	{
		return gameData.accountSupplementary;
	}
	
	public void setAccountsupplementary()
	{
		gameData.accountSupplementary = true;
	}
	
	public long getSignTime() {
		return signTime;
	}

	public void setSignTime(long signTime) {
		this.signTime = signTime;
	}

	public ArrayList<Boolean> getSignList() {
		return signList;
	}

	public void setSignList(ArrayList<Boolean> signList) {
		this.signList = signList;
	}
	
	public int getLevel()
	{
		return gameData.playerLevel;
	}
	
	public void addSafeBoxList(SafeBoxRecordVO vo)
	{
		Tool.print_debug_level0("addSafeBoxList......");
		if (safeListVO==null) 
		{
			safeListVO=new ArrayList<SafeBoxRecordVO>();
		}
		safeListVO.add(vo);
	}
	
	public ArrayList<SafeBoxRecordVO> getSafeListVO() {
		return safeListVO;
	}
	public void setSafeListVO(ArrayList<SafeBoxRecordVO> safeListVO) {
		Tool.print_debug_level0("setSafeListVO......");
		this.safeListVO = safeListVO;
	}
	public void removeInfoList(int otherUserId)
	{
		infoList.remove(otherUserId);
	}
	public void addInfoList(int otherUserId,FriendInfoVO info)
	{
		infoList.put(otherUserId, info);
	}

	public HashMap<Integer, FriendInfoVO> getInfoList() {
		return infoList;
	}

	public void setInfoState(int userId,int state)
	{
		if (infoList==null||infoList.size()==0) 
		{
			return;
		}
		FriendInfoVO vo=infoList.get(userId);
		if (vo==null) {
			Tool.print_error("不存在好友Id="+userId);
			return;
		}
		vo.read=state;
		Tool.print_debug_level0("添加好友信息状态："+ReadInfoState.getTitle(state)+"******"+vo);
	}
	
	public void setAddInfoState(int userId,boolean state)
	{
		if (list_addFriendsData==null||list_addFriendsData.size()==0) 
		{
			return;
		}
		for (AddFriendsVO addFriendsVO : list_addFriendsData)
		{
			if (addFriendsVO.id==userId) 
			{
				addFriendsVO.readState=state;
			}
		}
	}
	
	public void setInfoList(HashMap<Integer, FriendInfoVO> infoList) {
		this.infoList = infoList;
	}

	public Room getInsideRoom()
	{
		return insideRoom;
	}

	public void setInsideRoom(Room insideRoom)
	{
		this.insideRoom = insideRoom;
		Tool.print_debug_level0("设置Room，玩家:"+gameData.userId);
	}
	
	public void clearRoom()
	{
		this.insideRoom = null;
		Tool.print_debug_level0("清除Room，玩家:"+gameData.userId);
	}

//	public int getPos()
//	{
//		return pos;
//	}

//	public void setPos(int pos)
//	{
//		Tool.print_debug_level0(getNickName()+",玩家设置位置:"+pos);
//		this.pos = pos;
//	}

	public int getClassicGamePos()
	{
		return classicGamepos;
	}

	public void setClassicGamePos(int pos)
	{
		Tool.print_debug_level0(getNickName()+"经典场,玩家设置位置:"+pos);
		this.classicGamepos = pos;
	}
	
	public int getManyGamePos()
	{
		return manyGamepos;
	}

	public void setManyGamePos(int pos)
	{
		Tool.print_debug_level0(getNickName()+"万人场,玩家设置位置:"+pos);
		this.manyGamepos = pos;
	}
	
	public int getFruitGamePos()
	{
		return fruitGamepos;
	}

	public void setFruitGamePos(int pos)
	{
		Tool.print_debug_level0(getNickName()+"水果机,玩家设置位置:"+pos);
		this.fruitGamepos = pos;
	}
	
	public ArrayList<AddFriendsVO> getList_addFriendsData() {
		return list_addFriendsData;
	}

	public void setList_addFriendsData(ArrayList<AddFriendsVO> list_addFriendsData) {
		this.list_addFriendsData = list_addFriendsData;
	}
	public void addFriendsData(AddFriendsVO vo) {
		list_addFriendsData.add(vo);
	}

	public long getLastLogoutTime() {
		Timestamp timestamp = gameData.lastLogoutTime;
		if (timestamp == null) {
			return 0;
		}
		return timestamp.getTime();
	}

	public String getMapIndex() {
		return mapIndex;
	}

	public void setMapIndex(String mapIndex) {
		this.mapIndex = mapIndex;
	}

	@Autowired
	private FriendsService friendsService;
	
	
	
	/**
	 * 玩家（是否在线）上线后者下线
	 * 
	 * @param onLine
	 */
	
	public void online(boolean onLine) {
	}

	public Player() {
		// this.worldTownData = new WorldTownData(getUserId());
	}

	public Player(GameData gameData) {
		this.gameData = gameData;
	}

	public void setSession(MySession session) {
		this.session = session;
	}

	public MySession getSession() {
		return session;
	}

	public void sendResponse(Response response) {
		if(!isRobot())
		{
			GameServerHelper.sendResponse(session.getChannel(), response);
		}
	}

	public void initFriendsData(ArrayList<FriendsDataVO> friendsData_list) {
		this.list_friendsData = friendsData_list;
	}

	public void clear() {

	}

	public void setRobot(boolean b) {
		robot = b;
	}

	public boolean isRobot() {
		return robot;
	}

	public void setDetection(boolean b) {
		this.isDetection = b;
	}

	public boolean isDetection() {
		return isDetection;
	}

	public int getUserId() {
		return gameData.userId;
	}

	public String getNickName() {
		return gameData.nickName;
	}
	
	@Override
	public String toString()
	{
		return "Player [getUserId()=" + getUserId() + ", getNickName()=" + getNickName() + "]";
	}

	/**
	 * 更新玩家所有定时任务数据 <br>
	 * 玩家长时间离线，存档移出服务器内存时被调用
	 */
	public void updatePlayerData() {
		// TechnologyBiz.SaveTechnology(this);// 定時存儲技術信息
//		friendsService.updataUserFriendsInfo(this);// 定时更新玩家好友信息
		PlayerDaoImpl.updatePlayer(getCoins(), getExp(), getLevel(), getVictoryNum(), getFailureNum(), getCrytstal(), getModifyNickNameNum(), getRobPosNum(), getAddTimeNum(), getUserId());
	}

	public void clearMapIndex() {
		mapIndex = null;
	}

	public boolean isOnline() {
		if (gameData == null) {
			return false;
		}
		return UserCache.isOnline(getUserId());
	}

	public int getRoleId() {
		return gameData.getRoleId();
	}

	public void setLastLogoutTime(long lastLogoutTime) {
		gameData.lastLogoutTime = new Timestamp(lastLogoutTime);
	}

	public ArrayList<MailDataPO> getUserEmails() {
		return userEmails;
	}

	public void setUserEmails(ArrayList<MailDataPO> userEmails) {
		this.userEmails = userEmails;
	}

	public int getMailNoReadNum() {
		return mailNoReadNum;
	}

	public void setMailNoReadNum(int mailNoReadNum) {
		this.mailNoReadNum = mailNoReadNum;
	}

	public boolean isInWorld() {
		return !StringUtils.isEmpty(mapIndex);
	}

	public void addCoins(long winSocre)
	{
		if(winSocre == 0)
		{
			return;
		}
		gameData.coins += winSocre;
		setModify();
	}
	
	public boolean isModify()
	{
		return isModify;
	}
	
	private void setModify()
	{
		isModify = true;
	}

	public long getCoins()
	{
		return gameData.coins;
	}
	public long getBankCoins()
	{
		return gameData.bankCoins;
	}
	public int getVipLv()
	{
		return gameData.getVipLv();
	}
	public void setVipLv(int lv)
	{
		gameData.vipLv=lv;
	}
	
	public void putFriendsInfo(int userId,FriendInfoVO info)
	{
		infoList.put(userId, info);
	}
	public boolean haveFriendInfoId(int userId)
	{
		if (list_addFriendsData==null||list_addFriendsData.size()==0) {
			return false;
		}
		boolean have=false;
		for (AddFriendsVO addFriendsVO : list_addFriendsData) {
			if (addFriendsVO.id==userId)
			{
				have=true;
				break;
			}
		}
		return have;
	}
	
	/**
	 * 获取玩家的简单数据
	 * @return
	 */
	public PlayerSimpleData getSimpleData(boolean isManyPepolRoom)
	{
		long coins = isManyPepolRoom ? getManyPepolRoomCalcCoins() : getCoins();
		long betNum = getBetNum();
		if(betNum >0 && isManyPepolRoom)
		{
			betNum += 100000;
		}
		if(playerSimpleData == null)
		{
			playerSimpleData = new PlayerSimpleData(getUserId(), getNickName(), getHeadIconUrl(),coins,getClassicGamePos(),getManyGamePos(),getFruitGamePos(),
					gameData.getRoleId(),getVipLv(),gameData.playerLevel,gameData.crystals,gameData.sign,gameData.victoryNum,gameData.failureNum,betNum,getKillRoomPos());
		}
		playerSimpleData.setData(getNickName(), getHeadIconUrl(), coins,getClassicGamePos(),getManyGamePos(),getFruitGamePos(),gameData.getRoleId(),getVipLv(),gameData.playerLevel,
				gameData.crystals,gameData.sign,gameData.victoryNum,gameData.failureNum,betNum,isCheckPoker(),gameState,getKillRoomPos());
		return playerSimpleData;
	}
	
	public PlayerSimpleData getSimpleData()
	{
		return getSimpleData(false);
	}
	
	public void initRobotBanker(String nickName, int coins)
	{
		setRobot(true);
		gameData = new GameData();
		gameData.setNickName(nickName);
		gameData.coins = coins;
		gameData.setUserId(100000);
	}
	
	public void subCoinse(long bankerAllCoinse)
	{
		gameData.coins -= Math.abs(bankerAllCoinse);
		setModify();
	}
	
	public void subCrytstal(int buyGoldCost)
	{
		gameData.crystals -= Math.abs(buyGoldCost);
		setModify();
	}
	
	public void subBankCoinse(long bankerAllCoinse)
	{
		gameData.bankCoins -= Math.abs(bankerAllCoinse);
		setModify();
	}
	
	public void addBankCoinse(long bankerAllCoinse)
	{
		gameData.bankCoins += Math.abs(bankerAllCoinse);
		setModify();
	}
	
	public void clearModify()
	{
		isModify = false;
	}
	
	@Override
	public long getSortIndex()
	{
		return getCoins();
	}
	
	public void addOutPutCoin(long money)
	{
		gameData.monetTree+=Math.abs(money);
	}
	
	public void resOutPutCoin()
	{
		gameData.monetTree=0;
	}
	
	public int getTopUp()
	{
		return gameData.topUp;
	}
	
	public int getVipPayNum()
	{
		return gameData.vipPayNum;
	}
	
	public long getVipTime()
	{
		return gameData.vipTime;
	}
	
	public void addTopUp(int money)
	{
		if (money<0) 
		{
			return;
		}
		gameData.topUp+=money;
	}
	
	public void addVipPayNum(int money)
	{
		if (money<0) 
		{
			return;
		}
		gameData.vipPayNum+=money;
	}

	public void addExp(int exp)
	{
		gameData.exp += exp;
	}

	public int addAndGetExp(int exp)
	{
		gameData.exp += exp;
		setModify();
		return gameData.exp;
	}

	public void levelUP()
	{
		gameData.playerLevel++;
		setModify();
	}

	public int getExp()
	{
		return gameData.exp;
	}

	public void setExp(int playerExp)
	{
		if(playerExp == 0)
		{
			return;
		}
		gameData.exp = playerExp;
		setModify();
	}
	
	public void removeFriend(int userId)
	{
		if (list_friendsData==null) 
		{
			return;
		}
		FriendsDataVO remVO=null;
		for (FriendsDataVO vo : list_friendsData) 
		{
			if (vo.friendsData.id==userId) 
			{
				remVO=vo;
				break;
			}
		}
		if (remVO!=null) 
		{
			list_friendsData.remove(remVO);
			Tool.print_debug_level0("移除好友成功："+remVO);
		}
	}

	public String getPhoneNumber()
	{
 		return gameData.mobile_num;
	}

	public void initSign() {
		signTime=MyTimeUtil.getCurrTimeMM();
		ArrayList<Boolean> list=new ArrayList<Boolean>();
		for (int i = 0; i < 7; i++) 
		{
			list.add(false);
		}
		signList=list;
	}

	public void init(UserInfoModel userInfoModel)
	{
		gameData.mobile_num = userInfoModel.getRegPhoneNum();
	}

	public void setPhoneNumber(String phoneNumber)
	{
		gameData.mobile_num = phoneNumber;
	}

	public void setNickName(String nickName)
	{
		gameData.nickName = nickName;
	}

	public void setAccount(String account)
	{
		gameData.account = account;
	}

	public int getChangeNameCard()
	{
		return gameData.modifyNickName;
	}

	public void subChangeNameCard()
	{
		gameData.modifyNickName--;
	}

	public void removeAddFriendsEvent(int userId) 
	{
		if (list_addFriendsData==null) 
		{
			return;
		}
		ArrayList<AddFriendsVO> remove=new ArrayList<AddFriendsVO>();
		for (AddFriendsVO addFriendsVO : list_addFriendsData) 
		{
			if (addFriendsVO.id==userId)
			{
				remove.add(addFriendsVO);
			}
		}
		if (remove!=null) 
		{
			for (AddFriendsVO addFriendsVO : remove) 
			{
				list_addFriendsData.remove(addFriendsVO);
			}
		}
	}

	public void changeDie()
	{
		if(isClassicRoomDebug)Tool.print_debug_level0(getNickName()+",changeDie切换到Die");
		gameState = STATE_DIE;
	}

	public boolean isDie()
	{
		return gameState == STATE_DIE;
	}

	public void initState()
	{
		gameState = STATE_NORMAL;
		if(isClassicRoomDebug)Tool.print_debug_level0(getNickName()+",initState切换到NORMAL");
		setReday(false);
		isCheckPoker = false;
		
		list_handPokers.clear();
		clearComparePoker();
		clearRoundBet();
		clearRoundCalcBet();
	}
	
	/**
	 * 离开游戏时清理
	 * @param isTV 
	 */
	public void clearRoomData(boolean isTV)
	{
		Tool.print_debug_level0("clearRoomData，isTV："+isTV);
		if(!isTV)
		{
			if(isClassicRoomDebug)Tool.print_debug_level0(getNickName()+",clearRoomData切换到IDLE");
			gameState = STATE_IDLE;
			setReday(false);
			isCheckPoker = false;
			 
			list_handPokers.clear();
			clearComparePoker();
			clearRoundBet();
			clearRoundCalcBet();
			
			classicGame = null;
			insideRoom = null;
			clearPos();
		}
		manyPepolGame = null;
	}
	
	/**
	 * 站起时清理
	 */
	public void clearPlayer()
	{
		if(isClassicRoomDebug)Tool.print_debug_level0(getNickName()+",clearPlayer切换到IDLE");
		gameState = STATE_IDLE;
		setReday(false);
		isCheckPoker = false;
		 
		list_handPokers.clear();
		clearComparePoker();
		clearRoundBet();
		clearRoundCalcBet();
		
		clearPos();
	}
	
	public void changeChecked()
	{
		isCheckPoker = true;
	}

	public boolean isNormal()
	{
		return gameState == STATE_NORMAL;
	}

	public long getBetNum()
	{
		return roundBet;
	}

	public boolean isIdle()
	{
		return gameState == STATE_IDLE;
	}

//	public void setHeadIcon(byte[] headIcon)
//	{
//		gameData.headIcon = headIcon;
//	}

	public int getCrytstal()
	{
		return gameData.crystals;
	}

	public void addCrystals(int giftNum)
	{
		gameData.crystals += giftNum;
		setModify();
	}

	public void changeIdle()
	{
		if(isClassicRoomDebug)Tool.print_debug_level0(getNickName()+",changeIdle切换到idle");
		gameState = STATE_IDLE;
	}

	public void clearPoker()
	{
		list_handPokers.clear();
	}

	public void offlineClearAllRoom()
	{
		classicGame = null;
		manyPepolGame = null;
	}

	public long getManyPepolRoomCalcCoins()
	{
		return gameData.coins - roundBet - 100000;
	}

	public void setCoins(int num)
	{
		gameData.coins = num;
		setModify();
	}
	
	public int getVictoryNum()
	{
		return gameData.victoryNum;
	}
	
	public void addVictoryNum()
	{
		gameData.victoryNum++;
		setModify();
	}
	
	public void addFailureNum()
	{
		gameData.failureNum++;
		setModify();
	}
	
	public int getFailureNum()
	{
		return gameData.failureNum;
	}

	public int getModifyNickNameNum()
	{
		return gameData.modifyNickName;
	}
	
	public void addModifyNickName(int num)
	{
		gameData.modifyNickName += num;
		setModify();
	}

	public int getRobPosNum()
	{
		return gameData.robPos;
	}
	
	public void addRobPosNum(int num)
	{
		gameData.robPos += num;
		setModify();
	}

	public int getAddTimeNum()
	{
		return gameData.addTime;
	}

	public void addTimeNum(int num)
	{
		gameData.addTime += num;
		setModify();
	}

	public void close()
	{
		session.close();
	}

	public void addLotteryPayNum(int typeNum)
	{
		lotteryPayNum += typeNum;
	}
	
	public int getLotteryPayNum()
	{
		return lotteryPayNum;
	}
	
	public void clearLotteryPayNum()
	{
		lotteryPayNum = 0;
	}

	public void clearCheck()
	{
		isCheckPoker = false;
	}

	public void setHeadIconUrl(String url)
	{
		gameData.headIconUrl = url;
	}
	
	public String getHeadIconUrl()
	{
		return gameData.headIconUrl;
	}

	public void setVipPayNum(int num)
	{
		gameData.vipPayNum = 0;
	}

	public void setVipTime(long vipTime)
	{
		gameData.setVipTime(vipTime);
	}
}