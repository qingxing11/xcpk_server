package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威 绑定手机号
 */
public class BindPhoneRequest extends Request
{
	public int codeNum;

	public BindPhoneRequest()
	{
		msgType = MsgTypeEnum.BINDPHONE.getType();
	}
}
