package com.wt.util.log;

import com.wt.util.Tool;

public class LogUtil
{
	public static boolean isPrintLine;
	
	public static void setPrintLine(boolean isPrintLine)
	{
		LogUtil.isPrintLine = isPrintLine;
	}
	
	/**
	 * 获取当前输出在代码中的行数
	 * @param stack 需要显示的方法栈层数，第0层为StackTraceElement内部
	 */
	public static String getPrintLine(int stack)
	{
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		StackTraceElement tmp = trace[stack];
		return /* tmp.getClassName() + tmp.getMethodName() +"." + */  "(" + tmp.getFileName() + ":" + tmp.getLineNumber()+ ")";
	}
	
	public static void print_debug(String string)
	{
		if(isPrintLine)
		{
			System.out.print(getPrintLine(4));
		}
		PlayerLogger.logger.info(string);//需要显示的方法栈层数，往后退4层);
 	}
	
	public static void pring_coins(String string)
	{
		if(isPrintLine)
		{
			System.out.print(getPrintLine(4));
		}
		CoinsLogger.print(string);
	}
	
	public static void print_error(String string)
	{
		if(isPrintLine)
		{
			System.out.print(getPrintLine(4));
		}
		System.err.println(string);
		ErrorLogger.logger.error(string);
	}
	
	public static void print_error(String string,Throwable e)
	{
		System.err.println(string);
		ErrorLogger.logger.error(string, e);
	}
	
	public static void print_error(Throwable e)
	{
		e.printStackTrace();
		ErrorLogger.logger.error(e);
	}
	
	public static void print_dbError(Throwable e)
	{
		Tool.print_dbError(e);
	}

	public static void print_debug_admin(String string)
	{
		PlayerLogger.logger.error("[admin]"+string);
	}
}