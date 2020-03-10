package com.wt.db;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import com.wt.job.further.IFurtherJob;
import com.wt.job.further.MyFurtherJob;
import com.wt.util.log.LogUtil;
import com.wt.util.server.DBUtil;

/**
 * 数据库基础封装
 * @author WangTuo
 */
@SuppressWarnings("unchecked")
public class SqlSimpleUtil
{
	/**
	 * 查询字段，返回多行,可传参
	 * 
	 * @param sql
	 * @param params
	 * @return List(Map(K,V))
	 */
	public static <T> List<T> selectList(String sql, Object... params)
	{
		Connection conn = DBUtil.openConnection();
		ArrayList<T> results = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			results = (ArrayList<T>) qr.query(conn, sql, new MapListHandler(), params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return results;
	}

	/**
	 * 查询字段，只返回一行,可传参
	 * 
	 * @param sql
	 *                查询语句
	 * @param params
	 *                参数
	 * @return map
	 */
	public static <T> T selectObject(String sql, Object... params)
	{
		Connection conn = DBUtil.openConnection();
		T results = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			ResultSetHandler<T> rsh = (ResultSetHandler<T>) new MapHandler();
			results = qr.query(conn, sql, rsh, params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return results;
	}
	
	public static <T> T selectSingleObject(String sql,Object... params)
	{
		Connection conn = DBUtil.openConnection();
		T results = null;

		QueryRunner qr = new QueryRunner();
		ResultSetHandler<T> rsh = (ResultSetHandler<T>)  new ScalarHandler<T>();
		try
		{
			results = qr.query(conn, sql, rsh, params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return results;
	}
	
	/**
	 * 根据提供的javabean查询一行字段并反射到该bean,可传参
	 * 
	 * @param sql
	 *                查询语句
	 * @param clazz
	 *                查询的javaben类型
	 * @param params
	 *                参数
	 * @return
	 */
	public static <T> T selectBean(String sql, Class<T> clazz, Object... params)
	{
		Connection conn = DBUtil.openConnection();
		T results = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			if (clazz == null)
			{
				results = (T) qr.query(conn, sql, new MapHandler());
			}
			else
			{
				ResultSetHandler<T> resultSetHandler = new BeanHandler<T>(clazz);
				results = qr.query(conn, sql, resultSetHandler, params);
			}
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
			return null;
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return results;
	}
	
	
	public static <T> IFurtherJob selectBeanFurther(String sql, Class<T> clazz, Object... params)
	{
		IFurtherJob further = new MyFurtherJob()
		{
			public void execute()
			{
				T t = selectBean(sql, clazz, params);
				operationComplete(t);
			}
		};
		return further;
	}

	/**
	 * 根据提供的javaben查询多行字段到bean List ,可传参
	 * 
	 * @param sql
	 * @param clazz
	 *                查询的javaben类型
	 * @param params
	 *                参数
	 * @return
	 */
	public static <T> List<T> selectBeanList(String sql, Class<T> clazz, Object... params)
	{
		Connection conn = DBUtil.openConnection();
		List<T> results = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			if (clazz == null)
			{
				results = (ArrayList<T>) qr.query(conn, sql, new MapHandler());
			}
			else
			{
				ResultSetHandler<List<T>> resultSetHandler = new BeanListHandler<T>(clazz);
				results = (List<T>) qr.query(conn, sql, resultSetHandler, params);
			}
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
			return null;
		}
		finally
		{
 			DBUtil.closeConnection(conn);
		}
		return results;
	}

	/**
	 * 插入数据并返回自增主键
	 * @param sql
	 * @param params
	 * @return 返回自增主键值。如没有，插入成功返回0，否则返回null
	 */
	public static Integer insert(String sql, Object... params)
	{
		Connection conn = DBUtil.openConnection();
		Long results = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			results = qr.insert(conn, sql,new ScalarHandler<Long>(), params);
			 if(results == null)
			 {
				return 0;
			 }
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
			return null;
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
 		return results.intValue();
	}
	
	/**
	 * 批量插入一组数据
	 * @param sql 插入语句
	 * @param params 需要插入的记录二位数组
	 * @return 返回插入的第一条数据主键
	 */
	public static Long insertBatch(String sql,Object[][] params)
	{
		Connection conn = DBUtil.openConnection();
		Integer results = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			results = qr.insertBatch(conn, sql,new ScalarHandler<Integer>(), params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
			return null;
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		if(results == null)
		{
			return null;
		}
	
 		return results.longValue();
	}
	
	/**
	 * 批量插入一组数据
	 * @param sql 插入语句
	 * @param params 需要插入的记录二位数组
	 * @return 是否成功
	 */
	@SuppressWarnings("rawtypes") 
	public static boolean insertBatchNotIdentity(String sql,Object[][] params)
	{
		Connection conn = DBUtil.openConnection();
		boolean results = false;
		try
		{
			QueryRunner qr = new QueryRunner();
			qr.insertBatch(conn, sql,new ScalarHandler(), params);
			results = true;
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
			e.printStackTrace();
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
 		return results;
	}
	
	
	/**
	 * 更新或删除一条记录
	 * @param sql
	 * @param params
	 * @return 返回影响的记录条数
	 */
	public static int update(String sql,Object... params)
	{
		Connection conn =DBUtil.openConnection();
		int result = 0;
		try
		{
			QueryRunner qr = new QueryRunner();
			result = qr.update(conn, sql, params);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			LogUtil.print_dbError(e);
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 处理事务update，不主动提交
	 * 配合事务使用
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int transactionUpdate(String sql,Object... params)
	{
		Connection conn =DBUtil.openConnection();
		int result = 0;
		QueryRunner qr = new QueryRunner();
		try
		{
			result = qr.update(conn, sql, params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		return result;
	}
	
	public static int[] transactionUpdateBatch(String sql,Object[][] params)
	{
		Connection conn =DBUtil.openConnection();
		int[] result = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			result = qr.batch(conn, sql, params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		return result;
	}
	
	/***
	 * 批量更新或删除
	 * @param sql
	 * @param params
	 * @return 返回影响的记录条数array
	 */
	public static int[] updateBatch(String sql,Object[][] params)
	{
		Connection conn =DBUtil.openConnection();
		int[] result = null;
		try
		{
			QueryRunner qr = new QueryRunner();
			result = qr.batch(conn, sql, params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 获取包含指定字段值的数据条数
	 * @param sql 语句
	 * @param params 参数
	 * @return 数据条数
	 */
	public static long selectCount(String sql,Object... params)
	{
		Connection conn =DBUtil.openConnection();
		long result = 0;
		try
		{
			QueryRunner qr = new QueryRunner();
			ResultSetHandler<Long> resultSetHandler = new ScalarHandler<Long>();
			result = qr.query(conn,sql, resultSetHandler,params);
		}
		catch (SQLException e)
		{
			LogUtil.print_dbError(e);
		}
		finally
		{
			DBUtil.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 开启事务
	 * @throws SQLException
	 */
	public static void startTransaction() throws SQLException
	{
		DBUtil.openConnection().setAutoCommit(false);
	}

	/**
	 * 事务提交且释放连接
	 */
	public static void commitAndClose() {
		Connection conn = null;
		try {
			conn = DBUtil.openConnection();
			// 事务提交
			conn.commit();
			// 关闭资源
			conn.close();
			// 解除绑定
			DBUtil.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
	/**
	 * 事务回滚且释放资源
	 */
	public static void rollbackAndClose() {
		Connection conn = null;
		try {
			conn = DBUtil.openConnection();
			// 事务回滚
			conn.rollback();
			// 关闭资源
			conn.close();
			// 解除版定
			DBUtil.closeConnection(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
