package com.wt.archive;

import java.io.Serializable;
import java.sql.Timestamp;

import com.wt.annotation.note.FieldNote;
import com.wt.naval.dao.model.user.GameDataModel;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.util.MyTimeUtil;

public class GameData implements Serializable {
	public static boolean isDebug = true;
	private static final long serialVersionUID = -776491017019478358L;

	/**
	 * “transient”修饰符描述的字段不会被序列化
	 */
//	public transient UserData userData;


	/********************************
	 * 玩家游戏部分
	 ********************************************/
	@FieldNote(info = "昵称")
	public String nickName = "";// 昵称

	@FieldNote(info = "是否可用新手礼包")
	public int first_pay;

	// 月卡数据
	@FieldNote(info = "剩余天数")
	public int vip_day;// 剩余天数

	@FieldNote(info = "头像id(非微信)")
	public int headid;

	@FieldNote(info = "钻石")
	public int crystals;

	@FieldNote(info = "金币")
	public long coins;// 金币数

	/**
	 * 玩家领土权经验值
	 */
	public int exp;

	@FieldNote(info = "本月签到记录")
	public String checkin_status = "";// 本月签到记录

	// 签到
	public int sign_num_days;
	public String last_sign_date;

	@FieldNote(info = "已领取的服务器全局邮件")
	public String serverMail = "";
	public int win_num;// 胜利次数
	public int play_game_num;// 总游戏局数
 
	private int roleId;// 选择的性别


	public int userId;// 玩家的id

	/**
	 * 玩家等级
	 */
	public int playerLevel;
	
	public int vipLv;//玩家vip等级
	
	 public String mobile_num;
	/**
	 * 银行中金币
	 */
	public long bankCoins;
	
	public int treeLv;
	 
	/**
	 * 个性签名
	 */
	public String sign;
	
	/**
	 * 改名
	 */
	public int modifyNickName;
	
	/**
	 * 抢座
	 */
	public int robPos;
	
	/**
	 * 增时间
	 */
	public int addTime;
	
	/**
	 * 胜场
	 */
	public int victoryNum;
	
	/**
	 * 负场
	 */
	public int failureNum;
 
	public String account;
	
	/**充值金额*/
	public int topUp;
	/**
	 * 领取奖励倍数
	 */
	public int luckyNum;
	
	/**本月是否抽奖*/
	public  boolean isLucky;
	
	/**玩家头像*/
	public String headIconUrl;
	/**
	 * 账号是否已完善
	 */
	public boolean accountSupplementary;
	
	private  long startOutPutTime;
	
	public transient long vipTime;
	public transient int vipPayNum;
	
	/**产出金币*/
	public transient long monetTree;
	private transient long lastOutPutTime;
	
	public transient Timestamp last_login_time;
	public transient Timestamp reg_time;
	public transient Timestamp lastLogoutTime;
	
	private transient int curMonthAddTreeLv;


	public GameData() {
	}

	public void setVipTime(long vipTime)
	{
		this.vipTime= vipTime;
	}
	
	/**
	 * 登陆时user_data表的数据初始化
	 * 
	 * @param GameDataModel
	 *            读取玩家user_data表的数据类
	 */
	public GameData(GameDataModel gameDataModel) {
//		userData = new UserData();
//		userData.userId = gameDataModel.getUserId();
		this.userId = gameDataModel.getUserId();	
		this.account = gameDataModel.getAccount();
		// userData.autoAddId = gameDataModel.getAddAutoId();
		this.bankCoins = gameDataModel.getBankCoins();
		this.coins = gameDataModel.getCoins();
		this.crystals = gameDataModel.getCrystals();
		this.exp=gameDataModel.getExp();
		this.failureNum=gameDataModel.getFailureNum();
		this.nickName = gameDataModel.getNickName();
		this.playerLevel=gameDataModel.getPlayerLevel();
		this.roleId=gameDataModel.getRoleId();
		this.sign=gameDataModel.getSign();
		this.modifyNickName=gameDataModel.getModifyNickName();
		this.robPos=gameDataModel.getRobPos();
		this.addTime=gameDataModel.getAddTime();
		this.treeLv=gameDataModel.getTreeLv();
		this.victoryNum=gameDataModel.getVictoryNum();
		this.vipLv=gameDataModel.getVipLv();
		this.topUp=gameDataModel.getTopUp();
		this.luckyNum=gameDataModel.getLuckyNum();
		this.isLucky=MyTimeUtil.isSameMonth(gameDataModel.getLuckyTime(), MyTimeUtil.getCurrTimeMM());
		this.last_login_time = gameDataModel.getLastLoginTime();
		this.setRoleId(gameDataModel.getRoleId());
		this.lastOutPutTime=gameDataModel.getLastOutPutTime();
		this.startOutPutTime=gameDataModel.getStartOutPutTime();
		this.monetTree=gameDataModel.getMonetTree();	
		this.headIconUrl=gameDataModel.getHeadIconUrl();
		this.accountSupplementary = gameDataModel.isAccountSupplementary();
		this.curMonthAddTreeLv=gameDataModel.getCurMonthAddTreeLv();
		
		Timestamp vipTime = gameDataModel.getVipTime();
		this.vipTime = vipTime == null ? 0 : gameDataModel.getVipTime().getTime();
		this.vipPayNum = gameDataModel.getVipPayNum();
	}

