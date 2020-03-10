package com.brc.cmd.mail;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

import java.util.ArrayList;
import java.util.Arrays;

import com.wt.archive.MailDataPO;

public class GetAllMailResponse extends Response
{
	public ArrayList<MailDataPO> userMailDataPOS;
	
	public GetAllMailResponse(int code)
	{
		msgType = MsgTypeEnum.MAIL_GETALL.getType();
		this.code=code;
	}
	
	public GetAllMailResponse(ArrayList<MailDataPO> userMailDataPOS,int code)
	{
		msgType = MsgTypeEnum.MAIL_GETALL.getType();
		this.userMailDataPOS=userMailDataPOS;
		this.code=code;
	}

	@Override
	public String toString()
	{
		return "GetAllMailResponse [userMailDataPOS=" + userMailDataPOS + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + ", getData()=" + Arrays.toString(getData()) + ", getMsgType()=" + getMsgType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
}
