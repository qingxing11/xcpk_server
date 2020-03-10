package com.wt.proto;

public class TestM
{
	public static void main(String[] args) throws InterruptedException
	{
		testM();
	}

	private static void testM() throws InterruptedException
	{
		byte[] test = new byte[] {1,2,3,4,5,6,7,8,10};
		while (true)
		{
//			byte[] test = new byte[] {1,2,3,4,5,6,7,8,10};
			for (int i = 0 ; i < test.length ; i++)
			{
				byte b = test[i];
				
			}
			Thread.sleep(50);
		}
	}
}
