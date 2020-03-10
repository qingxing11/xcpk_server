package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetServerStatusRespone extends Response
{
	public int online_player_num;
	public int all_player_num;
	public int useMemory;
	public int maxMemory;
	
	public float[] cpuUse;
	
	public GetServerStatusRespone(){}
	
	public GetServerStatusRespone(int success, int callBackId) {
		this.msgType = MsgType.GM_服务器状态信息;
		this.callBackId = callBackId;
	}
}
