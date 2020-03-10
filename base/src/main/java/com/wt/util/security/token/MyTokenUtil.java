package com.wt.util.security.token;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.wt.util.Tool;
import com.wt.util.UuidUtil;
import com.wt.util.security.MyPBKDF2;
import com.wt.util.security.MySecurityUtil;

public class MyTokenUtil {
	public static void main(String[] args) throws Exception {
		String shortUuid = UuidUtil.generateShortUuid();
		String pwd = "";
		try
		{
			pwd = MyPBKDF2.createHash(shortUuid);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			Tool.print_error("生成加盐密钥错误:",e1);
		}
		System.out.println("pwd:"+pwd);
		String token = createToken(123123, pwd);
		System.out.println("token:"+token);
		System.out.println("len:"+token.getBytes().length);
		
		System.out.println(checkTonken(token));
//		String token = createToken("13585106053", 6670, "akwang");
//		System.out.println("token:" + token);
//
//		String[] decodeToken = token.split("[.]");
//		byte[] body_b = EncodeTool.base64Decode(decodeToken[0]);
//		String body = new String(body_b);
//		System.out.println("body:" + body);
//		
//		byte[] head_b = EncodeTool.base64Decode(decodeToken[1]);
//		String head = new String(head_b);
//		System.out.println("head:" + head);
//		
//		StringBuilder sb = new StringBuilder();
//		sb.append(EncodeTool.base64Encode(body.getBytes()));
//		sb.append(".");
//		sb.append(EncodeTool.base64Encode(head.getBytes()));
//		String sign = EncodeTool.encryptSHA(sb.toString());
//		System.err.println("sign:"+sign+",是否匹配:"+sign.equals(decodeToken[2]));
	}
	
	/**
	 * 创建一个简单token，只包含user_id和加盐密钥
	 * @param userId
	 * @return
	 */
	public static TokenVO createSimpleToken(int userId,String pwd)
	{
		TokenVO tokenVO = new TokenVO();
		tokenVO.uid = userId;
		try
		{
			tokenVO.pwd = MyPBKDF2.createHash(pwd);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (InvalidKeySpecException e)
		{
			e.printStackTrace();
		}
		return tokenVO;
	}
	
	private static final String JWT_HEAD = "{\"typ\":\"JWT\"," + "\"alg\":\"SHA1\"}";
	
	/**
	 * 创建token
	 * @param userId
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static String createToken(int userId, String pwd) throws Exception {
		TokenVO token = new TokenVO();
		token.uid = userId;
		token.pwd = pwd;

		StringBuilder stringBuilder = new StringBuilder();
		String body = JSONObject.toJSONString(token);
		body = MySecurityUtil.base64Encode(body.getBytes());
		
		String head_base64 = MySecurityUtil.base64Encode(JWT_HEAD.getBytes());
		stringBuilder.append(body);
		stringBuilder.append(".");
		stringBuilder.append(head_base64);
		String sign = null;
		sign = MySecurityUtil.encryptSHA(stringBuilder.toString());
		stringBuilder.append(".");
		stringBuilder.append(sign);
		
		return stringBuilder.toString();
	}
	
	/**
	 * 验证token是否有效
	 * @param token 登陆后获取的token
	 * @param user_id
	 * @param mySign 登陆后保存的签名
	 * @return 检查通过，返回code=success,否则返回code=error。其他返回值:nickName，userId,account
	 */
	public static HashMap<String, String> checkTonken(String token)
	{
		HashMap<String,String> map_result = new HashMap<>();
		String key = "code";
		String key_value = null;
		try
		{
			String[] decodeToken = token.split("[.]");
			byte[] body_b = MySecurityUtil.base64Decode(decodeToken[0]);
			String body = new String(body_b);
			System.out.println("检查token载体:" + body);
			
			byte[] head_b = MySecurityUtil.base64Decode(decodeToken[1]);
			String head = new String(head_b);
			System.out.println("检查token头:" + head);
			
			StringBuilder sb = new StringBuilder();
			sb.append(MySecurityUtil.base64Encode(body.getBytes()));
			sb.append(".");
			sb.append(MySecurityUtil.base64Encode(head.getBytes()));
			String sign = MySecurityUtil.encryptSHA(sb.toString());
			boolean isSuccess = sign.equals(decodeToken[2]);
			System.err.println("sign:"+sign+",是否匹配:"+isSuccess);
			
			TokenVO token_obj = JSONObject.parseObject(body,TokenVO.class);
			
			if(isSuccess)
			{
				key_value = "success";
				map_result.put("userId",token_obj.uid+"");
				map_result.put("account",token_obj.pwd);
			}
			else
			{
				key_value = "error";
			}
		}
		catch (Exception e)
		{
			key_value = "error";
		}
		map_result.put(key, key_value);
		return map_result;
	}
}
