package com.wt.util.log;

import org.apache.log4j.Logger;
public class ManagerLogger {

	public static final Logger logger = Logger.getLogger("ManagerLogger");
	
	public static void print(String s)
	{
		logger.info(s);
	}
}
