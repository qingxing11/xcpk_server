package com.wt.util.log;

import org.apache.log4j.Logger;

public class DbErrorLogger
{
	public static final Logger logger=Logger.getLogger("DbErrorLogger");
	
	public static void print(String s)
	{
		logger.info(s);
	}
}
