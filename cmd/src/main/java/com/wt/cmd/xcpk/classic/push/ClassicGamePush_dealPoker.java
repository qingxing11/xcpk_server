package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ClassicGamePush_dealPoker extends Response
{
	/**
	 * 庄家位置
	 */
	public int bankerPos;
	
	/**
	 * 扣底注数
	 */
	public int betNum;
	
	public int playPos;
	public ClassicGamePush_dealPoker()
	{
		msgType = MsgTypeEnum.classic_开始发牌.getType();
	}

	public ClassicGamePush_dealPoker(int pos,int betNum,int playPos)
	{
		msgType = MsgTypeEnum.classic_开始发牌.getType();
		bankerPos = pos;
		this.betNum = betNum;
		this.playPos = playPos;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_dealPoker [bankerPos=" + bankerPos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
