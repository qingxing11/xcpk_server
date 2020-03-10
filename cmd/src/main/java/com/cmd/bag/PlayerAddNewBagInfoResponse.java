package com.cmd.bag;

import java.util.ArrayList;

import com.wt.archive.BagPropVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 玩家添加新的道具
 */
public class PlayerAddNewBagInfoResponse extends Response
{

	public ArrayList<BagPropVO> prop_list;

	public PlayerAddNewBagInfoResponse()
	{
		msgType = MsgTypeEnum.GAME_ADDNEWPBAGROP.getType();
	}

	public PlayerAddNewBagInfoResponse(ArrayList<BagPropVO> prop_list)
	{
		msgType = MsgTypeEnum.GAME_ADDNEWPBAGROP.getType();
		this.prop_list = prop_list;
		this.code = SUCCESS;
	}
}
