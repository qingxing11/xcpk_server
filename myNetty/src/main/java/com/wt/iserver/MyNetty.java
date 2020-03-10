package com.wt.iserver;

import com.wt.config.Config;
import com.wt.decoder.LengthDecoder;
import com.wt.decoder.MessageEncoder_json;
import com.wt.decoder.MessageEncoder_protobuf;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * 通信模块主类，通信模块依赖编解码模块 <br>
 * 
 * @author WangTuo
 */
@SuppressWarnings("restriction") 
public class MyNetty
{
	private static MyNetty instance;
	private IServer iServer;

	private MyNetty()
	{}

	public static MyNetty instance()
	{
		if (instance == null)
		{
			instance = new MyNetty();
		}
		return instance;
	}

	private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private static final EventLoopGroup workerGroup = new NioEventLoopGroup();

	public void shutDown()
	{
		Tool.print_debug_level0("netty退出,拒绝新请求与链接...");

		if (!workerGroup.isShuttingDown())
		{
			workerGroup.shutdownGracefully();
		}

		if (!bossGroup.isShuttingDown())
		{
			bossGroup.shutdownGracefully();
		}
	}

	public static ChannelFuture future;

	private void run(long startTime,int port)
	{
		if(port == 0)
		{
			port = Config.instance().port;
		}
		
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);
		bootstrap.channel(NioServerSocketChannel.class);

		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

		bootstrap.childHandler(new ChildhannelHander());
		try
		{
			Tool.print_debug_level0("绑定端口:"+port);
			// 绑定端口，等待同步
			future = bootstrap.bind(port).sync();
			Tool.print_debug_level0("服务器启动完成," + (startTime == 0 ? "" : "耗时:" + (MyTimeUtil.getCurrTimeMM() - startTime)) + "ms,端口:" + port);
			future.channel().closeFuture().sync();
		}
		catch (Exception e)
		{
			Tool.print_debug_level0("游戏服务器出现异常:" + e);
			e.printStackTrace();
		}
		finally
		{
			shutDown();
		}
	}

	/**
	 * 服务器启动的时的初始化
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception
	{
		initShutdown();
//		ClassTool.scanningApi();
	}

	

	/**
	 * netty优雅退出
	 */
	private void initShutdown()
	{
		String sig_type = getOSSignalType();// 根据操作系统的名称来获取对应的信号名称
		Signal sig = new Signal(sig_type);// 应用进程启动的时候，初始化Signal实例
		Signal.handle(sig, new SignalHandler()
		{
			public void handle(Signal arg0)
			{
				Thread t = new Thread(new ShutdownHook(iServer), "ShutdownHook-Thread");
				Runtime.getRuntime().addShutdownHook(t);
				Runtime.getRuntime().exit(0);
				Tool.print_debug_level0("netty关闭...");
			}
		});
	}

	/**
	 * 获取操作系统信号类型，处理liunx kill -9，windows ctrl+c信号 <br>
	 * win ctrl +ｃ＝ INT ;liunx * kill -9 = USR2
	 * 
	 * @return
	 */
	private static String getOSSignalType()
	{
		return System.getProperties().getProperty("os.name").toLowerCase().startsWith("win") ? "INT" : "USR2";
	}

	
	public void start(int port,IServer iServer)
	{
		start(0,port, iServer);
	}

	public void start(long startTime,int port, IServer iServer)
	{
		this.iServer = iServer;
		try
		{
			init();
			run(startTime,port);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// // 启动服务器
	// public static void main(String[] args)
	// {
	// GameServerMain server = new GameServerMain();
	// server.start();
	// }

	/** 数据包最大长度 */
	private static final int MAX_LENGH = 1048576;//不超过1M

	private class ChildhannelHander extends ChannelInitializer<Channel>
	{
		@Override
		protected void initChannel(Channel ch) throws Exception
		{
			// System.out.println("接收数据1 : "+ ch);
			ChannelPipeline pipeline = ch.pipeline();

			pipeline.addLast(new LengthDecoder(MAX_LENGH, 0, 4, 0, 4));

			switch (Config.instance().serializerUtil)
			{
				case 1:
					pipeline.addLast(new MessageEncoder_protobuf());
					break;

				case 2:
					pipeline.addLast(new MessageEncoder_json());
					break;

				default:
					break;
			}

 			pipeline.addLast("idleStateHandler", new IdleStateHandler(7, 7, 0));

			// 服务器消息接收处理类
			pipeline.addLast(new ServerHandler(iServer));
		}
	}
}

/**
 * 将在服务器接收到人工强行停止信号(ctrl+c，kill -9)停止服务器时执行
 * 
 * @author WangTuo
 */
class ShutdownHook implements Runnable
{
	private IServer iServer;

	public ShutdownHook(IServer iServer)
	{
		this.iServer = iServer;
	}

	@Override
	public void run()
	{
		iServer.shutDown();
		MyNetty.instance().shutDown();
	}
}