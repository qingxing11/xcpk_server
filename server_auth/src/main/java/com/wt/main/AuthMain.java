package com.wt.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wt.cache.ResCache;
import com.wt.iserver.IServer;
import com.wt.iserver.MyNetty;
import com.wt.server.config.MyConfig;
import com.wt.tool.ClassTool;
import com.wt.tool.PrintMsgTool;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

@SpringBootApplication
public class AuthMain implements IServer
{
	public static void main(String[] args)
	{
		Tool.print_debug_level0("游戏服务器启动中...");
		init();

 		new AuthMain();
	}
	
	public AuthMain()
	{
		long serverStartTime = MyTimeUtil.getCurrTimeMM();
		
		MyNetty.instance().start(serverStartTime,0,this);
	}

	private static void init()
	{
		initConfig();
		initApi();
		PrintMsgTool.init();
	}

	private static void initConfig()
	{
		ResCache.initHotfix();
		Tool.setPrintLevel(MyConfig.instance().print_debug_leven);
	}

	private static void initApi()
	{
//		ClassTool.registerApi(ApiRes.class);
		ClassTool.scanningApi();
	}

	@Override
	public void shutDown()
	{

		Tool.print_debug_level0("保存服务器数据完成.");

		Tool.print_debug_level0("完成关闭服务,优雅退出完成");
	}


	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
//		Tool.print_debug_level0("通道断开");
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object msg)
	{}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		Channel incoming = ctx.channel();
		if (!incoming.isActive())
			System.out.println("SimpleClient:" + incoming.remoteAddress());

		cause.printStackTrace();
	}
}
