package com.wt.server.config;

import com.wt.config.BaseConfig;

/**
 * 服务器配置文件，对应“server.properties”文件
 * @author Wangtuo
 *
 */
public final class GamePayConfig
{
//	/** rsa秘钥 */
//	public String rsaPublicKey;
//	/** rsa秘钥 */
//	public String rsaPrivateKey;
//
//	/** 微信支付appId */
//	public String wxAppID;
//	/** 微信支付AppSecret */
//	public String wxAppSecret;
//	/** 微信支付商户id */
//	public String mchid;
//
//	/** 微信企业付款api */
//	public String wxCompanyPayApi;
//	
//	/** 微信下单api */
//	public String wxPayOrderApi;
//	
//	/**公众号*/
//	public String gz_appid;
//	public String gz_mchid;
//	
//	/**微信回调地址*/
//	public String payNotifyUrl_publish;
//	public String payNotifyUrl_test;
//	
//	/**阿里支付appid*/
//	public String aliPay_appId;
//	
//	/**阿里私钥*/
//	public String aliPay_privateKey;
//	/**支付宝自动公钥*/
//	public String aliPay_mchPublicKey;
//	/**收款账号邮箱*/
//	public String seller_email;
	private GamePayConfig(){}
	
	private static transient GamePayConfig instance;
	public static GamePayConfig instance()
	{
		if(instance == null)
		{
			instance = new GamePayConfig();
			BaseConfig.init(instance,"config/gamepay.properties");
		}
		return instance;
	}
	
//	public String getPayNotifyUr()
//	{
//		return payNotifyUrl_publish;
//	}
}
