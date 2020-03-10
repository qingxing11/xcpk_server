package com.wt.xcpk.killroom.push;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.KillRoomNoticVO;

public class Push_killRoomBigWin extends Response
{
	public  ArrayList<KillRoomNoticVO> list_noticWin;
	public Push_killRoomBigWin()
	{
		this.msgType = MsgTypeEnum.KillRoom_大赢家.getType();
	}
	
	public Push_killRoomBigWin(ArrayList<KillRoomNoticVO> list_noticWin)
	{
		this.msgType = MsgTypeEnum.KillRoom_大赢家.getType();
		this.list_noticWin = list_noticWin;
	}

	@Override
	public String toString()
	{
		return "Push_killRoomBigWin [list_noticWin=" + list_noticWin + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
