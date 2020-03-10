package com.wt.util.security;

import java.security.MessageDigest;

import com.wt.util.Base64Helper;

public class MySecurityUtil {

	/**
	 * SHA加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptSHA(String data) throws Exception {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		sha.update(data.getBytes());
		return byte2hex(sha.digest());
	}

	public static String byte2hex(byte[] b) // 二进制转字符串
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n < b.length - 1)
			// hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 位加密
	 */
	public static byte[] encryptForDis (byte[] bytes){

		byte buff;
		byte[] mes = bytes;
		for(int i=0; i<mes.length; i+=5){
			if(i + 3 > mes.length - 1) break;
			buff = (byte) ~mes[i + 2];
			mes[i + 2] = mes[i + 3];
			mes[i + 3] = buff;
		}
		return mes;
	}

	/**
	 * 位解密
	 */
	public static byte[] decryptForDis (byte[] bytes){
		byte buff;
		for(int i=0; i<bytes.length; i+=5){
			if(i + 3 > bytes.length - 1) break;
			buff = bytes[i + 2];
			bytes[i + 2] = (byte) ~bytes[i + 3];
			bytes[i + 3] = buff;
		}
		return bytes;
	}
	
	public static String base64Encode(byte[] byteArray) {
		return Base64Helper.encode(byteArray);
	}

	public static byte[] base64Decode(String base64EncodedString) {
		return Base64Helper.decode(base64EncodedString);
	}
}
