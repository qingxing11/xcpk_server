package com.gjc.cmd.safebox;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class SafeBoxTransferResponse extends Response
{
		public static final int Error_暂无转账功能=0;
		public static final int Error_转给自己=1;
	
		public String otherNike;
		
		public int otherId;
		
		public long transferCoins;
		
		public SafeBoxTransferResponse() 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账.getType();
		}

		public SafeBoxTransferResponse(int code) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账.getType();
			this.code=code;
		}
		
		public SafeBoxTransferResponse(int code,String otherNike, int otherId,long transferCoins) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账.getType();
			this.code=code;
			this.otherNike = otherNike;
			this.otherId=otherId;
			this.transferCoins=transferCoins;
		}



		@Override
		public String toString() {
			return "SafeBoxTransferResponse [otherNike=" + otherNike + ", otherId=" + otherId +",transferCoins="+transferCoins+ ", msgType=" + msgType
					+ ", data=" + Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
		}
		
}
