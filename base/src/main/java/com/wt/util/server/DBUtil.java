package com.wt.util.server;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.wt.util.Tool;

public class DBUtil
{
	private static DataSource dataSource;

	private static ThreadLocal<Connection> connLocal = new ThreadLocal<Connection>();

	static
	{
		dataSource = new ComboPooledDataSource("config/c3p0.properties");
	}

	public static Connection openConnection()
	{
		Connection conn = null;
		try
		{
			conn = connLocal.get();

			if (conn == null || conn.isClosed())
			{
				conn = dataSource.getConnection();
				connLocal.set(conn);
			}
		}
		catch (Exception e)
		{
			Tool.print_dbError(e.getMessage(), e);
		}
		
		return conn;
	}

	public static void closeConnection(ResultSet rs, PreparedStatement pst)
	{
		Connection conn = connLocal.get();
		try
		{
			if (rs != null)
			{
				rs.close();
			}
			if (pst != null)
			{
				pst.close();
			}
			if (conn != null && !conn.isClosed())
			{
				conn.close();
				connLocal.remove();
			}
		}
		catch (SQLException e)
		{
			Tool.print_dbError(e.getMessage(), e);
		}
	}

	/**
	 * dbutil用，Connection已自动关闭
	 * @param connection
	 */
	public static void closeConnection(Connection connection)
	{
		try
		{
			connection.close();
			Connection conn = connLocal.get();
			
			if (conn != null)
			{
				connLocal.remove();
			}
		}
		catch (SQLException e)
		{
			Tool.print_dbError(e.getMessage(), e);
		}
	}

	public static <T> T resultSetObject(Class<T> clazz, ResultSet rs) throws SQLException, InstantiationException, IllegalAccessException
	{
		T obj = clazz.newInstance();

		while (rs.next())
		{
			for (int i = 0 ; i < rs.getMetaData().getColumnCount() ; i++)// 读出数据库中的所有变量，按照数据库中的变量分配给本地def类
			{
				try
				{
					Field field = obj.getClass().getField(rs.getMetaData().getColumnName(i + 1));
					Object object = rs.getObject(field.getName());
					field.set(obj, object);
				}
				catch (Exception e)
				{
					Tool.print_dbError(e.getMessage(), e);
				}
			}
		}
		return obj;
	}
}
