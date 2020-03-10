package com.wt.naval.api;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.cmd.set.BindPhoneGetCodeRequest;
import com.brc.cmd.set.BindPhoneGetCodeResponse;
import com.brc.cmd.set.BindPhoneRequest;
import com.brc.cmd.set.BindPhoneResponse;
import com.brc.cmd.set.ChangeAccountRequest;
import com.brc.cmd.set.FindPasswordGetCodeRequest;
import com.brc.cmd.set.FindPasswordGetCodeResponse;
import com.brc.cmd.set.FindPasswordRequest;
import com.brc.cmd.set.FindPasswordResponse;
import com.brc.cmd.set.ModifyPwdRequest;
import com.brc.cmd.set.SupplementaryAccountRequest;
import com.brc.cmd.set.SupplementaryAccountResponse;
import com.brc.cmd.set.UpdatePlayerPhoneNumberRequest;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.netty.client.ClientHelper;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.account.AccountService;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.cmd.set") 
@Service
public class ApiSet 
{
	private static final boolean isDebug = true;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private ClientHelper clientHelper;
	
	@Protocol(msgType = MsgTypeEnum.SET_切换账号)
	public void changeAccount(ChannelHandlerContext ctx, ChangeAccountRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		request.userId = player.getUserId();
		clientHelper.sendRequest(request);
		
//		player.sendResponse(new Response(1000));
	}
	
	//获取验证码
	@Protocol(msgType = MsgTypeEnum.BINDPHONE_GETCODE)
	public void bindPhoneGetCode(ChannelHandlerContext ctx, BindPhoneGetCodeRequest request, MySession session)
	{
		BindPhoneGetCodeResponse response=null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.BINDPHONE_GETCODE,"获取验证码:"+player.getNickName());
		if(StringUtils.isNotEmpty(player.getPhoneNumber()))
		{
			response = new BindPhoneGetCodeResponse(BindPhoneGetCodeResponse.ERROR_已绑定手机号);
			player.sendResponse(response);
			return;
		}
		
		if(!player.isAccountsupplementary())
		{
			response = new BindPhoneGetCodeResponse(BindPhoneGetCodeResponse.ERROR_未完善账号);
			player.sendResponse(response);
			return;
		}
	
		int code = accountService.getBindPhoneCode(player.getUserId(),request.phoneNumber);
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.BINDPHONE_GETCODE,"获取验证码,code:"+code);
		if(code > 0)
		{
			player.setPhoneNumber(request.phoneNumber);
			response = new BindPhoneGetCodeResponse(BindPhoneGetCodeResponse.SUCCESS);
		}
		else
		{
			response = new BindPhoneGetCodeResponse(BindPhoneGetCodeResponse.ERROR_获取验证码失败);
		}
		player.sendResponse(response);
	}
	
	//绑定手机
	@Protocol(msgType = MsgTypeEnum.BINDPHONE)
	public void bindPhone(ChannelHandlerContext ctx, BindPhoneRequest request, MySession session)
	{
		BindPhoneResponse response=null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		boolean isSuccess = accountService.validateBindPhoneCode(player.getUserId(), request.codeNum);
		if(!isSuccess)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.BINDPHONE,"ERROR_验证码错误");
			response = new BindPhoneResponse(BindPhoneResponse.ERROR_验证码错误);
			return;
		}
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.BINDPHONE,"绑定手机成功,request.codeNum:"+request.codeNum);
		
		UpdatePlayerPhoneNumberRequest playerPhoneNumber = new UpdatePlayerPhoneNumberRequest(player.getUserId(),player.getPhoneNumber());
		clientHelper.sendRequest(playerPhoneNumber);
		
		response = new BindPhoneResponse(BindPhoneResponse.SUCCESS);
		player.sendResponse(response);
	}
	
	//修改密码
	@Protocol(msgType = MsgTypeEnum.MODIFYPWD)
	public void modifyPwd(ChannelHandlerContext ctx, ModifyPwdRequest request, MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.MODIFYPWD,"reuqest:"+request);
		
		request.userId = player.getUserId();
		clientHelper.sendRequest(request);
	}
	
	@Protocol(msgType = MsgTypeEnum.SET_完善账号)
	public void supplementaryAccount(ChannelHandlerContext ctx, SupplementaryAccountRequest request, MySession session)
	{
		SupplementaryAccountResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		if(isDebug || true)Tool.print_debug_level0(MsgTypeEnum.SET_完善账号,"reuqest:"+request+",是否已经完善:"+player.isAccountsupplementary());
		
		if(player.isAccountsupplementary())
		{
			response = new SupplementaryAccountResponse(SupplementaryAccountResponse.ERROR_已经是完善账号);
			player.sendResponse(response);
			return;
		}
		
		request.userId = player.getUserId();
		clientHelper.sendRequest(request);
	}
	
	
	@Protocol(msgType = MsgTypeEnum.SET_找回密码)
	public void findPassword(ChannelHandlerContext ctx, FindPasswordRequest request, MySession session)
	{
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码,"request:"+request);
		FindPasswordResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		
		boolean isSuccess = accountService.validateFindPasswordCode(player.getUserId(), request.code);
		if(!isSuccess)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码,"ERROR_验证码错误");
			response = new FindPasswordResponse(FindPasswordResponse.ERROR_验证码错误);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		request.userId = player.getUserId();
		clientHelper.sendRequest(request);
	}
	
	@Protocol(msgType = MsgTypeEnum.SET_找回密码获取验证码)
	public void findPasswordGetCode(ChannelHandlerContext ctx, FindPasswordGetCodeRequest request, MySession session)
	{
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码获取验证码,"reuqest:"+request);
		FindPasswordGetCodeResponse response = null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		
		if(!StringUtils.isNotEmpty(player.getPhoneNumber()))
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码获取验证码,"ERROR_还未绑定手机");
			response = new FindPasswordGetCodeResponse(FindPasswordGetCodeResponse.ERROR_还未绑定手机);
			player.sendResponse(response);
			return;
		}
		
		int code = accountService.getFindPasswordCode(player.getUserId(), player.getPhoneNumber());
		if(code > 0)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码获取验证码,"成功，验证码:"+code);
			response = new FindPasswordGetCodeResponse(FindPasswordGetCodeResponse.SUCCESS);
			GameServerHelper.sendResponse(ctx, response);
		}
		else
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码获取验证码,"获取验证码失败");
			response = new FindPasswordGetCodeResponse(FindPasswordGetCodeResponse.ERROR_获取验证码失败);
			GameServerHelper.sendResponse(ctx, response);
		}
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
