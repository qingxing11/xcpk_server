package com.gjc.cmd.sign;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushSignResponse extends Response
{
	public boolean todayIsSign;//可以签到
	
	public PushSignResponse() 
	{
		this.msgType= MsgTypeEnum.Sign_更新签到.getType();
	}

	public PushSignResponse(boolean todayIsSign)
	{
		this.msgType= MsgTypeEnum.Sign_更新签到.getType();
		this.code=PushSignResponse.SUCCESS;
		this.todayIsSign = todayIsSign;
	}



	@Override
	public String toString() {
		return "PushSignResponse [todayIsSign=" + todayIsSign + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
}
