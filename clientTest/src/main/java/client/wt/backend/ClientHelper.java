package client.wt.backend;

import com.wt.cmd.Request;

import client.wt.decoder.LengthDecoder;
import client.wt.decoder.MessageEncoder_Json_client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientHelper
{
	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 1985;

	private static Channel gameChannel = null;

	private static Bootstrap bootstrap = null;

	/** 数据包最大长度 */
	private static final int MAX_LENGH = 8192;

	private static Bootstrap getBootstrap()
	{// 引导程序
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group).channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<Channel>()
		{
			@Override
			protected void initChannel(Channel ch) throws Exception
			{
				ChannelPipeline pipeline = ch.pipeline();

				pipeline.addLast(new LengthDecoder(MAX_LENGH, 0, 4, 0, 4));
				pipeline.addLast(new MessageEncoder_Json_client());

				// 消息接收以及处理器
				pipeline.addLast("handler", new ClientHandler());
			}

		});
		return bootstrap;
	}

	// 创建信道
	private static Channel createChannel(String host, int port)
	{
		Channel channel = null;
		try
		{
			channel = getBootstrap().connect(host, port).sync().channel();
			System.out.println("通道建立成功");

		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("无法创建channel，连接Server(" + host + ":" + port + ")失败!");
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
	private static void sendMsgShortLink(String host, int port, Object data)
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

	// 客户端发送请求
	public static void sendRequest(Request request)
	{
		sendMsgShortLink(SERVER_HOST, SERVER_PORT, request);
	}

	public static void sendRequest(Request request,String host,int port)
	{
		sendMsgShortLink(host, port, request);
	}
}
