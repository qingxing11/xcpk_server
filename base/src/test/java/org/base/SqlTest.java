package org.base;

import com.wt.db.SqlSimpleUtil;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class SqlTest extends TestCase
{
//	public void testApp()
//	{
//		String batchInsert = "INSERT INTO `user_rank` (`userId`,`killNum`,`resourcesNum`) VALUES (?,?,?)";
//		
//		Object[][] params = new Object[][] {
//			{9999,100,100000},
//			{9998,99,100000},
//			{9997,98,101000},
//			{9996,50,100020},
//		};
//		boolean code = SqlSimpleUtil.insertBatchNotIdentity(batchInsert, params);
//		System.out.println("code:"+code);
//		assertNotNull(code);
//	}
	
	public void testSql()
	{
		String sql = "SELECT `id` FROM `user_email` WHERE `emailId` = ?";
		int id = SqlSimpleUtil.selectSingleObject(sql, 739);
		System.out.println("id:"+id);
	}
}
