package com.wt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil
{
	/**
	 * method
	 * @param url
	 * @return
	 */
	public static HashMap<String, String> decoderUrl(String url)
	{
		HashMap<String, String> map = new HashMap<>();
		String[] url_decode = url.split("[?]");
		String method = url_decode[0].substring(1);
		map.put("method", method);
		if (url_decode.length < 2)
		{
			return map;
		}
		String[] map_url = url_decode[1].split("&");
		for (String string : map_url)
		{
			String[] k_v = string.split("=");
			if (k_v.length > 1)
				map.put(k_v[0], k_v[1]);
		}
		return map;
	}
	
	public static String handlerHttpRequest(HttpServletRequest request)
	{
		StringBuilder sb = new StringBuilder();
		InputStream inputStream;
		BufferedReader in;
		String json = null;
		try
		{
			inputStream = request.getInputStream();
			String s;
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null)
			{
				sb.append(s);
			}
			in.close();
			inputStream.close();
			if(sb.length() <= 0)
			{
				Tool.print_debug_level0("参数空");
				json = "参数空";
			}
		}
		catch (IOException e)
		{
			Tool.print_error("IOException",e);
			e.printStackTrace();
			json = "IOException";
		}
		catch (Exception e)
		{
			Tool.print_error("Exception",e);
			e.printStackTrace();
			json = "Exception";
		}
		return json;
	}
	
	public static Map<?, ?> handlerHttpRequestByParameterMap(HttpServletRequest request)
	{
		Map<?,?> requestParams = request.getParameterMap();
		if (requestParams == null || requestParams.size() == 0)
		{
			return null;
		}
		return requestParams;
	}
}
