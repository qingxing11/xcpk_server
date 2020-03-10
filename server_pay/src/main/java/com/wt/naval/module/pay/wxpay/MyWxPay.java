package com.wt.naval.module.pay.wxpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.wt.pay.tool.MyPayUtil;
import com.wt.util.Tool;
import com.wt.util.UuidUtil;

public class MyWxPay
{
	public static Map<String, String> initiatingWxUserOrder(Map<String, String> map_response)
	{
		Map<String, String> map_paymap = new HashMap<String, String>();
		map_paymap.put("appid", map_response.get("appid"));
		map_paymap.put("partnerid", map_response.get("mch_id"));
		map_paymap.put("prepayid", map_response.get("prepay_id"));
		map_paymap.put("package", "Sign=WXPay");
		map_paymap.put("noncestr", map_response.get("nonce_str"));
		map_paymap.put("timestamp", Tool.getCurrTimeMM() / 1000 + "");

		String sign = "";
		try
		{
			sign = MyPayUtil.generateSignature(map_paymap, GamePayConfig.instance().wxAppSecret);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		map_paymap.put("sign", sign);
		return map_paymap;
	}

	/**
	 * 将生成的订单基础信息map发送给微信，并取得统一流水订单
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> initiatingWxOrder(Map<String, String> map)
	{
		String xml = "";
		try
		{
			xml = MyPayUtil.mapToXml(map);
		}
		catch (Exception e1)
		{
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}

		HttpResponse response = null;
		String result = null;
		HttpPost request = new HttpPost(GamePayConfig.instance().wxPayOrderApi);
		try
		{
			request.setEntity(new StringEntity(xml, "utf-8"));
			response = HttpClients.createDefault().execute(request);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(), "utf-8");

				 Tool.print_debug_level0("微信下单回应:" + result);

				Map<String, String> hashMap_xml = MyPayUtil.xmlToMap(result);
				return hashMap_xml;
			}
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据计费点信息初始化订单数据map
	 * 
	 * @param userId
	 * @param cost
	 * @param body
	 * @return
	 */
	public static HashMap<String, String> initPayOrderMap(int userId, String cost, String body)
	{
		HashMap<String, String> hashMap_payOrder = new HashMap<>();

		String out_trade_no = MyPayUtil.getOutTradeNo() + userId;
		hashMap_payOrder.put("appid", GamePayConfig.instance().wxAppID);// 应用ID
		hashMap_payOrder.put("mch_id", GamePayConfig.instance().mchid);// 商户号
		
		hashMap_payOrder.put("nonce_str", UuidUtil.generateShortUuid());// 随机字符串
		hashMap_payOrder.put("sign_type", "MD5");// 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
		hashMap_payOrder.put("body", body);// 商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
		hashMap_payOrder.put("out_trade_no", out_trade_no);// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@
		// ，且在同一个商户号下唯一。详见商户订单号
		hashMap_payOrder.put("total_fee", cost);// 订单总金额，单位为分，详见支付金额
		hashMap_payOrder.put("spbill_create_ip","127.0.0.1");// 用户端实际ip
		hashMap_payOrder.put("notify_url", GamePayConfig.instance().getPayNotifyUr());// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。

		hashMap_payOrder.put("trade_type", "APP");// 支付类型

		try
		{
			String sign = MyPayUtil.generateSignature(hashMap_payOrder, GamePayConfig.instance().wxAppSecret);
			hashMap_payOrder.put("sign", sign);// 签名
			return hashMap_payOrder;
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 根据计费点信息初始化订单数据map
	 * 
	 * @param userId
	 * @param cost
	 * @param body
	 * @return
	 */
	public static HashMap<String, String> initWebPayOrderMap(int userId, String cost, String body,String ip)
	{
		HashMap<String, String> hashMap_payOrder = new HashMap<>();

		String out_trade_no = MyPayUtil.getOutTradeNo() + userId;
		hashMap_payOrder.put("appid", GamePayConfig.instance().gz_appid);// 应用ID
		hashMap_payOrder.put("mch_id", GamePayConfig.instance().gz_mchid);// 商户号
		
		hashMap_payOrder.put("nonce_str", UuidUtil.generateShortUuid());// 随机字符串
		hashMap_payOrder.put("sign_type", "MD5");// 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
		hashMap_payOrder.put("body", body);// 商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
		hashMap_payOrder.put("out_trade_no", out_trade_no);// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@
		// ，且在同一个商户号下唯一。详见商户订单号
		hashMap_payOrder.put("total_fee", cost);// 订单总金额，单位为分，详见支付金额
		hashMap_payOrder.put("spbill_create_ip",ip);// 用户端实际ip
		hashMap_payOrder.put("notify_url", GamePayConfig.instance().getPayNotifyUr());// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。

		hashMap_payOrder.put("trade_type", "MWEB");// 支付类型
		
		String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"腾讯充值\"}} ";
		hashMap_payOrder.put("scene_info", scene_info);
		
		try
		{
			String sign = MyPayUtil.generateSignature(hashMap_payOrder, GamePayConfig.instance().wxAppSecret);
			hashMap_payOrder.put("sign", sign);// 签名
			return hashMap_payOrder;
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		return null;
	}
}
/*
 我手上现有的功能还剩微信H5计费
 玩家之间定位（客户端获取位置，服务器端共享）
 可以先做一个桌面版的GM管理工具(报表，列出所有房间，管理房间，封号,查询玩家等)，后面有网页前端了这个不要都无所谓
 后续的代理等功能，建议在把后台功能考虑完全，合理后再开始做。如果我拿着现在的某几个想法就上手弄，后面想再交接就困难了
 */



