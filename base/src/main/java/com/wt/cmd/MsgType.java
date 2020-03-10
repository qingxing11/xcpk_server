package com.wt.cmd;

public interface MsgType
{
	public static final int TEST_RTT = -1001;
	public static final int TEST = -1000;
	public static final int HEARTBEAT = -999;// 心跳包
	public static final int HEARTBEAT_RESPONSECLIENT = -998;// 心跳包
	public static final int SHUTDOWN = -997;// 回应服务器推送
	public static final int CLIENT_BUG = -996;// 客户端bug
	/**** 工具 ****/
	public static final int UTIL_SERVER_TIME = 100;// 服务器时间
	public static final int UTIL_SERVER_LIST = 101;// 服务器列表
	public static final int UTIL_CHECK_FILE = 102; // 检查文件是否最新
	public static final int UTIL_DOWNLOAD_RES = 103; // 下载文件
	public static final int UTIL_SERVER_PUSH_服务器推送 = 104;// 服务器主动推送
	public static final int UTIL_服务器开关 = 105;
	public static final int UTIL_获取推广二维码 = 106;
	public static final int UTIL_GET_NEW_MD5 = 107;
	public static final int UTIL_GET_NEW_FILE_URL = 108;

	public static final int EMAIL=110;
	
	/** 公共错误 **/
	public static final int ERROR_SESSIONERR = 151;// 权限错误，未登陆等
	public static final int ERROR_PARAMETER = 152;
	
	

	/** 定时任务 ***/
	public static final int TIMESTASK = 180;// 定时任务消息请求

	/**** 用户 ****/
	public static final int USER_LOGIN = 200; // 登录
	public static final int USER_REGISTER = 201; // 注册
	public static final int USER_SERVER_INFO = 202;
	public static final int USER_使用激活码 = 203;// 兑换礼物
	public static final int USER_CHANGE_NICK = 204;
	public static final int USER_REGISTER_GETCODE = 205; // 注册,获取验证码
	public static final int USER_ADMIN_LOGIN = 206;// 管理员权限登录任何账户，无需密码
	public static final int USER_REGISTER_TESTACCOUNT = 207;// 注册测试帐号
	public static final int USER_FASTLOGIN = 209;
	public static final int USER_注册游客账号 = 210;
	public static final int USER_游客快速登录 = 211;
	public static final int USER_反馈 = 212;
	public static final int USER_令牌登录 = 213;
	public static final int USER_鉴权失败 = 214;
	public static final int USER_DEBUGLOGIN = 215;
	public static final int USER_WX_LOGIN = 220;// 微信登录
	public static final int USER_VALIDATION_TOKEN = 221;// 验证token

	/****************** 游戏功能 *******************/
	public static final int GAME_上传游戏相关数值 = 300;
	public static final int GAME_获取指定玩家数据 = 301;
	public static final int GAME_领取邮件附件 = 302;
	public static final int GAME_删除邮件 = 303;
	public static final int GAME_阅读邮件 = 304;

	public static final int GAME_货币变化 = 331;
	public static final int GAME_打开签到 = 332;
	public static final int GAME_点击签到 = 333;
	/******************** 游戏设置 *****************************/

	/****************** 聊天功能 *******************/
	public static final int CHAT_玩家喊话 = 501;
	public static final int CHAT_玩家私聊 = 502;
	public static final int CHAT_表情聊天 = 503;
	public static final int CHAT_文字聊天 = 504;
	public static final int CHAT_语音聊天 = 505;

	public static final int CHAT_公屏频道 = 509;
	public static final int CHAT_同盟频道 = 510;
	public static final int CHAT_私聊频道 = 511;
	public static final int PUSHCHAT_系统频道 = 512;
	public static final int PUSHCHAT_外交频道 = 513;
	public static final int PUSHCHAT_日志频道 = 514;
	public static final int AskTemporaryChat_临时聊天 = 515;

	public static final int ADDFRIENDS = 520;// 添加好友
	public static final int ASKFRIENDS = 521;// 请求好友列表
	

