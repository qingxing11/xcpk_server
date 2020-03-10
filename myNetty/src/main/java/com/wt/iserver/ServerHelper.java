package com.wt.iserver;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wt.cmd.Response;
import com.wt.config.Config;
import com.wt.tool.ClassTool;
import com.wt.tool.PrintMsgTool;
import com.wt.util.Tool;
import com.wt.util.io.MySerializerUtil;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class ServerHelper
{
	/**
	 * 发信息
	 * @param ctx
	 * @param msg
	 */
	public static void sendResponse(ChannelHandlerContext ctx, Response msg)
	{
		sendResponse(ctx.channel(), msg, false);
	}
	
	/**
	 * 发信息
	 * @param ctx
	 * @param msg
	 * @param shortLink 是否为短链接
	 */
	public static void sendResponse(ChannelHandlerContext ctx, Response msg,boolean shortLink)
	{
		sendResponse(ctx.channel(), msg, shortLink);
	}

	public static void sendResponse(Channel ctx, Response msg)
	{
		sendResponse(ctx, msg, false);
	}

	/**
	 * 发送消息
	 * @param ctx
	 * @param msg
	 * @param shortLink
	 */
	public static void sendResponse(Channel ctx, Response msg, boolean shortLink)
	{
		digestAndWriteResponse(ctx, msg, shortLink);
	}
	
	/**
	 * 最终整理并且将消息交由消息发送编码器处理
	 * <br>线程安全
	 * @param ctx
	 * @param msg
	 * @param shortLink
	 */
	private static void digestAndWriteResponse(Channel ctx,Response msg,boolean shortLink)
	{
		if (ctx != null && ctx.isActive())
		{
			switch (Config.instance().serializerUtil)
			{
				case 1:
					if(Config.instance().responseLog)
					{
//						long useTime = PerformanceTool.getLastHandlerUseTime(ctx);
//		 				System.out.println("指令耗时:["+(useTime < 0 ? "首次调用,初始化session]" : useTime +"]ms"));
						if(!PrintMsgTool.isFilterMsgType(msg.msgType))
						{
							Tool.print_debug_level0("发送消息:" + msg);
						}
					}
					
					byte[] data = MySerializerUtil.serializer_protobufIOUtil(msg);
					msg.setData(data);
					break;
					
				case 2:
					if(Config.instance().responseLog)
					{
						if(!PrintMsgTool.isFilterMsgType(msg.msgType))
						{
							Tool.print_debug_level0("发送消息:" + JSONObject.toJSONString(msg,SerializerFeature.DisableCircularReferenceDetect));
						}
					}
					break;

				default:
					break;
			}
						
			ctx.writeAndFlush(msg);
			if (shortLink)
			{
				ctx.close();// 此指令为短链接
			}
		}
	}

	private static boolean isFilterMsg(int msgType)
	{
		return false;
	}

	public static void httpResponse(ChannelHandlerContext ctx, String res)
	{
		if(ctx == null)
			return;
		
		FullHttpResponse response = null;
		try
		{
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		response.headers().set("Access-Control-Allow-Origin", "*");
		response.headers().set("CONTENT_TYPE", "text/plain");
		response.headers().set("CONTENT_LENGTH", response.content().readableBytes());

		ctx.writeAndFlush(response);
		ctx.close();
	}

	public static void arrayToRequest_protobuf(ChannelHandlerContext ctx, int msgType, byte[] data)
	{
	}

//	public static void arrayToRequest_protobufNet(ChannelHandlerContext ctx, int msgType, byte[] data) 
//	{
//		ApiRegisterTool.handlerReuqest(ctx,msgType, data);
//	}

	public static void arrayToRequest_fastJson(ChannelHandlerContext ctx,int msgType,String str)
	{
		
	}
	
	public static void arrayToRequest(ChannelHandlerContext ctx,int msgType,byte[] data,int util)
	{
		ClassTool.handlerReuqest(ctx,msgType, data,util);
	}
}