package com.wt.client;

import com.wt.main.MjFrame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class GameClientHandler extends ChannelInboundHandlerAdapter
{

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelInactive(ctx);
	}
	

	// 处理器消息接收以及派发方法（本实例中消息接收均为byte[]，均派发到ClientHelper中）
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		if (msg instanceof ByteBuf)
		{
			ByteBuf buf = (ByteBuf)msg;
			int msgType = buf.readInt();
			byte[] data = new byte[buf.readableBytes()];
			buf.readBytes(data);
			System.err.println("msgType:"+msgType);
			
			MjFrame.response();
		}
	}

	// 错误打印
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		ctx.close();
	}

}
