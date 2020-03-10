package com.wt.cmd;

import com.wt.util.security.MySecurityUtil;

/**
 * 回应
 */
public class Response implements MsgType{
	// 统一返回码类型
	public static final int SUCCESS = 1000; // 成功
	public static final int FAILED = 1001; // 失败
	public static final int ERROR_资源不足 = 1002; // 玩家金币不够
	public static final int ERROR_NO_CREDITS = 1003; // 玩家点券不够
	public static final int ERROR_NO_LOVE_PT = 1004; // 玩家友情值不够
	public static final int ERROR_OUT_OF_LIMIT = 1005; // 超越了上限值
	public static final int ERROR_CANT_UPDATE_CURRENCY = 1006; // 无法扣除游戏币
	public static final int ERROR_WRONG_PARAM = 1007; // 玩家参数有误
	public static final int ERROR_UNKNOWN = 1008; // 未知错误
	public static final int ERROR_NEED_RELOGIN = 1009; // 需要重新登录
	public static final int ERROR_NOT_AVAILABLE = 1010; // 目前不可用
	public static final int ERROR_更新数据库失败 = 1011;//更新数据库不成功
	public static final int ERROR_通道鉴权错误 = 1012;
	public static final int ERROR_频繁聊天限制=1013;
	public static final int ERROR_IP注册账号达到上限 = 1014;//IP注册账号达到上限
	
	public int msgType;
	public byte[] data;
	public int callBackId;
	
	public int code;
	public Response() {
	}
	
	public Response(int code) {
		this.code = code;
	}
	
	public Response(int msgType,int code) {
		this.msgType = msgType;
		this.code = code;
	}
	
	public Response(int msgType, byte[] data)
	{
		this.msgType = msgType;
		this.data = MySecurityUtil.encryptForDis(data);
	}
	
	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}
	
	public int getMsgType()
	{
		return msgType;
	}

	public void setMsgType(int msgType)
	{
		this.msgType = msgType;
	}

	@Override
	public String toString()
	{
		return "Response [msgType=" + msgType + ", code=" + code + "]";
	}
}
