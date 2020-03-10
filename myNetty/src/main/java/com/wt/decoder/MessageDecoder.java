package com.wt.decoder;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;

/**
 * 自定义消息解码器,不限制消息长度
 * @author WangTuo
 *
 */
public class MessageDecoder extends ByteToMessageDecoder {
	private static final ByteBuf HEAD_FLAG = Unpooled.copiedBuffer("HD1.1", Charset.forName("UTF-8"));
	private static final ByteBuf TAIL_FLAG = Unpooled.copiedBuffer("TL1.1", Charset.forName("UTF-8"));

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		boolean isDebug = false;
		in.markReaderIndex();
		if(isDebug)
		{
			System.out.println("============================================");
			System.out.println("消息长度:"+in.readableBytes());
			ByteBuf tempStr = in.readSlice(in.readableBytes());
			byte[] bb = new byte[tempStr.readableBytes()];
			tempStr.readBytes(bb);
			System.out.println("str:"+new String(bb));
		}
		in.resetReaderIndex();
		
		if(in.readableBytes() < 5)
		{
			if(isDebug)
			{
				System.out.println("消息长度小于包頭,return null");
			}
			otherDecoder(ctx, in);
			return;
		}
		
//		// 查找包头位置
 		int headerOffset =indexOf(in, HEAD_FLAG);
		if (headerOffset < 0)
		{
			if(isDebug)
			{
				System.out.println("找不到包头,丢弃多余字节:");
			}
		
			// 找不到包头,丢弃多余字节
			if (in.readableBytes() > 4)
				in.skipBytes(in.readableBytes() - 4);
			
			otherDecoder(ctx, in);
			return ;
		}
		else
		{
			headerOffset += HEAD_FLAG.readableBytes();
			if(isDebug)
			{
				System.out.println("找到包头，跳到包头结束位置:"+headerOffset);
			}
			// 找到包头，跳到包头结束位置
 			in.skipBytes(headerOffset);
		}

		// 如发现剩余字节不够4位，回滚指针，等待下次解码
		int lastIndex = in.readableBytes();
		if(isDebug)
		{
			System.out.println("剩餘字節:"+lastIndex);
		}
		if (lastIndex < 4)
		{
			if(isDebug)
			{
				System.out.println("如发现剩余字节不够4位，回滚指针，等待下次解码");
			}
			in.resetReaderIndex();
			otherDecoder(ctx, in);
			return;
		}

		int bodyLength = in.readInt();
		if(isDebug)
		{
			System.out.println("读取length:"+bodyLength);
		}
		if (bodyLength < 0)
		{
			if(isDebug)
			{
				System.out.println("非法length，丢弃全部字节，等待下次解码,bodyLength:"+bodyLength);
			}
			otherDecoder(ctx, in);
			return;
		}
		
		// 计算剩余字节数(包体 +包尾)
		int len = bodyLength + 5;
		if(isDebug)
		{
			System.out.println("计算剩余字节数(包体 +包尾),如剩余字节不够，回滚指针，等待下次解码,len:"+len+",in.readableBytes():"+in.readableBytes());
		}
		// 如剩余字节不够，回滚指针，等待下次解码
		if (in.readableBytes() < len)
		{
			in.resetReaderIndex();
			if(isDebug)
			{
				System.out.println("剩余字节不够，回滚指针，等待下次解码");
			}
// 			otherDecoder(ctx, in);
			return;
		}
		
		// 获取包体
		ByteBuf frame = ctx.alloc().buffer(bodyLength);
		in.readBytes(frame);

		// 读取包尾
		ByteBuf tail = in.readBytes(5);
		// 包尾不匹配，则丢弃包体，等待下次解码
		if (!tail.equals(TAIL_FLAG))
		{
			if(isDebug)
			{
				System.out.println("包尾不匹配，则丢弃包体，等待下次解码:");
			}
			ReferenceCountUtil.release(frame);
			otherDecoder(ctx, in);
			return;
		}
		
		byte[] body = new byte[frame.readableBytes()];
		frame.readBytes(body);
		out.add(body);
		
		if(isDebug)
			System.out.println("完整读取一个数据包，剩余:"+in.readableBytes()+"字节");
		if(in.readableBytes() > 4)
		{
			// 重新读取包头
			in.markReaderIndex();
			ByteBuf head = in.readBytes(5);
			if(head.equals(HEAD_FLAG))//粘包，递归处理
			{
				if(isDebug)
				{
					System.out.println("粘包，递归处理");
				}
				in.resetReaderIndex();
				decode(ctx, in, out);
			}
		}
	}

	/**
	 * 本解析器无法解析，传给下个解析器
	 * @param ctx
	 * @param in
	 */
	private void otherDecoder(ChannelHandlerContext ctx, ByteBuf in)
	{
		in.resetReaderIndex();
 		in.retain();
 		ctx.fireChannelRead(in);
	}

	/**
	 * 判断目标ByteBuf中是否有指定内容
	 * 
	 * @param haystack
	 *            被对比方
	 * @param needle
	 *            对比内容
	 * @return
	 */
	private static int indexOf(ByteBuf haystack, ByteBuf needle) {
		int findIndex = 0;
		int lastIndex = 0;
// 		System.err.println("needle.len:"+needle.writerIndex());
		for (int needleIndex = 0; needleIndex < needle.writerIndex(); needleIndex++) {// 取出对比方的一個内容和被对比方进行对比
			int haystack_int = haystack.readableBytes();
			findIndex = haystack.indexOf(findIndex, haystack_int, needle.getByte(needleIndex));
			if(findIndex != -1 && lastIndex == 0)
			{
				lastIndex = findIndex;
			}
//			System.err.println("findIndex:"+findIndex+",haystack_int:"+haystack_int+",needleIndex:"+needleIndex+",lastIndex:"+lastIndex);
			if (findIndex < 0 || findIndex - lastIndex > 1) {
				return -1;
			}
			lastIndex = findIndex;
		}
		return findIndex - needle.writerIndex() + 1;
	}
}