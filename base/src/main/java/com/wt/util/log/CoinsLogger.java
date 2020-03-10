package com.wt.util.log;

import org.apache.log4j.Logger;
public class CoinsLogger {

	public static final Logger logger = Logger.getLogger("CoinsLogger");
	
	public static void print(String s)
	{
		logger.info(s);
	}
}