	/****************** pay&bind **********************/
	public static final int PAY_支付宝支付下单 = 600;
	public static final int PAY_获取支付结果 = 601;
	public static final int PAY_微信支付下单 = 602;
	public static final int PAY_提现 = 603;
	public static final int PAY_收益清单 = 604;
	public static final int PAY_提现清单 = 605;
	public static final int BIND_GETINFO = 606;
	public static final int BIND_GETBIND_LV1 = 607;
	public static final int PAY_获取苹果支付结果 = 608;
	public static final int PAY_支付宝网页支付下单 = 609;
	public static final int PAY_微信网页支付下单 = 610;
	public static final int PAY_支付回调消息 = 611;
	/********************* 服务器推送消息 ***********************/
	public static final int NOTICE_服务器计时关机 = 700;
	public static final int NOTICE_服务器公告 = 701;
	public static final int NOTICE_玩家兑换物品 = 702;
	public static final int NOTICE_玩家收获 = 703;
	public static final int NOTICE_玩家喊话 = 704;
	public static final int NOTICE_服务器准备重启 = 705;
	/********************* 商城信息 ***********************/
	public static final int MALL_商城信息 = 720;
	public static final int MALL_购买商品 = 721;

	/********************* 邮件 ***********************/
	public static final int MAIL_接收新邮件 = 740;
	public static final int MAIL_阅读邮件 = 741;
	public static final int MAIL_删除邮件 = 742;
	public static final int MAIL_领取附件 = 743;
	public static final int MAIL_获取所有邮件 = 744;
	public static final int MAIL_检查全服邮件 = 745;
	public static final int MAIL_GM发送邮件 = 750;

	/********************* 推送给指定玩家 ***********************/
	public static final int PUSHUSER_货币变化 = 800;
	public static final int PUSHUSER_文字聊天 = 801;
	public static final int PUSHUSER_表情聊天 = 802;

	public static final int PUSHTREASURE_皇家礼包 = 880;

	public static final int PUSHUSER_玩家私聊 = 899;

	public static final int PushFriendsOnLine_好友是否在线 = 910;

	
	/********************** 分享 *************************************/
	public static final int SHARE_主动分享 = 9000;
	public static final int SHARE_获取推广链接 = 9001;
	public static final int SHARE_获取邀请游戏链接 = 9002;

	/****************** 自定义，用于日志输出 ****************************/
	public static final int LOG_玩家增加金币 = 10001;

	public static final int LOG_微信支付回调 = 10009;
	public static final int LOG_支付宝回调 = 10010;
	public static final int LOG_支付回调验签成功 = 10011;
	public static final int LOG_添加玩家关联 = 10012;
	public static final int LOG_检测离线时间 = 10013;
	public static final int LOG_苹果支付回调验签成功 = 10014;

	/********************* 管理工具 *****************************/
	public static final int GM_服务器状态信息 = 10100;
	public static final int GM_发送公告 = 10101;
	public static final int GM_服务器关机 = 10102;
	public static final int GM_获取日志列表 = 10103;
	public static final int GM_GM工具登录 = 10104;
	public static final int GM_获取指定日志 = 10105;
	public static final int GM_重载配置 = 10106;
	public static final int GM_重启 = 10107;
	public static final int GM_设置机器人数量 = 10108;
	public static final int BACKEND_获取所有房间 = 10109;
	public static final int GM_发送邮件 = 10110;

	/********************* 负载测试 *****************************/
	public static final int LOADTEST_连接 = 10200;
	public static final int LOADTEST_测试1 = 10201;

	public static final int SERVER_PUSH_聊天服注册 = 10300;
	public static final int SERVER_PUSH_玩家聊天 = 10301;

	/*********** 后台 ************/
	public static final int BACKEND_刷新热更列表 = 20000;
	public static final int BACKEND_购买金币成功 = 20001;
	public static final int WINDOWS_WEBLOG = 20002;
	public static final int BACKEND_网页微信登陆 = 20003;

