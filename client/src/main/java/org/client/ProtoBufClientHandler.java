package org.client;

import com.wt.proto.Test1.Test;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProtoBufClientHandler extends ChannelInboundHandlerAdapter
{

	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
//		Test.Builder test = Test.newBuilder();
//		test.setMgsType("asdjaslkdjl;aksdjl;sakjd;laskjd;slakdja;lskdjaiwojdoiwajdowaijdasdjaslkdjl;aksdjl;sakjd;laskjd;slakdja;lskdjaiwojdoiwajdowai123456789");
//		ctx.writeAndFlush(test.build());
		System.out.println("新活动通道:"+ctx.channel().remoteAddress());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		System.out.println("channelRead");
		super.channelRead(ctx, msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
	{
		System.out.println("ProtoBufClientHandler.channelReadComplete()");
		super.channelReadComplete(ctx);
	}
}
