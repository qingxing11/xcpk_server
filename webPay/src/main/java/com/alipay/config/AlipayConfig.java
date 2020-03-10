﻿package com.alipay.config;

public class AlipayConfig {
	// 商户appid
	public static String APPID = "2017051607251031";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC8Mh+40eJqF5yb+/79qDckIg8sMx3ZU/oVSa/aZJM4+vfjNEvmlWCic7S1gyxYq8MLkqXJj7MPtujytMTp82+sA/feRuOtqLWo9yZTBp6GDYoA1aASJb+6swxP935PW0PpzmND5apZsGfbVR2iqbpoAmLWEKl61nZZwTNRGitggIHbRrZRt3dL9iCNakVJO6uonfvoDnvQ81PMAC57YSDpik7jAC+Qx7/CeNpDsDMpG2YX5TF5KV+NxCI2YI/PozAES8RdRU0t2yeQkIUTS1l2XsP0VLLaVNQ1ctRZNcHbnonUKI+ycFkIK3asXKjHbnHHKo+iJJLbPf7K9y4/PRiHAgMBAAECggEANnrcLAi2XrgPIijFHHFB5Tfm5Ii3r15cGyWFvtc2t1wPvro47rt3ig+s1I7wm4q9n/AIBl2PHJF6xigGiSNrxIQxkSs8Zd1dGVjSw147Ldtlg/BTsm0zH70NZ2jiK97jvh99JP6rXeNvbVMqjRhNWia2rfmM1n6XLEx9qpDE1d5eQaaAe5XJJ/Loytz/FPCYuYABr+pWBOBK1Mm0baKmoewMeOCZ//noijGA+bm3Syle4UvEBHP7xmb+BKhCNmN2EiAIH3NdQ13N67GYVzxn0O2DdCIhSqkSGr1hV0KvYBWiUFUtbwz8uqEkX31jtpE7JNPlFArjnRkaz3u6QA/BAQKBgQDtOz/rwj5MLxIljHHqnT6p8voqPmYo4FhNpy8ldJs8J2fSHg31i51lrP+sNXghQtYjZ05t7EZ6QgD2f0+3kpWMPSerG2NTwgQF9OMsYPDwVypuNzBN/m5b1bTMSHHLp98xKqgC7CIOKHIC49XBBlOXpf9EfD3LhNY96DNYn6OZJwKBgQDLFbv0QSBDRRm6lquvOTB5xiVsFY1Bu5JWjQicw1FON2J38/9Ntoax5g5uFFX1OjEL7ITF0HZL3m1YkoxoNMXLNjETfTUVxl4oVa3U7ywPPJo7s3WbJX1DlvcG+jQxUhE/NrSqS6TDIj/cl8Ow6sKij+/zOYxuLBRrHNxsYF5hoQKBgQC6t42iA+qAj/PadeYbYNQ3czSPFznVJm6Z/+JNuq+L8v/2Ew2tm8uwgS9i3VrF4xMkHmVOg08dXuHfIWke9VKbZG7ab94HyIbeIVbEUTCSGikdQnbUjFNUGKoKt2JJhsFWs1o1oqFTP2Ys/CH7oP0eONrnjloc21q8swvmzw82tQKBgQCmQYDBkZ9r8uIBUt/ybws0Zfa06V/e9EP0/ziRvYSOV+EQ+nuDj23trb0aeSN1wtHsRRDdMK/npIw3+qFfnfRfTFoY7mPLE755nsrUi4iyYJaTUXNyM9q8vpY07BhWWC0uZwa4c3zLEvrdCJF4AHiexN9Xa0F/4C5HEjWRs8MQgQKBgDzbSFnKfnhSPH6jiqY31mX28Qr+4foqwJlD2tR8f6HKDxy7iimETV6k0I1YvyR8oMC/ra0ZgoKPGMH8XC336dfTBulDfxDL7we5SOcof2ZY7l7cXkcoUMirYFOz2sUzya9VVeF9ez9i/oEGfddib7luXl9fXOY4TKFRKDVEeTLS";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://106.15.137.38:2015";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
	public static String return_url = "com.zqkj.zdb://";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjVlQhJO0Vi/Gn4PJcyD9Gx3KMYeb78VQRNhM6B5HommkvFV5z1AaUdf8hT7DCqCpzXecBk3xYofeIn5LvJyap2XveDfPVcqHwM8juANHhUQPGAFrzqpjypkxPg4lHvsmRJclPJJKZnhp/I7FOk4vSrfCoy8FCixQwkRcRJ7urt+Wes4unPCYwzgY38HghhCrR+ZiWx6wazAdxd/uwYHZH0uQD8dhW0EZHGHv0Gxy3o3mBG0a588Ogi01QWUrISzJ66+i3dtCPgiR3YqKMHbXPCW/zgvJGBt99ZNUWRx95dc8kLPA2ZwIheDZP6sutOEpmXPl92Q71B9z5oTDvOCMzQIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA2";
}
