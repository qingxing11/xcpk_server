package com.brc.cmd.lucky;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * @author 包小威
 *宝箱
 */
public class LuckyBoxRequest  extends Request
{
	 public int index;
	public LuckyBoxRequest()
	{
		msgType=MsgTypeEnum.LUCKY_BOX.getType();
	}
}
