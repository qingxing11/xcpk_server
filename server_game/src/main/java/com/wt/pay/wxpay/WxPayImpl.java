package com.wt.pay.wxpay;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.wt.util.Tool;
import com.wt.util.UuidUtil;

@Service
@PropertySource({ "classpath:wxpay.properties" })
public class WxPayImpl implements WxPayService
{
	@Value("${wxAppID}")
	private String wxAppID;
	
	@Value("${wxAppSecret}")
	private String wxAppSecret;
	
	@Value("${mchid}")
	private String mchid;
	
	@Value("${wxNotifyUrl}")
	private String wxNotifyUrl;
	
	@Value("${wxPayOrderApi}")
	private String wxPayOrderApi;
	
	@Override
	public String getWxPayOrder(int userId, String cost, String info,String attach)
	{
		Map<String, String> map_initOrder = initPayOrderMap(userId, cost, info,attach);// 根据计费点信息初始化订单数据map
		if (map_initOrder == null)
		{
			return null;
		}

		// 第二步，发送订单信息向微信下单
		Map<String, String> map_wxResponse = initiatingWxOrder(map_initOrder);// 将生成的订单基础信息map发送给微信，并取得统一流水订单
		if (map_wxResponse == null || "FAIL".equals(map_wxResponse.get("return_code")))
		{
			return null;
		}

		// 第三步，将微信回复的统一订单号等重新签名,并返回给客户端作为支付参数
		Map<String, String> map_userOrder = initiatingWxUserOrder(map_wxResponse);
		if (map_userOrder == null)
		{
			return null;
		}
		String order = JSONObject.toJSONString(map_userOrder);
		Tool.print_debug_level0("微信支付生成最终订单:"+order);
		return order;
	}
	
	
	
	/**
	 * 根据计费点信息初始化订单数据map
	 * 
	 * @param userId
	 * @param cost
	 * @param body
	 * @return
	 */
	private HashMap<String, String> initPayOrderMap(int userId, String cost, String body,String attach)
	{
		Tool.print_debug_level0("微信网页下单,userId:"+userId+",cost:"+cost+",body:"+body+",attach:"+attach);
		HashMap<String, String> hashMap_payOrder = new HashMap<>();

		String out_trade_no = WXPayUtil.getOutTradeNo() + userId;
		hashMap_payOrder.put("appid", wxAppID);// 应用ID
 		hashMap_payOrder.put("attach", attach);
		hashMap_payOrder.put("body", body);// 商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
		hashMap_payOrder.put("mch_id", mchid);// 商户号
		hashMap_payOrder.put("nonce_str", UuidUtil.generateShortUuid());// 随机字符串
 		hashMap_payOrder.put("sign_type", "MD5");// 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
		hashMap_payOrder.put("notify_url",wxNotifyUrl);// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
		hashMap_payOrder.put("out_trade_no", out_trade_no);// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@，且在同一个商户号下唯一。详见商户订单号
		hashMap_payOrder.put("spbill_create_ip","127.0.0.1");// 用户端实际ip
		hashMap_payOrder.put("total_fee", cost);// 订单总金额，单位为分，详见支付金额
		hashMap_payOrder.put("trade_type", "APP");// 支付类型

		try
		{
			String sign = WXPayUtil.generateSignature(hashMap_payOrder,wxAppSecret);
			
			Tool.print_debug_level0("sign:"+sign);
			hashMap_payOrder.put("sign", sign);// 签名
			return hashMap_payOrder;
		}
		catch (Exception e1)
		{
			Tool.print_error("微信网页下单,userId:"+userId+",cost:"+cost+",body:"+body+",attach:"+attach+"初始化订单数据map错误",e1);
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
	public HashMap<String, String> initWebPayOrderMap(int userId, String cost, String body,String ip)
	{
		HashMap<String, String> hashMap_payOrder = new HashMap<>();

		String out_trade_no = WXPayUtil.getOutTradeNo() + userId;
		hashMap_payOrder.put("appid", wxAppID);// 应用ID
		hashMap_payOrder.put("mch_id", mchid);// 商户号
		
		hashMap_payOrder.put("nonce_str", UuidUtil.generateShortUuid());// 随机字符串
		hashMap_payOrder.put("sign_type", "MD5");// 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
		hashMap_payOrder.put("body", body);// 商品描述交易字段格式根据不同的应用场景按照以下格式：APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
		hashMap_payOrder.put("out_trade_no", out_trade_no);// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@
		// ，且在同一个商户号下唯一。详见商户订单号
		hashMap_payOrder.put("total_fee", cost);// 订单总金额，单位为分，详见支付金额
		hashMap_payOrder.put("spbill_create_ip",ip);// 用户端实际ip
		hashMap_payOrder.put("notify_url", wxNotifyUrl);// 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。

		hashMap_payOrder.put("trade_type", "MWEB");// 支付类型
		
		String scene_info = "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"https://pay.qq.com\",\"wap_name\": \"腾讯充值\"}} ";
		hashMap_payOrder.put("scene_info", scene_info);
		
		try
		{
			String sign = WXPayUtil.generateSignature(hashMap_payOrder, wxAppSecret);
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
	 * 将生成的订单基础信息map发送给微信，并取得统一流水订单
	 * 
	 * @param map
	 * @return
	 */
	private Map<String, String> initiatingWxOrder(Map<String, String> map)
	{
		String xml = "";
		try
		{
			xml = WXPayUtil.mapToXml(map);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		HttpResponse response = null;
		String result = null;
		HttpPost request = new HttpPost(wxPayOrderApi);
		try
		{
			request.setEntity(new StringEntity(xml, "utf-8"));
			response = HttpClients.createDefault().execute(request);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				Tool.print_debug_level0("微信下单返回:"+result);
				Map<String, String> hashMap_xml = WXPayUtil.xmlToMap(result);
				return hashMap_xml;
			}
		}
		catch (ClientProtocolException e)
		{
			Tool.print_error("initiatingWxOrder生成微信订单错误",e);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			Tool.print_error("initiatingWxOrder生成微信订单错误",e);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Tool.print_error("initiatingWxOrder生成微信订单错误",e);
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, String> initiatingWxUserOrder(Map<String, String> map_response)
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
			sign = WXPayUtil.generateSignature(map_paymap, wxAppSecret);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		map_paymap.put("sign", sign);
		return map_paymap;
	}
}
