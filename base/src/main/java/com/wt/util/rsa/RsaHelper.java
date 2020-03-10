package com.wt.util.rsa;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import com.wt.util.StringHelper;

public class RsaHelper
{
	/**
	 * 
	 * 生成RSA密钥对(默认密钥长度为1024)
	 * 
	 * 
	 * 
	 * @return
	 */

	public static KeyPair generateRSAKeyPair()
	{

		return generateRSAKeyPair(1024);
	}

	/**
	 * 
	 * 生成RSA密钥对
	 * 
	 * 
	 * 
	 * @param keyLength
	 * 
	 *                密钥长度，范围：512～2048
	 * 
	 * @return
	 */

	public static KeyPair generateRSAKeyPair(int keyLength)
	{
		try
		{
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

			kpg.initialize(keyLength);

			return kpg.genKeyPair();

		}
		catch (NoSuchAlgorithmException e)
		{
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

		sb.append("<Modulus>")

		.append(Base64Helper.encode(pubKey.getModulus().toByteArray()))

		.append("</Modulus>");

		sb.append("<Exponent>")

		.append(Base64Helper.encode(pubKey.getPublicExponent()

		.toByteArray())).append("</Exponent>");

		sb.append("</RSAKeyValue>");

		return sb.toString();

	}

	public static PublicKey decodePublicKeyFromXml(String xml)
	{
		xml = xml.replaceAll("\r", "").replaceAll("\n", "");

		BigInteger modulus = new BigInteger(1, Base64Helper.decode(StringHelper

		.GetMiddleString(xml, "<Modulus>", "</Modulus>")));

		BigInteger publicExponent = new BigInteger(1,

		Base64Helper.decode(StringHelper.GetMiddleString(xml,

		"<Exponent>", "</Exponent>")));

		RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
		KeyFactory keyf;

		try
		{

			keyf = KeyFactory.getInstance("RSA");

			return keyf.generatePublic(rsaPubKey);

		}
		catch (Exception e)
		{

			return null;
		}
	}

	public static PrivateKey decodePrivateKeyFromXml(String xml)
	{

		xml = xml.replaceAll("\r", "").replaceAll("\n", "");

		BigInteger modulus = new BigInteger(1, Base64Helper.decode(StringHelper

		.GetMiddleString(xml, "<Modulus>", "</Modulus>")));

		BigInteger publicExponent = new BigInteger(1,

		Base64Helper.decode(StringHelper.GetMiddleString(xml,

		"<Exponent>", "</Exponent>")));

		BigInteger privateExponent = new BigInteger(1,

		Base64Helper.decode(StringHelper.GetMiddleString(xml, "<D>",

		"</D>")));

		BigInteger primeP = new BigInteger(1, Base64Helper.decode(StringHelper

		.GetMiddleString(xml, "<P>", "</P>")));

		BigInteger primeQ = new BigInteger(1, Base64Helper.decode(StringHelper

		.GetMiddleString(xml, "<Q>", "</Q>")));

		BigInteger primeExponentP = new BigInteger(1,

		Base64Helper.decode(StringHelper.GetMiddleString(xml, "<DP>",

		"</DP>")));

		BigInteger primeExponentQ = new BigInteger(1,

		Base64Helper.decode(StringHelper.GetMiddleString(xml, "<DQ>",

		"</DQ>")));

		BigInteger crtCoefficient = new BigInteger(1,

		Base64Helper.decode(StringHelper.GetMiddleString(xml,

		"<InverseQ>", "</InverseQ>")));

		RSAPrivateCrtKeySpec rsaPriKey = new RSAPrivateCrtKeySpec(modulus,

		publicExponent, privateExponent, primeP, primeQ,

		primeExponentP, primeExponentQ, crtCoefficient);

		KeyFactory keyf;

		try
		{

			keyf = KeyFactory.getInstance("RSA");

			return keyf.generatePrivate(rsaPriKey);

		}
		catch (Exception e)
		{

			return null;

		}

	}

	public static String encodePrivateKeyToXml(PrivateKey key)
	{

		if (!RSAPrivateCrtKey.class.isInstance(key))
		{

			return null;

		}

		RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) key;

		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>");

		sb.append("<Modulus>")

		.append(Base64Helper.encode(priKey.getModulus().toByteArray()))

		.append("</Modulus>");

		sb.append("<Exponent>")

		.append(Base64Helper.encode(priKey.getPublicExponent()

		.toByteArray())).append("</Exponent>");

		sb.append("<P>")

		.append(Base64Helper.encode(priKey.getPrimeP().toByteArray()))

		.append("</P>");

		sb.append("<Q>")

		.append(Base64Helper.encode(priKey.getPrimeQ().toByteArray()))

		.append("</Q>");

		sb.append("<DP>")

		.append(Base64Helper.encode(priKey.getPrimeExponentP()

		.toByteArray())).append("</DP>");

		sb.append("<DQ>")

		.append(Base64Helper.encode(priKey.getPrimeExponentQ()

		.toByteArray())).append("</DQ>");

		sb.append("<InverseQ>")

		.append(Base64Helper.encode(priKey.getCrtCoefficient()

		.toByteArray())).append("</InverseQ>");

		sb.append("<D>")

		.append(Base64Helper.encode(priKey.getPrivateExponent()

		.toByteArray())).append("</D>");

		sb.append("</RSAKeyValue>");

		return sb.toString();

	}

