package com.wt.biz;

import com.wt.cache.ResCache;
import com.wt.cmd.res.GetResRequest;
import com.wt.cmd.res.GetResResponse;
import com.wt.dao.impl.ResImpl;
import com.wt.def.ResDef;
import com.wt.main.AuthServerHelper;
import com.wt.model.HotfixModel;
import com.wt.naval.vo.res.HotfixUpdateFileVO;

import io.netty.channel.ChannelHandlerContext;

public class ResBiz
{
	public static HotfixModel getHotfixModel_android()
	{
		return ResImpl.getHotfixModel_andorid();
	}
	
	public static HotfixModel getHotfixModel_ios()
	{
		return ResImpl.getHotfixModel_ios();
	}
	
	public static HotfixModel getHotfixModel_windows()
	{
		return ResImpl.getHotfixModel_windows();
	}
	
	public static boolean checkVersion(ChannelHandlerContext ctx, GetResRequest request)
	{
		GetResResponse response = null;
		switch (request.clientPlatform)
		{
			case ResCache.PLATFORM_ANDROID:
				if(request.bigVersion < ResDef.getResDef().hotfixModel_android.big_version)
				{
					response = new GetResResponse(GetResResponse.SUCCESS_需要大版本更新,ResDef.getResDef().hotfixModel_android.install_pack_url,ResDef.getResDef().hotfixModel_android.big_version);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				
				if(request.clientVersion < ResDef.getResDef().hotfixModel_android.start_version || request.clientVersion > ResDef.getResDef().hotfixModel_android.now_version)
				{
					response = new GetResResponse(GetResResponse.ERROR_版本非法);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				else if(request.clientVersion == ResDef.getResDef().hotfixModel_android.now_version)
				{
					HotfixUpdateFileVO updateFileVO = new HotfixUpdateFileVO();
					updateFileVO.gateway_host = ResDef.getResDef().hotfixModel_android.gateway_host;
					updateFileVO.gateway_port = ResDef.getResDef().hotfixModel_android.gateway_port;
					
					response = new GetResResponse(GetResResponse.ERROR_不需要更新,updateFileVO);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				break;

			case ResCache.PLATFORM_IOS:
				if(request.bigVersion < ResDef.getResDef().hotfixModel_ios.big_version)
				{
					response = new GetResResponse(GetResResponse.SUCCESS_需要大版本更新,ResDef.getResDef().hotfixModel_ios.install_pack_url,ResDef.getResDef().hotfixModel_ios.big_version);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				
				if(request.clientVersion < ResDef.getResDef().hotfixModel_ios.start_version || request.clientVersion > ResDef.getResDef().hotfixModel_ios.now_version)
				{
					response = new GetResResponse(GetResResponse.ERROR_版本非法);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				else if(request.clientVersion == ResDef.getResDef().hotfixModel_ios.now_version)
				{
					HotfixUpdateFileVO updateFileVO = new HotfixUpdateFileVO();
					updateFileVO.gateway_host = ResDef.getResDef().hotfixModel_android.gateway_host;
					updateFileVO.gateway_port = ResDef.getResDef().hotfixModel_android.gateway_port;
					response = new GetResResponse(GetResResponse.ERROR_不需要更新,updateFileVO);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				break;
				
			case ResCache.PLATFORM_WINDOWS:
				if(request.bigVersion < ResDef.getResDef().hotfixModel_windows.big_version)
				{
					response = new GetResResponse(GetResResponse.SUCCESS_需要大版本更新,ResDef.getResDef().hotfixModel_windows.install_pack_url,ResDef.getResDef().hotfixModel_windows.big_version);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				
				if(request.clientVersion < ResDef.getResDef().hotfixModel_windows.start_version || request.clientVersion > ResDef.getResDef().hotfixModel_windows.now_version)
				{
					response = new GetResResponse(GetResResponse.ERROR_版本非法);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				else if(request.clientVersion == ResDef.getResDef().hotfixModel_windows.now_version)
				{
					HotfixUpdateFileVO updateFileVO = new HotfixUpdateFileVO();
					updateFileVO.gateway_host = ResDef.getResDef().hotfixModel_android.gateway_host;
					updateFileVO.gateway_port = ResDef.getResDef().hotfixModel_android.gateway_port;
					response = new GetResResponse(GetResResponse.ERROR_不需要更新,updateFileVO);
					AuthServerHelper.sendResponse(ctx, response);
					return false;
				}
				break;
				
			default:
				return false;
		}
		return true;
	}
}