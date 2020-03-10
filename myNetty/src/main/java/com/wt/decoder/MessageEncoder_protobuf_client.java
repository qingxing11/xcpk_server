package com.wt.decoder;

import com.wt.cmd.Request;
import com.wt.util.io.MySerializerUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器
 * 
 * @author WangTuo
 */
public class MessageEncoder_protobuf_client extends MessageToByteEncoder<Request> {
	@Override
	protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf out) throws Exception {
		byte[] data = MySerializerUtil.serializer_protobufIOUtil(request);

		ByteBuf buf = ctx.alloc().buffer();
		buf.writeInt(data.length + 4);// 包头
		buf.writeInt(request.msgType);// 协议号
		buf.writeBytes(data);// 包体
		out.writeBytes(buf);

		if (buf.refCnt() > 0)
			buf.release();
	}
}