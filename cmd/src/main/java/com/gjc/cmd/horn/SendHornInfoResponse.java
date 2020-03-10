package com.gjc.cmd.horn;

import java.util.Arrays;

import com.gjc.naval.horn.HornInfoVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class SendHornInfoResponse extends Response
{
	public final static int Error_金币不足=0;
	public HornInfoVO vo;
	public int subMoney;
	
	public SendHornInfoResponse()
	{
		this.msgType= MsgTypeEnum.Horn_喇叭消息.getType();
	}
	public SendHornInfoResponse(int code)
	{
		this.msgType= MsgTypeEnum.Horn_喇叭消息.getType();
		this.code=code;
	}

	public SendHornInfoResponse(int code,HornInfoVO vo, int subMoney) 
	{
		this.msgType= MsgTypeEnum.Horn_喇叭消息.getType();
		this.code=code;
		this.vo = vo;
		this.subMoney = subMoney;
	}
	
	
	@Override
	public String toString() 
	{
		return "SendHornInfoResponse [vo=" + vo + ", subMoney=" + subMoney + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
	
}
