package com.brc.cmd.mail;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威
 * 请求所有邮件
 */
public class GetAllMailRequest extends Request
{
	public GetAllMailRequest()
	{
		msgType = MsgTypeEnum.MAIL_GETALL.getType();
	}
}
