package com.brc.cmd.set;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * @author 包小威
 * 绑定手机号
 */
public class SupplementaryAccountResponse extends Response 
{
	public static final int ERROR_已经是完善账号 = 0;
	public static final int ERROR_账号重复 = 1;
	public static final int ERROR_昵称重复 = 2;
	public static final int ERROR_数据插入失败 = 3;
	
	public String account;
	public String nickName;
	public String password;

	public int userId;
	
	 public  SupplementaryAccountResponse()
	 {
		 msgType = MsgTypeEnum.SET_完善账号.getType();
	}
	
	 public  SupplementaryAccountResponse(int code)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.SET_完善账号.getType();
	}
	 
	 public  SupplementaryAccountResponse(int code,int userId)
	 {
		 this.code=code;
		 msgType = MsgTypeEnum.SET_完善账号.getType();
		 this.userId = userId;
	}
	 
	 public  SupplementaryAccountResponse(int code,String account,String nickName,String password,int userId)
	 {
		 msgType = MsgTypeEnum.SET_完善账号.getType();
		 this.code = code;
		 this.account = account;
		 this.nickName =nickName;
		 this.password = password;
		 this.userId = userId;
	 }

	@Override
	public String toString()
	{
		return "SupplementaryAccountResponse [account=" + account + ", nickName=" + nickName + ", password=" + password + ", userId=" + userId + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
