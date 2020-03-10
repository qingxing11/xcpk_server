package com.wt.xcpk.killroom;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威 获取所有红包
 */
public class KillRoomGetAllRedResponse extends Response
{
	public ArrayList<RedEnvelopeInfo> redEnvelopeInfo;// 返回具体红包信息

	public int redEnvelopeState;

	public int userId;

	public KillRoomGetAllRedResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_获取所有红包.getType();
	}
}
