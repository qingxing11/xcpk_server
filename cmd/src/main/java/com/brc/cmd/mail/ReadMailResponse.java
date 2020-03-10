package com.brc.cmd.mail;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ReadMailResponse extends Response
{
	public static final int ERROR_邮件不存在= 0;
	public static final int ERROR_已阅读 = 1;
	public int  emailId;
	
	public ReadMailResponse(int code)
	{
		msgType = MsgTypeEnum.MAIL_READ.getType();
		this.code=code;
	}
	
	public ReadMailResponse(int emailId,int code)
	{
		this.emailId=emailId;
		this.code=code;
		msgType = MsgTypeEnum.MAIL_READ.getType();
	}

	@Override
	public String toString()
	{
		return "ReadMailResponse [emailId=" + emailId + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + ", getData()=" + Arrays.toString(getData()) + ", getMsgType()=" + getMsgType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
	
}
