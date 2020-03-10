package com.wt.naval.api;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.mail.DeleteMailRequest;
import com.brc.cmd.mail.DeleteMailResponse;
import com.brc.cmd.mail.GetAllMailRequest;
import com.brc.cmd.mail.GetAllMailResponse;
import com.brc.cmd.mail.GetAttachRequest;
import com.brc.cmd.mail.GetAttachResponse;
import com.brc.cmd.mail.ReadMailRequest;
import com.brc.cmd.mail.ReadMailResponse;
import com.brc.naval.mail.MailService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.archive.MailDataPO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.cmd.mail") @Service public class ApiMail
{
	@Autowired
	private PlayerService playerService;
	@Autowired
	private MailService mailService;

	// 请求所有邮件
	@Protocol(msgType = MsgTypeEnum.MAIL_GETALL)
	public void getAllMail(ChannelHandlerContext ctx, GetAllMailRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return;
		}
		Tool.print_debug_level0(MsgTypeEnum.MAIL_GETALL, player.getNickName());
		GetAllMailResponse response = null;
		ArrayList<MailDataPO> userMailDataPOs = mailService.getUserMailDataPOs(session.getUserId());
		response = new GetAllMailResponse(userMailDataPOs, GetAllMailResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	// 读取一封邮件
	@Protocol(msgType = MsgTypeEnum.MAIL_READ)
	public void readMail(ChannelHandlerContext ctx, ReadMailRequest request, MySession session)
	{
		ReadMailResponse response = null;
		Player player = playerService.getPlayer(session.getUserId());

		MailDataPO mailDataPO = mailService.mailExist(request.mailId);
		if (mailDataPO == null) // 判断邮件是否存在
		{
			response = new ReadMailResponse(ReadMailResponse.ERROR_邮件不存在);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		if (mailService.isRead(mailDataPO)) // 判断邮件是否已读
		{
			response = new ReadMailResponse(ReadMailResponse.ERROR_已阅读);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		mailService.readMail(player, mailDataPO);
		response = new ReadMailResponse(request.mailId, ReadMailResponse.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
	}

	// 领取邮件
	@Protocol(msgType = MsgTypeEnum.MAIL_GETATTACH)
	public void getAttach(ChannelHandlerContext ctx, GetAttachRequest request, MySession session)
	{
		GetAttachResponse response = null;
		MailDataPO mailDataPO = mailService.mailExist(request.mailId);
		if (mailDataPO == null) // 判断邮件是否存在
		{
			response = new GetAttachResponse(GetAttachResponse.ERROR_邮件不存在);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (!mailService.isCanReceive(mailDataPO))
		{
			response = new GetAttachResponse(GetAttachResponse.ERROR_附件不存在);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}

		mailService.getAttach(mailDataPO);
		response = new GetAttachResponse(GetAttachResponse.SUCCESS, mailDataPO.getAttachContent(), request.mailId);
		GameServerHelper.sendResponse(ctx, response);
	}

	// 删除邮件
	@Protocol(msgType = MsgTypeEnum.MAIL_DELETE)
	public void deleteMail(ChannelHandlerContext ctx, DeleteMailRequest request, MySession session)
	{
		DeleteMailResponse response = null;
		MailDataPO mailDataPO = mailService.mailExist(request.mailId);
		if (mailDataPO == null) // 判断邮件是否存在
		{
			response = new DeleteMailResponse(DeleteMailResponse.ERROR_邮件不存在);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		if (mailService.isCanReceive(mailDataPO))
		{
			response = new DeleteMailResponse(DeleteMailResponse.ERROR_附件未领取);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		mailService.removeMail(request.mailId);
		response = new DeleteMailResponse(DeleteMailResponse.SUCCESS, request.mailId);
		GameServerHelper.sendResponse(ctx, response);
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
			e.printStackTrace();
		}
	}
}
