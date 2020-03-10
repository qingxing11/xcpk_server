package com.brc.naval.getsimple;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class GetSimpleResponse extends Response {
	
	public static final int ERROR_玩家为空 = 0;
	public static final int ERROR_数据为空=1;
	public PlayerSimpleData simpleData;
	
	public GetSimpleResponse (int code)
	{
		this.code=code;
		msgType=MsgTypeEnum.GET_SIMPLEDATA.getType();
	}
	public GetSimpleResponse (int code,PlayerSimpleData simpleData)
	{
		this.code=code;
		msgType=MsgTypeEnum.GET_SIMPLEDATA.getType();
		this.simpleData=simpleData;
	}
	@Override
	public String toString()
	{
		return "GetSimpleResponse [simpleData=" + simpleData + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + ", getData()=" + Arrays.toString(getData()) + ", getMsgType()=" + getMsgType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
}
