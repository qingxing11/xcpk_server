package com.wt.poker;

import java.util.HashMap;

import com.wt.util.MyUtil;

import junit.framework.TestCase;

public class Test extends TestCase
{
	public void test()
	{
		for (int i = 0 ; i < 100 ; i++)
		{
			System.out.println("MyUtil.getRandom(0, 1);:"+MyUtil.getRandom(0, 2));
		}
	}
}
