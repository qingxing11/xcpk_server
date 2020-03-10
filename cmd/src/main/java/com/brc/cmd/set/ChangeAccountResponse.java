package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 绑定手机号
 */
public class ChangeAccountResponse extends Response 
{
	public static final int ERROR_账号密码错误 = 0;
	
	public String account;
	public String password;
	public int userId;
	
	 public  ChangeAccountResponse()
	 {
		 msgType = MsgTypeEnum.SET_切换账号.getType();
	}
	
	 public  ChangeAccountResponse(int code)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.SET_切换账号.getType();
	}

	public ChangeAccountResponse(int code, int userId,String account, String password)
	{
		 this.code=code;
		 msgType = MsgTypeEnum.SET_切换账号.getType();
		 this.userId = userId;
		 this.account = account;
		 this.password = password;
	}

	@Override
	public String toString()
	{
		return "ChangeAccountResponse [account=" + account + ", password=" + password + ", userId=" + userId + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
