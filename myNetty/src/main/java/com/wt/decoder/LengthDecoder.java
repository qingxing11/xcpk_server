package com.wt.decoder;

import com.wt.util.log.LogUtil;
import com.wt.util.security.MySession;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * Length解码器，返回bytebuf
 * @author WangTuo
 */
public class LengthDecoder extends LengthFieldBasedFrameDecoder
{
	private int maxLengh;
	  /** 
	     * @param maxFrameLength 解码时，处理每个帧数据的最大长度 
	     * @param lengthFieldOffset 该帧数据中，存放该帧数据的长度的数据的起始位置 
	     * @param lengthFieldLength 记录该帧数据长度的字段本身的长度 
	     * @param lengthAdjustment 修改帧数据长度字段中定义的值，可以为负数 
	     * @param initialBytesToStrip 解析的时候需要跳过的字节数 
	     */  
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
		boolean isDebug = false;
		
		if (in == null)
			return null;

		Object obj = super.decode(ctx, in);
		if (obj == null)
			return null;

		ByteBuf buf = (ByteBuf) obj;
		int readableLen = buf.readableBytes(); // 可读的数据大小

		if(isDebug)
		{
			ByteBuf tempStr = buf.readSlice(buf.readableBytes());
			byte[] bb = new byte[tempStr.readableBytes()];
			tempStr.readBytes(bb);
			System.err.println("收到消息:"+new String(bb));
			buf.resetReaderIndex();
		}
		
		if (readableLen > maxLengh)
		{ // 数据异常
			logError(ctx.channel().attr(MySession.attr_session).get());
			ctx.channel().close();
			 
			return null;
		}

		Object result = null;
		
		if (readableLen >= 4 && readableLen < maxLengh)// 消息头占4个字节
		{//TODO 泄露检查
			ByteBuf frame = ctx.alloc().buffer(readableLen);
			buf.readBytes(frame);
			result = frame;
		}
		
		return result;
	}

	/**
	 * 记录错误
	 */
	private void logError(MySession con)
	{
		if (con != null)
		{
			LogUtil.print_error("data tool long Exception! playerID:" + con.getUserId());;
		}
		else
		{
			LogUtil.print_error("data tool long Exception!");
		}
	}
}
