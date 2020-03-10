package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ManyPepolPush_checkPoker extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public ManyPepolPush_checkPoker()
	{
		msgType = MsgTypeEnum.manypepol_有玩家看牌.getType();
	}

	public ManyPepolPush_checkPoker(int pos)
	{
		msgType = MsgTypeEnum.manypepol_有玩家看牌.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_checkPoker [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}
