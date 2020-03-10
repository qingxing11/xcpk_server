package com.yt.cmd.fruitMachine.push;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.KillRoomNoticVO;

public class FruitPush_bigWin extends Response
{
	public ArrayList<KillRoomNoticVO> list_KillRoomNoticVO;
	
	public FruitPush_bigWin() {
		msgType = MsgTypeEnum.Fruit_大赢家.getType();
	}

	@Override
	public String toString()
	{
		return "FruitPush_bigWin [list_KillRoomNoticVO=" + list_KillRoomNoticVO + ", msgType=" + msgType + "]";
	}
}