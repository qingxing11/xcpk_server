package com.gjc.cmd.sign;

import java.util.ArrayList;
import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushSignDataResponse extends Response
{
	public ArrayList<Boolean> list;
	public boolean todayIsSign;//可以签到
	
	public PushSignDataResponse(ArrayList<Boolean> list, boolean todayIsSign) 
	{
		this.msgType= MsgTypeEnum.Sign_签到数据.getType();
		this.code=PushSignDataResponse.SUCCESS;
		this.list = list;
		this.todayIsSign=todayIsSign;
	}

	@Override
	public String toString() {
		return "PushSignDataResponse [list=" + list + ", todayIsSign=" + todayIsSign + ", msgType=" + msgType
				+ ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}

	public PushSignDataResponse() 
	{
		this.msgType= MsgTypeEnum.Sign_签到数据.getType();
	}
}
