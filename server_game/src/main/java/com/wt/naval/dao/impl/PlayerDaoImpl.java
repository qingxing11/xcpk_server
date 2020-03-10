package com.wt.naval.dao.impl;

import com.wt.db.SqlSimpleUtil;
import com.wt.job.IJob;
import com.wt.job.MyJobTask;

public class PlayerDaoImpl
{
	public static void main(String[] args)
	{
		System.out.println(deleteBanId(1));
	}

	
	private static final String ADD_USER_COINS = "UPDATE `user_data` SET `coins` = coins + ? WHERE `userId` = ?";
	public static void addPlayerCoins(long winSocre,int userId)
	{
		MyJobTask.addTask(new IJob()
		{
			@Override
			public void execute()
			{
				SqlSimpleUtil.update(ADD_USER_COINS, winSocre, userId);
			}
		});
	}
	
	public static void updatePlayerCoins(long winSocre,int userId)
	{
		MyJobTask.addTask(new IJob()
		{
			@Override
			public void execute()
			{
				SqlSimpleUtil.update(UPDATE_USER_COINS, winSocre, userId);
			}
		});
	}
	
	private static final String SUB_USER_COINS = "UPDATE `user_data` SET `coins` = coins - ? WHERE `userId` = ?";
	public static void subPlayerCoins(long subSocre,int userId)
	{
		MyJobTask.addTask(new IJob()
		{
			@Override
			public void execute()
			{
				SqlSimpleUtil.update(SUB_USER_COINS, subSocre, userId);
			}
		});
	}
	
	private static final String ADD_PLAYER_CRYTSTAL = "UPDATE `user_data` SET `crystals` = crystals + ? WHERE `userId` = ?";
	public static void addPlayerCrytstal(int giftNum,int userId)
	{
		SqlSimpleUtil.update(ADD_PLAYER_CRYTSTAL, giftNum, userId);
	}

	private static final String UPDATE_USER_COINS = "UPDATE `user_data` SET `coins` = ? WHERE `userId` = ?";
	public static void updatePlayersCoins(Object[][] params)
	{
		SqlSimpleUtil.updateBatch(UPDATE_USER_COINS, params);
	}
	
	
	private static final String UPDATE_USETSING="UPDATE `user_data` SET `sign` = ? WHERE `userId` = ?";
	public static int updateSign(int userId,String sign)
	{
		return SqlSimpleUtil.update(UPDATE_USETSING, sign, userId);
	}
	
	private static final String UPDATE_LUCKY="UPDATE `user_data` SET `luckyNum` = ?,`luckyTime`=? WHERE `userId` = ?";
	public static int updateLucky(float luckyNum,long luckyTime,int userId)
	{
		return SqlSimpleUtil.update(UPDATE_LUCKY, luckyNum, luckyTime,userId);
	}
	
	private static final String SELECT_LUCKY="SELECT `luckyNum` FROM `user_data` WHERE `userId` = ?";
	
	public static int selectLucky(int userId)
	{
		return SqlSimpleUtil.selectSingleObject(SELECT_LUCKY , userId);
	}

	private static final String UPDATE_EXP="UPDATE `user_data` SET `exp` = exp + ? WHERE `userId` = ?";
	public static void addExp(int userId, int exp)
	{
		SqlSimpleUtil.update(UPDATE_EXP, exp,userId);
	}

	private static final String UPDATE_PLAYER ="UPDATE `user_data` SET `coins` = ?,`exp` = ?,`playerLevel` = ?,`victoryNum` = ?,`failureNum` = ? ,`crystals` = ?,`modifyNickName` = ?,`robPos` = ?,`addTime` = ? WHERE `userId` = ?";
	public static void updatePlayer(Object[][] params)//coins,exp,level,victoryNum,failureNum,crystals,modifyNickName,robPos,addTime
	{
		SqlSimpleUtil.updateBatch(UPDATE_PLAYER, params);
	}
	
	public static void updatePlayer(long coins,int exp,int level,int vectorNum,int faiureNum,int crystals,int modifyNickName,int robPos,int addTime,int userId)//coins,exp,level,victoryNum,failureNum,crystals,modifyNickName,robPos,addTime
	{
		SqlSimpleUtil.update(UPDATE_PLAYER, coins,exp,level,vectorNum,faiureNum,crystals,modifyNickName,robPos,addTime,userId);
	}

	private static final String UPDATE_PLAYER_ACCOUNTSUPPLEMENTARY ="UPDATE `user_data` SET `accountSupplementary` = 1,`account` = ?,`nickName` = ? WHERE `userId` = ?";
	public static void updatePlayerSupplementaryAccount(String account,String nickName,int userId)
	{
		SqlSimpleUtil.update(UPDATE_PLAYER_ACCOUNTSUPPLEMENTARY,account, nickName,userId);
	}
	
	private static final String UDPATE_USERNICKNAME= "UPDATE `user_data` SET`nickName`=?,`modifyNickName` = `modifyNickName` - 1 WHERE `userId` = ?";
	public static int updateNicknameByChangeNameCard(int userId,String nickName)
	{
		return SqlSimpleUtil.update(UDPATE_USERNICKNAME, nickName, userId);
	}
	
	private static final String UDPATE_USERSUBCARD= "UPDATE `user_data` SET `roleId` = ? WHERE `userId` = ?";
	public static int updateGender(int userId,int gender)
	{
		return SqlSimpleUtil.update(UDPATE_USERSUBCARD,gender,userId);
	}

	private static final String UDPATE_USER_HEADIMG= "UPDATE `user_data` SET `headIcon` = ? WHERE `userId` = ?";
	public static int updateHeadImage(int userId, byte[] headIcon)
	{
		return SqlSimpleUtil.update(UDPATE_USER_HEADIMG,headIcon,userId);
	}
	
	private static final String UDPATE_USER_HEADIMGURL= "UPDATE `user_data` SET `headIconUrl` = ? WHERE `userId` = ?";
	public static int updateHeadImageUrl(int userId, String url)
	{
		return SqlSimpleUtil.update(UDPATE_USER_HEADIMGURL,url,userId);
	}

	private static final String UDPATE_USER_LUCKYNum= "UPDATE `user_data` SET `luckyNum` = ? WHERE `userId` = ?";
	public static int updatePlayerLuckyNum(int num,int userId)
	{
		return SqlSimpleUtil.update(UDPATE_USER_LUCKYNum,num,userId);
	}
	
	private static final String ADD_BANID = "INSERT INTO `user_banned`(`bannedId`) VALUES(?)";
	public static boolean banId(int userId)
	{
		return SqlSimpleUtil.insert(ADD_BANID, userId) != null;
	}
	
	private static final String DELETE_BANID = "DELETE FROM `user_banned` WHERE `bannedId` = ?";
	public static boolean deleteBanId(int userId)
	{
		return SqlSimpleUtil.update(DELETE_BANID, userId) > 0;
	}
	
	private static final String GET_BANNED = "SELECT * FROM user_banned WHERE bannedId = ?";
	public static boolean isBanned(int userId)
	{
		return SqlSimpleUtil.selectObject(GET_BANNED, userId) != null;
	}

	private static final String UPDATE_PLAYER_IP = "UPDATE user_data SET ip = ? WHERE userId = ?";
	public static int updatePlayerIP(int userId, String clientIP)
	{
		return SqlSimpleUtil.update(UPDATE_PLAYER_IP,clientIP,userId);
	}
}
