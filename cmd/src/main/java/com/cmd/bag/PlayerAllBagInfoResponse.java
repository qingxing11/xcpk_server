package com.cmd.bag;

import java.util.ArrayList;

import com.wt.archive.BagPropVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PlayerAllBagInfoResponse extends Response
{

	public int maxCapacity;// 背包的最大容量

	public ArrayList<BagPropVO> prop_list;

	public PlayerAllBagInfoResponse()
	{
		msgType =  MsgTypeEnum.GAME_PLAYERALLBAGINFO.getType();
	}

	public PlayerAllBagInfoResponse(int maxCapacity)
	{
		msgType = MsgTypeEnum.GAME_PLAYERALLBAGINFO.getType();
		this.code = SUCCESS;
		this.maxCapacity = maxCapacity;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("GAME_PLAYERALLBAGINFO,maxCapacity=" + maxCapacity + ",code=" + code + ",prop_list={");
		if (prop_list == null)
		{
			sb.append("null}");
		}
		else
		{
			for (BagPropVO prop : prop_list)
			{
				sb.append(prop.getSrcId() + ",");
			}
			sb.append("}");
		}

		return sb.toString();
	}

}
