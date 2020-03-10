package com.wt.naval.module.pay.applepay;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.wt.util.Tool;
import com.wt.util.rsa.Base64Helper;


public class MyApplePay {
 	public static void main(String[] args) throws Exception
	{
	}
	
 	private static final String apple_buy = "https://buy.itunes.apple.com/verifyReceipt";
 	private static final String apple_sandbox = "https://sandbox.itunes.apple.com/verifyReceipt";
	public static void checkApplePay(String txt,int userId,String nickName) {
		HttpResponse response = null;
		String result = null;
		String apple_api = "";
		//从数据库中获取 是否沙盒测试支付，正常支付。注意apple审核时也是沙盒环境支付的
//		if(PayDaoImpl.ApplePayConfig().equals("sandbox"))  //txt.indexOf("Sandbox") > -1) 
//		{
//			apple_api = apple_sandbox;
//		}
//		else
//		{
//			apple_api = apple_buy;
//		}
		
		Tool.print_debug_level0("苹果支付验证回执地址："+apple_api);
		HttpPost request = new HttpPost(apple_api);
		try {
 			txt = Base64Helper.encode(txt.getBytes("utf-8"));
			String app_json = "{\"receipt-data\":\""+txt+"\"}";
			System.out.println("发送:"+app_json);
			request.setEntity(new StringEntity(app_json));
			response = HttpClients.createDefault().execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
				
				AppleResult appleResult = JSONObject.parseObject(result, AppleResult.class);
				
				Tool.print_debug_level0("result:" + result);
				checkApplePay_result(appleResult,userId,nickName);
			}
			else
			{
//				Channel channel = UserCache.map_channel.get(userId);
 		 
				Tool.print_debug_level0("苹果支付延签失败:userId:"+userId+(!nickName.isEmpty() ? nickName : ""));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void checkApplePay_result(AppleResult appleResult,int userId,String nickName)
	{
		if(appleResult.status == 0)
		{
		}
		else
		{
//			Channel channel = UserCache.map_channel.get(userId);
			
			Tool.print_debug_level0("苹果支付延签失败:userId:"+userId+(!nickName.isEmpty() ? nickName : ""));
		}
	}
}