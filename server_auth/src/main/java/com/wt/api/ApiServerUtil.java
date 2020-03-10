package com.wt.api;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.serverutil.GetServerListResponse;
import com.wt.main.AuthServerHelper;
import com.wt.naval.vo.ServerInfoVO;
import com.wt.server.config.MyConfig;
import com.wt.util.Tool;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.serverutil") 
public class ApiServerUtil
{
	@Protocol(msgType = MsgTypeEnum.UTIL_SERVER_LIST)
	public static void getServerList(ChannelHandlerContext ctx, Request obj)
	{
		String serverListStr = AuthServerHelper.httpGet(MyConfig.instance().centerEureka_host + "getServerList");
		Tool.print_debug_level0(MsgType.UTIL_SERVER_LIST,serverListStr);
		
		ArrayList<ServerInfoVO>  list_serverList =  (ArrayList<ServerInfoVO>) JSON.parseArray(serverListStr,ServerInfoVO.class);
		
		GetServerListResponse response = new GetServerListResponse(GetServerListResponse.SUCCESS, list_serverList);
		AuthServerHelper.sendResponse(ctx, response);
	}
}