	public static String getTile(int tileIndex)
	{
		switch (tileIndex)
		{
			case USER_VALIDATION_TOKEN:
				return "进入游戏服验证token";

			case BACKEND_购买金币成功:
				return "BACKEND_购买金币成功";

			case BACKEND_获取所有房间:
				return "BACKEND_获取所有房间";

			case PAY_微信网页支付下单:
				return "微信网页支付下单";

			case PAY_支付宝网页支付下单:
				return "支付宝网页支付下单";

			case PAY_获取苹果支付结果:
				return "获取苹果支付结果";

			case UTIL_GET_NEW_FILE_URL:
				return "获取文件下载地址";

			case UTIL_GET_NEW_MD5:
				return "获取文件Md5列表";

			case LOG_检测离线时间:
				return "检测离线时间";

			case SHARE_获取推广链接:
				return "获取推广链接";

			case UTIL_服务器开关:
				return "服务器开关";

			case PAY_提现清单:
				return "提现清单";

			case PAY_收益清单:
				return "收益清单";

			case PAY_提现:
				return "提现";

			case LOG_支付回调验签成功:
				return "支付回调验签成功";

			case USER_反馈:
				return "玩家反馈";

			case LOG_支付宝回调:
				return "支付宝回调";

			case LOG_微信支付回调:
				return "微信支付回调";

			case PAY_微信支付下单:
				return "微信支付下单";

			case CHAT_文字聊天:
				return "文字聊天";

			case CHAT_表情聊天:
				return "表情聊天";

			case USER_游客快速登录:
				return "游客快速登录";

			case GM_设置机器人数量:
				return "GM设置机器人数量";

			case GM_重启:
				return "GM重启";

			case USER_FASTLOGIN:
				return "有令牌快速登录";

			case USER_WX_LOGIN:
				return "微信登录";

			case UTIL_CHECK_FILE:
				return "检查文件";

			case LOADTEST_测试1:
				return "测试1";

			case LOADTEST_连接:
				return "LOADTEST_连接";

			case GM_重载配置:
				return "GM_重载配置";

			case MsgType.GM_获取指定日志:
				return "GM_获取指定日志";

			case MsgType.GM_服务器状态信息:
				return "GM_服务器状态信息";

			case MsgType.GM_服务器关机:
				return "GM_服务器关机";

			case MsgType.GM_发送公告:
				return "GM_发送公告";

			case MsgType.GM_获取日志列表:
				return "GM_获取日志列表";

			case MsgType.GM_GM工具登录:
				return "GM工具登录";

			case MsgType.SHARE_主动分享:
				return "主动分享";

			case MsgType.CHAT_玩家喊话:
				return "玩家喊话";

			case MsgType.UTIL_SERVER_TIME:
				return "获取服务器时间";

			case MsgType.LOG_玩家增加金币:
				return "玩家增加金币";

			case MsgType.PAY_获取支付结果:
				return "获取支付结果";

			case MsgType.PAY_支付宝支付下单:
				return "支付宝支付下单";

			case MsgType.USER_REGISTER_GETCODE:
				return "获取注册验证码";

			case MsgType.USER_ADMIN_LOGIN:
				return "管理员权限登录";

			case MsgType.GAME_阅读邮件:
				return "GAME_阅读邮件";

			case MsgType.GAME_删除邮件:
				return "GAME_删除邮件";

			case MsgType.GAME_领取邮件附件:
				return "GAME_领取邮件附件";

			case MsgType.USER_LOGIN:
				return "登录";

			case MsgType.USER_REGISTER:
				return "注册";

			case MsgType.USER_SERVER_INFO:
				return "server_info";

			case MsgType.USER_使用激活码:
				return "兑换礼物";

			case MsgType.USER_CHANGE_NICK:
				return "换名字";

			case MsgType.GAME_上传游戏相关数值:
				return "上传游戏相关数值";

			case MsgType.GAME_获取指定玩家数据:
				return "获取指定玩家数据";
		}
		return null;
	}
}