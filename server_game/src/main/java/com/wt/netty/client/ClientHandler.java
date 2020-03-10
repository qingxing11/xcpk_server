package com.wt.netty.client;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Service
public class ClientHandler extends ChannelInboundHandlerAdapter
{
	@Autowired
	private GlobalMsgHandler gmh;
	
	private static GlobalMsgHandler globalMsgHandler;
	
	@PostConstruct
	private void init()
	{
		globalMsgHandler = gmh;
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof ByteBuf)
		{
			ByteBuf buf = (ByteBuf)msg;
			int msgType = buf.readInt();
			byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);
			
			//处理收到的消息
			globalMsgHandler.bufToRequest(msgType,data);
		}
	}

	// 错误打印
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		ctx.close();
	}
}
