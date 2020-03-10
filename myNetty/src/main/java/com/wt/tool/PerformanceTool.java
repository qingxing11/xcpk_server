package com.wt.tool;

import com.wt.util.MyTimeUtil;
import com.wt.util.security.MySession;

import io.netty.channel.Channel;

public class PerformanceTool
{
	/**
	 * 获取最后一个消息从消息解码器解码出来后到调用该方法时所消耗的时间
	 * @param channel
	 * @return 使用时间或者,当通道附加属性 {@link MySession} 为空时返回-1
	 */
	public static long getLastHandlerUseTime(Channel channel)
	{
		MySession session = channel.attr(MySession.attr_session).get();
		if(session == null)
		{
			return -1;
		}
		return MyTimeUtil.getCurrTimeMM() - session.getStartTime();
	}
}

