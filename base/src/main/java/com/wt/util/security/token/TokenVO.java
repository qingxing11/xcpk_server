package com.wt.util.security.token;

public class TokenVO
{
	/**该token的使用者id*/
	public int uid;
	public String pwd;
	
	public TokenVO(int uid, String pwd)
	{
		super();
		this.uid = uid;
		this.pwd = pwd;
	}
	
	public TokenVO()
	{}

	@Override
	public String toString()
	{
		return "TokenVO [uid=" + uid + ", pwd=" + pwd + "]";
	}
}
