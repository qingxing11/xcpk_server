package com.wt.iserver;

import io.netty.channel.ChannelHandlerContext;

/**
 * 服务器事件,启动一个服务器时必须实现该接口
 * @author WangTuo
 *
 */
public interface IServer
{
	/**
	 * 服务器优雅退出
	 */
	void shutDown();
	
	/**
	 * 玩家正常退出
	 * @param ctx
	 */
	void channelInactive(ChannelHandlerContext ctx);
	
	/**
	 * 心跳事件
	 * @param ctx
	 * @param msg
	 */
	void userEventTriggered(ChannelHandlerContext ctx, Object msg);
	
	/**
	 * 事件处理方法由一个ExceptionEvent异常事件调用， 这个异常事件起因于Netty的I/O异常或一个处理器实现的内部异常。
	 * 多数情况下，捕捉到的异常应当被记录下来，并在这个方法中关闭这个channel通道。
	 * 当然处理这种异常情况的方法实现可能因你的实际需求而有所不同， 例如，在关闭这个连接之前你可能会发送一个包含了错误码的响应消息。
	 */
	void exceptionCaught(ChannelHandlerContext ctx, Throwable cause);
}
