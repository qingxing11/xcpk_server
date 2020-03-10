package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威 绑定手机号
 */
public class ChangeAccountRequest extends Request
{
	public String account;
	
	public String password;
	
	public int userId;

	public ChangeAccountRequest()
	{
		msgType = MsgTypeEnum.SET_切换账号.getType();
	}
}
