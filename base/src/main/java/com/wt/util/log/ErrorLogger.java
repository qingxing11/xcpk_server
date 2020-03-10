package com.wt.util.log;

import org.apache.log4j.Logger;

public class ErrorLogger
{
	public static final Logger logger=Logger.getLogger("ErrorLogger");
	
	public static void print(String s)
	{
		logger.info(s);
	}
}
