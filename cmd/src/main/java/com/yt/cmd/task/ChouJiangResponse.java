package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ChouJiangResponse extends Response {
	
	public static final  int   您今天已经免费抽过奖了 = 0;
	public static final  int   没找到该奖项 = 1;
	public static final  int   您当前可以免费抽奖一次 =2;
	public static final  int   您的钻石不足了 =3;
    public int pathId;
	public ChouJiangResponse(int code) {
		this.msgType = MsgTypeEnum.ChouJiangResult.getType();
		this.code=code;
	}
	public ChouJiangResponse(int code,int pathId) {
		this.msgType = MsgTypeEnum.ChouJiangResult.getType();
		this.code=code;
		this.pathId=pathId;
	}
	@Override
	public String toString() {
		return "ChouJiangResponse [pathId=" + pathId + ", msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}
