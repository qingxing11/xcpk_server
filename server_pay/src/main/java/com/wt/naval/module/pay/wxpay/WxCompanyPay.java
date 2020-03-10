package com.wt.naval.module.pay.wxpay;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.wt.pay.tool.MyPayUtil;
import com.wt.util.UuidUtil;

public class WxCompanyPay
{
	public static void main(String[] args)
	{
//		 String xml = initCompanyPayPostXml("oOVqPwD5lSgz9R1wPoRkc18lkltA","刺客","100","测试付款");
		
 		String xml = initCompanyPayPostXml("oIptMxF6H21-SYvsNfMcY_Za08Sc","王拓","1","测试付款");
//		System.out.println("xml:"+xml);
 		postWxCompanyPay(xml);
	}
	
	public static Map<String, String> postWxCompanyPay(String xml)
	{
		HttpResponse response = null;
		String result = null;
		HttpPost request = new HttpPost(GamePayConfig.instance().wxCompanyPayApi);
		  CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(getP12()).build();
		try
		{
			request.setEntity(new StringEntity(xml,"utf-8"));
			 
			response = httpclient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				result = EntityUtils.toString(response.getEntity());
				Map<String, String> map = MyPayUtil.xmlToMap(result);
				System.out.println("map:"+map);
				return map;
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
	 * @param openId
	 * @param name
	 * @param amount
	 * @param desc
	 * @return
	 */
	public static String initCompanyPayPostXml(String openId,String name,String amount,String desc)
	{
		String out_trade_no = MyPayUtil.getOutTradeNo();
		
		Map<String,String> map_xml = new HashMap<>();
		map_xml.put("mch_appid", GamePayConfig.instance().gz_appid);
		map_xml.put("mchid", GamePayConfig.instance().gz_mchid);
		map_xml.put("nonce_str", UuidUtil.generateShortUuid());//随机字符串，不长于32位
		map_xml.put("partner_trade_no", out_trade_no);
		map_xml.put("openid",openId);
		map_xml.put("check_name","FORCE_CHECK");
 		map_xml.put("re_user_name",name);
		map_xml.put("amount", amount);
		map_xml.put("desc",desc);
		map_xml.put("spbill_create_ip","127.0.0.1");
		try
		{
			map_xml.put("sign",MyPayUtil.generateSignature(map_xml, "085c4509ef0a343913c9330a74ba2dea"));
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		String xml = "";
		try
		{
			xml = MyPayUtil.mapToXml(map_xml);
		}
		catch (Exception e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		 return xml;
	}
	
	private static SSLConnectionSocketFactory getP12()
	{
		FileInputStream instream = null;
		try
		{
			// 指定读取证书格式为PKCS12
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			// 读取本机存放的PKCS12证书文件
			instream = new FileInputStream("./cert/apiclient_cert.p12");
			// 指定PKCS12的密码(商户ID)
			keyStore.load(instream, GamePayConfig.instance().gz_mchid.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, GamePayConfig.instance().gz_mchid.toCharArray()).build();
			// 指定TLS版本
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] {
					"TLSv1"
			}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			// 设置httpclient的SSLSocketFactory
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
			return sslsf;
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (CertificateException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (KeyStoreException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		catch (KeyManagementException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		catch (UnrecoverableKeyException e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(instream != null)
				{
					instream.close();
				}
			}
			catch (IOException e)
			{
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return null;
	}
}
