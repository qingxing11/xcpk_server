package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威 绑定手机号
 */
public class SupplementaryAccountRequest extends Request
{
	public String account;
	public String nickName;
	public String password;

	public int userId;
	public SupplementaryAccountRequest()
	{
		msgType = MsgTypeEnum.SET_完善账号.getType();
	}
	@Override
	public String toString()
	{
		return "SupplementaryAccountRequest [account=" + account + ", nickName=" + nickName + ", password=" + password + ", userId=" + userId + ", msgType=" + msgType + "]";
	}
}
