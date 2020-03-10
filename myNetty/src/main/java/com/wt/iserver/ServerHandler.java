package com.wt.iserver;

import java.io.UnsupportedEncodingException;

import com.wt.config.Config;
import com.wt.util.MyTimeUtil;
import com.wt.util.security.MySession;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	private IServer iServer;

	public ServerHandler(IServer iServer) {
		this.iServer = iServer;
	}

	/**
	 * 读取解码器传递出来的数据
	 */
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		sessionEvent(ctx);
		if (msg != null) {
			if (msg instanceof ByteBuf) {
				handlerBytebufRequest(ctx, (ByteBuf) msg);
			}
		}
	}

	private boolean sessionEvent(final ChannelHandlerContext ctx) {
		MySession session = ctx.channel().attr(MySession.attr_session).get();
		if (session != null) {
			session.setStartTime(MyTimeUtil.getCurrTimeMM());

			session.clearIdleNum();
			return true;
		}
		return false;
	}

	private void handlerBytebufRequest(ChannelHandlerContext ctx, ByteBuf buf) throws UnsupportedEncodingException {
		int msgType = buf.readInt();
		byte[] data = new byte[buf.readableBytes()];
		buf.readBytes(data);

		ServerHelper.arrayToRequest(ctx, msgType, data, Config.instance().serializerUtil);

		if (buf.refCnt() > 0)
			buf.release();
	}

	/**
	 * 事件处理方法由一个ExceptionEvent异常事件调用， 这个异常事件起因于Netty的I/O异常或一个处理器实现的内部异常。
	 * 多数情况下，捕捉到的异常应当被记录下来，并在这个方法中关闭这个channel通道。 当然处理这种异常情况的方法实现可能因你的实际需求而有所不同，
	 * 例如，在关闭这个连接之前你可能会发送一个包含了错误码的响应消息。
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		iServer.exceptionCaught(ctx, cause);
//		Channel incoming = ctx.channel();
//		if (!incoming.isActive())
//			System.out.println("SimpleClient:" + incoming.remoteAddress());
//
//		cause.printStackTrace();
		ctx.close();
	}

	public void channelInactive(ChannelHandlerContext ctx) {
		iServer.channelInactive(ctx);
	}

	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	/**
	 * 心跳处理
	 */
	public void userEventTriggered(ChannelHandlerContext ctx, Object msg) throws Exception {
		iServer.userEventTriggered(ctx, msg);
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("活动通道,id:" + ctx.channel().remoteAddress().toString());
		super.channelActive(ctx);
	}
}
