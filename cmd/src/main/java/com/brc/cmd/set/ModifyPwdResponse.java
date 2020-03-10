package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威 修改密码
 */
public class ModifyPwdResponse extends Response
{
	public static final int ERROR_原始密码错误 = 0;
	public static final int ERROR_账号不存在  = 1;

	public int userId;
	public String password;
	
	public ModifyPwdResponse()
	{
		msgType = MsgTypeEnum.MODIFYPWD.getType();
	}

	public ModifyPwdResponse(int code)
	{
		this.code = code;
		msgType = MsgTypeEnum.MODIFYPWD.getType();
	}
	
	public ModifyPwdResponse(int code,int userId)
	{
		this.code = code;
		msgType = MsgTypeEnum.MODIFYPWD.getType();
		this.userId = userId;
	}

	public ModifyPwdResponse(int code, String newPwd, int userId)
	{
		this.code = code;
		msgType = MsgTypeEnum.MODIFYPWD.getType();
		this.password = newPwd;
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "ModifyPwdResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}
