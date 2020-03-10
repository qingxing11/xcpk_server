package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ClassicGamePush_playerFollow extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public int betNum;
	public ClassicGamePush_playerFollow()
	{
		msgType = MsgTypeEnum.classic_有玩家跟注.getType();
	}

	public ClassicGamePush_playerFollow(int pos,int num)
	{
		msgType = MsgTypeEnum.classic_有玩家跟注.getType();
		this.pos = pos;
		this.betNum = num;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerFollow [pos=" + pos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
