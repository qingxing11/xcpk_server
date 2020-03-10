package com.wt.cmd.res;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.naval.vo.res.HotfixUpdateFileVO;

public class GetResResponse extends Response
{
	public static final int ERROR_平台非法 = 0;
	public static final int ERROR_版本非法 = 1;
	public static final int ERROR_不需要更新 = 2;
	public static final int SUCCESS_需要大版本更新 = 3;
	
	public HotfixUpdateFileVO updateFileVO;

	public String install_pack_url;
	public int bigVersion;
	public GetResResponse()
	{
		msgType = MsgType.UTIL_GET_NEW_FILE_URL;
	}
	
	public GetResResponse(int code)
	{
		this.code = code;
		msgType = MsgType.UTIL_GET_NEW_FILE_URL;
	}
	
	public GetResResponse(int code,String install_pack_url,int bigVersion)
	{
		this.code = code;
		msgType = MsgType.UTIL_GET_NEW_FILE_URL;
		this.install_pack_url = install_pack_url;
		this.bigVersion = bigVersion;
	}
	
	public GetResResponse(int code,HotfixUpdateFileVO updateFileVO)
	{
		this.code = code;
		msgType = MsgType.UTIL_GET_NEW_FILE_URL;
		this.updateFileVO = updateFileVO;
	}
}
