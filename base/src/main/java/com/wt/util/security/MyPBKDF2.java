package com.wt.util.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/***
 * 密码加盐和验证
 * @author WangTuo
 */
public class MyPBKDF2
{
//	public static void main(String[] args)
//	{
//		String password = "1234556";
//		String pbkdf;
//		try
//		{
//			long start = MyTimeUtil.getCurrTimeMM();
//			pbkdf = createHash(password);
//			System.out.println("use:"+(MyTimeUtil.getCurrTimeMM() - start));
//			System.out.println("pbkdf:"+pbkdf);
//			
//			start = MyTimeUtil.getCurrTimeMM();
//			boolean pass = validatePassword(password, pbkdf);
//			System.out.println("use:"+(MyTimeUtil.getCurrTimeMM() - start));
//			System.out.println("pass:"+pass);
//		}
//		catch (NoSuchAlgorithmException e)
//		{
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//		catch (InvalidKeySpecException e)
//		{
//			// TODO 自动生成的 catch 块
//			e.printStackTrace();
//		}
//	}
	
//	/**
//	 * Tests the basic functionality of the PasswordHash class
//	 * 
//	 * @param args
//	 *                ignored
//	 */
//	public static void main(String[] args)
//	{
//		try
//		{
//			// Print out 10 hashes
//			for (int i = 0 ; i < 10 ; i++)
//				System.out.println(createHash("p\r\nassw0Rd!"));
//			// Test password validation
//			boolean failure = false;
//			System.out.println("Running tests...");
//
//			for (int i = 0 ; i < 100 ; i++)
//			{
//				String password = "" + i;
//				String hash = createHash(password);
//				String secondHash = createHash(password);
//				if (hash.equals(secondHash))
//				{
//					System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
//					failure = true;
//				}
//				String wrongPassword = "" + (i + 1);
//				if (validatePassword(wrongPassword, hash))
//				{
//					System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
//					failure = true;
//				}
//				if (!validatePassword(password, hash))
//				{
//					System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
//					failure = true;
//				}
//			}
//			if (failure)
//				System.out.println("TESTS FAILED!");
//			else
//				System.out.println("TESTS PASSED!");
//		}
//		catch (Exception ex)
//		{
//			System.out.println("ERROR: " + ex);
//		}
//	}
	
	private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

	private static final int SALT_BYTE_SIZE = 24;

	private static final int HASH_BYTE_SIZE = 24;

	/**
	 * 迭代次数
	 */
	private static final int PBKDF2_ITERATIONS = 1000;

	private static final int ITERATION_INDEX = 0;

	private static final int SALT_INDEX = 1;

	private static final int PBKDF2_INDEX = 2;

	/**
	 * 返回一个加盐后的PBKDF2 hash串
	 * @param password 需要加密的密码串
	 * @return  盐后的PBKDF2 hash串
	 */
	public static String createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		return createHash(password.toCharArray());
	}

	/**
	 * 返回一个加盐后的PBKDF2 hash串
	 * @param password
	 * @return 加盐后的PBKDF2 hash串
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String createHash(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);
		byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
		return PBKDF2_ITERATIONS + ":" + toHex(salt) + ":" + toHex(hash);
	}

	/**
	 * 用hash串验证密码
	 * @param password 真实密码
	 * @param correctHash 加盐密码
	 * @return 
	 */
	public static boolean validatePassword(String password, String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		return validatePassword(password.toCharArray(), correctHash);
	}

	/**
	 *  用hash串验证密码
	 * @param password 真实密码
	 * @param correctHash 加盐密码
	 * @return  
	 */
	public static boolean validatePassword(char[] password, String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		String[] params = correctHash.split(":");
		int iterations = Integer.parseInt(params[ITERATION_INDEX]);
		byte[] salt = fromHex(params[SALT_INDEX]);
		byte[] hash = fromHex(params[PBKDF2_INDEX]);
		byte[] testHash = pbkdf2(password, salt, iterations, hash.length);
		return slowEquals(hash, testHash);
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison
	 * method is used so that password hashes cannot be extracted from an
	 * on-line system using a timing attack and then attacked off-line.
	 * 
	 * @param a
	 *                the first byte array
	 * @param b
	 *                the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */

	private static boolean slowEquals(byte[] a, byte[] b)
	{
		int diff = a.length ^ b.length;
		for (int i = 0 ; i < a.length && i < b.length ; i++)
			diff |= a[i] ^ b[i];
		return diff == 0;
	}

	/**
	 * Computes the PBKDF2 hash of a password.
	 * 
	 * @param password
	 *                the password to hash.
	 * @param salt
	 *                the salt
	 * @param iterations
	 *                the iteration count (slowness factor)
	 * @param bytes
	 *                the length of the hash to compute in bytes
	 * @return the PBDKF2 hash of the password
	 */

	private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
		return skf.generateSecret(spec).getEncoded();
	}

	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 * 
	 * @param hex
	 *                the hex string
	 * @return the hex string decoded into a byte array
	 */

	private static byte[] fromHex(String hex)
	{
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0 ; i < binary.length ; i++)
		{
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 * 
	 * @param array
	 *                the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */

	private static String toHex(byte[] array)
	{
		BigInteger bi = new BigInteger(1, array);

		String hex = bi.toString(16);

		int paddingLength = (array.length * 2) - hex.length();

		if (paddingLength > 0)

			return String.format("%0" + paddingLength + "d", 0) + hex;

		else

			return hex;
	}
}
