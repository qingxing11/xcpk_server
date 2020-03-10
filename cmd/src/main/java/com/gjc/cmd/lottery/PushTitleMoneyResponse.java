package com.gjc.cmd.lottery;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushTitleMoneyResponse extends Response
{
		public long titleMoney;

		public PushTitleMoneyResponse() 
		{
			this.msgType= MsgTypeEnum.Lottery_金钱改变.getType();
		}
		public PushTitleMoneyResponse(long titleMoney)
		{
			this.msgType= MsgTypeEnum.Lottery_金钱改变.getType();
			this.code=PushTitleMoneyResponse.SUCCESS;
			this.titleMoney = titleMoney;
		}


		@Override
		public String toString() {
			return "PushTitleMoneyResponse [titleMoney=" + titleMoney + ", msgType=" + msgType + ", data="
					+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
		}
}
