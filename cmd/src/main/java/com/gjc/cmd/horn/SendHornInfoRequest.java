package com.gjc.cmd.horn;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class SendHornInfoRequest extends Request
{
	public String info;
	
	public SendHornInfoRequest() 
	{
		this.msgType= MsgTypeEnum.Horn_喇叭消息.getType();
	}

	public SendHornInfoRequest(String info) 
	{
		this.msgType= MsgTypeEnum.Horn_喇叭消息.getType();
		this.info = info;
	}



	@Override
	public String toString()
	{
		return "SendHornInfoRequest [info=" + info + ", msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
	
}
