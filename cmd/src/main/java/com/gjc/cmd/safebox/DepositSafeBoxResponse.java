package com.gjc.cmd.safebox;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class DepositSafeBoxResponse extends Response
{
		public static final int Error_暂无银行功能=0;
		public static final int Error_存入超过上线=1;
		
		public long money;

		public DepositSafeBoxResponse() 
		{
			this.msgType= MsgTypeEnum.SafeBox_存入银行.getType();
		}
		
		public DepositSafeBoxResponse(int code) 
		{
			this.msgType= MsgTypeEnum.SafeBox_存入银行.getType();
			this.code=code;
		}
		
		public DepositSafeBoxResponse(int code,long money) 
		{
			this.msgType= MsgTypeEnum.SafeBox_存入银行.getType();
			this.code=code;
			this.money = money;
		}



		@Override
		public String toString() 
		{
			return "DepositSafeBoxResponse [money=" + money + ", msgType=" + msgType + ", data=" + Arrays.toString(data)+ ", callBackId=" + callBackId + ", code=" + code + "]";
		}
		
	
}
