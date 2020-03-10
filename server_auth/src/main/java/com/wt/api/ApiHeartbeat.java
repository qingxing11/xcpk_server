package com.wt.api;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.heartbeat.HeartbeatResponse;
import com.wt.main.AuthServerHelper;
import com.wt.tool.ClassTool;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.heartbeat")
@Service
public class ApiHeartbeat
{
	@Protocol(msgType = MsgTypeEnum.HEARTBEAT)
	public static void heartbeat(ChannelHandlerContext ctx,Request request)
	{
		HeartbeatResponse response = new HeartbeatResponse();
		AuthServerHelper.sendResponse(ctx, response);
	}
	
	@PostConstruct
	private void init()
	{
		try
		{
			ClassTool.registerApi(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
