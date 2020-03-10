package decoder;

import com.mysql.cj.api.Session;
import com.wt.util.LogUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Length解码器，返回bytebuf
 * @author WangTuo
 */
public class LengthDecoder extends LengthFieldBasedFrameDecoder
{
	private int maxLengh;
	public LengthDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip)
	{
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
		this.maxLengh = maxFrameLength;
	}

	@Override
	protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length)
	{
		return buffer.slice(index, length);
	}


	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
	{
		if (in == null)
			return null;

		Object obj = super.decode(ctx, in);
		if (obj == null)
			return null;

		ByteBuf buf = (ByteBuf) obj;
		int readableLen = buf.readableBytes(); // 可读的数据大小

		if (readableLen > maxLengh)
		{ // 数据异常
// 			logError(ctx.attr(key).get)
			ctx.channel().close();
			return null;
		}

		Object result = null;
		if (readableLen >= 4 && readableLen < maxLengh)// 消息头占4个字节
		{
			ByteBuf frame = ctx.alloc().buffer(readableLen);
			buf.readBytes(frame);
			result = frame;
		}
		return result;
	}

	/**
	 * 记录错误
	 */
	private void logError(Session con)
	{
	}
}
