package com.wt.exception.player;

public class PlayerSimpleDataNotFoundException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5153901976460685823L;

	public PlayerSimpleDataNotFoundException()
	{
		super();
	}

	public PlayerSimpleDataNotFoundException(String message)
	{
		super(message);
	}

	public PlayerSimpleDataNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
