package testdb;
import com.wt.db.SqlSimpleUtil;
import com.wt.naval.dao.model.antiaddiction.UserAntiAddictionModel;

import junit.framework.TestCase;

public class TestDB extends TestCase
{
	public void testDB()
	{
		String GET_USERANTIADDICTIONMODEL = "SELECT * FROM `user_antiAddiction` WHERE `userId` = ?";
		UserAntiAddictionModel addictionModel = SqlSimpleUtil.selectBean(GET_USERANTIADDICTIONMODEL,UserAntiAddictionModel.class, 1);
		System.out.println("addictionModel:"+addictionModel);
	}
}
