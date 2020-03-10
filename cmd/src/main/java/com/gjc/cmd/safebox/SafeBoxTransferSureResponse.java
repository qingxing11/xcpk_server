package com.gjc.cmd.safebox;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class SafeBoxTransferSureResponse extends Response
{
		public static final int Error_金额不足=0;
		public long money;
		
		public int otherId;
		public int type;
		public long time;
		
		public float pre;
		
		public SafeBoxTransferSureResponse(int code,long money, int otherId, int type, long time,float pre) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账确认.getType();
			this.code=code;
			this.money = money;
			this.otherId = otherId;
			this.type = type;
			this.time = time;
			this.pre=pre;
		}
		public SafeBoxTransferSureResponse() 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账确认.getType();
		}
		public SafeBoxTransferSureResponse(int code) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账确认.getType();
			this.code=code;
		}
		
		public SafeBoxTransferSureResponse(int code,long money) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账确认.getType();
			this.code=code;
			this.money = money;
		}
		
		@Override
		public String toString() {
			return "SafeBoxTransferSureResponse [money=" + money + ", otherId=" + otherId + ", type=" + type + ", time="
					+ time + ", pre=" + pre + ", msgType=" + msgType + ", data=" + Arrays.toString(data)
					+ ", callBackId=" + callBackId + ", code=" + code + "]";
		}
}
