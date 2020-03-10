package com.wt.util.rsa;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.wt.util.server.ServerStatics;

public class RSAJavaToCSharp
{

	public static void main(String[] args)
	{
		String tes = ServerStatics.privateKey;
		byte[] temp = b64decode(tes);
		String ver = getRSAPrivateKeyAsNetFormat(temp);// 转换私钥
		System.out.println("ver:"+ver);

		String tes1 = ServerStatics.publicKey;
		byte[] temp1 = b64decode(tes1);
		String ver1 = getRSAPublicKeyAsNetFormat(temp1);// 转换公钥
		System.out.println("ver1:"+ver1);
	}

	private static String getRSAPrivateKeyAsNetFormat(byte[] encodedPrivkey)
	{
		try
		{
			StringBuffer buff = new StringBuffer(1024);

			PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);

			buff.append("<RSAKeyValue>");
			buff.append("<Modulus>" + b64encode(removeMSZero(pvkKey.getModulus().toByteArray())) + "</Modulus>");

			buff.append("<Exponent>" + b64encode(removeMSZero(pvkKey.getPublicExponent().toByteArray())) + "</Exponent>");

			buff.append("<P>" + b64encode(removeMSZero(pvkKey.getPrimeP().toByteArray())) + "</P>");

			buff.append("<Q>" + b64encode(removeMSZero(pvkKey.getPrimeQ().toByteArray())) + "</Q>");

			buff.append("<DP>" + b64encode(removeMSZero(pvkKey.getPrimeExponentP().toByteArray())) + "</DP>");

			buff.append("<DQ>" + b64encode(removeMSZero(pvkKey.getPrimeExponentQ().toByteArray())) + "</DQ>");

			buff.append("<InverseQ>" + b64encode(removeMSZero(pvkKey.getCrtCoefficient().toByteArray())) + "</InverseQ>");

			buff.append("<D>" + b64encode(removeMSZero(pvkKey.getPrivateExponent().toByteArray())) + "</D>");
			buff.append("</RSAKeyValue>");

			return buff.toString().replaceAll("[ \t\n\r]", "");
		}
		catch (Exception e)
		{
			System.err.println(e);
			return null;
		}
	}

	public static String getRSAPublicKeyAsNetFormat(byte[] encodedPrivkey)
	{
		try
		{
			StringBuffer buff = new StringBuffer(1024);

			PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(encodedPrivkey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKey pukKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encodedPrivkey));
			// RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey)
			// keyFactory.generatePrivate(pvkKeySpec);

			// PublicKey publicKey
			// =KeyFactory.getInstance("RSA").generatePublic(pvkKeySpec);

			buff.append("<RSAKeyValue>");
			buff.append("<Modulus>" + b64encode(removeMSZero(pukKey.getModulus().toByteArray())) + "</Modulus>");
			buff.append("<Exponent>" + b64encode(removeMSZero(pukKey.getPublicExponent().toByteArray())) + "</Exponent>");
			buff.append("</RSAKeyValue>");
			return buff.toString().replaceAll("[ \t\n\r]", "");
		}
		catch (Exception e)
		{
			System.err.println(e);
			return null;
		}
	}

	public static String encodePublicKeyToXml(PublicKey key)
	{
		if (!RSAPublicKey.class.isInstance(key))
		{
			return null;
		}
		RSAPublicKey pubKey = (RSAPublicKey) key;
		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>");
		sb.append("<Modulus>").append(Base64.encode(pubKey.getModulus().toByteArray())).append("</Modulus>");
		sb.append("<Exponent>").append(Base64.encode(pubKey.getPublicExponent().toByteArray())).append("</Exponent>");
		sb.append("</RSAKeyValue>");
		return sb.toString();
	}

	private static byte[] removeMSZero(byte[] data)
	{
		byte[] data1;
		int len = data.length;
		if (data[0] == 0)
		{
			data1 = new byte[data.length - 1];
			System.arraycopy(data, 1, data1, 0, len - 1);
		}
		else
			data1 = data;

		return data1;
	}

	private static String b64encode(byte[] data)
	{

		String b64str = new String(Base64.encode(data));
		return b64str;
	}

	public static byte[] b64decode(String data)
	{
		byte[] decodeData = Base64.decode(data);
		return decodeData;
	}
}