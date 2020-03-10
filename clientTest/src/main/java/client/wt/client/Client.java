package client.wt.client;

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

public class Client
{
	public static Bootstrap bootstrap = null;

	/** 数据包最大长度 */
	public static final int MAX_LENGH = 8192;
	public static Bootstrap getBootstrap()
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

				//pipeline.addLast("encode", new StringEncoder());
				pipeline.addLast("lengthDecode", new LengthDecoder(MAX_LENGH, 0, 4, 0, 4));
				pipeline.addLast(new MessageEncoder_Json_client());

				// 消息接收以及处理器
				pipeline.addLast("handler", new ClientHandler());
			}

		});
		Client.bootstrap = bootstrap;
		return bootstrap;
	}

	// 创建信道
	public static Channel createChannel(String host, int port)
	{
		Channel channel = null;
		try
		{
			channel = getBootstrap().connect(host, port).sync().channel();
			System.out.println("通道建立成功");

		}
		catch (Exception e)
		{
			// e.printStackTrace();
			System.out.println("无法创建channel，连接Server(" + host + ":" + port + ")失败!");
			return null;
		}
		return channel;
	}

	// 发送消息-长连接：每次继续使用之前的Channel
	public void sendMsg(Channel channel, Request request)
	{
		try
		{
			if (channel != null)
			{
				channel.writeAndFlush(request).sync();
			}
			else
			{
				System.out.println("channel为空，消息发送失败!!!");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (e.toString().equals("java.nio.channels.ClosedChannelException"))
			{
				setConnectStatus(false);// 与服务器断开连接
			}
		}
	}

	// 标记长连接断开
	private static boolean connected = false;

	public void setConnectStatus(boolean connected)
	{
		Client.connected = connected;
	}

	public static boolean isConnected()
	{
		return connected;
	}

	// 发送消息-短连接：每次使用新Channel发送消息
	public static void sendMsgShortLink(String host, int port, Object data)
	{
		try
		{
			Channel newChannel = createChannel(host, port);
			if (newChannel != null)
			{
				newChannel.writeAndFlush(data).sync();
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

}
