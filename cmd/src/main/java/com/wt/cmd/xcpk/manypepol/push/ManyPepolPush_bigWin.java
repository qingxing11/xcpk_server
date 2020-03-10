package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.KillRoomNoticVO;

public class ManyPepolPush_bigWin extends Response
{
	public KillRoomNoticVO killRoomNoticVO;
	
	public ManyPepolPush_bigWin() {
		msgType = MsgTypeEnum.manypepol_大赢家.getType();
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_bigWin [killRoomNoticVO=" + killRoomNoticVO + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
