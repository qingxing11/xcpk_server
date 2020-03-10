package com.wt.decoder;

import com.wt.cmd.Response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器
 * 
 * @author WangTuo
 */
public class MessageEncoder_protobuf extends MessageToByteEncoder<Response>
{
	@Override
	protected void encode(ChannelHandlerContext ctx, Response response, ByteBuf out) throws Exception
	{
		byte[] data = response.getData();
		
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeInt(data.length + 4);// 包头
		buf.writeInt(response.msgType);// 协议号
		buf.writeBytes(data);// 包体
		out.writeBytes(buf);
		
		if(buf.refCnt() > 0)
			buf.release();
	}
}