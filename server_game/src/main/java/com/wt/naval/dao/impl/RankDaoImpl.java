package com.wt.naval.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wt.db.SqlSimpleUtil;
import com.wt.util.Tool;
import com.wt.xcpk.vo.RankLogVO;
import com.wt.xcpk.vo.RankVO;

public class RankDaoImpl
{
	private static final String GET_COINS_RANK = "SELECT coins,userId,nickName,playerLevel FROM user_data ORDER BY coins DESC LIMIT 0, 20";
	public static ArrayList<RankVO> getCoinsRank()
	{
		ArrayList<RankVO> list_value = new ArrayList<RankVO>();
		List<HashMap<String,Object>> list =  SqlSimpleUtil.selectList(GET_COINS_RANK);
		int index = 0;
		for (HashMap<String, Object> hashMap : list)
		{
			long coins = (long) hashMap.get("coins");
			int userId = (int) hashMap.get("userId");
			String nickName = (String) hashMap.get("nickName");
			int level = (int)hashMap.get("playerLevel");
			RankVO rankVO = new RankVO(coins,userId,nickName,index,level);
			index++;
			
			list_value.add(rankVO);
		}
		return list_value;
	}
	
//		private static final String DELETE_RANKING_BIGWIN = "DELETE FROM rank_bigwin_copy";
//		private static final String UPDATE_BIGWIN = "INSERT INTO rank_bigwin_copy (`userId`,`nickName`,`score`,`rank`,`level`) VALUES (?,?,?,?,?)";
	private static final String DELETE_RANKING_BIGWIN = "DELETE FROM rank_bigwin";
	private static final String UPDATE_BIGWIN = "INSERT INTO rank_bigwin (`userId`,`nickName`,`score`,`rank`,`level`) VALUES (?,?,?,?,?)";
	
	public static void updateBigWin(Object[][] ranking)
	{
		try
		{
			SqlSimpleUtil.startTransaction();
			SqlSimpleUtil.transactionUpdate(DELETE_RANKING_BIGWIN);
			SqlSimpleUtil.transactionUpdateBatch(UPDATE_BIGWIN,ranking);
			
 			SqlSimpleUtil.commitAndClose();
		}
		catch (SQLException e)
		{
			Tool.print_dbError("updateBigWin错误", e);
		}
	}
	
//	private static final String GET_BIGWIN = "SELECT * FROM rank_bigwin_copy";
	private static final String GET_BIGWIN = "SELECT * FROM rank_bigwin";
	public static List<RankVO> getBigWinList()
	{
		List<RankVO> list = SqlSimpleUtil.selectBeanList(GET_BIGWIN, RankVO.class);
		return list;
	}
	
	
	private static final String INSERT_RANK_BIGWIN = "INSERT INTO rank_reward_log (`userId`,`reward`,`InsertTime`,`type`) VALUES (?,?,?,?)";
	public static int insertBigWinLog(int userId,long coins, String time, int type)
	{
		return SqlSimpleUtil.insert(INSERT_RANK_BIGWIN, userId,coins,time,type);
	}
	
	private static final String GET_BIGWIN_LOG = "SELECT * FROM rank_reward_log where userId=? and type=? and to_days(InsertTime) = to_days(now())";
	public static List<RankLogVO> getBigWinRankRewardLog(int userId,int type)
	{
		Tool.print_debug_level0(" 领取排行榜3：userId:" + userId+",type="+type);
		List<RankLogVO> list = SqlSimpleUtil.selectBeanList(GET_BIGWIN_LOG, RankLogVO.class,userId,type);
		return list;
	}
	
	private static final String GET_PAYNUM = "SELECT * FROM rank_paynum_copy";
//	private static final String GET_PAYNUM = "SELECT * FROM rank_paynum";
	public static List<RankVO> getPayNumList()
	{
		List<RankVO> list = SqlSimpleUtil.selectBeanList(GET_PAYNUM, RankVO.class);
		return list;
	}

	
//	private static final String DELETE_RANKING_PAYNUM = "DELETE FROM rank_paynum_copy";
//	private static final String UPDATE_PAYNUM = "INSERT INTO rank_paynum_copy (`userId`,`nickName`,`score`,`rank`,`level`) VALUES (?,?,?,?,?)";
	private static final String DELETE_RANKING_PAYNUM = "DELETE FROM rank_paynum";
	private static final String UPDATE_PAYNUM = "INSERT INTO rank_paynum (`userId`,`nickName`,`score`,`rank`,`level`) VALUES (?,?,?,?,?)";
	
	public static void updatePayNum(Object[][] ranking)
	{
		try
		{
			SqlSimpleUtil.startTransaction();
			SqlSimpleUtil.transactionUpdate(DELETE_RANKING_PAYNUM);
			SqlSimpleUtil.transactionUpdateBatch(UPDATE_PAYNUM,ranking);
			
 			SqlSimpleUtil.commitAndClose();
		}
		catch (SQLException e)
		{
			Tool.print_dbError("updatePayNum错误", e);
		}
	}
}
