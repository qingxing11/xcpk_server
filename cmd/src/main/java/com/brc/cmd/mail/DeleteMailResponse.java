package com.brc.cmd.mail;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class DeleteMailResponse extends Response
{
	public static final int ERROR_邮件不存在= 0;
	public static final int ERROR_附件未领取=1;
	public int mailId;
	public DeleteMailResponse(int code)
	{
		msgType=MsgTypeEnum.MAIL_DELETE.getType();
		this.code=code;
	}
	public DeleteMailResponse(int code,int mailId)
	{
		msgType=MsgTypeEnum.MAIL_DELETE.getType();
		this.code=code;
		this.mailId=mailId;
	}
	@Override
	public String toString()
	{
		return "DeleteMailResponse [mailId=" + mailId + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + ", getData()=" + Arrays.toString(getData()) + ", getMsgType()=" + getMsgType() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + "]";
	}
}
