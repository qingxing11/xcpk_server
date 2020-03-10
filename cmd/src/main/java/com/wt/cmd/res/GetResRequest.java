package com.wt.cmd.res;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetResRequest extends Request
{
	public int clientVersion;
	
	/**
	 * 0:android
	 * 1:ios
	 * 2:pc
	 */
	public int clientPlatform;
	
	public int bigVersion;
	public GetResRequest()
	{
		msgType = MsgType.UTIL_GET_NEW_FILE_URL;
	}
	
	public GetResRequest(int clientVersion,int clientPlatform,int bigVersion)
	{
		msgType = MsgType.UTIL_GET_NEW_FILE_URL;
		this.clientVersion = clientVersion;
		this.clientPlatform = clientPlatform;
		this.bigVersion = bigVersion;
	}
}
