package com.wt.netty.client;

import org.springframework.stereotype.Service;

import com.wt.cmd.Request;
import com.wt.decoder.LengthDecoder;
import com.wt.decoder.MessageEncoder_protobuf_client;
import com.wt.util.Tool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@Service
public class ClientHelper
{
	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 6666;

	private static Channel gameChannel = null;

	/** 数据包最大长度 */
	private static final int MAX_LENGH = 8192;

	public static void initAuth(String authIP, int authPort)
	{
		SERVER_HOST = authIP;
		SERVER_PORT = authPort;
	}
	
	EventLoopGroup group = new NioEventLoopGroup();
	Bootstrap bootstrap = null;
	private Bootstrap getBootstrap()
	{// 引导程序
		if(bootstrap == null)
		{
			bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<Channel>()
			{
				@Override
				protected void initChannel(Channel ch) throws Exception
				{
					ChannelPipeline pipeline = ch.pipeline();

					pipeline.addLast(new LengthDecoder(MAX_LENGH, 0, 4, 0, 4));
					pipeline.addLast(new MessageEncoder_protobuf_client());

					// 消息接收以及处理器
					pipeline.addLast("handler", new ClientHandler());
				}
			});
		}
		return bootstrap;
	}

	// 创建信道
	private Channel createChannel(String host, int port)
	{
		Channel channel = null;
		try
		{
			channel = getBootstrap().connect(host, port).sync().channel();

		}
		catch (Exception e)
		{
			e.printStackTrace();
			Tool.print_error("无法创建channel，连接Server(" + host + "," + port + ")失败!");
			return null;
		}
		return channel;
	}

	// 标记长连接断开
	private static boolean connected = false;

	public static void setConnectStatus(boolean connected)
	{
		ClientHelper.connected = connected;
	}

	public static boolean isConnected()
	{
		return connected;
	}

	// 发送消息-短连接：每次使用新Channel发送消息
	private void sendMsgShortLink(String host, int port, Object data)
	{
		try
		{
			gameChannel = createChannel(host, port);
			if (gameChannel != null)
			{
				gameChannel.writeAndFlush(data).sync();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void close()
	{
		gameChannel.close();
	}

	// 客户端请求
	/**
	 * 作为客户端发送一个消息给账号服
	 * @param request
	 */
	public void sendRequest(Request request)
	{
		sendMsgShortLink(SERVER_HOST, SERVER_PORT, request);
	}

	public void sendRequest(Request request,String host,int port)
	{
		sendMsgShortLink(host, port, request);
	}
}
