package com.wt.naval.vo;

public class RegisterCode
{
	/**验证码生成时间*/
	public long time;
	/**验证码*/
	public String code;
	public RegisterCode(String code, long time)
	{
		this.time = time;
		this.code = code;
	}
}
