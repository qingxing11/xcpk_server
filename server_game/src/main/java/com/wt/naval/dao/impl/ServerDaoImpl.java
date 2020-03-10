package com.wt.naval.dao.impl;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.wt.archive.GMMailData;
import com.wt.naval.cache.ServerCache;
import com.wt.util.server.DBUtil;

public class ServerDaoImpl
{
	private static final String GET_SERVER_MAILS = "SELECT * FROM `server_mail` where status = ?";

	public static ArrayList<GMMailData> getServerMails()
	{
		int maxIndex = 0;
		ArrayList<GMMailData> arrayList_mails = new ArrayList<>();
		PreparedStatement pst = null;
		ResultSet rs = null;

		try
		{
			pst = DBUtil.openConnection().prepareStatement(GET_SERVER_MAILS);
			pst.setInt(1, GMMailData.status_有效);
			rs = pst.executeQuery();

			while (rs.next())
			{
				GMMailData mailData = new GMMailData();
				for (int i = 0 ; i < rs.getMetaData().getColumnCount() ; i++)
				{

					try
					{
						String name = rs.getMetaData().getColumnName(i + 1);
						Field field = mailData.getClass().getField(name);
						Object obj = null;

						switch (name)
						{
							case "condType":
							case "type":
							case "attachmentType":
							case "status":
								obj = rs.getByte(name);
								break;
							case "time":
								obj = rs.getTimestamp(name).getTime() / 1000;
								break;
							default:
								obj = rs.getObject(name);

						}

						field.set(mailData, obj);

					}
					catch (NoSuchFieldException | IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}

				arrayList_mails.add(mailData);
				maxIndex = mailData.mailId > maxIndex ? mailData.mailId : maxIndex;

			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally {
			DBUtil.closeConnection(rs, pst);
		}
		ServerCache.index_serverMail = ++maxIndex;
		return arrayList_mails;
	}
}
