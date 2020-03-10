package com.cmd.bag;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class PlayerAddNewBagInfoRequest extends Request
{
	public ArrayList<Integer> srcId_list;

	public PlayerAddNewBagInfoRequest()
	{
		msgType = MsgTypeEnum.GAME_ADDNEWPBAGROP.getType();
	}

	public PlayerAddNewBagInfoRequest(ArrayList<Integer> srcId_list)
	{
		msgType = MsgTypeEnum.GAME_ADDNEWPBAGROP.getType();
		this.srcId_list = srcId_list;
	}
}
