package com.wt.naval.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.wt.db.SqlSimpleUtil;
import com.wt.util.Tool;
import com.wt.util.server.DBUtil;

import model.SafeboxRecordModel;

public class SafeBoxDaoImpl
{
	public static void main(String[] args)
	{
		getSafeBoxRecord(32229);
	}
	
	private static final String UPDATE_SafeBox = "UPDATE `user_data` SET `coins`=`coins`-?,`bankCoins`=`bankCoins`+? WHERE `userId` = ?";

	/** 存入保险箱 */
	public static boolean updataUserSafeBox(int userId, long sub)
	{
		return SqlSimpleUtil.update(UPDATE_SafeBox, Math.abs(sub), Math.abs(sub), userId) > 0;
	}

	private static final String UPDATE_SafeBox_Takeout = "UPDATE `user_data` SET `coins`=`coins`+?,`bankCoins`=`bankCoins`-? WHERE `userId` = ?";
	/** 取出保险箱 */
	public static boolean updataUserSafeBoxTakeOut(int userId, long sub)
	{
		return SqlSimpleUtil.update(UPDATE_SafeBox_Takeout, Math.abs(sub), Math.abs(sub), userId) > 0;
	}

	private static final String Update_SafeBox_Change = "UPDATE `user_data` SET `coins`=`coins`+? WHERE `userId` = ?";
	private static final String Update_SafeBox_Change2 = "UPDATE `user_data` SET `coins`=`coins`-? WHERE `userId` = ?";

	/** 游戏币转账 */
	public static boolean updataUserChangeSafeBox(int userId, long coins, int otherId, long otherCoins)
	{
		boolean isSuccess = false;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
			conn = DBUtil.openConnection();
			conn.setAutoCommit(false);

			pst = conn.prepareStatement(Update_SafeBox_Change2);
			int index = 0;
			pst.setLong(++index, coins);
			pst.setInt(++index, userId);
			pst.executeUpdate();
			pst.close();

			pst = conn.prepareStatement(Update_SafeBox_Change);
			index = 0;
			pst.setLong(++index, otherCoins);
			pst.setInt(++index, otherId);
			pst.executeUpdate();
			pst.close();
	
			isSuccess = true;
		}
		catch (SQLException e)
		{
			isSuccess = false;
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
				Tool.print_dbError("游戏币转账失败，回滚失败", e1);
			}
			e.printStackTrace();
			Tool.print_dbError("游戏币转账环节出错", e);
		}
		finally
		{
			try
			{
				conn.setAutoCommit(true);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			DBUtil.closeConnection(rs, pst);
		}
		return isSuccess;
	}

	private static final String Inset_SafeBox_Record = "INSERT INTO `safeBox`(`userId`,`time`,`type`,`money`,`otherId`,`sign`,`pre`) VALUES (?,?,?,?,?,?,?)";

	/** 转账记录 */
	public static int insetSafeBoxRecord(int userId, Timestamp time, int type, long money, int otherId, boolean sign, float pre)
	{
		return SqlSimpleUtil.insert(Inset_SafeBox_Record, userId, time, type, money, otherId, sign, pre);
	}

	private static final String UPDATE_SafeBox_sign = "UPDATE `safeBox` SET `sign`=? WHERE `key` = ?";

	/** 转账记录状态 */
	public static int updataUserSafeBoxSign(int key, boolean sign)
	{
		return SqlSimpleUtil.update(UPDATE_SafeBox_sign, sign, key);
	}

	private static final String Select_SafeBox_Record = "SELECT * FROM `safeBox` WHERE `userId`=? OR otherId = ? order by time desc limit 20";

	/** 获取转账记录 */
	public static List<SafeboxRecordModel> getSafeBoxRecord(int userId)
	{
		return SqlSimpleUtil.selectBeanList(Select_SafeBox_Record, SafeboxRecordModel.class, userId,userId);

	}

	private static final String Select_SafeBox_Record_Other = "SELECT * FROM `safeBox` WHERE `otherId`=? order by time desc limit 20";

	/** 推送转账记录 */
	public static List<SafeboxRecordModel> getSafeBoxRecordOtherId(int otherId)
	{
		return SqlSimpleUtil.selectBeanList(Select_SafeBox_Record_Other, SafeboxRecordModel.class, otherId);

	}
}
