package com.wt.util.log;

import org.apache.log4j.Logger;
public class PlayerLogger
{
	public static final Logger logger=Logger.getLogger("PlayerLogger");
	
	public static void print(String s)
	{
		logger.info(s);
	}
}
