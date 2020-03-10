package decoder;

import com.wt.cmd.Request;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义编码器 
 * @author WangTuo
 */
public class MessageEncoder_client extends MessageToByteEncoder<Request> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Request response, ByteBuf out) throws Exception
	{
		ByteBuf buf = ctx.alloc().buffer();
		buf.writeInt(response.getData().length +4);//包头
		buf.writeInt(response.msgType);//协议号
		buf.writeBytes(response.getData());//包体
 		out.writeBytes(buf);
	}
}