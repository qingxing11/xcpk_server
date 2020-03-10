package com.wt.pay.ali;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.wt.pay.AliPayOrderInfo;
import com.wt.pay.util.MyPayUtil;
import com.wt.util.Tool;

@Service
@PropertySource({ "classpath:alipay.properties" })
public class AliPayImpl implements AliPayService
{
	@Value("${ali.appid}")
	private String appid;

	@Value("${ali.privateKey}")
	private String privateKey;

	@Value("${ali.publicKey}")
	private String publicKey;
	
	@Value("${ali.aliNotifyUrl}")
	private String aliNotifyUrl;

	/**
	 * 获得一个支付宝支付订单
	 * 
	 * @param userId
	 * @param cost
	 *                支付的钱（分）
	 * @param subject
	 *                计费点抬头
	 * @param body
	 *                计费点描述
	 * @return AliPayOrderInfo
	 */
	public AliPayOrderInfo getAliPayOrder(int userId, String cost, String subject, int payId)
	{
		String out_trade_no = MyPayUtil.getOutTradeNo() + userId;

		// 实例化客户端
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appid,privateKey, "json", "UTF-8", publicKey, "RSA2");
		// 实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		// SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(userId+":"+payId);
		model.setSubject(subject);
		model.setOutTradeNo(out_trade_no);
		model.setTimeoutExpress("30m");
		model.setTotalAmount(cost);
		model.setProductCode("QUICK_MSECURITY_PAY");
		request.setBizModel(model);
		request.setNotifyUrl(aliNotifyUrl);

		// 发送消息:{"callBackId":0,"code":1000,"msgType":600,"payOrder":"alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017051607251031&biz_content=%7B%22body%22%3A%2210%E6%8A%8A%E9%92%A5%E5%8C%99%22%2C%22out_trade_no%22%3A%2289107up3cFcJU6658%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%B5%8B%E8%AF%95%E8%AE%A1%E8%B4%B9%E7%82%B9%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%221%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F106.15.137.38%3A1985&sign=Tbd3oP0E%2B%2BfDphO37hEp5s9YvgvJRDB4hovbXjGZmTibcD%2BJsiCGk%2FGPOG4IOpiOVM%2FCG0MytvnGpjxgCJsKkzh5IqYDpIlXkk4KZzQY0ddyXX%2FnnffRODZOyJPNViCTvTpR8BjIc3%2FYQM6eFLAvJuI%2BOKVKEVz8liLd9OQmwYsxemWKgtrfV%2F9k9YdtiwV1urjJgyND%2FpD%2FVbFmvmAAYXNyzTIBwjDKLwFO9Ay1re4%2BYTPziII%2BPg9K3Fboz4%2Bk7BkLEfhLDqTcAsthcWNpi5MH6Xj33O%2B6vvwIc6egOWAaqj5efTPj%2BWcfzBU9G4a1gcDqWpRPd1DXtccmuUKv1g%3D%3D&sign_type=RSA2&timestamp=2017-07-10+10%3A34%3A49&version=1.0"}

		try
		{
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			// System.out.println(responseStr =
			// response.getBody());//就是orderString
			// 可以直接给客户端请求，无需再做处理。
			AliPayOrderInfo aliPayOrder = new AliPayOrderInfo();
			aliPayOrder.initOrderInfo(out_trade_no, cost, response.getBody());
			return aliPayOrder;
		}
		catch (AlipayApiException e)
		{
			Tool.print_error(e);
		}
		return null;
	}
	
	public void test(int userId, String cost, String subject, String body)
	{
//		String out_trade_no = MyPayUtil.getOutTradeNo() + userId;
//		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
//		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//		model.setBody("我是测试数据");
//		model.setSubject("App支付测试Java");
//		model.setOutTradeNo(outtradeno);
//		model.setTimeoutExpress("30m");
//		model.setTotalAmount("0.01");
//		model.setProductCode("QUICK_MSECURITY_PAY");
//		request.setBizModel(model);
//		request.setNotifyUrl("商户外网可以访问的异步地址");
//		try {
//		        //这里和普通的接口调用不同，使用的是sdkExecute
//		        AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//		        System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
//		    } catch (AlipayApiException e) {
//		        e.printStackTrace();
//		}
	}
	
	public String getPayInfo(int payId)
	{
		switch (payId)
		{
			case 0:
				return "5diamond";
				
			case 1:
				return "11diamond";
				
			case 2:
				return "35diamond";
				
			case 3:
				return "60diamond";
				
			case 4:
				return "130diamond";
				
			case 5:
				return "750diamond";

			default:
				break;
		}
		return null;
	}
	
	public float getCost(int payId)
	{
		switch (payId)
		{
			case 0:
				return 5f;
				
			case 1:
				return 10;
				
			case 2:
				return 30;
				
			case 3:
				return 50;
				
			case 4:
				return 100;
				
			case 5:
				return 500;

			default:
				return 500;
		}
	}
}
