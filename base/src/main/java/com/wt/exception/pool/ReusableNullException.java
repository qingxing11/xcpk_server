package com.wt.exception.pool;

import com.wt.util.Tool;

public class ReusableNullException extends Exception
{
	private static final long serialVersionUID = -5776552146516829750L;

	public ReusableNullException()
	{
		super();
		Tool.print_error("ReusableNullException");
	}

	public ReusableNullException(String message)
	{
		super(message);
		Tool.print_error(message);
	}

	public ReusableNullException(String message, Throwable cause)
	{
		super(message, cause);
		Tool.print_error(message,cause);
	}
}
