package com.wt.api;

import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.biz.ResBiz;
import com.wt.cache.ResCache;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.res.GetResRequest;
import com.wt.cmd.res.GetResResponse;
import com.wt.main.AuthServerHelper;
import com.wt.naval.vo.res.HotfixUpdateFileVO;
import com.wt.util.Tool;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.res") public class ApiRes
{
	@Protocol(msgType = MsgTypeEnum.UTIL_GET_NEW_FILE_URL)
	public static void getRes(ChannelHandlerContext ctx, Request obj)
	{
		GetResRequest request = (GetResRequest) obj;
		GetResResponse response = null;

		Tool.print_debug_level0("请求更新资源文件,平台:" + request.clientPlatform + ",版本:" + request.clientVersion);

		if (request.clientPlatform < 0 || request.clientPlatform > 2)
		{
			response = new GetResResponse(GetResResponse.ERROR_平台非法);
			AuthServerHelper.sendResponse(ctx, response);
		}

		if (!ResBiz.checkVersion(ctx, request))
		{
			return;
		}

		HotfixUpdateFileVO updateFileVO = ResCache.getUpdateFile(request.clientVersion, request.clientPlatform);

		Tool.print_debug_level0("获取升级文件成功:" + updateFileVO);
		response = new GetResResponse(GetResResponse.SUCCESS, updateFileVO);
		AuthServerHelper.sendResponse(ctx, response, true);
	}
}
