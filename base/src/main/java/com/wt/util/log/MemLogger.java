package com.wt.util.log;

import org.apache.log4j.Logger;

public class MemLogger
{
	public static final Logger logger=Logger.getLogger("MemLogger");
	
	public static void print(String s)
	{
		logger.info(s);
	}
}
