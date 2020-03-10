package com.yt.xcpk.killroomSendRedEnvelope;

import java.util.ArrayList;

import com.wt.naval.vo.user.Player;
import com.wt.xcpk.killroom.RedEnvelopeInfo;

public interface KillRoomSendRedEnvelopeService {

	/**
	 * 获取生成的红包数组
	 * 
	 * @param moneySum
	 *            红包总额
	 * @param redEnvelopeNum
	 *            红包个数
	 * @param minMoneyo
	 *            每个小红包的最大额
	 * @param maxMoney
	 *            每个小红包的最小额
	 * @return 存放生成的每个小红包的值的数组
	 */
	ArrayList<RedEnvelopeInfo> getRenEnvelopeInfo(int userId, long moneySum, int redEnvelopeNum, long maxMoney, long minMoney);

	/**
	 * 返回当前发红包的状态
	 * 
	 * @return
	 */
	int returnSendRedState();

	/**
	 * 设置当前发红包状态为可发
	 */
	void setSendRedStateToCanSend();

	/**
	 * 设置 当前发红包状态为不可发
	 */
	void setSendRedStateToNotCanSend();

	/***
	 * 抢红包 已经被抢的添加到已经被抢的列表中 当所有红包被抢完时 发送消息通知所有在房间内的玩家
	 * 
	 * @param redEnvelopeIndex
	 */
	void GrabRedEnvelope(RedEnvelopeInfo redEnvelope);

	/**
	 * 获取还未抢完的红包
	 * 
	 * @return
	 */
	ArrayList<RedEnvelopeInfo> getCurrentShengYuRedEnvelope();

	/**
	 * 获取本局所有的红包信息
	 * 
	 * @return
	 */
	ArrayList<RedEnvelopeInfo> getCurrentBureauAllRedEnvelopeInfo();

	/**
	 * 获取被抢的红包
	 * 
	 * @return
	 */
	ArrayList<RedEnvelopeInfo> getCurrentWasRobbedRedEnvelope();

	/***
	 * 给新进入房间的玩家发送红包信息
	 */
	void giveEnterRoomPlayerSendShengYuRedEnvelopeInfo(Player player);
	
	
	void getCurrentBeWasRobbedRenEnvelope(int userId,int redEnvelopeIndex,long redEnvelopeValue);
}