	// 用公钥加密
	public static byte[] encryptData(byte[] data, PublicKey pubKey)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.ENCRYPT_MODE, pubKey);

			return cipher.doFinal(data);

		}
		catch (Exception e)
		{

			return null;

		}

	}

	// 用私钥解密

	public static byte[] decryptData(byte[] encryptedData, PrivateKey priKey)
	{
		try
		{
			Cipher cipher = Cipher.getInstance("RSA");

			cipher.init(Cipher.DECRYPT_MODE, priKey);

			return cipher.doFinal(encryptedData);

		}
		catch (Exception e)
		{
			return null;
		}
	}

	/**
	 * 
	 * 根据指定私钥对数据进行签名(默认签名算法为"SHA1withRSA")
	 * 
	 * 
	 * 
	 * @param data
	 * 
	 *                要签名的数据
	 * 
	 * @param priKey
	 * 
	 *                私钥
	 * 
	 * @return
	 */

	public static byte[] signData(byte[] data, PrivateKey priKey)
	{

		return signData(data, priKey, "SHA1withRSA");

	}

	/**
	 * 
	 * 根据指定私钥和算法对数据进行签名
	 * 
	 * 
	 * 
	 * @param data
	 * 
	 *                要签名的数据
	 * 
	 * @param priKey
	 * 
	 *                私钥
	 * 
	 * @param algorithm
	 * 
	 *                签名算法
	 * 
	 * @return
	 */

	public static byte[] signData(byte[] data, PrivateKey priKey,

	String algorithm)
	{

		try
		{

			Signature signature = Signature.getInstance(algorithm);

			signature.initSign(priKey);

			signature.update(data);

			return signature.sign();

		}
		catch (Exception ex)
		{

			return null;

		}

	}

	/**
	 * 
	 * 用指定的公钥进行签名验证(默认签名算法为"SHA1withRSA")
	 * 
	 * 
	 * 
	 * @param data
	 * 
	 *                数据
	 * 
	 * @param sign
	 * 
	 *                签名结果
	 * 
	 * @param pubKey
	 * 
	 *                公钥
	 * 
	 * @return
	 */

	public static boolean verifySign(byte[] data, byte[] sign, PublicKey pubKey)
	{

		return verifySign(data, sign, pubKey, "SHA1withRSA");

	}

	/**
	 * 
	 * 
	 * 
	 * @param data
	 *                数据
	 * 
	 * @param sign
	 *                签名结果
	 * 
	 * @param pubKey
	 *                公钥
	 * 
	 * @param algorithm
	 *                签名算法
	 * 
	 * @return
	 */

	public static boolean verifySign(byte[] data, byte[] sign,

	PublicKey pubKey, String algorithm)
	{

		try
		{

			Signature signature = Signature.getInstance(algorithm);

			signature.initVerify(pubKey);

			signature.update(data);

			return signature.verify(sign);

		}
		catch (Exception ex)
		{

			return false;

		}

	}

}