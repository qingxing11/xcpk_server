package com.wt.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class UuidUtil
{

	public static final long MIN_VALUE = 0x8000000000000000L;
	public static final long MAX_VALUE = 0x7fffffffffffffffL;

	final static char[] digits = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '-', '_'
	};

	private static String toUnsignedString(long i, int shift)
	{
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;// 1*2^6 *2的n次方
		long mask = radix - 1;
		do
		{
			buf[--charPos] = digits[(int) (i & mask)];
			i >>>= shift;
		}
		while (i != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	// j为2的次方，如转成16进制就是4，32进制就是5...
	public static String getRand(long i, int j)
	{
		return toUnsignedString(i, j);
	}

	// 随机码＋时间戳＋随机码的生成
	public static Long getRand()
	{
		String str1, str2, str3;
		str1 = getRandStr(2);
		str3 = getRandStr(3);
		str2 = (new Date()).getTime() + "";
		// System.out.println(str1+str2+str3);
		return Long.parseLong(str1 + str2 + str3);
	}

	// 主键生成
	public static String getKey()
	{
		// return getRand(getRand(), 6);
		return generateShortUuid();
	}

	// 生成指定长度的随机串
	public static String getRandStr(Integer length)
	{
		String str = "";
		while (str.length() != length)
		{
			str = (Math.random() + "").substring(2, 2 + length);
		}
		return str;
	}

	public static void main(String[] args)
	{
		// for (int i = 0; i < 100; i++)
		// System.out.println(getKey());
		// System.out.println("=====================================");
		// for (int i = 0; i < 100; i++)
		// System.out.println(generateShortUuid());

		int repeatCount = 0;
		Set<String> set = new HashSet<String>();
		Random rand = new Random();
		for (int i = 0 ; i < 5000 ; i++)
		{
			if (!set.add(generateShortUuid()))
			{// generateShortUuid()
				repeatCount++;
			}
			// while (!set.add(rand.nextInt(900000) + "" + 100000))
			// ;
		}

		System.out.println("重复次数：" + repeatCount);
		// for (int i = 0; i < 100; i++)
		// generateNums();
	}

	// -----------------------------------------------测试10W 无重复
	// 短8位UUID思想其实借鉴微博短域名的生成方式，但是其重复概率过高，而且每次生成4个，需要随即选取一个。
	// 本算法利用62个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分成8组，每4个为一组，然后通过模62操作，结果作为索引取出字符，这样重复率大大降低。

	public static String[] chars = new String[] {
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};

	public static String[] nums = new String[] {
			"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
	};

	public static String generateShortUuid()
	{
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0 ; i < 8 ; i++)
		{
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

	public static String generateNums()
	{
		int[] array = {
				0, 1, 2, 3, 4, 5, 6, 7, 8, 9
		};
		Random rand = new Random();
		for (int i = 10 ; i > 1 ; i--)
		{
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0 ; i < 6 ; i++)
			result = result * 10 + array[i];
		return null;
	}
}