	/**
	 * 注册时初始化
	 * 
	 * @param userInfoModel
	 */
	public void initGuest(UserInfoModel userInfoModel) {
		this.userId = userInfoModel.getUserId();
		this.setNickName(userInfoModel.getNickName());
		this.reg_time = userInfoModel.getRegTime();
		this.mobile_num = userInfoModel.getRegPhoneNum();
		this.last_login_time = userInfoModel.getLastLoginTime();
		// userData.autoAddId = 0;// 自增Id；初始化为0

		this.account = userInfoModel.getAccount();
		this.nickName = userInfoModel.getNickName();
		
		this.coins = 5000;
		this.crystals = 0;
		// this.crystals = 200;
		long curTime=MyTimeUtil.getCurrTimeMM();
		this.lastOutPutTime=curTime;
		this.startOutPutTime=curTime;
		this.treeLv=0;
		this.topUp=0;
		this.playerLevel = 1;
		this.roleId=0;
		this.vipLv = 0;
		
		modifyNickName = 2;
		robPos = 2;
		addTime = 2;
		
		this.curMonthAddTreeLv=0;
	}
	
	
	public int getCurMonthAddTreeLv() {
		return curMonthAddTreeLv;
	}

	public void resCurMonthAddTreeLv() {
		this.curMonthAddTreeLv = 0;
	}
	
	public void setCurMonthAddTreeLv(int curMonthAddTreeLv) {
		this.curMonthAddTreeLv = curMonthAddTreeLv;
	}
	
	public void AddCurMonthAddTreeLv(int curMonthAddTreeLv) {
		this.curMonthAddTreeLv +=Math.abs(curMonthAddTreeLv);
	}
	
	public int getTreeLv() {
		return treeLv;
	}

	public void setTreeLv(int treeLv) {
		this.treeLv = treeLv;
	}
	
	public long getMonetTree() {
		return monetTree;
	}

	public long getLastOutPutTime() {
		return lastOutPutTime;
	}

	public void setLastOutPutTime(long lastOutPutTime) {
		this.lastOutPutTime = lastOutPutTime;
	}

	public long getStartOutPutTime() {
		return startOutPutTime;
	}

	public void setStartOutPutTime(long startOutPutTime) {
		this.startOutPutTime = startOutPutTime;
	}

	// 手动调用
	public void init() {
	}

	public int getVipLv()
	{
		return vipLv;
	}
	
	
	
	/**
	 * 服务器端调用
	 * 
	 * @param bean
	 * @param gameDef
	 * @param clientTime
	 *            客户端当前时间
	 */
	public void initGameData(GameData bean, long clientTime) {
	}

	// 此部分不保存在存档中
	private transient String publicKey = "";

	public transient long timeDeviation;

	// 设置key
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	// 获取key
	public String getPublicKey() {
		return publicKey;
	}


	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "GameData [nickName=" + nickName + ", first_pay=" + first_pay + ", vip_day=" + vip_day + ", headid=" + headid + ", crystals=" + crystals + ", coins=" + coins + ", exp=" + exp + ", checkin_status=" + checkin_status + ", sign_num_days=" + sign_num_days + ", last_sign_date=" + last_sign_date + ", serverMail=" + serverMail + ", win_num=" + win_num + ", play_game_num=" + play_game_num +  ", roleId=" + roleId + ", userId=" + userId + ", playerLevel=" + playerLevel + ", vipLv=" + vipLv + ", mobile_num=" + mobile_num + ", bankCoins=" + bankCoins + ", treeLv=" + treeLv + ", sign=" + sign + ", modifyNickName=" + modifyNickName + ", robPos=" + robPos + ", addTime=" + addTime + ", victoryNum=" + victoryNum + ", failureNum=" + failureNum
				+ ", account=" + account + ", topUp=" + topUp + ", luckyNum=" + luckyNum + ", isLucky=" + isLucky + ", headIconUrl=" + headIconUrl + ", accountSupplementary=" + accountSupplementary + "]";
	}
	
	
}
