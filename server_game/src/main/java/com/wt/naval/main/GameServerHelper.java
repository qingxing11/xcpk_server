package com.wt.naval.main;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.wt.archive.GMMailData;
import com.wt.archive.MailData;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.cmd.err.SendMsg_PlayerNotFound;
import com.wt.cmd.err.SendMsg_SessionErr;
import com.wt.iserver.ServerHelper;
import com.wt.naval.cache.UserCache;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;
import com.wt.util.log.LogUtil;
import com.wt.util.security.MySession;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class GameServerHelper
{
	public static void sendResponse(ChannelHandlerContext ctx, Response msg)
	{
		sendResponse(ctx.channel(), msg);
	}

	public static void sendResponse(Channel ctx, Response msg)
	{
		ServerHelper.sendResponse(ctx, msg);
	}

	public static void sendResponse(int userId, Response msg)
	{
		Channel ctx = UserCache.getChannel(userId);
		if (ctx != null)
		{
			ServerHelper.sendResponse(ctx, msg);
		}
		else
		{
			Tool.print_error("以user_id获取通道时为空,user_id:" + userId);
		}
	}

	public static void wxHttpResponse(ChannelHandlerContext ctx, boolean isComplete)
	{
		String res = "<xml><return_code><![CDATA[" + (isComplete ? "SUCCESS" : "FAIL") + "]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		httpResponse(ctx, res);
	}

	public static void httpResponse(ChannelHandlerContext ctx, String res)
	{
		if (ctx == null)
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

	public static void alipayHttpResponse(ChannelHandlerContext ctx, boolean isComplete)
	{
		String res = isComplete ? "success" : "failure";
		httpResponse(ctx, res);
	}

	public static void serverPushAll(Response response)
	{
		// System.out.println("尝试向所有玩家推送,玩家数:"+UserCache.sessions.size());
		Iterator<Entry<Integer, Channel>> iter = UserCache.getAllChannel().entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry<Integer, Channel> entry = (Map.Entry<Integer, Channel>) iter.next();
			Channel channel = entry.getValue();
			if (channel.isActive())
			{
				channel.writeAndFlush(response);
			}
		}
	}

	public static void serverBroadcast(Response response)
	{
		for (Entry<Integer, Channel> entry : UserCache.getAllChannel().entrySet())
		{
			Channel channel = entry.getValue();
			GameServerHelper.sendResponse(channel, response);
		}
	}

	/**
	 * 不带附件 & 不带筛选条件(默认发送给指定玩家)
	 */
	public static void serverMail(int userId, byte type, String fromNick, String title, String content)
	{
		serverMail(GMMailData.cond_指定玩家, String.valueOf(userId), userId, type, fromNick, title, content);
	}

	/**
	 * 带附件 & 不带筛选条件(默认发送给指定玩家)
	 */
	public static void serverMail(int userId, byte type, String fromNick, String title, String content, String itemType, String itemNum)
	{
		serverMail(GMMailData.cond_指定玩家, String.valueOf(userId), userId, type, fromNick, title, content, itemType, itemNum);
	}

	/**
	 * 不带附件
	 * 
	 * @param condType
	 * @param condContent
	 * @param userId
	 * @param type
	 * @param fromNick
	 * @param title
	 * @param content
	 */
	public static void serverMail(byte condType, String condContent, int userId, byte type, String fromNick, String title, String content)
	{
		MailData mailData = new MailData();
		mailData.init(userId, type, fromNick, title, content);

		handleServerMail(condType, condContent, mailData);
	}

	/**
	 * 带附件
	 * 
	 * @param condType
	 * @param condContent
	 * @param userId
	 * @param type
	 * @param fromNick
	 * @param title
	 * @param content
	 * @param itemType
	 * @param itemNum
	 */
	public static void serverMail(byte condType, String condContent, int userId, byte type, String fromNick, String title, String content, String itemType, String itemNum)
	{
		MailData mailData = new MailData();
		mailData.init(userId, type, fromNick, title, content, itemType, itemNum);

		handleServerMail(condType, condContent, mailData);

	}

	private static void handleServerMail(byte condType, String condContent, MailData mailData)
	{
		switch (condType)
		{

			case GMMailData.cond_指定玩家:
				int toUserId = Integer.parseInt(condContent);
				serverMailToPlayer(toUserId, mailData);
				break;
		}
	}

	private static void serverMailToPlayer(int userId, MailData mailData)
	{
		Player gameData = UserCache.getPlay(userId);

		if (gameData == null)
		{

			return;
		}

	}

	public static void sendMessageByUnknownErr(ChannelHandlerContext ctx)
	{
		LogUtil.print_error("ERROR_UNKNOWN");
		// SendMsg_UnknownErr response = new SendMsg_UnknownErr();
		// sendResponse(ctx, response);
	}

	public static void sendPlayerNotFound(ChannelHandlerContext ctx)
	{
		SendMsg_PlayerNotFound response = new SendMsg_PlayerNotFound();
		sendResponse(ctx, response);
	}
	
	public static void sendPlayerNotFound(Channel ctx)
	{
		SendMsg_PlayerNotFound response = new SendMsg_PlayerNotFound();
		sendResponse(ctx, response);
	}

	public static void sendMessageBySessionErr(ChannelHandlerContext ctx)
	{
		SendMsg_SessionErr response = new SendMsg_SessionErr();
		sendResponse(ctx, response);
	}

	public Player getPlayer(MySession mySession)
	{
		if (mySession == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取Player, "获取player时session为空,未知玩家");
			return null;
		}
		Player player = UserCache.getPlay(mySession.getUserId());
		if (player == null)
		{
			sendPlayerNotFound(mySession.getChannel());
		}
		return player;
	}

	public Player getPlayer(ChannelHandlerContext ctx)
	{
		if (ctx == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取Player, "获取player时ChannelHandlerContext为空,未知玩家");
			return null;
		}
		MySession mySession = ctx.channel().attr(MySession.attr_session).get();
		if (mySession == null)
		{
			Tool.print_error(MsgTypeEnum.LOG_获取Player, "获取player时session为空,未知玩家");
			sendMessageBySessionErr(ctx);
			return null;
		}
		Player player = UserCache.getPlay(mySession.getUserId());
		if (player == null)
		{
			sendPlayerNotFound(ctx);
		}
		return player;
	}

}
