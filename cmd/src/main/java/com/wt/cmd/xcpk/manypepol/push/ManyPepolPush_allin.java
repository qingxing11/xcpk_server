package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ManyPepolPush_allin extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public int betNum;
	public ManyPepolPush_allin()
	{
		msgType = MsgTypeEnum.manypepol_其他玩家全压.getType();
	}

	public ManyPepolPush_allin(int pos,int betNum)
	{
		msgType = MsgTypeEnum.manypepol_其他玩家全压.getType();
		this.pos = pos;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_allin [pos=" + pos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
