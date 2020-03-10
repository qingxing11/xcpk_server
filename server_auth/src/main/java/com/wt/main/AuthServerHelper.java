package com.wt.main;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.wt.cmd.Response;
import com.wt.iserver.ServerHelper;
import com.wt.util.Tool;
import com.wt.util.log.LogUtil;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class AuthServerHelper
{
	public static void sendResponse(ChannelHandlerContext ctx, Response msg,boolean shortLink)
	{
		sendResponse(ctx.channel(), msg,shortLink);
	}
	
	public static void sendResponse(ChannelHandlerContext ctx, Response msg)
	{
		sendResponse(ctx.channel(), msg,false);
	}
	
	public static void sendResponse(Channel ctx, Response msg)
	{
		ServerHelper.sendResponse(ctx, msg);
	}
	
	public static void sendResponse(Channel ctx, Response msg,boolean shortLink)
	{
		ServerHelper.sendResponse(ctx, msg,shortLink);
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
		response.headers().set("Access-Control-Allow-Origin", "*"); // 跨域
		response.headers().set("CONTENT_TYPE", "text/plain");
		response.headers().set("CONTENT_LENGTH", response.content().readableBytes());

		ctx.write(response);
		ctx.flush();
		ctx.close();
	}

	public static void sendMessageByUnknownErr(ChannelHandlerContext ctx)
	{
		LogUtil.print_error("ERROR_UNKNOWN");
		//SendMsg_UnknownErr response = new SendMsg_UnknownErr();
		//sendResponse(ctx, response);
	}
	
	public static void sendMessageBySessionErr(ChannelHandlerContext ctx)
	{
		LogUtil.print_error("验权错误");
		//SendMsg_SessionErr response = new SendMsg_SessionErr();
		//sendResponse(ctx, response);
	}

	public static String httpGet(String url)
	{
		CloseableHttpClient httpCilent = HttpClients.createDefault(); 
		HttpGet httpGet = new HttpGet(url);
		try
		{
			HttpResponse response = httpCilent.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			
			if(statusCode == HttpStatus.SC_OK)
			{
				return EntityUtils.toString(response.getEntity());
			}
			Tool.print_error("http请求时出错,code:"+statusCode);
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			Tool.print_error("http请求时出错!",e);
			return null;
		}
		finally
		{
			try
			{
				httpCilent.close();// 释放资源
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
