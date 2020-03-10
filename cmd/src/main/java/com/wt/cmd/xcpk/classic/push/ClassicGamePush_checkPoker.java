package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ClassicGamePush_checkPoker extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public ClassicGamePush_checkPoker()
	{
		msgType = MsgTypeEnum.classic_有玩家看牌.getType();
	}

	public ClassicGamePush_checkPoker(int pos)
	{
		msgType = MsgTypeEnum.classic_有玩家看牌.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_checkPoker [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}
