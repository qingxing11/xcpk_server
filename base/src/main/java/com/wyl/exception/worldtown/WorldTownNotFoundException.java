package com.wyl.exception.worldtown;

import com.wt.util.Tool;

public class WorldTownNotFoundException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 540976179578764246L;
	
	
	public WorldTownNotFoundException() {
		super();
		Tool.print_error("WorldTownNotFoundException");
	}
	
	public WorldTownNotFoundException(String message) {
		super(message);
		Tool.print_error(message);
	}
	
	public WorldTownNotFoundException(String message, Throwable cause)
	{
		super(message, cause);
		Tool.print_error(message,cause);
	}

}
