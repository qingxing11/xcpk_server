package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.loadtest.LoadTestConnectResponse;
import com.wt.naval.biz.LoadTestBiz;
import com.wt.naval.main.GameServerHelper;
import com.wt.tool.ClassTool;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.loadtest")
@Service
public class ApiLoadTest
{
	public static void loadTestConnect(ChannelHandlerContext ctx, int callbackId)
	{
		LoadTestConnectResponse response = new LoadTestConnectResponse(LoadTestConnectResponse.SUCCESS, callbackId);

		LoadTestBiz.addOnline(ctx.channel());

		System.out.println("ctx:" + ctx.channel().id());
		GameServerHelper.sendResponse(ctx, response);
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
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
