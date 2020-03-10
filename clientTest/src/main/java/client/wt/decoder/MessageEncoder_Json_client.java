package client.wt.decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wt.cmd.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器 
 * @author WangTuo
 */
public class MessageEncoder_Json_client extends MessageToByteEncoder<Request> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Request response, ByteBuf out) throws Exception
	{
		 byte[] data = JSON.toJSONBytes(response,SerializerFeature.DisableCircularReferenceDetect);
			
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeInt(data.length + 4);// 包头
		buf.writeInt(response.msgType);// 协议号
		buf.writeBytes(data);// 包体
		out.writeBytes(buf);
		
		if(buf.refCnt() > 0)
		{
			buf.release();
		}
	}
}