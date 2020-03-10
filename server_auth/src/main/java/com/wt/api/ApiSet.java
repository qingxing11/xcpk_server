package com.wt.api;

import com.brc.cmd.set.ChangeAccountRequest;
import com.brc.cmd.set.ChangeAccountResponse;
import com.brc.cmd.set.FindPasswordRequest;
import com.brc.cmd.set.FindPasswordResponse;
import com.brc.cmd.set.ModifyPwdRequest;
import com.brc.cmd.set.ModifyPwdResponse;
import com.brc.cmd.set.SupplementaryAccountRequest;
import com.brc.cmd.set.SupplementaryAccountResponse;
import com.brc.cmd.set.UpdatePlayerPhoneNumberRequest;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.biz.UserBiz;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.dao.impl.UserDaoImpl;
import com.wt.main.AuthServerHelper;
import com.wt.naval.dao.model.user.UserInfoModel;
import com.wt.util.Tool;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.brc.cmd.set") 
public class ApiSet
{
	boolean isDebug = true;
	
	@Protocol(msgType = MsgTypeEnum.SET_更新玩家手机号)
	public void updatePlayerPhoneNumber(ChannelHandlerContext ctx, UpdatePlayerPhoneNumberRequest request)
	{
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_更新玩家手机号,"更新手机号:"+request.phoneNumber+",userId:"+request.userId);
		UserDaoImpl.updatePhoneNumber(request.userId,request.phoneNumber);
		Response response = new Response(Response.SUCCESS);
		AuthServerHelper.sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.SET_完善账号)
	public void supplementaryAccount(ChannelHandlerContext ctx, SupplementaryAccountRequest request)
	{
		SupplementaryAccountResponse response = null;
		boolean isAccountExist = UserDaoImpl.isAccountExists(request.account);
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_完善账号,"request:"+request);
		if(isAccountExist)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_完善账号,"ERROR_账号重复");
			response = new SupplementaryAccountResponse(SupplementaryAccountResponse.ERROR_账号重复,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
			return;
		}
		
		boolean isNickNameExist = UserDaoImpl.isNickNameExists(request.nickName);
		if(isNickNameExist)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_完善账号,"ERROR_昵称重复");
			response = new SupplementaryAccountResponse(SupplementaryAccountResponse.ERROR_昵称重复,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
			return;
		}
		
		boolean isSuccess = UserDaoImpl.updateUserSupplementaryAccount(request.account,request.nickName,request.password,request.userId);
		if(!isSuccess)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_完善账号,"数据库插入失败");
			response = new SupplementaryAccountResponse(SupplementaryAccountResponse.ERROR_数据插入失败,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
			return;
		}
		
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_完善账号,"成功");
		response = new SupplementaryAccountResponse(SupplementaryAccountResponse.SUCCESS,request.account,request.nickName,request.password,request.userId);
		AuthServerHelper.sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.SET_切换账号)
	public void changeAccount(ChannelHandlerContext ctx, ChangeAccountRequest request)
	{
		ChangeAccountResponse response = null;
		
		boolean isSuccess = UserDaoImpl.checkAccount(request.account, request.password);
		if(isSuccess)
		{//检查是否有符合的账号密码
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_切换账号,"成功");
			response = new ChangeAccountResponse(ChangeAccountResponse.SUCCESS,request.userId,request.account,request.password);
		}
		else
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_切换账号,"ERROR_账号密码错误");
			response = new ChangeAccountResponse(ChangeAccountResponse.ERROR_账号密码错误,request.userId,request.account,request.password);
		}
		AuthServerHelper.sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.MODIFYPWD)
	public void modifyPwd(ChannelHandlerContext ctx, ModifyPwdRequest request)
	{
		ModifyPwdResponse response = null;
		UserInfoModel user = UserBiz.getUser(request.userId);
		if(user == null)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.MODIFYPWD,"ERROR_账号不存在");
			response = new ModifyPwdResponse(ModifyPwdResponse.ERROR_账号不存在,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
			return;
		}
		
		if(!request.oldPwd.equals(user.getPassword()))
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.MODIFYPWD,"ERROR_原始密码错误");
			response = new ModifyPwdResponse(ModifyPwdResponse.ERROR_原始密码错误,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
			return;
		}
		boolean isSuccess = UserDaoImpl.updateUserPassword(request.newPwd,request.userId);
		if(!isSuccess)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.MODIFYPWD,"ERROR_更新数据库失败");
			response = new ModifyPwdResponse(ModifyPwdResponse.ERROR_更新数据库失败,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
		}
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.MODIFYPWD,"成功");
		response = new ModifyPwdResponse(ModifyPwdResponse.SUCCESS,request.newPwd,request.userId);
		AuthServerHelper.sendResponse(ctx, response);
	}
	
	@Protocol(msgType = MsgTypeEnum.SET_找回密码)
	public void findPassword(ChannelHandlerContext ctx, FindPasswordRequest request)
	{
		FindPasswordResponse response = null;
		if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码,"request:"+request);
		boolean isSuccess  = UserDaoImpl.updateUserPassword(request.password, request.userId);
		if(isSuccess)
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码,"SUCCESS");
			response = new FindPasswordResponse(FindPasswordResponse.SUCCESS,request.password,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
		}
		else
		{
			if(isDebug)Tool.print_debug_level0(MsgTypeEnum.SET_找回密码,"ERROR_更新数据库失败");
			response = new FindPasswordResponse(FindPasswordResponse.ERROR_更新数据库失败,request.userId);
			AuthServerHelper.sendResponse(ctx, response);
		}
	}
}
