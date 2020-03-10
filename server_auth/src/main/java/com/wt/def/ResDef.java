package com.wt.def;

import com.wt.model.HotfixModel;

public class ResDef
{
	private ResDef() {}
	
	public static ResDef resDef;
	
	public HotfixModel hotfixModel_android;
	public HotfixModel hotfixModel_ios;
	public HotfixModel hotfixModel_windows;
	
	public static ResDef getResDef()
	{
		if(resDef == null)
		{
			resDef = new ResDef();
		}
		return resDef;
	}
}
