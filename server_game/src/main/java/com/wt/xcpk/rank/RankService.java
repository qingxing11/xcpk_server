package com.wt.xcpk.rank;

import java.util.ArrayList;

import com.wt.xcpk.vo.RankVO;

/**
 * 1充值榜显示前20名-每日充值榜前三次名，分别获取充值所得钻石100%50%30%金币奖励，次日直接到账（奖励按1元=1钻石=1万金币结算）

<br>2土豪榜：显示前20名账号金币最多的玩家。

<br>3赢金榜：显示赢金榜最多的前20名。奖励规则参考友乐

<br>注：每榜前三，显示特殊称谓，在任意房间游戏，头像那里外显特殊称谓。
 * @author WangTuo
 *
 */
public interface RankService
{
	/**
	 * 增加玩家充值数
	 */
	void addPayNum(int userId,int payNum,String nickName,int level);
	
	/**
	 * 增加玩家赢金数,影响大赢家排行
	 * @param userId
	 * @param payNum
	 */
	void addWinConisNum(int userId,long num,String nickName,int level);
	
	/**
	 * 充值榜显示前20名-每日充值榜前三次名，分别获取充值所得钻石100%50%30%金币奖励，次日直接到账（奖励按1元=1钻石=1万金币结算）
	 * @return
	 */
	ArrayList<RankVO> getPayRank();
	
	/**
	 * 土豪榜：显示前20名账号金币最多的玩家
	 * @return
	 */
	ArrayList<RankVO> getCoinsRank();
	
	/**
	 * 赢金榜：显示赢金榜最多的前20名。奖励规则参考友乐
	 * @return
	 */
	ArrayList<RankVO> getBigWinRank();
	
}
