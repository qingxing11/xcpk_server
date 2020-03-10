package com.wt.exception.army;

import com.wt.util.Tool;

/**
 * 单位详细数据没有找到异常，已记录在日志 
 * @author akwang
 *
 */
public class ArmyExtendedNotFoundException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5153901976460685823L;

	public ArmyExtendedNotFoundException()
	{
		super();
		Tool.print_error("ArmyExtendedNotFoundException");
	}

	public ArmyExtendedNotFoundException(String message)
	{
		super(message);
		Tool.print_error(message);
	}

	public ArmyExtendedNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
		Tool.print_error(message,cause);
	}
}
