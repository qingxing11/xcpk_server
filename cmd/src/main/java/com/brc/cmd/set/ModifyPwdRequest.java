package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威
 * 修改密码
 */
public class ModifyPwdRequest  extends Request
{
	public String oldPwd;
	public String newPwd;
	public int userId;
	
	public ModifyPwdRequest()
	{
		msgType = MsgTypeEnum.MODIFYPWD.getType();
	}

	@Override
	public String toString()
	{
		return "ModifyPwdRequest [oldPwd=" + oldPwd + ", newPwd=" + newPwd + ", userId=" + userId + ", msgType=" + msgType + "]";
	}
}
