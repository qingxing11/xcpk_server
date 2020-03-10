package com.wt.cmd.user;

import java.io.Serializable;

public class PlayerNicknameVO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8243552963040979441L;

	public int key;
	public String value;

	public PlayerNicknameVO()
	{}

	public PlayerNicknameVO(int key, String value)
	{
		this.key = key;
		this.value = value;
	}
}
