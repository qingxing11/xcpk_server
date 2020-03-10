package com.brc.cmd.ranking;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 领取低保
 *
 */
public class LowResponse extends Response
{
	public static final int ERROR_金币较多 = 0;
	public static final int ERROR_次数不足 = 1;
	
	
	public int number;

	public LowResponse(int code)
	{
		this.code = code;
		msgType = MsgTypeEnum.LOW.getType();
	}

	public LowResponse(int code, int number)
	{
		this.code = code;
		msgType = MsgTypeEnum.LOW.getType();
		this.number = number;
	}

	@Override
	public String toString()
	{
		return "LowResponse [number=" + number + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + ", getData()=" + Arrays.toString(getData()) + ", getMsgType()=" + getMsgType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
}
