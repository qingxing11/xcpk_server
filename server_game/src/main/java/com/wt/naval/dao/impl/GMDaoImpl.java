package com.wt.naval.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.wt.util.server.DBUtil;

public class GMDaoImpl
{
	private static final String USER_IS_ADMIN = "SELECT COUNT(`phone_number`) FROM `gm_phone` WHERE `phone_number`=?";
	
	public static boolean isAdminPhoneNumber(String phone)
	{
		int count = 0;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = DBUtil.openConnection().prepareStatement(USER_IS_ADMIN);
			pst.setString(1, phone);
			rs = pst.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
				System.out.println("count:"+count);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DBUtil.closeConnection(rs, pst);
		}

		if (count > 0) {
			return true;
		}
		return false;
	}
}
