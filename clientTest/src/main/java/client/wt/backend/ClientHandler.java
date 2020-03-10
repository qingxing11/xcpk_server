package client.wt.backend;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter
{
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
			
			//TODO 处理收到的消息
			BackendClientHandler.bufToRequest(msgType,data);
		}
	}

	// 错误打印
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		ctx.close();
	}

}
