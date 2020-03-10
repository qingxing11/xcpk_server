package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.KillRoomNoticVO;

public class ClassicGamePush_bigWin extends Response
{
	public KillRoomNoticVO killRoomNoticVO;
	
	public ClassicGamePush_bigWin() {
		msgType = MsgTypeEnum.classic_大赢家.getType();
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_bigWin [killRoomNoticVO=" + killRoomNoticVO + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
