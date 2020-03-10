package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 开始发牌，播放发牌动画，客户端扣底注
 * @author WangTuo
 *
 */
public class ClassicGamePush_allin extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public int betNum;
	public ClassicGamePush_allin()
	{
		msgType = MsgTypeEnum.classic_其他玩家全压.getType();
	}

	public ClassicGamePush_allin(int pos,int betNum)
	{
		msgType = MsgTypeEnum.classic_其他玩家全压.getType();
		this.pos = pos;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_checkPoker [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}
