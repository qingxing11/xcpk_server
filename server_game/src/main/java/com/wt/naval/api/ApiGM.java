package com.wt.naval.api;

import java.io.File;

import javax.annotation.PostConstruct;

import com.wt.annotation.api.Protocol;
import com.wt.archive.GMMailData;
import com.wt.archive.MailData;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.Response;
import com.wt.cmd.backend.ServerShutDownRespone;
import com.wt.cmd.gm.GMNoticeRequest;
import com.wt.cmd.gm.GMNoticeRespone;
import com.wt.cmd.gm.GMSendMailRequest;
import com.wt.cmd.gm.GMSendMailResponse;
import com.wt.cmd.gm.GMUtilLoginRespone;
import com.wt.cmd.gm.GetLogListRespone;
import com.wt.cmd.gm.GetServerStatusRequest;
import com.wt.cmd.notice.NoticePush_serverNotice;
import com.wt.naval.biz.GMBiz;
import com.wt.naval.cache.ServerCache;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.timer.servertask.AdminInputTask;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;

import io.netty.channel.ChannelHandlerContext;

//@RegisterApi(packagePath = "com.wt.cmd.gm") 

public class ApiGM
{
	@Protocol(msgType = MsgTypeEnum.GM_服务器状态信息) // 指定本消息对应的协议号
	public static void getServerStatus(ChannelHandlerContext ctx, Request obj)
	{
		GetServerStatusRequest request = (GetServerStatusRequest) obj;
		getServerStatus(ctx, request.callBackId);
	}

	private static void getServerStatus(ChannelHandlerContext ctx, int callBackId)
	{

	}
	
	@Protocol(msgType = MsgTypeEnum.NOTICE_服务器公告)
	public static void gMNotice(ChannelHandlerContext ctx,Request obj) {
		GMNoticeRequest request = (GMNoticeRequest) obj;
		Tool.print_debug_level1(MsgType.NOTICE_服务器公告, request.txt);
		GMNoticeRespone response = new GMNoticeRespone(GMNoticeRespone.SUCCESS);

		NoticePush_serverNotice serverPushNoticeResponse = new NoticePush_serverNotice(request.txt);
		GameServerHelper.serverPushAll(serverPushNoticeResponse);

		GameServerHelper.sendResponse(ctx, response);

	}

	@Protocol(msgType = MsgTypeEnum.GM_服务器关机) // 指定本消息对应的协议号
	public static void serverShutDown(ChannelHandlerContext ctx, int callbackId, int time)
	{
		ServerShutDownRespone response = new ServerShutDownRespone(ServerShutDownRespone.SUCCESS);

//		AdminInputTask.startCmd_shutDown(time);

		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.GM_获取日志列表) // 指定本消息对应的协议号
	public static void getLogList(ChannelHandlerContext ctx, int callbackId)
	{
		try
		{
			File file = new File(".\\logs\\");
			String[] path_str = file.list();

			GetLogListRespone response = new GetLogListRespone(GetLogListRespone.SUCCESS, path_str);

			GameServerHelper.sendResponse(ctx, response);
		}
		catch (Exception e)
		{
			GetLogListRespone response = new GetLogListRespone(GetLogListRespone.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
		}
	}

	@Protocol(msgType = MsgTypeEnum.GM_GM工具登录) // 指定本消息对应的协议号
	public static void gmLogin(ChannelHandlerContext ctx, int callbackId, String phone)
	{
		GMUtilLoginRespone response = null;

		System.out.println("phone:" + phone);
		if (!GMBiz.isAdminPhone(phone))
		{
			Tool.print_debug_level2(phone, MsgType.GM_GM工具登录, "不允许的手机号尝试GM工具登录:" + phone);

			response = new GMUtilLoginRespone(GMUtilLoginRespone.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		Tool.print_debug_level2(phone, MsgType.GM_GM工具登录, "登录成功:" + phone);
		response = new GMUtilLoginRespone(GMUtilLoginRespone.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	@Protocol(msgType = MsgTypeEnum.GM_获取指定日志) // 指定本消息对应的协议号
	public static void getLog(ChannelHandlerContext ctx, int callbackId, String logName)
	{
//		String phone = UserCache.getGMPhone(ctx.channel());
//		GetLogRespone response = null;
//		if (phone == null)
//		{
//			Tool.print_error(MsgType.GM_获取指定日志, "ERROR_未登录的号码请求日志:" + logName);
//
//			response = new GetLogRespone(GetLogRespone.ERROR_未登录的号码);
//			GameServerHelper.sendResponse(ctx, response);
//			return;
//		}
//
//		File file = new File(".\\logs\\" + logName);
//		StringBuffer stringBuffer = GMBiz.getLogFile(file);
//		if (stringBuffer == null)
//		{
//			Tool.print_error(phone, MsgType.GM_获取指定日志, "获取日志错误:" + file);
//
//			response = new GetLogRespone(GetLogRespone.ERROR_UNKNOWN);
//		}
//		else
//		{
//			Tool.print_debug_level0(phone, MsgType.GM_获取指定日志, "获取日志成功:" + logName);
//			response = new GetLogRespone(GetLogRespone.SUCCESS, stringBuffer);
//		}
//		GameServerHelper.sendResponse(ctx, response);
	}

	public static void reloadConfig(ChannelHandlerContext ctx)
	{
		AdminInputTask.startCmd_reloadServerData();

		GameServerHelper.httpResponse(ctx, "reloadConfig_SUCCESS");
	}

	public static void reStart(ChannelHandlerContext ctx, Request obj)
	{
		reStart(ctx);
	}

	private static void reStart(ChannelHandlerContext ctx)
	{
		Tool.print_debug_level0(MsgType.GM_重启, "GM重启游戏");
		Response response = new Response();
		response.msgType = MsgType.GM_重启;
		response.code = Response.SUCCESS;

		UserCache.offlineAll();

		GameServerHelper.sendResponse(ctx, response);
	}


	@Protocol(msgType = MsgTypeEnum.GM_发送邮件) // 指定本消息对应的协议号
	public static void GMSendMail(ChannelHandlerContext ctx, Request obj)
	{
		Tool.print_debug_level0(MsgType.GM_发送邮件, "GM发送邮件");

		GMSendMailRequest request = (GMSendMailRequest) obj;
		gmSendMail(request.mailData);

		// TODO 错误检查
		GMSendMailResponse response = new GMSendMailResponse(Response.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);

	}

	private static void gmSendMail(GMMailData mailData)
	{
		switch (mailData.attachmentType)
		{
			case MailData.ATTACHMENT_带附件:
				GameServerHelper.serverMail(mailData.condType, mailData.condContent, mailData.toUserId, mailData.type, mailData.fromNick, mailData.title, mailData.content, mailData.itemType, mailData.itemNum);
				break;
			case MailData.ATTACHMENT_不带附件:
				GameServerHelper.serverMail(mailData.condType, mailData.condContent, mailData.toUserId, mailData.type, mailData.fromNick, mailData.title, mailData.content);
				break;
		}

		switch (mailData.condType)
		{
			case GMMailData.cond_全服玩家:
				break;
			case GMMailData.cond_指定玩家:
				// 指定玩家的邮件,发过之后就无效了
				mailData.status = GMMailData.status_无效;
				break;
		}

		ServerCache.addServerMail(mailData);
	}
	
	@PostConstruct
	private void init()
	{
		try
		{
			ClassTool.registerApi(this);
		}
		catch (Exception e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
