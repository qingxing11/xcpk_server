package com.wt.util;

public class StringHelper
{
	// 数组中是否包括某字符串
	public static boolean arrayContainsString(String[] array, String key)
	{
		for (String tmpString : array)
		{
			if (tmpString.equals(key))
			{
				return true;
			}
		}
		return false;
	}

	public static String GetLeftString(String source, String strTail)
	{
		return GetLeftString(source, strTail, false);
	}

	public static String GetLeftString(String source, String strTail, boolean KeepHeadAndTail)
	{
		return GetMiddleString(source, "", strTail, KeepHeadAndTail);
	}

	public static String GetRightString(String source, String strHead)
	{
		return GetRightString(source, strHead, false);
	}

	public static String GetRightString(String source, String strHead, boolean KeepHeadAndTail)
	{
		return GetMiddleString(source, strHead, "", KeepHeadAndTail);
	}

	public static String GetMiddleString(String source, String strHead, String strTail)
	{
		return GetMiddleString(source, strHead, strTail, false);
	}

	public static String GetMiddleString(String source, String strHead, String strTail, boolean KeepHeadAndTail)
	{
		try
		{
			int indexHead, indexTail;

			if (strHead == null || strHead.isEmpty())
			{
				indexHead = 0;
			}
			else
			{
				indexHead = source.indexOf(strHead);
			}

			if (strTail == null || strTail.isEmpty())
			{
				indexTail = source.length();
			}
			else
			{
				indexTail = source.indexOf(strTail, indexHead + strHead.length());
			}
			if (indexTail < 0)
			{
				indexTail = source.length();
			}

			String rtnStr = "";
			if ((indexHead >= 0) && (indexTail >= 0))
			{
				if (KeepHeadAndTail)
				{
					rtnStr = source.substring(indexHead, indexTail + strTail.length());
				}
				else
				{
					rtnStr = source.substring(indexHead + strHead.length(), indexTail);
				}
			}
			return rtnStr;
		}
		catch (Exception ex)
		{
			return "";
		}
	}
}