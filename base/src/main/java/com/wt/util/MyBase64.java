package com.wt.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

public class MyBase64
{

	public static void main(String[] args)
	{
		byte[] b = MyBase64.decode("com+catstudio+littlesoldiers");
		String str = new String(b);
		System.out.println(str);
		System.out.println(MyBase64.encode(b));
	}

	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	/*
	 * Replace all instances of a String in a String. @param s String to
	 * alter.
	 * 
	 * @param f String to look for. @param r String to replace it with, or
	 * null to just remove it.
	 */
	public static String replace(String s, String f, String r)
	{
		if (s == null)
		{
			return s;
		}
		if (f == null)
		{
			return s;
		}
		if (r == null)
		{
			r = "";
		}

		int index01 = s.indexOf(f);
		while (index01 != -1)
		{
			s = s.substring(0, index01) + r + s.substring(index01 + f.length());
			index01 += r.length();
			index01 = s.indexOf(f, index01);
		}
		return s;
	}

	/**
	 * Method removes HTML tags from given string.
	 * 
	 * @param text
	 *                Input parameter containing HTML tags (eg.
	 *                <strong>cat</strong>)
	 * @return String without HTML tags (eg. cat)
	 */
	public static String removeHtml(String text)
	{
		try
		{
			int idx = text.indexOf("<");
			if (idx == -1)
			{
				return text;
			}

			String plainText = "";
			String htmlText = text;
			int htmlStartIndex = htmlText.indexOf("<", 0);
			if (htmlStartIndex == -1)
			{
				return text;
			}
			while (htmlStartIndex >= 0)
			{
				plainText += htmlText.substring(0, htmlStartIndex);
				int htmlEndIndex = htmlText.indexOf(">", htmlStartIndex);
				htmlText = htmlText.substring(htmlEndIndex + 1);
				htmlStartIndex = htmlText.indexOf("<", 0);
			}
			plainText = plainText.trim();
			return plainText;
		}
		catch (Exception e)
		{
			System.err.println("Error while removing HTML: " + e.toString());
			return text;
		}
	}

	public MyBase64 getObject()
	{
		return new MyBase64();
	}

	public String encodeMD5(String str)
	{
		return encode(MyMD5.getMD5(str));
	}

	/** Base64 encode the given data */
	public static String encode(byte[] data)
	{
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end)
		{
			int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8) | (((int) data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;

			if (n++ >= 14)
			{
				n = 0;
				buf.append(" ");
			}
		}

		if (i == start + len - 2)
		{
			int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		}
		else if (i == start + len - 1)
		{
			int d = (((int) data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	private static int decode(char c)
	{
		if (c >= 'A' && c <= 'Z')
		{
			return ((int) c) - 65;
		}
		else if (c >= 'a' && c <= 'z')
		{
			return ((int) c) - 97 + 26;
		}
		else if (c >= '0' && c <= '9')
		{
			return ((int) c) - 48 + 26 + 26;
		}
		else
		{
			switch (c)
			{
				case '+':
					return 62;
				case '/':
					return 63;
				case '=':
					return 0;
				case '.':
					return 64;
				default:
					throw new RuntimeException("unexpected code: " + c);
			}
		}
	}

	/**
	 * Decodes the given Base64 encoded String to a new byte array. The byte
	 * array holding the decoded data is returned.
	 */

	public static byte[] decode(String s)
	{

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try
		{
			decode(s, bos);
		}
		catch (IOException e)
		{
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try
		{
			bos.close();
			bos = null;
		}
		catch (IOException ex)
		{
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private static void decode(String s, OutputStream os) throws IOException
	{
		int i = 0;

		int len = s.length();

		while (true)
		{
			while (i < len && s.charAt(i) <= ' ')
			{
				i++;
			}

			if (i == len)
			{
				break;
			}

			int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
			{
				break;
			}
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
			{
				break;
			}
			os.write(tri & 255);

			i += 4;
		}
	}
}
