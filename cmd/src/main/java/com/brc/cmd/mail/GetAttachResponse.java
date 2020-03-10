package com.brc.cmd.mail;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 领取一封邮件附件
 */
public class GetAttachResponse extends Response
{
	public static final int ERROR_邮件不存在= 0;
	public static final int ERROR_附件不存在=1;
	
	public String attachString;
	public   int mailId;
	
	public GetAttachResponse(int code)
	{
		msgType = MsgTypeEnum.MAIL_GETATTACH.getType();
		this.code=code;
	}
	public GetAttachResponse(int code,String attachString,int mailId)
	{
		msgType = MsgTypeEnum.MAIL_GETATTACH.getType();
		this.code=code;
		this.attachString=attachString;
		this.mailId=mailId;
	}
	@Override
	public String toString()
	{
		return "GetAttachResponse [attachString=" + attachString + ", mailId=" + mailId + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + ", getData()=" + Arrays.toString(getData()) + ", getMsgType()=" + getMsgType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
}
